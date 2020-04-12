from django.contrib import admin
from django.urls import path, include
from django.http import HttpResponse

urlpatterns = [
    path('demo', include('demo.urls')),
    path('user', include('user.urls')),
    path('user/', include('user.urls')),
    path('pku/cat/helper/', admin.site.urls),
]

def cat_not_found(request, exception):
    next_url = request.path_info
    return HttpResponse('404 '+str(next_url))
    
handler404 = cat_not_found
