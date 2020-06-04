from django.urls import path

from . import views

urlpatterns = [
    path('', views.post),
    path('post', views.post),
    path('post/', views.post),
    path('posts', views.posts),
    path('posts/', views.posts),
    path('demo/', views.demo),
    path('comments', views.comments),
    path('comments/', views.comments),
    path('favor', views.favor),
    path('favor/', views.favor),
    path('comment', views.comment),
    path('comment/', views.comment),
]