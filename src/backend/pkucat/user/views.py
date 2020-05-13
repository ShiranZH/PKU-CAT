from django.shortcuts import render
from django.http import HttpResponse, JsonResponse
from django.core.validators import validate_email
from django.core.exceptions import ValidationError
from django.core.mail import send_mail
import django.contrib.auth as auth

from demo.config import CODE
from .models import Verification, User

def register_validation(request):
    if request.method == 'POST':
        email = request.POST.get('email')
        username = request.POST.get('username')
        password = request.POST.get('password')
        verification_code = request.POST.get('verificationCode')
        if email is None or username is None or password is None or verification_code is None:
            code = CODE['parameter_error']
            msg = 'wrong parameter'
        else:
            try:
                validate_email(email)
                if email.split('@')[1] != 'pku.edu.cn':
                    code = CODE['parameter_error']
                    msg = 'wrong email'
                elif User.objects.filter(pku_mail=email).exists():
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

def register(request):
    if request.method == 'POST':
        email = request.POST.get('email')
        if email is None:
            code = CODE['parameter_error']
            msg = 'wrong email'
        else:
            try:
                validate_email(email)
                if email.split('@')[1] != 'pku.edu.cn':
                    code = CODE['parameter_error']
                    msg = 'wrong email'
                elif User.objects.filter(pku_mail=email).exists():
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

def login(request):
    if request.method == 'POST':
        username = request.POST.get('username')
        password = request.POST.get('password')
        if username is None or password is None:
            code = CODE['parameter_error']
            msg = 'wrong parameter'
        elif not User.objects.filter(username=username).exists():
            code = CODE['parameter_error']
            msg = 'wrong parameter'
        else:
            user = auth.authenticate(username=username, password=password)
            if user is None:
                code = CODE['parameter_error']
                msg = 'username or password error'
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

def logout(request):
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

def profile(request):
    return HttpResponse('profile')


