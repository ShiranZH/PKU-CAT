from django.urls import path, include

from . import views

urlpatterns = [
    path('register_validation', views.register_validation),
    path('register/validation', views.register_validation),
    path('register', views.register),
    path('login', views.login),
    path('logout', views.logout),
    path('profile', views.profile),
    path('password', views.password),
    path('test', views.mytest),
    path('archive', include('archive.urls')),
    path('archive/', include('archive.urls')),
]
