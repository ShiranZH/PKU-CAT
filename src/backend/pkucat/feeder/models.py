# -*- coding:utf-8 _*-
""" 
@author: tengyuanjian
@file: models.py
@time: 2020/04/11
@contact: tyyy@pku.edu.cn
"""

from django.db import models
from .user.models import User
from .archive.models import Cat


class Feed(models.Model):
    """
    饲养员信息
    feeder：饲养员，User对象
    cat：猫咪，Cat对象
    """
    feeder = models.ForeignKey(
        User,
        on_delete = models.CASCADE
    )
    cat = models.ForeignKey(
        Cat,
        on_delete = models.CASCADE
    )

    class Meta:
        unique_together = ('feeder', 'cat')


class Application_Feeder(models.Model):
    """
    饲养员申请信息
    feeder：申请成为饲养员的用户，User对象
    cat：申请饲养的猫咪，Cat对象
    """
    feeder = models.ForeignKey(
        User,
        on_delete = models.CASCADE
    )
    cat = models.ForeignKey(
        Cat,
        on_delete = models.CASCADE
    )

    class Meta:
        unique_together = ('feeder', 'cat')