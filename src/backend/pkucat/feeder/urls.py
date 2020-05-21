# -*- coding:utf-8 _*-
""" 
@author: tengyuanjian
@file: urls.py
@time: 2020/04/16
@contact: tyyy@pku.edu.cn
"""

from django.urls import path
from . import views


urlpatterns = [
    path('feeders', views.feeders),
    path('apply', views.apply),
    path('applys', views.applys),
    path('agree', views.agree),
]