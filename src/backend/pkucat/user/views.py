from django.shortcuts import render
from django.http import HttpResponse, JsonResponse
from django.core.validators import validate_email
from django.core.exceptions import ValidationError
from django.core.mail import send_mail
import django.contrib.auth as auth
from django.views.decorators.csrf import csrf_exempt, csrf_protect
from django.http import QueryDict

from demo.config import CODE
from .models import Verification, User
import file

@csrf_exempt
def register_validation(request):
    code = -1
    msg = ''
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
                    User.objects.create_user(username=username, password=password,
                                            pku_mail=email)
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
                msg = 'wrong parameter'
            else:
                username = User.objects.get(pku_mail=email).username
                user = auth.authenticate(username=username, password=password)
                if user is None:
                    code = CODE['parameter_error']
                    msg = 'email or password error'
                elif request.user.is_authenticated:
                    if request.user.id != user.id:
                        code = CODE['user_error']
                        msg = 'error'
                    else:
                        code = CODE['success']
                        msg = 'success'
                else:
                    auth.login(request, user)
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
        user = User.objects.get(id=request.user.id)
        if request.method == 'GET':
            user_profile['user'] = {'name':user.username, "userID":user.id}
            user_profile['avatar'] = user.avatar if user.avatar != '' else 'static/user/avatar_default.jpg'
            user_profile['mail'] = user.pku_mail 
            user_profile['whatsup'] = user.whatsup
            code = CODE['success']
            msg = 'success'
        elif request.method == 'PUT':
            PUT = QueryDict(request.body)

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
            msg = 'wrong method'

    response = {
        'code': code,
        'data': {
            'msg': msg,
            'profile': user_profile,
        }
    }
    return JsonResponse(response)

