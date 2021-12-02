import sys

import requests

#--------------------------------------------------

url = "http://167.114.103.227:8517/"
token = "rxkJYyq2gWNFSAbEH4Ax7c5pg4ADnJrrd7gU5gwBNb5EqA57zVqDV8cfLhwSyXMF"

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
