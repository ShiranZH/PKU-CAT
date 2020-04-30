import os
from django.test import TestCase, Client
from django.http import HttpResponse, JsonResponse
from .models import Post, Comment, Photo

from user.models import User

class PostTests(TestCase):
    def test_get_posts(self):
        User.objects.create_user(username='testuser', password='000000', pku_mail='testuser@pku.edu.cn', avatar='/home/ywq/cat1.jpg')
        user1 = User.objects.get(id=1)
        post1 = Post()
        post1.publisher=user1
        post1.text = 'test1'
        post1.save()
        photo1 = Photo()
        photo1.post_id = post1
        photo1.photo = '/home/ywq/cat1.jpg'
        photo1.save()

        post2 = Post()
        post2.publisher=user1
        post2.text = 'test2'
        post2.save()

        c = Client()
        response = c.get('/posts/')
        self.assertEquals(type(response), JsonResponse)
        response = response.json()
        print(response)
        self.assertEqual(response['code'], 200)