from django.shortcuts import render
from django.http import HttpResponse, JsonResponse
from django.core.validators import validate_email
from django.core.exceptions import ValidationError
from django.core.mail import send_mail
import django.contrib.auth as auth
from django.views.decorators.csrf import csrf_exempt, csrf_protect
from django.http import QueryDict

from feeder.models import *
from demo.config import CODE
from user.models import Verification, User
import file
import hashlib

import time

@csrf_exempt
def register_validation(request):
    code = -1
    msg = ''
    user_profile = {}
    if request.method == 'POST':
        email = request.POST.get('email')
        username = request.POST.get('username')
        password = request.POST.get('password')
        verification_code = request.POST.get('verificationCode')
        if email is None or username is None or password is None or verification_code is None:
            code = CODE['parameter_error']
            msg = 'wrong parameter'
        else:
            email += "@pku.edu.cn"
            try:
                validate_email(email)
                if User.objects.filter(pku_mail=email).exists():
                    code = CODE['database_error']
                    msg = 'email already registered'
                elif str(verification_code) \
                        != Verification.objects.get(pku_mail=email).verification_code:
                    code = CODE['parameter_error']
                    msg = 'wrong verification code'
                elif User.objects.filter(username=username).exists():
                    code = CODE['database_error']
                    msg = "duplicate username"
                else:
                    user = User.objects.create_user(username=username, password=password,
                                            pku_mail=email)
                    user.sha256_password = hashlib.sha256(bytes(password, encoding="utf-8")).hexdigest()
                    user.save()
                    auth.login(request, user)
                    user = User.objects.get(id=user.id)
                    user_profile['user'] = {'name':user.username, "userID":user.id}
                    user_profile['avatar'] = user.avatar if user.avatar != '' else '/static/user/avatar_default.jpg'
                    user_profile['email'] = user.pku_mail 
                    user_profile['whatsup'] = user.whatsup
                    user_profile['is_admin'] = user.is_superuser
                    user_profile['feed'] = [1, 2] if user.is_superuser else [] # TODO
                    code = CODE['success']
                    msg = 'success'
            except ValidationError:
                code = CODE['parameter_error']
                msg = 'wrong email'
    else:
        code = CODE['method_error']
        msg = 'wrong method'

    response = {
        'code': code,
        'data': {
            'msg':msg,
            "profile": user_profile,
        }
    }
    return JsonResponse(response)

@csrf_exempt
def register(request):
    code = -1
    msg = ''
    if request.method == 'POST':
        email = request.POST.get('email')
        if email is None:
            code = CODE['parameter_error']
            msg = 'wrong email'
        else:
            email += "@pku.edu.cn"
            try:
                validate_email(email)
                if User.objects.filter(pku_mail=email).exists():
                    code = CODE['database_error']
                    msg = 'email already registered'
                else:
                    result = Verification.get_verification_code(email)
                    if result[0] == -1:
                        code = CODE['user_error']
                        msg = 'repeated acquisition in 30 seconds'
                    else:
                        send_mail(u'燕园吸猫助手验证码', str(result[1]), 'pkucat_helper@sina.com', [email])
                        code = CODE['success']
                        msg = 'success'
                    
            except ValidationError:
                code = CODE['parameter_error']
                msg = 'wrong email'
    else:
        code = CODE['method_error']
        msg = 'wrong method'

    response = {
        'code': code,
        'data': {
            'msg':msg
        }
    }
    return JsonResponse(response)

@csrf_exempt
def login(request):
    code = -1
    msg = ''
    user_profile = {}
    if request.method == 'POST':
        email = request.POST.get('email')
        password = request.POST.get('password')
        if email is None or password is None:
            code = CODE['parameter_error']
            msg = 'wrong parameter'
        else:
            email += "@pku.edu.cn"
            if not User.objects.filter(pku_mail=email).exists():
                code = CODE['parameter_error']
                msg = 'email or password error'
            else:
                username = User.objects.get(pku_mail=email).username
                user = User.objects.get(pku_mail=email)
                if user.sha256_password != password:
                    user = None
                # user = auth.authenticate(username=username, password=password)
                if user is None:
                    code = CODE['parameter_error']
                    msg = 'email or password error'
                elif request.user.is_authenticated and request.user.id != user.id:
                    code = CODE['user_error']
                    msg = 'error'
                else:
                    auth.login(request, user)
                    user = User.objects.get(id=user.id)
                    user_profile['user'] = {'name':user.username, "userID":user.id}
                    user_profile['avatar'] = user.avatar if user.avatar != '' else '/static/user/avatar_default.jpg'
                    user_profile['email'] = user.pku_mail 
                    user_profile['whatsup'] = user.whatsup
                    user_profile['is_admin'] = user.is_superuser
                    user_profile['feed'] = [1, 2] if user.is_superuser else [] # TODO
                    code = CODE['success']
                    msg = 'success'
    else:
        code = CODE['method_error']
        msg = 'wrong method'

    response = {
        'code': code,
        'data': {
            'msg':msg,
            "profile": user_profile,
        }
    }
    return JsonResponse(response)

@csrf_exempt
def password(request):
    code = -1
    msg = ''
    user_profile = {}
    if request.method == 'GET':
        email = request.GET.get('email')
        if email is None:
            code = CODE['parameter_error']
            msg = 'wrong parameter'
        else:
            email += "@pku.edu.cn"
            try:
                validate_email(email)
                user = User.objects.filter(pku_mail=email)
                if user.exists():
                    user = user[0]
                    result = Verification.get_verification_code(user.pku_mail, "password")
                    if result[0] == -1:
                        code = CODE['user_error']
                        msg = 'repeated acquisition in 30 seconds'
                    else:
                        send_mail(u'燕园吸猫助手验证码', str(result[1]), 'pkucat_helper@sina.com', [email])
                        code = CODE['success']
                        msg = 'success'
                else:
                    code = CODE['parameter_error']
                    msg = 'wrong email'
            except ValidationError:
                code = CODE['parameter_error']
                msg = 'wrong email'
    elif request.method == 'POST':
        email = request.POST.get('email')
        password = request.POST.get('password')
        verification_code = request.POST.get('verificationCode')
        if email is None or password is None or verification_code is None:
            code = CODE['parameter_error']
            msg = 'wrong parameter'
        else:
            email += "@pku.edu.cn"
            try:
                validate_email(email)
                veri = Verification.objects.filter(pku_mail=email)
                if veri.exists() and veri[0].verification_code == str(verification_code):
                    user = User.objects.get(pku_mail=email)
                    user.set_password(password)
                    user.sha256_password = hashlib.sha256(bytes(password, encoding="utf-8")).hexdigest()
                    user.save()
                    code = CODE['success']
                    msg = 'success'
                else:
                    code = CODE['parameter_error']
                    msg = 'wrong verification code'
            except ValidationError:
                code = CODE['parameter_error']
                msg = 'wrong email'
    else:
        code = CODE['method_error']
        msg = 'wrong method'

    response = {
        'code': code,
        'data': {
            'msg':msg,
        }
    }
    return JsonResponse(response)

@csrf_exempt
def logout(request):
    code = -1
    msg = ''
    if request.method == 'POST':
        auth.logout(request)
        code = CODE['success']
        msg = 'success'
    else:
        code = CODE['method_error']
        msg = 'wrong method'
    response = {
        'code': code,
        'data': {
            'msg':msg
        }
    }
    return JsonResponse(response)

@csrf_exempt
def profile(request):
    code = -1
    msg = ''
    user_profile = {}
    if not request.user.is_authenticated:
        code = CODE['user_error']
        msg = "not authorized"
    else:
        if request.method == 'GET':
            user_id = request.GET.get("userID") if request.GET.get("userID") else request.user.id
            if User.objects.filter(id=user_id).exists():
                user = User.objects.get(id=user_id)
                user_profile['user'] = {'name':user.username, "userID":user.id}
                user_profile['avatar'] = user.avatar if user.avatar != '' else '/static/user/avatar_default.jpg'
                user_profile['email'] = user.pku_mail 
                user_profile['whatsup'] = user.whatsup
                user_profile['is_admin'] = user.is_superuser
                user_profile['feed'] = []
                for feed in Feed.objects.filter(feeder=user):
                    user_profile['feed'].append(feed.cat.id)
                code = CODE['success']
                msg = 'success'
            else:
                code = CODE['database_error']
                msg = 'user does not exist'
        elif request.method == 'PUT':
            user = User.objects.get(id=request.user.id)
            
            PUT = QueryDict(request.body)
            # PUT = eval(str(request.body, encoding="utf-8"))
            username = PUT.get('username')
            avatar = PUT.get('avatar')
            whatsup = PUT.get('whatsup')

            if username or avatar or whatsup:
                msg = ""
                if username:
                    if not User.objects.filter(username=username).exists():
                        user.username = username
                    elif username != user.username:
                        code = CODE['parameter_error']
                        msg += "duplicate username/"
                
                if avatar:
                    if file.exists_file(avatar):
                        user.avatar = avatar
                    else:
                        code = CODE['parameter_error']
                        msg += "unexisted avatar/"
                
                if whatsup:
                    user.whatsup = whatsup

                if not msg:
                    user.save()
                    code = CODE['success']
                    msg = "success"
                else:
                    msg = msg[:-1]
            else:
                code = CODE['parameter_error']
                msg = 'parameter error'
        else:
            code = CODE['method_error']
            msg = 'wrong method' + request.method
    response = {
        'code': code,
        'data': {
            'msg': msg,
            'profile': user_profile,
        }
    }
    return JsonResponse(response)
import requests
@csrf_exempt
def mytest(request):
    msg = ''
    msg += "request.method: " + repr(request.method) + '\n'
    msg += "request.body:   " + repr(request.body) + '\n'
    msg += "request.GET:    " + repr(request.GET) + '\n'
    msg += "request.FILES:  " + repr(request.FILES) + '\n'
    msg += "request.POST:   " + repr(request.POST) + '\n'
    msg += "QueryDict:   " + repr(QueryDict(request.body)) + '\n'
    # hashlib.sha256(bytes("ZHD123",encoding="utf-8")).hexdigest()

    return HttpResponse(msg)
