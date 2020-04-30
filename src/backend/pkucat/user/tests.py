# -*- coding: utf-8 -*-
from django.test import TestCase
from django.http import HttpResponse, JsonResponse
from .models import Verification, User

from demo.config import CODE

class UserTests(TestCase):
    def test_regiser(self):
        User.objects.create_user(username='pkucathelper', password='123456', pku_mail='pkucathelper@pku.edu.cn')

        # 方法错误
        response = self.client.get('/user/register')
        self.assertEqual(type(response), JsonResponse)
        response = response.json()
        self.assertEqual(response['code'], CODE['method_error'])

        response = self.client.put('/user/register')
        self.assertEqual(type(response), JsonResponse)
        response = response.json()
        self.assertEqual(response['code'], CODE['method_error'])

        response = self.client.delete('/user/register')
        self.assertEqual(type(response), JsonResponse)
        response = response.json()
        self.assertEqual(response['code'], CODE['method_error'])

        # 邮箱格式错误
        response = self.client.post('/user/register')
        self.assertEqual(type(response), JsonResponse)
        response = response.json()
        self.assertEqual(response['code'], CODE['parameter_error'])

        response = self.client.post('/user/register', {'email': "pkuzhd"})
        self.assertEqual(type(response), JsonResponse)
        response = response.json()
        self.assertEqual(response['code'], CODE['parameter_error'])

        response = self.client.post('/user/register', {'email': "pkuzhd@126.com"})
        self.assertEqual(type(response), JsonResponse)
        response = response.json()
        self.assertEqual(response['code'], CODE['parameter_error'])

        # 邮箱已注册
        response = self.client.post('/user/register', {'email': "pkucathelper@pku.edu.cn"})
        self.assertEqual(type(response), JsonResponse)
        response = response.json()
        self.assertEqual(response['code'], CODE['database_error'])

        # 成功
        response = self.client.post('/user/register', {'email': "pkuzhd@pku.edu.cn"})
        self.assertEqual(type(response), JsonResponse)
        response = response.json()
        self.assertEqual(response['code'], CODE['success'])

        # 成功
        response = self.client.post('/user/register', {'email': "1600012621@pku.edu.cn"})
        self.assertEqual(type(response), JsonResponse)
        response = response.json()
        self.assertEqual(response['code'], CODE['success'])

        # 30s内重复请求
        response = self.client.post('/user/register', {'email': "pkuzhd@pku.edu.cn"})
        self.assertEqual(type(response), JsonResponse)
        response = response.json()
        self.assertEqual(response['code'], CODE['user_error'])

        verification_code = Verification.objects.get(pku_mail='pkuzhd@pku.edu.cn').verification_code

        # 参数错误-无用户名
        response = self.client.post('/user/register_validation', 
            {'password':'123456', 'email': "pkuzhd@pku.edu.cn",
             'verificationCode':verification_code})
        self.assertEqual(type(response), JsonResponse)
        response = response.json()
        self.assertEqual(response['code'], CODE['parameter_error'])

        # 参数错误-无密码
        response = self.client.post('/user/register_validation', 
            {'username':'pkuzhd', 'email': "pkuzhd@pku.edu.cn",
             'verificationCode':verification_code})
        self.assertEqual(type(response), JsonResponse)
        response = response.json()
        self.assertEqual(response['code'], CODE['parameter_error'])

        # 参数错误-无邮箱
        response = self.client.post('/user/register_validation', 
            {'username':'pkuzhd', 'password':'123456',
             'verificationCode':verification_code})
        self.assertEqual(type(response), JsonResponse)
        response = response.json()
        self.assertEqual(response['code'], CODE['parameter_error'])

        # 参数错误-无验证码
        response = self.client.post('/user/register_validation', 
            {'username':'pkuzhd', 'password':'123456', 'email': "pkuzhd@pku.edu.cn"})
        self.assertEqual(type(response), JsonResponse)
        response = response.json()
        self.assertEqual(response['code'], CODE['parameter_error'])

        # 参数错误-验证码错误
        wrong_code = int(verification_code)-1 if int(verification_code)>500000 else int(verification_code)+1
        response = self.client.post('/user/register_validation', 
            {'username':'pkuzhd', 'password':'123456', 'email': "pkuzhd@pku.edu.cn",
             'verificationCode':wrong_code})
        self.assertEqual(type(response), JsonResponse)
        response = response.json()
        self.assertEqual(response['code'], CODE['parameter_error'])

        # 用户名重复
        response = self.client.post('/user/register_validation', 
            {'username':'pkucathelper', 'password':'123456', 'email': "pkuzhd@pku.edu.cn",
             'verificationCode':verification_code})
        self.assertEqual(type(response), JsonResponse)
        response = response.json()
        self.assertEqual(response['code'], CODE['database_error'])

        # 成功
        response = self.client.post('/user/register_validation', 
            {'username':'pkuzhd', 'password':'123456', 'email': "pkuzhd@pku.edu.cn",
             'verificationCode':verification_code})
        self.assertEqual(type(response), JsonResponse)
        response = response.json()
        self.assertEqual(response['code'], CODE['success'])

        verification_code = Verification.objects.get(pku_mail='1600012621@pku.edu.cn').verification_code

        # 成功
        response = self.client.post('/user/register_validation', 
            {'username':'pkuzhd', 'password':'123456', 'email': "1600012621@pku.edu.cn",
             'verificationCode':verification_code})
        self.assertEqual(type(response), JsonResponse)
        response = response.json()
        self.assertEqual(response['code'], CODE['database_error'])
