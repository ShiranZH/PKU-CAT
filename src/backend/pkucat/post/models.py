from django.db import models
from user.models import User

'''
重定义文件上传路径
/static/%post_id/%filename
'''
def upload_to(instance, fielname):
    return '/'.join(['/home/ubuntu/static', instance.post_id, filename])

'''
Post: 动态类

属性:
    post_id: 主键
    time: 日期时间
    text: 文本
    is_video: 多媒体内容类别,True=视频,False=图片,Null=没有多媒体内容
    video: 视频地址(可以为空)
    self_favor: 用户是否为自己点赞,True=点赞

外键:
    publisher: 动态发布者, user.models.User类
'''
class Post(models.Model):
    post_id = models.AutoField(primary_key=True)
    publisher = models.ForeignKey(User, on_delete=models.CASCADE)
    time = models.DateTimeField(auto_now_add=True)
    text = models.CharField(max_length=2000, null=True)
    is_video = models.NullBooleanField(default=None)
    video = models.CharField(max_length=128, null=True)
    self_favor = models.BooleanField(default=False)

    class Meta:
        verbose_name = '动态'
        verbose_name_plural = '动态'

'''
Comment: 评论类

属性:
    time: 创建时间
    text: 文本

外键:
    post: 评论动态主键, post.models.Post类
    user: 评论发布者主键, user.models.User类
    parent: 回复评论主键, post.models.Comment类

'''
class Comment(models.Model):
    post = models.ForeignKey(Post, on_delete=models.CASCADE) 
    user = models.ForeignKey(User, on_delete=models.CASCADE)
    time = models.DateTimeField(auto_now_add=True)
    text = models.CharField(max_length=500)
    parent = models.ForeignKey('self', on_delete=models.CASCADE, null=True)

    class Meta:
        verbose_name = '评论'
        verbose_name_plural = '评论'

'''
Photo: 图片类

属性:
    photo: 图片地址

外键:
    post: 评论动态主键, post.models.Post类
'''
class Photo(models.Model):
    post = models.ForeignKey(Post, on_delete=models.CASCADE)
    photo = models.CharField(max_length=128)

    class Meta:
        verbose_name = '图片'
        verbose_name_plural = '图片'

'''
Favor: 点赞记录类

外键:
    post_id: 评论动态主键, post.models.Post类
    user: 评论用户主键, user.models.User类

元数据:
    unique_together: (post, user)联合唯一
'''
class Favor(models.Model):
    post = models.ForeignKey(Post, on_delete=models.CASCADE)
    user = models.ForeignKey(User, on_delete=models.CASCADE)

    class Meta:
        unique_together = ('post', 'user')
        verbose_name = '点赞记录'
        verbose_name_plural = '点赞记录'
