from django.shortcuts import render
from django.http import HttpResponse, JsonResponse
from demo.config import CODE


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


