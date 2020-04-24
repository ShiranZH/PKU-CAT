<<<<<<< HEAD
from django.shortcuts import render
from django.http import HttpResponse

def demo(request):
    return render(request, 'demo/demo.html')


def cat_not_found(request, exception):
    return render(request, 'demo/404.html', status=404)
=======
from django.shortcuts import render
from django.http import HttpResponse

def demo(request):
    return render(request, 'demo/demo.html')


def cat_not_found(request, exception):
    return render(request, 'demo/404.html', status=404)
>>>>>>> e2d708ed7687c459bea18b7d72b70579e7b4d609
