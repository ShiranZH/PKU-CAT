from django.urls import path, include

from . import views
from archive import views as archive_views

urlpatterns = [
    path('register_validation', views.register_validation),
    path('register/validation', views.register_validation),
    path('register', views.register),
    path('login', views.login),
    path('logout', views.logout),
    path('profile', views.profile),
    path('password', views.password),
    path('archive', archive_views.archive),
    path('archive/', archive_views.archive),
    path('archives', archive_views.archives),
    path('archives/', archive_views.archives),
    path('post', include('post.urls')),
    path('post/', include('post.urls')),
    path('posts', include('post.urls')),
    path('posts', include('post.urls')),
    path('feeder', include('feeder.urls')),
    path('feeder/', include('feeder.urls')),
]
