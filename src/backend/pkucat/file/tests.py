# -*- coding: utf-8 -*-
from django.test import TestCase
from django.http import HttpResponse, JsonResponse
from user.models import Verification, User

from demo.config import CODE

class FileTests(TestCase):
    def test_upload(self):
        User.objects.create_user(username='pkuzhd', password='123456', pku_mail='pkuzhd@pku.edu.cn')
        
        # 未登录
        response = self.client.post('/file', {'email':'pkuzhd', 'password':'123456'})
        self.assertEqual(type(response), JsonResponse)
        response = response.json()
        self.assertEqual(response['code'], CODE['user_error'])

        # 登录
        response = self.client.post('/user/login', {'email':'pkuzhd', 'password':'123456'})
        self.assertEqual(type(response), JsonResponse)
        response = response.json()
        self.assertEqual(response['code'], CODE['success'])

        # 方法错误
        response = self.client.get('/user/login', {'email':'pkuzhd', 'password':'123456'})
        self.assertEqual(type(response), JsonResponse)
        response = response.json()
        self.assertEqual(response['code'], CODE['method_error'])

        response = self.client.put('/user/login', {'email':'pkuzhd', 'password':'123456'})
        self.assertEqual(type(response), JsonResponse)
        response = response.json()
        self.assertEqual(response['code'], CODE['method_error'])

        response = self.client.delete('/user/login', {'email':'pkuzhd', 'password':'123456'})
        self.assertEqual(type(response), JsonResponse)
        response = response.json()
        self.assertEqual(response['code'], CODE['method_error'])

        # 参数错误
        response = self.client.post('/file', {'email':'pkuzhd', 'password':'123456'})
        self.assertEqual(type(response), JsonResponse)
        response = response.json()
        self.assertEqual(response['code'], CODE['parameter_error'])

        # 成功上传
        files = {'picture': [open('/home/zhd/PKU-CAT/README.md','rb')]}
        response = self.client.post('/file', files)
        self.assertEqual(type(response), JsonResponse)
        response = response.json()
        self.assertEqual(response['code'], CODE['success'])
