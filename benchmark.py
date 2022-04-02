import subprocess as sp
from datetime import datetime
import os

def bench_upload():
    print("Launching AnonFS")
    process = sp.Popen("java -jar /home/abhiram/eclipse-workspace/AnonFS.jar 2>/dev/null", stdout=sp.PIPE,stdin=sp.PIPE,shell=True)
    # print(process.stdout.read())
    print("Launch is done, waiting for connection")
    input("Press enter after connecting")
    process.stdout.readline()
    process.stdout.readline()
    process.stdin.write(b"upload test.jpg\n")
    process.stdin.flush()
    clock = datetime.now()
    line = process.stdout.readline().decode()
    while("test.jpg" not in line):
        line = process.stdout.readline().decode()
        print(line)
    print("That took ",(datetime.now()-clock).seconds,"s")
    print("Got success!")
    print("Success line:",line)

# def bench_upload():
#     print("Launching AnonFS")
#     process = os.popen("java -jar /home/abhiram/eclipse-workspace/AnonFS.jar")
#     # print(process.stdout.read())
#     print("Launch is done, waiting for connection")
#     input("Press enter after connecting")
#     process.write(b"upload test.jpg\n")
#     process.flush()
#     clock = datetime.now()
#     line = process.readline().decode()
#     while("test.jpg" not in line):
#         line = process.readline().decode()
#         print(line)
#     print("That took ",(datetime.now()-clock).seconds,"s")
#     print("Got success!")
#     print("Success line:",line)

bench_upload()
