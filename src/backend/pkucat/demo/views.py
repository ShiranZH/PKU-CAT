from django.shortcuts import render
from django.http import HttpResponse

def demo(request):
    return render(request, 'demo/demo.html')


def cat_not_found(request, exception):
    
    next_url = request.path_info
    return HttpResponse(str(next_url))
    # return render(request, 'demo/404.html', status=404)
