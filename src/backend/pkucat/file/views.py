from django.shortcuts import render
from django.http import HttpResponse, JsonResponse
from django.core.validators import validate_email
from django.core.exceptions import ValidationError
from django.core.mail import send_mail
import django.contrib.auth as auth
from django.views.decorators.csrf import csrf_exempt, csrf_protect
from datetime import datetime
import os.path

from demo.config import CODE

static_path = "/home/ubuntu/static/"

@csrf_exempt
def upload(request):
    code = -1
    msg = ""
    video_urls = []
    picture_urls = []

    if not request.user.is_authenticated:
        code = CODE['user_error']
        msg = "not authorized"
    elif request.method == 'POST':
        if request.FILES:
            pictures = request.FILES.getlist('picture', None)
            videos = request.FILES.getlist('video', None)
            if not os.path.exists(static_path + "picture"):
                os.mkdir(static_path + "picture")
            if not os.path.exists(static_path + "video"):
                os.mkdir(static_path + "video")
            
            if pictures or videos:
                for picture in pictures:
                    name = datetime.now().strftime('%Y%m%d%H%M%S%f')+os.path.splitext(picture.name)[-1]
                    picture_urls.append("/static/picture/"+name)
                    with open(static_path + "picture/" + name, "wb") as f:
                        f.write(picture.read())
                for video in videos:
                    name = datetime.now().strftime('%Y%m%d%H%M%S%f')+os.path.splitext(video.name)[-1]
                    video_urls.append("/static/video/"+name)
                    with open(static_path + "video/" + name, "wb") as f:
                        f.write(video.read())
                    
                code = CODE['success']
                msg = "success"
            else:
                code = CODE['parameter_error']
                msg = 'parameter error'
        else:
            code = CODE['parameter_error']
            msg = 'parameter error'

    else:
        code = CODE['method_error']
        msg = 'wrong method'

    response = {
        'code': code,
        'data': {
            'msg':msg,
            'video': video_urls,
            'picture': picture_urls,
        }
    }
    return JsonResponse(response)


