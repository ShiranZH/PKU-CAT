import os
import json
import jieba.analyse
from multiprocessing import Process
from django.shortcuts import render
from django.http import HttpResponse, JsonResponse, QueryDict
from .models import Post, Comment, Photo, Favor, TextKey
from user.models import User

gen_response = {
    'success': { 
        "code": 200,
        "data": {
            "msg": "success"
        }
    },
    'method_err': {
        "code": 600,
        "data": {
            "msg": "wrong method"
        } 
    },
    'param_err': {
        "code": 700,
        "data": {
            "msg": "wrong parameter"
        }
    },
    'post_not_exist': {
        "code": 300,
        "data": {
            "msg": "post not exists"
        }
    },
    'user_not_exist': {
        "code": 300,
        "data": {
            "msg": "user not exists"
        }
    },
    'user_error': {
        "code": 400,
        "data": {
            "msg": "not authorized"
        }
    }
}

# 插入关键词多进程
def insert_key(text, post):
    #获取tf-idf排序下前10个关键词
    keywords = jieba.analyse.extract_tags(text, topK=10)
    for key in keywords:
        try:
            TextKey(key=key, post=post).save()
        except: pass

def demo(request):
    return HttpResponse('Post Response')

def posts(request):
#动态列表

    if request.method == 'GET':
    #请求动态列表，GET /posts/?limit&start
        print('server receive GET', request.get_full_path())

        # 检查登陆状态
        if not request.user.is_authenticated:
            return JsonResponse(gen_response["user_error"])

        try:
            num = int(request.GET.get('limit', default=10)) #请求动态数量
            start = int(request.GET.get('start', default=-1)) #起始动态id
        except:
            return JsonResponse(gen_response['param_err'])

        if start < 0:
            post_list = Post.objects.order_by('-post_id')[:num] #获取最新动态
        else:
            post_list = Post.objects.filter(post_id__lt=start).order_by('-post_id')[:num]
        
        posts = []
        for item in post_list:
            
            # 加载图片（仅多媒体内容为图片时加载一张）
            multimedia = []
            if item.is_video == False:
                photo_path = Photo.objects.filter(post=item)[0].photo
            multimedia.append(photo_path)
            
            # 判断是否给自己点赞
            user = User.objects.get(id=request.user.id)
            if Favor.objects.filter(post=item).filter(user=user):
                self_favor = 1
            else:
                self_favor = 0

            # 一条摘要动态的数据结构
            publisher_info = {
                'userID': item.publisher.id,
                'username': item.publisher.username,
                'avatar': item.publisher.avatar
            }
            favorcnt = Favor.objects.filter(post=item).count()
            post_info =  {
                'postID': item.post_id,
                'publisher': publisher_info,
                'time': item.time,
                'text': item.text,
                'multimediaContent': multimedia,
                'favor': {
                    'self': self_favor,
                    'favorCnt': favorcnt
                }
            }
            posts.append(post_info)
        
        response = {
            "code": 200,
            "data": {
                "downloadCount": len(posts),
                "posts": posts
            } 
        }
        return JsonResponse(response)
    else:
        return JsonResponse(gen_response['method_err'])

def post(request):
#动态获取、添加、删除
    if request.method == 'GET':
    #请求动态，GET /post/?postID&comments
        print('server receive GET', request.get_full_path())

        # 检查登陆状态
        if not request.user.is_authenticated:
            return JsonResponse(gen_response["user_error"])

        try:
            post_id = int(request.GET.get('postID')) #动态id
            comments_num = int(request.GET.get('comments', default=10)) #评论数量
        except:
            return JsonResponse(gen_response['param_err'])

        # 获取动态数据
        try:
            post = Post.objects.get(post_id=post_id)
        except:
            return JsonResponse(gen_response['post_not_exist'])
        
        # 获取评论区列表
        total_cnt = Comment.objects.filter(post=post).count()
        comments = Comment.objects.filter(post=post)[:comments_num]
        comment_list = []
        for comment in comments:
        
            user = {
                'userID': comment.user.id,
                'username': comment.user.username,
                'avatar': comment.user.avatar
            }
            comment_list.append({
                'commentID': comment.id,
                'user': user,
                'time': comment.time,
                'text': comment.text
            })
        if len(comment_list):
            _start = comment_list[0]['commentID']
        else:
            _start = -1
        comment_list_info = {
            'postID': post.post_id,
            'totalCount': total_cnt,
            'downloadCount': len(comment_list),
            'start': _start,
            'comments': comment_list
        }

        publisher_info = {
            'userID': post.publisher.id,
            'username': post.publisher.username,
            'avatar': post.publisher.avatar
        }

        # 加载多媒体内容
        multimedia = []
        if post.is_video == False:
            photos = Photo.objects.filter(post=post)
            for photo_info in photos:
                multimedia.append(photo_info.photo)
        elif post.is_video == True:
            multimedia.append(post.video)

        # 判断是否给自己点赞
        user = User.objects.get(id=request.user.id)
        if Favor.objects.filter(post=post).filter(user=user):
            self_favor = 1
        else:
            self_favor = 0

        favorcnt = Favor.objects.filter(post=post).count()
        post_info =  {
            'postID': post.post_id,
            'publisher': publisher_info,
            'time': post.time,
            'text': post.text,
            'isVideo': post.is_video,
            'multimediaContent': ['multimedia'],
            'commentList': comment_list_info,
            'favor': {
                'self': self_favor,
                'favorCnt': favorcnt
            }
        }
        response = {
            "code": 200,
            "data": {
                "downloadCount": 1,
                "post": post_info
            } 
        }
        return JsonResponse(response)
        
    if request.method == 'POST':
    # 发布动态, POST /post/
        print('server receive POST', request.get_full_path())
        
        # 检查登陆状态
        if not request.user.is_authenticated:
            return JsonResponse(gen_response["user_error"])
        
        info = request.POST.get('post')
        info = json.loads(info)
  
        # 分析参数
        if not 'publisher' in info:
            return JsonResponse(gen_response['param_err'])
        if not 'text' in info and not 'isVideo' in info:
            return JsonResponse(gen_response['param_err'])

        # 从User数据库获取user
        user_id = info['publisher']
        try: 
            publisher = User.objects.get(id=user_id)
        except:
            return JsonResponse(gen_response['user_not_exist'])

        # 创建数据库动态数据
        post = Post()
        post.publisher = publisher
        try:
            post.text = info['text']
        except: pass
        try:
            post.is_video = info['isVideo']
        except:
            post.is_video = None
        if post.is_video:
            post.video = info['multimediaContent'][0]
        post.save()

        # 存储图片
        if post.is_video == False:
            for photo_path in info['multimediaContent']:
                photo = Photo()
                photo.photo = photo_path
                photo.post = post
                photo.save()
        
        # 使用多进程创建关键词数据, 避免影响主进程运行
        p = Process(target=insert_key, args=(post.text, post))
        p.start()
        
        response = {
            "code": 200,
            "data": {
                "postID": post.post_id,
                "time": post.time
            } 
        }
        return JsonResponse(response)

    if request.method == 'DELETE':
    # 删除动态, DELETE /post/?postID=123
        print('server receive DELETE', request.get_full_path())
        
        # 检查登陆状态
        if not request.user.is_authenticated:
            return JsonResponse(gen_response["user_error"])
        
        DELETE = json.loads(request.body)
        try:
            post_id = int(DELETE.get('postID'))
        except:
            return JsonResponse(gen_response['param_err'])
        
        # 不存在id
        try:
            post = Post.objects.get(post_id=post_id)
        except:
            return JsonResponse(gen_response['post_not_exist'])

        try:
            post.delete()
        except:
            response = {
                "code": 300,
                "data": {
                    "msg": "delete fail"
                }
            }
            return JsonResponse(response)

        response = {
            "code": 200,
            "data": {
                "msg": "delete post successfully"
            }
        }
        return JsonResponse(response)
    
    response = gen_response['method_err']
    return JsonResponse(response)

def search(request):
# 搜索动态
    if request.method == 'GET':
    # 搜索动态, GET /posts/search/?userID=123&keyword=大威
        print('server receive GET', request.get_full_path())
        
        # 检查登陆状态
        if not request.user.is_authenticated:
            return JsonResponse(gen_response["user_error"])
        
        try:
            user_id = int(request.GET.get('userID', default=-1))
            keyword = request.GET.get('keyword', default=None)
            if user_id < 0 and keyword==None:
                return JsonResponse(gen_response['param_err'])
        except:
            return JsonResponse(gen_response['param_err'])

        if user_id >= 0:
            # 从User数据库获取user
            try: 
                publisher = User.objects.get(id=user_id)
            except:
                return JsonResponse(gen_response['user_not_exist'])

            post_list = Post.objects.filter(publisher=publisher)

        else:
            # 获取包含该关键词的post
            keyword_list = TextKey.objects.filter(key=keyword)
            post_list = []
            for key in keyword_list:
                post_list.append(key.post) 

        posts = []
        for item in post_list:
            
            # 加载图片（一张）
            multimedia = []
            if item.is_video == False:
                photo_path = Photo.objects.filter(post=item.post_id)[0].photo
                multimedia.append(photo_path)
            
            # 判断是否给自己点赞
            if item.self_favor:
                self_favor = 1
            else:
                self_favor = 0
            # 一条摘要动态的数据结构
            publisher_info = {
                'userID': item.publisher.id,
                'username': item.publisher.username,
                'avatar': item.publisher.avatar
            }
            favorcnt = Favor.objects.filter(post=item).count()
            post_info =  {
                'postID': item.post_id,
                'publisher': publisher_info,
                'time': item.time,
                'text': item.text,
                'multimediaContent': multimedia,
                'favor': {
                    'self': self_favor,
                    'favorCnt': favorcnt
                }
            }
            posts.append(post_info)
        
        response = {
            "code": 200,
            "data": {
                "downloadCount": len(posts),
                "posts": posts
            } 
        }
        return JsonResponse(response)
    
    response = gen_response['method_err']
    return JsonResponse(response)

def comments(request):
    if request.method == 'GET':
    # 获取评论列表, GET /post/comments/?postID=123&limit=10&start=10
        print('server receive GET', request.get_full_path())
        
        # 检查登陆状态
        if not request.user.is_authenticated:
            return JsonResponse(gen_response["user_error"])
        
        try:
            post_id = int(request.GET.get('postID')) # 评论的动态id
            num = int(request.GET.get('limit', default=10)) # 获取多少个评论
            start = int(request.GET.get('start', default=-1)) # 上一次获取的结束评论号
        except:
            return(JsonResponse(gen_response['param_err'])) 
        
        try:
            post = Post.objects.get(post_id=post_id)
        except:
            return JsonResponse(gen_response['post_not_exist']) 
        
        # 获取评论区列表
        total_cnt = Comment.objects.filter(post=post).count()

        if start < 0:
            comments = Comment.objects.filter(post=post)[:num] #获取最新动态
        else:
            comments = Comment.objects.filter(post=post).filter(id__gt=start)[:num]

        comment_list = []
        for comment in comments:
            
            user = {
                'userID': comment.user.id,
                'username': comment.user.username,
                'avatar': comment.user.avatar
            }
            comment_list.append({
                'commentID': comment.id,
                'user': user,
                'time': comment.time,
                'text': comment.text
            })
        if len(comment_list):
            _start = comment_list[0]['commentID']
        else:
            _start = -1
        comment_list_info = {
            'postID': post.post_id,
            'totalCount': total_cnt,
            'downloadCount': len(comment_list),
            'start': _start,
            'comments': comment_list
        }

        response = {
            "code": 200,
            "data": {
                "commentList": comment_list_info
            } 
        }
        return JsonResponse(response)
    response = gen_response['method_err']
    return JsonResponse(response)

def favor(request):
    if request.method == 'POST':
    # 点赞, POST /post/favor/?postID=123&userID=123
        print('server receive POST', request.get_full_path())
        
        # 检查登陆状态
        if not request.user.is_authenticated:
            return JsonResponse(gen_response["user_error"])
        
        try:
            post_id = int(request.POST.get('postID'))
            user_id = int(request.POST.get('userID'))
        except:
            return(JsonResponse(gen_response['param_err'])) 
        
        try:
            post = Post.objects.get(post_id=post_id)
        except:
            return JsonResponse(gen_response['post_not_exist'])

        try: 
            user = User.objects.get(id=user_id)
        except:
            return JsonResponse(gen_response['user_not_exist'])
        
        fail = {
            "code": 300,
            "data": {
                "msg": "favored already"
            }
        }
        
        if Favor.objects.filter(post=post).filter(user=user):
            return JsonResponse(fail)
        try:
            Favor(post=post, user=user).save()
        except:
            pass

        return JsonResponse(gen_response['success'])
    elif request.method == 'DELETE':
    # 取消点赞, DELETE /post/favor/?postID=123&userID=123
        print('server receive DELETE', request.get_full_path())
        
        # 检查登陆状态
        if not request.user.is_authenticated:
            return JsonResponse(gen_response["user_error"])
        
        DELETE = json.loads(request.body)
        try:
            post_id = int(DELETE.get('postID'))
            user_id = int(DELETE.get('userID'))
        except:
            return(JsonResponse(gen_response['param_err'])) 
        
        try:
            post = Post.objects.get(post_id=post_id)
        except:
            return JsonResponse(gen_response['post_not_exist'])

        try: 
            user = User.objects.get(id=user_id)
        except:
            return JsonResponse(gen_response['user_not_exist'])
        
        fail = {
            "code": 300,
            "data": {
                "msg": "never favored"
            }
        }
        try:
            favor = Favor.objects.filter(post=post).filter(user=user)[0]
            favor.delete()
        except:
            return JsonResponse(fail)

        return JsonResponse(gen_response['success'])

    return JsonResponse(gen_response['method_err'])

def comment(request):
    if request.method == 'POST':
    # 评论, POST /post/comment/?postID=123&userID=123&text=“test”
        print('server receive POST', request.get_full_path())
        
        # 检查登陆状态
        if not request.user.is_authenticated:
            return JsonResponse(gen_response["user_error"])
        
        try:
            post_id = int(request.POST.get('postID'))
            user_id = int(request.POST.get('userID'))
            text = request.POST.get('text', default='')
        except:
            return(JsonResponse(gen_response['param_err'])) 

        try:
            post = Post.objects.get(post_id=post_id)
        except:
            return JsonResponse(gen_response['post_not_exist'])

        try: 
            user = User.objects.get(id=user_id)
        except:
            return JsonResponse(gen_response['user_not_exist'])

        comment = Comment()
        comment.post = post
        comment.user = user
        comment.text = text
    
        try:
            comment.save()
        except:
            response = {
                "code": 300,
                "data": {
                    "msg": "comment fail"
                }
            }
            return JsonResponse(response)
        
        return JsonResponse(gen_response['success'])
    response = gen_response['method_err']
    return JsonResponse(response)





