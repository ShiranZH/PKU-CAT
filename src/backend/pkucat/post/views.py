import os
from django.shortcuts import render
from django.http import HttpResponse, JsonResponse, QueryDict
from .models import Post, Comment, Photo

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
    }
}

def index(request):
    return HttpResponse('Post Part!')

def demo(request):
    return HttpResponse('Post Response')

def posts(request):
#动态列表
    if request.method == 'GET':
    #请求动态列表，GET /posts/?limit&start
        try:
            num = request.GET.get('limit', default=10) #请求动态数量
            start = request.GET.get('start', default=-1) #起始动态id
        except:
            response = gen_response['param_err']
            return JsonResponse(response)
        print(num, start)
        print(request.path)
        print(Post)
        if start < 0:
            post_list = Post.objects.order_by('-post_id')[:num] #获取最新动态
        else:
            post_list = Post.objects.filter(post_id__lt=start).order_by('-post_id')[:num]
        posts = []
        for p in post_list:
            # 加载头像
            avatar_path = p.publisher.avatar
            avatar_file = open(avatar_path, "rb") 
            avatar = avatar_file.read()
            avatar_file.close()
            # 加载图片（一张）
            multimedia = []
            if p.is_video == False:
                photo = Photo.objects.filter(post_id=p.post_id)[0]
                photo_file = open(photo.photo, "rb")
                multimedia.append(photo_file.read())
                photo_file.close()
            # 判断是否给自己点赞
            if p.self_favor:
                self_favor = 1
            else:
                self_favor = 0
            # 一条摘要动态的数据结构
            PublisherInfo = {
                'userID': p.publisher.id,
                'username': p.publisher.username,
                'avatar': ''
            }
            PostInfo =  {
                'postID': p.post_id,
                'publisher': PublisherInfo,
                'time': p.time,
                'text': p.text,
                'multimediaContent': multimedia,
                'favor': {
                    'self': self_favor,
                    'favorCnt': p.favorcnt
                }
            }
            posts.append(PostInfo)
        
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
        try:
            post_id = request.GET.get('postID') #动态id
            comments_num = request.GET.get('comments', default=10) #评论数量
        except:
            return JsonResponse(gen_response['param_err'])
        
        # 获取动态数据
        try:
            post = Post.objects.get(post_id=post_id)
        except:
            return JsonResponse(gen_response['post_not_exist'])
        
        # 获取评论区列表
        total_cnt = Comment.objects.filter(post_id=post.post_id).count()
        comments = Comment.objects.filter(post_id=post.post_id)[:num]
        comment_list = []
        for comment in comments:
            # 加载头像
            avatar_file = open(comment.user.avatar, "rb") 
            avatar = avatar_file.read()
            avatar_file.close()
            user = {
                'userID': comment.user.id,
                'username': comment.user.username,
                'avatar': avatar
            }
            comment_list.append({
                'commentID': comment.comment_id,
                'user': user,
                'time': comment.time,
                'text': comment.text
            })
        CommentList = {
            'postID': post.post_id,
            'totalCount': total_cnt,
            'downloadCount': len(comment_list),
            'start': 0,
            'comments': comment_list
        }

        # 加载头像
        avatar_path = post.publisher.avatar
        avatar_file = open(avatar_path, "rb") 
        avatar = avatar_file.read()
        avatar_file.close()
        PublisherInfo = {
            'userID': post.publisher.id,
            'username': post.publisher.username,
            'avatar': avatar.read()
        }

        # 加载图片
        if is_video == False:
            photos = Photo.objects.filter(post_id=post.post_id)
            for photo in photos:
                file = open(photo.photo, "rb")
                multimedia.append(file.read())
                file.close()
        # 判断是否给自己点赞
        if post.self_favor:
            self_favor = 1
        else:
            self_favor = 0
        Post1 =  {
            'postID': post.post_id,
            'publisher': PublisherInfo,
            'time': post.time,
            'text': post.text,
            'isVideo': post.is_video,
            'multimediaContent': multimedia,
            'favor': {
                'self': self_favor,
                'favorCnt': post.favorcnt
            }
        }
        response = {
            "code": 200,
            "data": {
                "downloadCount": 1,
                "post": Post1
            } 
        }
        return JsonResponse(response)
        
    if request.method == 'POST':
    # 发布动态, POST /post/
        info = request.POST.get('post')
        print(info)
        # 创建数据库数据
        post = Post()
        post.publisher = info.publisher
        post.text = info.text
        post.is_video = info.isVideo
        if post.is_video:
            post.video = multimediaContent[0]
        post.save()
        # 存储图片
        if post.is_video == False:
            root = '/home/ubuntu/static/post/{}/'.format(post.post_id)
            os.mkdir(root)
            for i, photo in enumerate(post.multimediaContent):
                file_path = root + "{}.jpg".format(i)
                with open(file_path, "wb") as f:
                    f.write(photo)
                photo = Photo()
                photo.photo = file_path
                photo.post_id = post.post_id
                photo.save()
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
        DELETE = QueryDict(request.body)
        post_id = DELETE.get('postID')
        # 不存在id或重复删除
        try:
            post = Post.objects.get(post_id=post_id)
            post.delete()
        except:
            return JsonResponse(gen_response['post_not_exist'])
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
    # 搜索动态, GET /posts/search/?userID=123&keyword=大卫
        try:
            user_id = request.GET.get('userID', default=-1)
            keyword = request.GET.get('keyword', default=None)
            if user_id < 0 and keyword==None:
                response = gen_response['param_err']
                return JsonResponse(response)
        except:
            response = gen_response['param_err']
            return JsonResponse(response)

        post_list = Post.objects.filter(user_id=user_id)
        posts = []
        for post in post_list:
            # 加载头像
            avatar_path = post.publisher.avatar
            avatar_file = open(avatar_path, "rb") 
            avatar = avatar_file.read()
            avatar_file.close()
            # 加载图片（一张）
            if post.is_video == False:
                photo = Photo.objects.filter(post_id=post.post_id)[0]
                photo_file = open(photo.photo, "rb")
                multimedia.append(photo_file.read())
                photo_file.close()
            # 判断是否给自己点赞
            if post.self_favor:
                self_favor = 1
            else:
                self_favor = 0
            # 一条摘要动态的数据结构
            PublisherInfo = {
                'userID': post.publisher.id,
                'username': post.publisher.username,
                'avatar': avatar
            }
            Post =  {
                'postID': post.post_id,
                'publisher': PublisherInfo,
                'time': post.time,
                'text': post.text,
                'multimediaContent': multimedia,
                'favor': {
                    'self': self_favor,
                    'favorCnt': post.favorcnt
                }
            }
            avatar.close()
            photo.close()
            posts.append(Post)
        
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
        try:
            post_id = request.GET.get('postID') # 评论的动态id
            num = request.GET.get('limit', default=10) # 获取多少个评论
            start = request.GET.get('start', default=-1) # 上一次获取的结束评论号
        except:
            response = gen_response['param_err']
        
        try:
            post = Post.objects.get(post_id=post_id)
        except:
            return JsonResponse(gen_response['post_not_exist']) 
        
        # 获取评论区列表
        total_cnt = Comment.objects.filter(post_id=post_id).count()
        comments = Comment.objects.filter(post_id=post_id).filter(comment_id__gt=start)[:num]
        comment_list = []
        for comment in comments:
            # 加载头像
            avatar_file = open(comment.user.avatar, "rb") 
            avatar = avatar_file.read()
            avatar_file.close()
            user = {
                'userID': post.publisher.id,
                'username': post.publisher.username,
                'avatar': avatar.read()
            }
            comment_list.append({
                'commentID': comment.comment_id,
                'user': user,
                'time': comment.time,
                'text': comment.text
            })
        CommentList = {
            'postID': post.post_id,
            'totalCount': total_cnt,
            'downloadCount': len(comment_list),
            'start': start + 1,
            'comments': comment_list
        }
        response = {
            "code": 200,
            "data": {
                "commentList": CommentList
            } 
        }
        return JsonResponse(response)
    response = gen_response['method_err']
    return JsonResponse(response)

def favor(request):
    if request.method == 'POST':
    # 点赞, POST /post/favor/?postID=123&userID=123
        try:
            post_id = request.POST.get('postID')
            user_id = request.POST.get('userID')
        except:
            response = gen_response['param_err']
        
        try:
            post = Post.objects.get(post_id=post_id)
        except:
            return JsonResponse(gen_response['post_not_exist'])
        
        if post.user.id == user_id:
            if post.self_favor:
                response = {
                    "code": 300,
                    "data": {
                        "msg": "favored already"
                    }
                }
                return JsonResponse(response)
            else:
                post.self_favor = True
        else:
            post.favorcnt = post.favorcnt + 1
        post.save()

        return JsonResponse(gen_response['success'])
    elif request.method == 'DELETE':
    # 取消点赞, DELETE /post/favor/?postID=123&userID=123
        DELETE = QueryDict(request.body)
        try:
            post_id = DELETE.get('postID')
            user_id = DELETE.get('userID')
        except:
            response = gen_response['param_err']
        
        try:
            post = Post.objects.get(post_id=post_id)
        except:
            return JsonResponse(gen_response['post_not_exist'])
        
        if post.user.id == user_id:
            if not post.self_favor:
                response = {
                    "code": 300,
                    "data": {
                        "msg": "no self favor"
                    }
                }
                return JsonResponse(response)
            else:
                post.self_favor = False
        else:
            post.favorcnt = post.favorcnt - 1
        post.save()

        return JsonResponse(gen_response['success'])

    return JsonResponse(gen_response['method_err'])

def comment(request):
    if request.method == 'POST':
    # 评论, POST /post/comment/?postID=123&userID=123&text=“test”
        try:
            post_id = request.POST.get('postID')
            user_id = request.POST.get('userID')
            text = request.POST.get('text', default='')
        except:
            response = gen_response['param_err']

        try:
            post = Post.objects.get(post_id=post_id)
        except:
            return JsonResponse(gen_response['post_not_exist'])

        total_cnt = Comment.objects.filter(post_id=post_id).count()
        comment = Comment()
        comment.comment_id = total_cnt + 1
        comment.post_id = post_id
        comment.user = user_id
        comment.text = text
        try:
            comment.save()
        except:
            try:
                total_cnt = Comment.objects.filter(post_id=post_id).count()
                comment.comment_id = total_cnt + 1
                comment.save()
            except:
                response = {
                    "code": 300,
                    "data": {
                        "msg": "comment fail"
                    }
                }
                return JsonResponse(response)
        response = gen_response['success']
        return JsonResponse(response)
    response = gen_response['method_err']
    return JsonResponse(response)





