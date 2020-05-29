#-*- coding:utf-8 _*-  
""" 
@author: tengyuanjian
@file: views.py 
@time: 2020/05/16
@contact: tyyy@pku.edu.cn
"""

from django.test import TestCase
from django.http import HttpResponse, JsonResponse
from user.models import Verification, User
from archive.models import Cat
from .models import ApplicationFeeder

from demo.config import CODE


# apply api的测试
class ApplyTests(TestCase):
    def login(self):
        # 创建用户并进行登录
        user = User.objects.create_user(username='tyy', password='123456', pku_mail='tyyy@pku.edu.cn')
        self.client.post('/user/login', {'email':'tyyy', 'password':'123456'})
        return user

    def test_not_login(self):
        # case：未登录
        response = self.client.get('/feeder/apply')
        self.assertEqual(type(response), JsonResponse)
        json = response.json()
        self.assertEqual(json['code'], CODE['user_error'])
        self.assertEqual(json['data']['msg'], "not authorized")

    def test_successPost(self):
        self.login()
        # 添加猫咪
        newCat = Cat.objects.create(name='dawei', catID=1)
        newCat.save()

        # case: 有效的猫咪ID进行申请
        response = self.client.post('/feeder/apply', {'catID': newCat.id})
        self.assertEqual(type(response), JsonResponse)
        json = response.json()
        self.assertEqual(json['code'], CODE['success'])
        self.assertEqual(json['data']['msg'], "success")
    
    def test_wrongCat(self):
        self.login()
        # case: 无效的猫咪ID进行申请
        response = self.client.post('/feeder/apply', {'catID': 0})
        self.assertEqual(type(response), JsonResponse)
        json = response.json()
        self.assertEqual(json['code'], CODE['database_error'])
        self.assertEqual(json['data']['msg'], "no match cat in database")

    def test_wrongMethod(self):
        self.login()
        # case: 错误的方法
        response = self.client.get('/feeder/apply', {'catID': 0})
        self.assertEqual(type(response), JsonResponse)
        json = response.json()
        self.assertEqual(json['code'], CODE['method_error'])
        self.assertEqual(json['data']['msg'], "wrong method")

        response = self.client.put('/feeder/apply', {'catID': 0})
        self.assertEqual(type(response), JsonResponse)
        json = response.json()
        self.assertEqual(json['code'], CODE['method_error'])
        self.assertEqual(json['data']['msg'], "wrong method")

    def test_wrongParameter_post(self):
        self.login()
        # case: 错误的参数
        response = self.client.post('/feeder/apply', {'cat': 1})
        self.assertEqual(type(response), JsonResponse)
        json = response.json()
        self.assertEqual(json['code'], CODE['parameter_error'])
        self.assertEqual(json['data']['msg'], "wrong parameters")

    def test_successDelete(self):
        user = self.login()
        # 添加猫咪及申请
        newCat = Cat.objects.create(name='dawei', catID=1)
        newCat.save()
        newApply = ApplicationFeeder.objects.create(feeder=user, cat=newCat)

        # case: 成功删除申请
        response = self.client.delete('/feeder/apply', {'applyID': newApply.id})
        self.assertEqual(type(response), JsonResponse)
        json = response.json()
        self.assertEqual(json['code'], CODE['success'])
        self.assertEqual(json['data']['msg'], "success")

    def test_wrongParameter_delete(self):
        self.login()
        # case: 错误的参数
        response = self.client.delete('/feeder/apply', {'apply': 1})
        self.assertEqual(type(response), JsonResponse)
        json = response.json()
        self.assertEqual(json['code'], CODE['parameter_error'])
        self.assertEqual(json['data']['msg'], "wrong parameters")        

    def test_wrongApply(self):
        self.login()
        # case: 错误的参数
        response = self.client.delete('/feeder/apply', {'applyID': 5})
        self.assertEqual(type(response), JsonResponse)
        json = response.json()
        self.assertEqual(json['code'], CODE['database_error'])
        self.assertEqual(json['data']['msg'], "no match application in database")  


# agree api的测试
class AgreeTests(TestCase):
    def login(self):
        # 创建用户并进行登录
        user = User.objects.create_user(username='tyy', password='123456', pku_mail='tyyy@pku.edu.cn', is_superuser=True)
        self.client.post('/user/login', {'email':'tyyy', 'password':'123456'})
        return user

    def login_notSuperuser(self):
        User.objects.create_user(username='tyj', password='123456', pku_mail='1600012607@pku.edu.cn', is_superuser=False)
        self.client.post('/user/login', {'email':'1600012607', 'password':'123456'})

    def test_not_login(self):
        # case：未登录
        response = self.client.get('/feeder/agree')
        self.assertEqual(type(response), JsonResponse)
        json = response.json()
        self.assertEqual(json['code'], CODE['user_error'])
        self.assertEqual(json['data']['msg'], "not authorized")
    
    def test_successAgree(self):
        # 登陆
        user = self.login()
        # 添加猫咪
        newCat = Cat.objects.create(name='dawei', catID=1)
        newCat.save()
        # 添加申请
        newApply = ApplicationFeeder.objects.create(feeder=user, cat=newCat)

        # case：成功同意申请
        response = self.client.post('/feeder/agree', {'applyID': newApply.id})
        self.assertEqual(type(response), JsonResponse)
        json = response.json()
        self.assertEqual(json['code'], CODE['success'])
        self.assertEqual(json['data']['msg'], "success")

    def test_notSuperUser(self):
        # 登陆
        self.login_notSuperuser()
        # 添加猫咪
        newCat = Cat.objects.create(name='dawei', catID=1)
        newCat.save()
        # 进行申请
        self.client.post('/feeder/apply', {'catID': newCat.id})

        # case：用户非管理员
        response = self.client.post('/feeder/agree', {'applyID': 1})
        self.assertEqual(type(response), JsonResponse)
        json = response.json()
        self.assertEqual(json['code'], CODE['user_error'])
        self.assertEqual(json['data']['msg'], "user is not super user")

    def test_wrongApply(self):
        # 登陆
        user = self.login()
        # case：无效的申请id
        response = self.client.post('/feeder/agree', {'applyID': 0})
        self.assertEqual(type(response), JsonResponse)
        json = response.json()
        self.assertEqual(json['code'], CODE['database_error'])
        self.assertEqual(json['data']['msg'], "no match application in database")

    def test_wrongParameter(self):
        # 登陆
        user = self.login()
        # case：无效的申请id
        response = self.client.post('/feeder/agree', {'apply': 0})
        self.assertEqual(type(response), JsonResponse)
        json = response.json()
        self.assertEqual(json['code'], CODE['parameter_error'])
        self.assertEqual(json['data']['msg'], "wrong parameters")

    def test_wrongMethod(self):
        self.login()
        # case: 错误的方法
        response = self.client.get('/feeder/agree', {'applyID': 0})
        self.assertEqual(type(response), JsonResponse)
        json = response.json()
        self.assertEqual(json['code'], CODE['method_error'])
        self.assertEqual(json['data']['msg'], "wrong method")

        response = self.client.put('/feeder/agree', {'applyID': 0})
        self.assertEqual(type(response), JsonResponse)
        json = response.json()
        self.assertEqual(json['code'], CODE['method_error'])
        self.assertEqual(json['data']['msg'], "wrong method")

        response = self.client.delete('/feeder/agree', {'applyID': 0})
        self.assertEqual(type(response), JsonResponse)
        json = response.json()
        self.assertEqual(json['code'], CODE['method_error'])
        self.assertEqual(json['data']['msg'], "wrong method")


# feeder api的测试
class FeedersTests(TestCase):
    def login(self):
        # 创建用户并进行登录
        user = User.objects.create_user(username='tyy', password='123456', pku_mail='tyyy@pku.edu.cn', is_superuser=True)
        self.client.post('/user/login', {'email':'tyyy', 'password':'123456'})
        return user
    
    def login_notSuperuser(self):
        User.objects.create_user(username='tyj', password='123456', pku_mail='1600012607@pku.edu.cn', is_superuser=False)
        self.client.post('/user/login', {'email':'1600012607', 'password':'123456'}) 

    def test_not_login(self):
        # case：未登录
        response = self.client.get('/feeder/feeders')
        self.assertEqual(type(response), JsonResponse)
        json = response.json()
        self.assertEqual(json['code'], CODE['user_error'])
        self.assertEqual(json['data']['msg'], "not authorized")

    def test_noFeeder(self):
        # 登陆
        user = self.login()

        # case：成功获取饲养员
        response = self.client.get('/feeder/feeders')
        self.assertEqual(type(response), JsonResponse)
        json = response.json()
        self.assertEqual(json['code'], CODE['database_error'])
        self.assertEqual(json['data']['msg'], "no feeder yet")

    def test_successGetAll(self):
        # 登陆
        user = self.login()
        # 添加猫咪
        newCat = Cat.objects.create(name='dawei', catID=1)
        newCat.save()
        # 添加申请
        newApply = ApplicationFeeder.objects.create(feeder=user, cat=newCat)
        response = self.client.post('/feeder/agree', {'applyID': newApply.id})

        # case：成功获取饲养员
        response = self.client.get('/feeder/feeders')
        self.assertEqual(type(response), JsonResponse)
        json = response.json()
        self.assertEqual(json['code'], CODE['success'])
        self.assertEqual(json['data']['msg'], "success")

    def test_successGetOne(self):
        # 登陆
        user = self.login()
        # 添加猫咪
        newCat = Cat.objects.create(name='dawei', catID=1)
        newCat.save()
        # 添加申请
        newApply = ApplicationFeeder.objects.create(feeder=user, cat=newCat)
        response = self.client.post('/feeder/agree', {'applyID': newApply.id})

        # case：成功获取饲养员
        response = self.client.get('/feeder/feeders', {'catID': newCat.id})
        self.assertEqual(type(response), JsonResponse)
        json = response.json()
        self.assertEqual(json['code'], CODE['success'])
        self.assertEqual(json['data']['msg'], "success")

    def test_wrongCat(self):
        # 登陆
        user = self.login()
        # 添加猫咪
        newCat = Cat.objects.create(name='dawei', catID=1)
        newCat.save()
        # 添加申请
        newApply = ApplicationFeeder.objects.create(feeder=user, cat=newCat)
        response = self.client.post('/feeder/agree', {'applyID': newApply.id})

        # case：成功获取饲养员
        response = self.client.get('/feeder/feeders', {'catID': 0})
        self.assertEqual(type(response), JsonResponse)
        json = response.json()
        self.assertEqual(json['code'], CODE['database_error'])
        self.assertEqual(json['data']['msg'], "no feeder for this cat")

    def test_catWithNoFeeder(self):
        # 登陆
        user = self.login()
        # 添加猫咪
        newCat = Cat.objects.create(name='dawei', catID=1)
        newCat.save()

        # case：成功获取饲养员
        response = self.client.get('/feeder/feeders', {'catID': newCat.id})
        self.assertEqual(type(response), JsonResponse)
        json = response.json()
        self.assertEqual(json['code'], CODE['database_error'])
        self.assertEqual(json['data']['msg'], "no feeder for this cat")

    def test_wrongMethod(self):
        self.login()
        # case: 错误的方法
        response = self.client.post('/feeder/feeders',)
        self.assertEqual(type(response), JsonResponse)
        json = response.json()
        self.assertEqual(json['code'], CODE['method_error'])
        self.assertEqual(json['data']['msg'], "wrong method")

        response = self.client.put('/feeder/feeders')
        self.assertEqual(type(response), JsonResponse)
        json = response.json()
        self.assertEqual(json['code'], CODE['method_error'])
        self.assertEqual(json['data']['msg'], "wrong method")

        response = self.client.delete('/feeder/feeders')
        self.assertEqual(type(response), JsonResponse)
        json = response.json()
        self.assertEqual(json['code'], CODE['method_error'])
        self.assertEqual(json['data']['msg'], "wrong method") 


# applys api的测试
class ApplysTests(TestCase):
    def login(self):
        # 创建用户并进行登录
        user = User.objects.create_user(username='tyy', password='123456', pku_mail='tyyy@pku.edu.cn', is_superuser=True)
        self.client.post('/user/login', {'email':'tyyy', 'password':'123456'})
        return user
    
    def login_notSuperuser(self):
        User.objects.create_user(username='tyj', password='123456', pku_mail='1600012607@pku.edu.cn', is_superuser=False)
        self.client.post('/user/login', {'email':'1600012607', 'password':'123456'}) 
    
    def test_not_login(self):
        # case：未登录
        response = self.client.get('/feeder/applys')
        self.assertEqual(type(response), JsonResponse)
        json = response.json()
        self.assertEqual(json['code'], CODE['user_error'])
        self.assertEqual(json['data']['msg'], "not authorized")

    def test_notSuperUser(self):
        # 登陆
        self.login_notSuperuser()

        # case：用户非管理员
        response = self.client.get('/feeder/applys')
        self.assertEqual(type(response), JsonResponse)
        json = response.json()
        self.assertEqual(json['code'], CODE['user_error'])
        self.assertEqual(json['data']['msg'], "user is not super user")

    def test_noApply(self):
        # 登陆
        self.login()

        # case：数据库中无申请数据
        response = self.client.get('/feeder/applys')
        self.assertEqual(type(response), JsonResponse)
        json = response.json()
        self.assertEqual(json['code'], CODE['database_error'])
        self.assertEqual(json['data']['msg'], "no application in database")

    def test_success(self):
        # 登陆
        user = self.login()
        # 添加猫咪
        newCat = Cat.objects.create(name='dawei', catID=1)
        newCat.save()
        # 添加申请
        newApply = ApplicationFeeder.objects.create(feeder=user, cat=newCat)

        # case：成功获取申请
        response = self.client.get('/feeder/applys')
        self.assertEqual(type(response), JsonResponse)
        json = response.json()
        self.assertEqual(json['code'], CODE['success'])
        self.assertEqual(json['data']['msg'], "success")
    
    def test_wrongMethod(self):
        self.login()
        # case: 错误的方法
        response = self.client.post('/feeder/applys',)
        self.assertEqual(type(response), JsonResponse)
        json = response.json()
        self.assertEqual(json['code'], CODE['method_error'])
        self.assertEqual(json['data']['msg'], "wrong method")

        response = self.client.put('/feeder/applys')
        self.assertEqual(type(response), JsonResponse)
        json = response.json()
        self.assertEqual(json['code'], CODE['method_error'])
        self.assertEqual(json['data']['msg'], "wrong method")

        response = self.client.delete('/feeder/applys')
        self.assertEqual(type(response), JsonResponse)
        json = response.json()
        self.assertEqual(json['code'], CODE['method_error'])
        self.assertEqual(json['data']['msg'], "wrong method") 