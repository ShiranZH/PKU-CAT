# -*- coding: utf-8 -*-
from django.db import models
import django.contrib.auth.models
from django.utils import timezone
import random

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
    avatar = models.CharField(max_length=128, blank=True)
    whatsup = models.CharField(max_length=128, blank=True)
    pku_mail = models.EmailField(unique=True)
    sha256_password = models.CharField(max_length=64, blank=True)
    
    class Meta:
        verbose_name = '用户'
        verbose_name_plural = '用户'

'''
验证码类
    同一个邮箱的验证码30s内只能获取一次
    验证码记录每两天清理一次

属性:
    pku_mail: 邮箱
    verification_code: 验证码
    update_date: 上次请求时间
方法:
    get_verification_code(pku_mail)
        获取邮箱对应的验证码，成功返回(0, code)
        30s内重复请求返回(-1, -1)

静态属性:
    clear_date:
'''
class Verification(models.Model):
    pku_mail = models.EmailField(unique=True)
    verification_code = models.CharField(max_length=6)
    update_date = models.DateTimeField()
    veri_type = models.CharField(max_length=32) # "register" 或者 "password"

    clear_date = timezone.now()

    class Meta:
        verbose_name = '验证码'
        verbose_name_plural = '验证码'

    def get_verification_code(pku_mail, veri_type="register"):
        if Verification.objects.filter(pku_mail=pku_mail).exists():
            veri = Verification.objects.get(pku_mail=pku_mail)
            now = timezone.now()
            if veri_type == veri.veri_type and (now-veri.update_date).total_seconds() < 30:
                return (-1, -1)
            else:
                verification_code = random.randint(100000, 999999)
                veri.verification_code = verification_code
                veri.update_date = timezone.now()
                veri.veri_type = veri_type
                veri.save()
                return (0, verification_code)  
        else:
            verification_code = random.randint(100000, 999999) 
            veri = Verification(pku_mail=pku_mail,
                                verification_code=verification_code,
                                update_date=timezone.now())
            veri.veri_type = veri_type
            veri.save()
            return (0, verification_code)                                


    