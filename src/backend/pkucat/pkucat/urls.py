from django.contrib import admin
from django.urls import path, include
from django.http import HttpResponse

urlpatterns = [
    path('demo', include('demo.urls')),
    path('file', include('file.urls')),
    path('file/', include('file.urls')),
    path('user', include('user.urls')),
    path('user/', include('user.urls')),
    path('pku/cat/helper/', admin.site.urls),
    path('test/hello/', include('demo.urls')),
    path('admin/', admin.site.urls),
    path('post/', include('post.urls')),
    path('posts/', include('post.urls')),
    path('archive/', include('archive.urls')),
    path('archives/', archive_views.archives, name='archive2'),
    path('feeder/', include('feeder.urls')),
]

def cat_not_found(request, exception):
    next_url = request.path_info
    return HttpResponse('404 '+str(next_url))
    
handler404 = cat_not_found