#!/usr/bin/env python

import MySQLdb
import logging
import sys

host = '10.23.1.62'
user = 'root'
passwd = 'root'
port = 3306
dbname = 'designerdb'

def demo():
    try:
        tCon = MySQLdb.connect(host=host,user=user,port=port,passwd=passwd,db= dbname ,charset="utf8")
        cursor = tCon.cursor()
        for line in open('./output.sql','r'):
            cursor.execute(line)
            print line
        tCon.commit()
    except MySQLdb.Error,e:
        logging.error("error,%d: %s" %(e.args[0], e.args[1]))
        tCon.rollback()
        cursor.close()
        tCon.close()
        sys.exit()
    cursor.close()
    tCon.close()

if __name__ == '__main__':
    demo()
    raw_input()
