from django.db import models
import django.contrib.auth.models

'''
User: 用户类

继承自django.contrib.auth.models.User的属性:
    id: 唯一标识符
    username: 用户名(不可重复)
    password: 密码(加密保存)
    email: 邮箱
    is_superuser: 是否为管理员

其他属性:
    avatar: 头像的uri
    whatsup: 个性签名
'''
class User(django.contrib.auth.models.User):
    avatar = models.CharField(max_length=128)
    whatsup = models.CharField(max_length=128)


    