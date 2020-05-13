import os.path

path = "/home/ubuntu"

def delete_file(url):
    if os.path.exists(path + url):
        os.remove(path + url)
    
def exists_file(url):
    return os.path.exists(path + url)