from django.shortcuts import render
from django.http import HttpResponse, JsonResponse
from demo.config import CODE


def cat_not_found(request, exception):
    next_url = request.path_info
    return HttpResponse('404 '+str(next_url)+str(CODE))


def register_validation(request):
    return HttpResponse('register_validation')

def register(request):
    return HttpResponse('register')

def login(request):
    return HttpResponse('login')

def logout(request):
    return HttpResponse('logout')

def profile(request):
    return HttpResponse('profile')


