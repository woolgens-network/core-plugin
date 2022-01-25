import sys

import requests

#--------------------------------------------------

url = "http://167.114.103.227:8517/"
token = "98h7kFwfJ76G9wC2m6ZwkGtQWpdfs5Yn3s5xGBKvdEnq3DJVdUGZreJzEnNeF2TG"

path = sys.argv[1]
file_name = sys.argv[2]

#--------------------------------------------------



file = open(path, "rb")
file_bytes = file.read()
file.close()

data = {"name": (None, file_name), "data": (None, file_bytes)}

response = requests.post(url, headers={"Authentication": token}, files=data)
success = response.content
if(success):
    print("File successfully uploaded")
else:
    print("File cannot be uploaded")
