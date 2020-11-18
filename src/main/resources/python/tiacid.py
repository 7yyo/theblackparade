#!/usr/bin/python3
import pymysql
import threading
import time
import random

HOST = '172.16.4.87'    # ip
PORT = 14300            # port
USER = 'root'           # username
PASSWD = ''             # password
DB = 'test'             # database
CHARSET = 'utf8'
# datafornum * batchsize = number of account
DATAFORNUM = 1000       # number of batch insert loop
BATCHSIZE = 200         # batch insert size
# datafornum * batchsize = number of account
TRANSFERFORNUM = 100    # number of transactions executed per thread
THREADNUM = 10          # number of threads
BALANCE = 1000          # account balance

connection = pymysql.connect(host = HOST,port = PORT,user = USER,passwd = PASSWD,db = DB,charset = CHARSET)
connectionInfo = " ["+str(time.asctime( time.localtime(time.time())))+"] Connnected success, [url] = " + HOST + ":" + str(PORT) + ", [username] = " + USER + ", [password] = " + PASSWD + ", [database] = " + DB
print(connectionInfo)

cursor = connection.cursor()

print(" ["+str(time.asctime( time.localtime(time.time())))+"] Start init table data")
try:
    cursor.execute("drop database if exists tiacid")
    cursor.execute("create database if not exists tiacid")
    cursor.execute("use tiacid")
    cursor.execute("create table account (account_id int,balance decimal(16,0))")
    cursor.execute("create table customer_flow_table (account_id int,type int,money decimal(16,0),transfer_time datetime)")
    connection.commit()
except:
    print(" ["+str(time.asctime( time.localtime(time.time())))+"] Execute DDL failure, please rollback")
    connection.rollback()
    print(" ["+str(time.asctime( time.localtime(time.time())))+"] Create data successfully.")

n = 0
for i in range(DATAFORNUM):
    sql = "insert into account values"
    for j in range(1, BATCHSIZE + 1):
        n += 1
        if j % BATCHSIZE == 0:
            sql += "(" + str(n) + "," + str(BALANCE) + ");"
            break
        sql += "(" + str(n) + "," + str(BALANCE) +"),"
    # print(sql)
    try:
        cursor.execute(sql)
        connection.commit()
    except:
        connection.rollback()
connection.close()

class transferJob (threading.Thread):
    def __init__(self, connection):
        threading.Thread.__init__(self)
        self.connection = connection
    def run(self):
        runTransfer(self.connection, )
        # print ("finish thread .")

def runTransfer(connection):
    for i in range(TRANSFERFORNUM):
        connection.ping(reconnect = True)
        cursor = connection.cursor()
        cursor.execute("use tiacid")
        accountFrom = random.randint(1, BATCHSIZE * DATAFORNUM)
        accountTo = random.randint(1, BATCHSIZE * DATAFORNUM)
        #print("<=" + str(accountFrom))
        #print("=>" + str(accountTo))
        try:
            cursor.execute("update account set balance = ((select balance from account where account_id = " + str(accountFrom) + ") - 0.1) where account_id = " + str(accountFrom) + ";")
            cursor.execute("update account set balance = ((select balance from account where account_id = " + str(accountTo) + ") + 0.1) where account_id = " + str(accountTo) + ";")
            cursor.execute("insert into customer_flow_table values(" + str(accountFrom) + "," + str(1) + ", 0.1, now());")
            cursor.execute("insert into customer_flow_table values(" + str(accountTo) + "," + str(2) + ", 0.1, now());")
            connection.commit()
        except:
            print(" ["+str(time.asctime( time.localtime(time.time())))+"] Execute transfer DML failure, please rollback")
            connection.rollback()
    connection.close()

threadList = []
for i in range(THREADNUM):
    connection = pymysql.connect(host = HOST,port = PORT,user = USER,passwd = PASSWD,db = DB,charset = CHARSET)
    thread = transferJob(connection)
    threadList.append(thread)
    thread.start()
    print(" ["+str(time.asctime( time.localtime(time.time())))+"] Thread start successfully. thread id :=" + str(i))

print(" ["+str(time.asctime( time.localtime(time.time())))+"] Transfering ...")

for thread in threadList:
    thread.join()

print(" ["+str(time.asctime( time.localtime(time.time())))+"] Transfer finish ...")
print(" ["+str(time.asctime( time.localtime(time.time())))+"] Start check acid result.")

totalBalance = 0
totalFlow = 0
connection = pymysql.connect(host = HOST,port = PORT,user = USER,passwd = PASSWD,db = DB,charset = CHARSET)
connection.ping(reconnect=True)
cursor = connection.cursor()
cursor.execute("use tiacid")
cursor.execute("select sum(balance) from account")
results = cursor.fetchall()
for row in results:
    totalBalance = row[0]
cursor.execute("select count(*) from customer_flow_table")
results = cursor.fetchall()
for row in results:
    totalFlow = row[0]

if int(totalBalance) != n * BALANCE:
    print(" ["+str(time.asctime( time.localtime(time.time())))+"] Balance = " + str(totalBalance) + " is error, should be " + str(n * BALANCE))
else:
    print(" ["+str(time.asctime( time.localtime(time.time())))+"] Balance = " + str(totalBalance) + ". success!")
if int(totalFlow) != 2 * TRANSFERFORNUM * THREADNUM:
    print(" ["+str(time.asctime( time.localtime(time.time())))+"] Number of flow = " + str(totalFlow) + " is error, should be " + str(2 * TRANSFERFORNUM * THREADNUM))
else:
    print(" ["+str(time.asctime( time.localtime(time.time())))+"] Number of flow = " + str(totalFlow) + ". success!")