from django.db import models

class PublisherInfo(models.Model):
    userID = models.IntegerField(primary_key=True)
    username = models.CharField(max_length=50)
    avatar = models.FileField(upload_to='static/avatars/')

class Post(models.Model):
    postID = models.AutoField(primary_key=True)
    publisher = models.ForeignKey(PublisherInfo, on_delete=models.CASCADE)
    time = models.DateTimeField()
    text = models.CharField(max_length=2000)
    isvideo = models.NullBooleanField(default=None)
    video = models.FileField(null=True, upload_to='static/videos/')
    self_favor = models.BooleanField(default=False)
    favorcnt = models.IntegerField(default=0)

class Comment(models.Model):
    commentID = models.IntegerField(primary_key=True)
    postID = models.ForeignKey(Post, on_delete=models.CASCADE) 
    user = models.ForeignKey(PublisherInfo, on_delete=models.CASCADE) 
    text = models.CharField(max_length=500)

class Photo(models.Model):
    postID = models.ForeignKey(Post, on_delete=models.CASCADE)
    photo = models.FileField(upload_to='static/photos/')
