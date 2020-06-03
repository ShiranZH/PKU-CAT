import os
import json
from django.test import TestCase, Client
from django.http import HttpResponse, JsonResponse, QueryDict
from .models import Post, Comment, Photo, Favor, TextKey
from user.models import User

class PostTests(TestCase):
    # 初始化
    def setUp(self):
        # 添加用户
        u = User.objects.create_user(username='testuser1', password='123456', pku_mail='testuser1@pku.edu.cn')
        u.sha256_password = hash("123456")
        u.save()

        u = User.objects.create_user(username='testuser2', password='123456', pku_mail='testuser2@pku.edu.cn')
        u.sha256_password = hash("123456")
        u.save()

        u = User.objects.create_user(username='testuser3', password='123456', pku_mail='testuser3@pku.edu.cn')
        u.sha256_password = hash("123456")
        u.save()
        self.user, self.reader, self.unlogin = User.objects.order_by('id')

        # 测试数据库添加数据
        post1 = Post()
        post1.publisher=self.user
        post1.text = 'test1'
        post1.self_favor = True
        post1.is_video = False
        post1.save()
        Comment.objects.create(post=post1, user=self.user, text='comment0')
        
        photo1 = Photo()
        photo1.post = post1
        photo1.photo = '/home/ywq/cat1.jpg'
        photo1.save()
        
        post2 = Post(post_id=post1.post_id+2)
        post2.publisher=self.user
        post2.text = 'test2'
        post2.save()
        Favor.objects.create(post=post2, user=self.reader)
        Comment.objects.create(post=post2, user=self.user, text='comment1')
        Comment.objects.create(post=post2, user=self.user, text='comment2')
        
        self.post1 = post1
        self.post2 = post2

    # 测试POST动态功能
    def test_post_post(self):

        # 未登陆

        # 参数错误
        data = json.dumps({'publisher':self.user.id, 'test':'test3'})
        response = self.client.post('/post/', {'post': data})
        self.assertEquals(type(response), JsonResponse)
        response = response.json()
        print('client receive response', response)
        self.assertEqual(response['code'], 700)

        # 不正确的用户id
        data = json.dumps({'publisher':-1,'text':'test3'})
        response = self.client.post('/post/', {'post': data})
        self.assertEquals(type(response), JsonResponse)
        response = response.json()
        print('client receive response', response)
        self.assertEqual(response['code'], 300)

        # 成功
        data = json.dumps({'publisher':self.user.id,'text':'test3'})
        response = self.client.post('/post/', {'post': data})
        self.assertEquals(type(response), JsonResponse)
        response = response.json()
        print('client receive response', response)
        self.assertEqual(response['code'], 200)
        self.assertTrue(response['data']['postID'] > self.post2.post_id)

    # 测试GET动态列表
    def test_get_posts(self):

        # 方法错误
        response = self.client.post('/posts/')
        self.assertEquals(type(response), JsonResponse)
        response = response.json()
        print('client receive response', response)
        self.assertEqual(response['code'], 600)

        # 参数类型错误
        response = self.client.post('/posts/', {'start':'last'})
        self.assertEquals(type(response), JsonResponse)
        response = response.json()
        print('client receive response', response)
        self.assertEqual(response['code'], 600)

        # 参数测试
        response = self.client.get('/posts/', {'limit':1})
        self.assertEquals(type(response), JsonResponse)
        response = response.json()
        print('client receive response', response)
        self.assertEqual(response['code'], 200)
        self.assertEqual(response['data']['downloadCount'], 1)
        end_id = response['data']['posts'][0]['postID']
        response = self.client.get('/posts/', {'start':end_id})
        self.assertEquals(type(response), JsonResponse)
        response = response.json()
        print('client receive response', response)
        self.assertEqual(response['code'], 200)
        self.assertEqual(response['data']['downloadCount'], 1)
        self.assertEqual(response['data']['posts'][0]['postID'], self.post1.post_id)

        # 成功
        response = self.client.get('/posts/')
        self.assertEquals(type(response), JsonResponse)
        response = response.json()
        print('client receive response', response)
        self.assertEqual(response['code'], 200)
        self.assertEqual(response['data']['downloadCount'], 2)

        info1 = response['data']['posts'][0] 
        self.assertEqual(info1['postID'], self.post2.post_id)
        self.assertEqual(info1['favor']['self'], 0)
        self.assertEqual(info1['favor']['favorCnt'], 1)
        self.assertEqual(info1['publisher']['userID'], self.user.id)

        info2 = response['data']['posts'][1] 
        self.assertEqual(info2['postID'], self.post1.post_id)
        self.assertEqual(info2['favor']['self'], 1)
        self.assertEqual(info2['favor']['favorCnt'], 0)
        self.assertEqual(info2['publisher']['userID'], self.user.id)
    
    # 测试GET动态
    def test_get_post(self):

        # 参数错误
        response = self.client.get('/post/', {'post':self.post1.post_id})
        self.assertEquals(type(response), JsonResponse)
        response = response.json()
        print('client receive response', response)
        self.assertEqual(response['code'], 700)
        
        response = self.client.get('/post/', {'postID':self.post1.post_id, 'comments':'yes'})
        self.assertEquals(type(response), JsonResponse)
        response = response.json()
        print('client receive response', response)
        self.assertEqual(response['code'], 700)

        # 不正确的动态id
        response = self.client.get('/post/', {'postID':200})
        self.assertEquals(type(response), JsonResponse)
        response = response.json()
        print('client receive response', response)
        self.assertEqual(response['code'], 300)

        # comments参数测试
        response = self.client.get('/post/', {'postID':self.post2.post_id, 'comments':1})
        self.assertEquals(type(response), JsonResponse)
        response = response.json()
        print('client receive response', response)
        self.assertEqual(response['code'], 200)
        self.assertEqual(response['data']['post']['commentList']['downloadCount'], 1)

        # 成功
        Comment.objects.create(post=self.post2, user=self.user, text='comment3')

        response = self.client.get('/post/', {'postID':self.post2.post_id})
        self.assertEquals(type(response), JsonResponse)
        response = response.json()
        print('client receive response', response)
        self.assertEqual(response['code'], 200)

        info1 = response['data']['post']    
        self.assertEqual(info1['postID'], self.post2.post_id)
        self.assertEqual(info1['favor']['self'], 0)
        self.assertEqual(info1['favor']['favorCnt'], 1)
        self.assertEqual(info1['publisher']['userID'], self.user.id)
        self.assertEqual(info1['commentList']['totalCount'], 3)
        self.assertEqual(info1['commentList']['downloadCount'], 3)
        
    # 测试点赞
    def test_favor(self):

        # 参数错误
        response = self.client.post('/post/favor/', {'post':self.post1.post_id, 'userID':self.user.id})
        self.assertEquals(type(response), JsonResponse)
        response = response.json()
        print('client receive response', response)
        self.assertEqual(response['code'], 700)

        response = self.client.post('/post/favor/', {'postID':self.post1.post_id, 'user':self.user.id})
        self.assertEquals(type(response), JsonResponse)
        response = response.json()
        print('client receive response', response)
        self.assertEqual(response['code'], 700)

        # 不正确的用户id
        response = self.client.post('/post/favor/', {'postID':self.post1.post_id, 'userID':200})
        self.assertEquals(type(response), JsonResponse)
        response = response.json()
        print('client receive response', response)
        self.assertEqual(response['code'], 300)

        # 不正确的动态id
        response = self.client.post('/post/favor/', {'postID':200, 'userID':self.user.id})
        self.assertEquals(type(response), JsonResponse)
        response = response.json()
        print('client receive response', response)
        self.assertEqual(response['code'], 300)

        # 重复点赞
        response = self.client.post('/post/favor/', {'postID':self.post1.post_id, 'userID':self.user.id})
        self.assertEquals(type(response), JsonResponse)
        response = response.json()
        print('client receive response', response)
        self.assertEqual(response['code'], 300)
        
        response = self.client.post('/post/favor/', {'postID':self.post2.post_id, 'userID':self.reader.id})
        self.assertEquals(type(response), JsonResponse)
        response = response.json()
        print('client receive response', response)
        self.assertEqual(response['code'], 300)  

        # 成功
        response = self.client.post('/post/favor/', {'postID':self.post1.post_id, 'userID':self.reader.id})
        self.assertEquals(type(response), JsonResponse)
        response = response.json()
        print('client receive response', response)
        self.assertEqual(response['code'], 200)

        response = self.client.post('/post/favor/', {'postID':self.post2.post_id, 'userID':self.user.id})
        self.assertEquals(type(response), JsonResponse)
        response = response.json()
        print('client receive response', response)
        self.assertEqual(response['code'], 200)

    # 测试取消点赞
    def test_delete_favor(self):

        # 参数错误
        data = json.dumps({'post':self.post1.post_id, 'userID':self.user.id})
        response = self.client.delete('/post/favor/', data)
        self.assertEquals(type(response), JsonResponse)
        response = response.json()
        print('client receive response', response)
        self.assertEqual(response['code'], 700)

        data = json.dumps({'postID':self.post1.post_id, 'user':self.user.id})
        response = self.client.delete('/post/favor/', data)
        self.assertEquals(type(response), JsonResponse)
        response = response.json()
        print('client receive response', response)
        self.assertEqual(response['code'], 700)

        # 不正确的用户id
        data = json.dumps({'postID':self.post1.post_id, 'userID':200})
        response = self.client.delete('/post/favor/', data)
        self.assertEquals(type(response), JsonResponse)
        response = response.json()
        print('client receive response', response)
        self.assertEqual(response['code'], 300)

        # 不正确的动态id
        data = json.dumps({'postID':200, 'userID':self.user.id})
        response = self.client.delete('/post/favor/', data)
        self.assertEquals(type(response), JsonResponse)
        response = response.json()
        print('client receive response', response)
        self.assertEqual(response['code'], 300)

        # 点赞不存在
        data = json.dumps({'postID':self.post1.post_id, 'userID':self.reader.id})
        response = self.client.delete('/post/favor/', data)
        self.assertEquals(type(response), JsonResponse)
        response = response.json()
        print('client receive response', response)
        self.assertEqual(response['code'], 300)

        data = json.dumps({'postID':self.post2.post_id, 'userID':self.user.id})
        response = self.client.delete('/post/favor/', data)
        self.assertEquals(type(response), JsonResponse)
        response = response.json()
        print('client receive response', response)
        self.assertEqual(response['code'], 300)

        # 正确
        data = json.dumps({'postID':self.post1.post_id, 'userID':self.user.id})
        response = self.client.delete('/post/favor/', data)
        self.assertEquals(type(response), JsonResponse)
        response = response.json()
        print('client receive response', response)
        self.assertEqual(response['code'], 200)
        
        data = json.dumps({'postID':self.post2.post_id, 'userID':self.reader.id})
        response = self.client.delete('/post/favor/', data)
        self.assertEquals(type(response), JsonResponse)
        response = response.json()
        print('client receive response', response)
        self.assertEqual(response['code'], 200)  

    # 测试获取评论列表
    def test_get_comments(self):

        # 参数错误
        response = self.client.get('/post/comments/', {'post':self.post1.post_id})
        self.assertEquals(type(response), JsonResponse)
        response = response.json()
        print('client receive response', response)
        self.assertEqual(response['code'], 700)
        
        response = self.client.get('/post/comments/', {'postID':self.post1.post_id, 'limit':'yes'})
        self.assertEquals(type(response), JsonResponse)
        response = response.json()
        print('client receive response', response)
        self.assertEqual(response['code'], 700)

        # 不正确的动态id
        response = self.client.get('/post/comments/', {'postID':200})
        self.assertEquals(type(response), JsonResponse)
        response = response.json()
        print('client receive response', response)
        self.assertEqual(response['code'], 300)

        # comments参数测试
        response = self.client.get('/post/comments/', {'postID':self.post2.post_id, 'limit':1})
        self.assertEquals(type(response), JsonResponse)
        response = response.json()
        print('client receive response', response)
        self.assertEqual(response['code'], 200)
        self.assertEqual(response['data']['commentList']['downloadCount'], 1)

        end_id = response['data']['commentList']['comments'][-1]['commentID']
        response = self.client.get('/post/comments/', {'postID':self.post2.post_id, 'start':end_id})
        self.assertEquals(type(response), JsonResponse)
        response = response.json()
        print('client receive response', response)
        self.assertEqual(response['code'], 200)
        self.assertEqual(response['data']['commentList']['downloadCount'], 1)

        # 成功
        Comment.objects.create(post=self.post2, user=self.user, text='comment3')

        response = self.client.get('/post/comments/', {'postID':self.post2.post_id})
        self.assertEquals(type(response), JsonResponse)
        response = response.json()
        print('client receive response', response)
        self.assertEqual(response['code'], 200)

        info1 = response['data']['commentList']    
        self.assertEqual(info1['postID'], self.post2.post_id)
        self.assertEqual(info1['totalCount'], 3)
        self.assertEqual(info1['downloadCount'], 3)

    # 测试POST评论
    def test_comment(self):

        # 参数错误
        response = self.client.post('/post/comment/', {'post':self.post1.post_id, 'userID':self.user.id})
        self.assertEquals(type(response), JsonResponse)
        response = response.json()
        print('client receive response', response)
        self.assertEqual(response['code'], 700)

        response = self.client.post('/post/comment/', {'postID':self.post1.post_id, 'user':self.user.id})
        self.assertEquals(type(response), JsonResponse)
        response = response.json()
        print('client receive response', response)
        self.assertEqual(response['code'], 700)

        # 不正确的用户id
        response = self.client.post('/post/comment/', {'postID':self.post1.post_id, 'userID':200})
        self.assertEquals(type(response), JsonResponse)
        response = response.json()
        print('client receive response', response)
        self.assertEqual(response['code'], 300)

        # 不正确的动态id
        response = self.client.post('/post/comment/', {'postID':200, 'userID':self.user.id})
        self.assertEquals(type(response), JsonResponse)
        response = response.json()
        print('client receive response', response)
        self.assertEqual(response['code'], 300)

        # 成功
        response = self.client.post('/post/comment/', {'postID':self.post1.post_id, 'userID':self.reader.id})
        self.assertEquals(type(response), JsonResponse)
        response = response.json()
        print('client receive response', response)
        self.assertEqual(response['code'], 200)

        response = self.client.post('/post/comment/', {'postID':self.post2.post_id, 'userID':self.user.id, 'text':'comment3'})
        self.assertEquals(type(response), JsonResponse)
        response = response.json()
        print('client receive response', response)
        self.assertEqual(response['code'], 200)

        response = self.client.get('/post/comments/', {'postID':self.post2.post_id})
        self.assertEquals(type(response), JsonResponse)
        response = response.json()
        print('client receive response', response)
        self.assertEqual(response['code'], 200)
        self.assertEqual(response['data']['commentList']['downloadCount'], 3)

    # 测试搜索功能
    def test_search(self):

        # 方法错误
        response = self.client.post('/posts/search/', {'userID':self.user.id})
        self.assertEquals(type(response), JsonResponse)
        response = response.json()
        print('client receive response', response)
        self.assertEqual(response['code'], 600)

        # 不正确的用户id
        response = self.client.get('/posts/search/', {'userID':200})
        self.assertEquals(type(response), JsonResponse)
        response = response.json()
        print('client receive response', response)
        self.assertEqual(response['code'], 300)

        # 成功
        Post.objects.create(publisher=self.reader, text='test3')

        response = self.client.get('/posts/search/', {'userID':self.user.id})
        self.assertEquals(type(response), JsonResponse)
        response = response.json()
        print('client receive response', response)
        self.assertEqual(response['code'], 200)
        self.assertEqual(response['data']['downloadCount'], 2)
    
    # 测试删除动态功能
    def test_delete(self):

        # 参数错误
        data = json.dumps({'post':self.post1.post_id})
        response = self.client.delete('/post/', data)
        self.assertEquals(type(response), JsonResponse)
        response = response.json()
        print('client receive response', response)
        self.assertEqual(response['code'], 700)

        # 不正确的动态id
        data = json.dumps({'postID':200})
        response = self.client.delete('/post/', data)
        self.assertEquals(type(response), JsonResponse)
        response = response.json()
        print('client receive response', response)
        self.assertEqual(response['code'], 300)

        # 正确
        data = json.dumps({'postID':self.post1.post_id})
        response = self.client.delete('/post/', data)
        self.assertEquals(type(response), JsonResponse)
        response = response.json()
        print('client receive response', response)
        self.assertEqual(response['code'], 200)
        
        response = self.client.get('/posts/')
        response = response.json()
        self.assertEqual(response['code'], 200)
        self.assertEqual(response['data']['downloadCount'], 1)
        self.assertEqual(response['data']['posts'][0]['postID'], self.post2.post_id)



