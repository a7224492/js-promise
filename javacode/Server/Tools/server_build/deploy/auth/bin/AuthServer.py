#!/usr/bin/env python 
# -*- coding: utf-8 -*-

import os
import sys
import subprocess
import signal
import time
#------------------------------------------- VAR BEGIN ----------------------------------


log_file_name="AuthServer"
java_param_main_class="com.kodgames.authserver.AuthServer"
java_param_server_name_id="AuthServer"
java_param_server_mem="-Xmx2048m -Xms1024m -Xss256k -Duser.timezone=GMT+08"

group_type="auth"
area_id="1"
log_path= os.path.dirname(os.getcwd()) + "/../log/"

kod_product_name = group_type + '_' + area_id + '_'
java_param_product = '-Dkod_product=' + kod_product_name + ' -Dlog_name=' + log_file_name
java_param_mem="-XX:+HeapDumpOnOutOfMemoryError "+ java_param_server_mem
# java_param_mem='-XX:+HeapDumpOnOutOfMemoryError ' + java_param_server_mem
java_param_debug = ''

java_param_path = '-Dlog_path=' + log_path 

java_param_gc="-XX:+PrintGCTimeStamps -XX:+PrintHeapAtGC -Xmn512M -XX:+PrintGCDetails -XX:+UseParNewGC -XX:MaxTenuringThreshold=5 -XX:ParallelGCThreads=8 -XX:+UseCMSCompactAtFullCollection -XX:CMSFullGCsBeforeCompaction=3 -XX:SurvivorRatio=4 -XX:CMSInitiatingOccupancyFraction=80 -XX:PermSize=32m -XX:MaxPermSize=68m -XX:+UseConcMarkSweepGC"

java_param_charset = '-Dfile.encoding=utf-8'
java_param_jar='-server -cp resources:../dist/*:../lib/*:../resource'
#------------------------------------------- VAR END ----------------------------------

def runtime_limit_tuning(openfile,corefile):
    max_openfile_nums = 'ulimit -c ' + openfile
    max_corefile_size = 'ulimit -n ' + corefile
    for cmd in [max_corefile_size,max_openfile_nums]:
        try:
            os.system(cmd)
        except Exception, e:
            raise e

def debug_mode(port):
  # debug_port = None
  # return False
  # print debug_mode()
  # if debug_mode() is not None:
  # if debug_port == None:
  # # if debug_port != '':
  if debug_port is not None:
      global java_param_debug
      java_param_debug = '-Xdebug -Xnoagent -Xrunjdwp:transport=dt_socket,address=' + debug_port + ',server=y,suspend=n'
      print '-- Debug mode and debug port is -- ', debug_port
def make_logPath():
    if not os.path.exists(log_path):
        try:
            os.makedirs(log_path)
#            os.makedirs(bp_log_path)
        except Exception, e:
            raise e

def make_zdbPath():
    if not os.path.exists('zdb'):
        try:
            os.makedirs('zdb')
        except Exception, e:
            raise e

def proc_start():
    print 'starting ' + log_file_name
    if not os.path.exists(ppidfile_path):
        _str = ' '
        list = [
                '/usr/bin/java',
                java_param_product, java_param_mem, java_param_debug,
                java_param_path, java_param_charset,java_param_gc, java_param_jar,
                java_param_main_class,java_param_server_name_id
                ]
        cmd = _str.join(list)
        try:
            # child_pid = os.fork()
            pid = os.fork()
            if pid > 0:
                # Exit parent process
                sys.exit(0)
        except OSError, e:
            print >> sys.stderr, "fork failed: %d (%s)" % (e.errno, e.strerror)
            sys.exit(1)

        os.setsid()
        os.umask(0)
        # print cmd
        f = open("/dev/null", 'r')
        proc = subprocess.Popen(cmd, shell=True, stdout=f,cwd=current_dir)

        # write pid file
        ppid = proc.pid    #children pid
        fh = open(ppidfile_path,'w')
        # global ppid_str
        ppid_str = __builtins__.str(ppid)
        # fh.write(ppid_str)
        fh.write(ppid_str)
        fh.close()
        # subprocess.call(cmd,shell=True,cwd=current_dir,stdout=f)
        time.sleep(5)
        f.close()
        os._exit(os.EX_OK)
        # sys.exit(0)

    else:
        print 'FAILED'
        print ppidfile_path, 'is exist'
        print 'Please stop this service or remove pidfile manually'
        sys.exit(1)

def proc_stop():
    if os.path.exists(ppidfile_path):
        fh = open(ppidfile_path, 'r')
        ppid = fh.read()
        ppid = int(ppid)
        print 'Terminating', log_file_name
        # signal ref
        # see http://blog.chinaunix.net/uid-609765-id-2087511.html
        # os.kill(ppid,signal.SIGKILL)  # 09
        os.kill(ppid,signal.SIGTERM)    # 15
        print 'Removing', ppidfile_path
        os.remove(ppidfile_path)

def proc_stat():
    if os.path.exists(ppidfile_path):
        fh = open(ppidfile_path, 'r')
        ppid = fh.read()
        print log_file_name, 'pid is ', ppid
    else:
        print ppidfile_path, 'is not exist'

if __name__ == '__main__':
    current_dir = os.getcwd()
    os.chdir(current_dir)
    ppidfile_path = current_dir + '/' + log_file_name + '.pid'

    if len(sys.argv) > 3:
        print 'please check your argv'
        print "Usage: {start|stop|stat|debug port_number}"
        sys.exit(1)
    if sys.argv[1] == 'start':
        runtime_limit_tuning(openfile='unlimited', corefile='65535')
        make_logPath()
        make_zdbPath()
        proc_start()
    elif sys.argv[1] == 'stop':
        proc_stop()
    elif sys.argv[1] == 'stat':
        proc_stat()
    elif sys.argv[1] == 'debug':
        debug_port=sys.argv[2]
        debug_mode(debug_port)
        runtime_limit_tuning(openfile='unlimited', corefile='65535')
        make_logPath()
        proc_start()
    else:
        print "Usage: {start|stop|stat|debug port_number}"
