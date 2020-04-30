from django.urls import path

from . import views

urlpatterns = [
    path('', views.post),
    path('demo/', views.demo),
    path('index/', views.index),
    path('comments/', views.comments),
    path('favor/', views.favor),
    path('comment/', views.comment),
]