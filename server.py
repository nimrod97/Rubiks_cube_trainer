# import socket
# # # import RPi.GPIO as GPIO
# import time
#
#
# TCP_IP = '0.0.0.0'
# TCP_PORT = 8888
# BUFFER_SIZE = 1024
# s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
# s.bind((TCP_IP, TCP_PORT))
# s.listen(10)
# while True:
#     connection, addr = s.accept()
#     print("New connection made!")
#     connection.settimeout(1.0)
#     while True:
#         try:
#             data = connection.recv(BUFFER_SIZE)
#             data = data.decode()
#             print(data)
#         except socket.timeout:
#             connection.close()
#             break
# # #Defines Server Values
# # listensocket = socket.socket()
# # Port = 8888
# # maxConnections = 999
# # IP = "localhost" #Gets Hostname Of Current Macheine
# #
# # listensocket.bind(('',Port))
# #
# # # Opens Server
# # listensocket.listen(maxConnections)
# # print("Server started at " + IP + " on port " + str(Port))
# #
# # # Accepts Incomming Connection
# # (clientsocket, address) = listensocket.accept()
# #
# # running = True
# #
# # # Sets Up Indicator LED
# # # GPIO.setmode(GPIO.BOARD)
# # # GPIO.setup(7,GPIO.OUT)
# #
# # #Main
# # while running:
# #     message = clientsocket.recv(1024).decode() #Receives Message
# #     print(message) #Prints Message
#     #Turns On LED
#     # if not message == "":
#     #     GPIO.output(7,True)
#     #     time.sleep(5)
#     #     GPIO.output(7,False)
#     # Closes Server If Message Is Nothing (Client Terminated)
#     # else:
#     #     clientsocket.close()
#     #     running = False
#
#
#
# # import socket
# # import sys
#
# # data settings
# # data_size = 16 # sending 16 bytes = 128 bits (binary touch states, for example)
# #
# # # server settings
# # # server_name = str([l for l in ([ip for ip in socket.gethostbyname_ex(socket.gethostname())[2] if not ip.startswith("127.")][:1], [[(s.connect(('8.8.8.8', 53)), s.getsockname()[0], s.close()) for s in [socket.socket(socket.AF_INET, socket.SOCK_DGRAM)]][0][1]]) if l][0][0]) # https://stackoverflow.com/a/1267524
# # server_name = "localhost"
# # server_port = 8888
# # server_address = (server_name, server_port)
# #
# # # start up server
# # print ('Setting up server on:', server_address)
# # server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
# # server_socket.bind(server_address)
# # server_socket.listen(1)
# #
# # # wait for connection
# # print ('Waiting for a client connection...')
# # connection, client_address = server_socket.accept()
# # print ('Connected to:', client_address)
# #
# # # data formatting
# # def data2binary(data):
# #     return ' '.join([format(ord(i), 'b').zfill(8) for i in data])
# #
# # # listen for data for forever
# # while True:
# #     data = connection.recv(data_size)
# #
# #     print ('Received', data) # print as raw bytes
# # #print 'Received', data2binary(data) # print in binary
#
# # process data

from flask import Flask

# import request
from flask import request
app = Flask(__name__)

@app.route("/")
def showHomePage():
    return "This is home page"

@app.route("/debug", methods=["POST"])
def debug():
    text = request.form["sample"]
    print(text)
    return "received"

if __name__ == "__main__":
    app.run(host="0.0.0.0")
