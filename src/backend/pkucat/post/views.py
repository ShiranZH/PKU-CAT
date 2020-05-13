from django.shortcuts import render
from django.shortcuts import HttpResponse

def index(request):
    return HttpResponse('Post Part!')

def demo(request):
    return HttpResponse('Post Response')

def post(request):
    if request.method == 'GET':
        num = request.GET.get('limit', default=10)
        start = request.GET.get('start', default=1)
        comments_num = request.GET.get('comments', default=10)
        Post = ''
        response = {
            "code": 200,
            "data": {
                "downloadCount": number,
                "data": [
                    Post, 
                ]
            } 
        }
        return HttpResponse(response)
    if request.method == 'POST':
        info = request.POST.get('post')
        response = {
            "code": 200,
            "data": {
                "msg": "success"
            }
        }
        return HttpResponse(response)
    if request.method == 'DELETE':
        post_id = request.DELETE.get('id')
        response = {
            "code": 200,
            "data": {
                "msg": "delete post successfully"
            }
        }
        return HttpResponse(response)
    response = {
        "code": 600,
        "data": {
            "msg": "wrong method"
        } 
    }
    return HttpResponse(response)

def search(request):
    if request.method == 'GET':
        catid = request.GET.get('catid', default=-1)
        userid = request.GET.get('userid', default=-1)
        keyword = request.GET.get('keyword', default=None)
        comments_num = request.GET.get('comments', default=10)
        Post = ''
        response = {
            "code": 200,
            "data": {
                "downloadCount": number,
                "data": [
                    Post, 
                ]
            } 
        }
        return HttpResponse(response)
    response = {
        "code": 600,
        "data": {
            "msg": "wrong method"
        } 
    }
    return HttpResponse(response)

def comments(request):
    if request.method == 'GET':
        try:
            type = request.GET.get('type', default="post")
            if (not type.equals('post')):
                response = {
                    "code": 700,
                    "data": {
                        "msg": "wrong parameter"
                    }
                }
                return HttpResponse(response)
            id = request.GET.get('root-id', default=-1)
            limit = request.GET.get('limit', default=10)
            start = request.GET.get('start', default=10)
            commentList = ''
            response = {
                "code": 200,
                "data": {
                    "commentList": commentList
                } 
            }
        except:
            response = {
                "code": 700,
                "data": {
                    "msg": "wrong parameter"
                }
            }
        return HttpResponse(response)
    response = {
        "code": 600,
        "data": {
            "msg": "wrong method"
        }
    }
    return HttpResponse(response)

def favor(request):
    if request.method == 'PUT':
        try:
            id = request.PUT.get('postID')
            response = {
                "code": 200,
                "data": {
                    "msg": "success"
                } 
            }
        except:
            response = {
                "code": 700,
                "data": {
                    "msg": "wrong parameter"
                }
            }
        return HttpResponse(response)
    elif request.method == 'DELETE':
        try:
            id = request.DELETE.get('postID')
            response = {
                "code": 200,
                "data": {
                    "msg": "success"
                } 
            }
        except:
            response = {
                "code": 700,
                "data": {
                    "msg": "wrong parameter"
                }
            }
        return HttpResponse(response)
    response = {
        "code": 600,
        "data": {
            "msg": "wrong method"
        }
    }
    return HttpResponse(response)

def comment(request):
    if request.method == 'POST':
        try:
            postID = request.POST.get('postID')
            text = request.POST.get('text', default='')
            commentList = ''
            response = {
                "code": 200,
                "data": {
                    "commentList": commentList
                } 
            }
        except:
            response = {
                "code": 300,
                "data": {
                    "msg": "post not exists"
                }
            }
        return HttpResponse(response)
    response = {
        "code": 600,
        "data": {
            "msg": "wrong method"
        }
    }
    return HttpResponse(response)





