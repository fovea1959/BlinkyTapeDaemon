#!/usr/bin/python2

# vim: tabstop=8 expandtab shiftwidth=4 softtabstop=4

import logging
import os               # Miscellaneous OS interfaces.
import sys              # System-specific parameters and functions.
import re
import paho.mqtt.client as mqtt

class AppException(Exception):
    def __init__(self, message, cause=None):
        if cause == None:
            super(AppException, self).__init__(message)
        else:
            super(AppException, self).__init__(message + u', caused by ' + repr(cause))
        self.cause = cause

def createDaemon():
    try:
        pid = os.fork()
    except OSError, e:
        raise Exception, "%s [%d]" % (e.strerror, e.errno)

    if (pid == 0):	# The first child.
        os.setsid()
        try:
            pid = os.fork()	# Fork a second child.
        except OSError, e:
            raise Exception, "%s [%d]" % (e.strerror, e.errno)

        if (pid == 0):	# The second child.
            os.chdir('/')
            os.umask(0)
        else:
            os._exit(0)	# Exit parent (the first child) of the second child.
    else:
        os._exit(0)	# Exit parent of the first child.

    import resource		# Resource usage information.
    maxfd = resource.getrlimit(resource.RLIMIT_NOFILE)[1]
    if (maxfd == resource.RLIM_INFINITY):
        maxfd = 1024
  
    for fd in range(0, maxfd):
        try:
            os.close(fd)
        except OSError:	# ERROR, fd wasn't open to begin with (ignored)
            pass

    os.open('/dev/null', os.O_RDWR)	# standard input (0)
    os.dup2(0, 1)			# standard output (1)
    os.dup2(0, 2)			# standard error (2)

    return(0)

invalid_hosts = set()

def send_to_blinky(name, value):
    if name in invalid_hosts:
        return

    params = urllib.urlencode({ 'value': value, 'name': name})

    try:
        conn = httplib.HTTPConnection("wegscd-linux.whirlpool.com", 9999)
        headers = {"Content-type": "application/x-www-form-urlencoded"}
        url = "/rest/datum/{0}".format(name);
        conn.request('POST', url, params, headers)
        response = conn.getresponse()
        if response.status == 200:
            return True
        else:
            logging.warn ("post to %s did not work %d %s", url, response.status, response.reason)
            if response.status == 400:
                invalid_hosts.add(name)
            return False
        conn.close()
    except (httplib.HTTPException, socket.error) as e:
        logging.warn ("post to %s did not work: %s", url, e)
        return False

if __name__ == '__main__':
    import platform, time, httplib, urllib, socket, argparse, signal

    def abspatharg(string):
        return os.path.abspath(string)

    parser = argparse.ArgumentParser(description='Bridge MQTT topics to BlinkyTapeDaemon')
    parser.add_argument('--log', type=abspatharg, help='log file')
    parser.add_argument('--foreground', action='store_true', help='stay in foreground')
    parser.add_argument('--verbose', action='count', help='crank up logging')

    args = parser.parse_args()

    level = logging.DEBUG if args.verbose > 1 else (logging.INFO if args.verbose else logging.WARN)
    logging.basicConfig (level=level, format='%(asctime)s %(levelname)s %(name)s %(message)s')
    if args.log:
        handler = logging.FileHandler(args.log, 'a')
        logging.root.addHandler(handler)

    logging.info ("starting")

    if args.verbose > 1:
        httplib.HTTPConnection.debuglevel = 1

    if not args.foreground:
        logging.info ('Going to background')

        # turn off logging
        for handler in logging.root.handlers[:]:
            handler.close()
            logging.root.removeHandler(handler)

        # fork
        retCode = createDaemon()

        # set logging back up
        if retCode:
            # only need stderr if we failed
            handler = logging.StreamHandler()
            fmt = logging.Formatter ('%(asctime)s %(levelname)s %(name)s %(message)s')
            handler.setFormatter(fmt)
            logging.root.addHandler(handler)

        # and log to file again
        if args.log:
            handler = logging.FileHandler(args.log, 'a')
            fmtstring = '%(asctime)s [%(process)d] ' + args.sid + ' %(levelname)s %(name)s %(message)s'
            fmt = MyLoggingFormatter.MyLoggingFormatter (fmtstring, "%Y-%m-%dT%H:%M:%S%z")
            handler.setFormatter(fmt)
            logging.root.addHandler(handler)

        if retCode:
            raise AppException('createDaemon returned code {}'.format(retCode))

        logging.info ('Made transition to background, process ID = %s, parent process ID = %s, process group ID = %s, session ID = %s',
            os.getpid(), os.getppid(), os.getpgrp(), os.getsid(0))
        logging.debug ('Test debug message')

    exit_requested = False

    def sigterm_handler (_signo, _stack_frame):
        logging.warn ('Took signal %d, setting exit_requested', _signo)
        global exit_requested
        exit_requested = True

    signal.signal (signal.SIGTERM, sigterm_handler)


    mqtt_client = mqtt.Client()

    def on_message (client, userdata, msg):
        topic = msg.topic
        payload = str(msg.payload)
        logging.debug ("topic: %s payload: %s", topic, payload)
        host = re.findall(r'splunk/hosts/(.*)/cpu', topic)
        if len(host) > 0:
            host = host[0]
            logging.debug ("host: %s", host)
            send_to_blinky (host + '-cpu', payload)

    mqtt_client.on_message = on_message
    mqtt_client.connect("wegscd-linux.whirlpool.com", 1883, 60)
    mqtt_client.subscribe("splunk/hosts/+/cpu")

    while not exit_requested:
        try:
            logging.info ("hitting loop_forever()")
            mqtt_client.loop_forever()
            logging.warn ("left loop_forever()")
        except KeyboardInterrupt:
            logging.warn ("got Keyboard interrupt")
            exit_requested = True
