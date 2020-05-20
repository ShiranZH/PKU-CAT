from django.urls import path

from . import views

urlpatterns = [
    path('', views.post),
    path('demo/', views.demo),
    path('comments/', views.comments),
    path('favor/', views.favor),
    path('comment/', views.comment),
]