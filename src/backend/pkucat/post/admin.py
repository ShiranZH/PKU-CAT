from django.contrib import admin
from post.models import Post, Comment, Favor, Photo

# class FeedAdmin(admin.ModelAdmin):
#     fields = ['feeder', 'cat']
#     list_display = ['feeder', 'cat']

admin.site.register(Post)
admin.site.register(Comment)
admin.site.register(Favor)
admin.site.register(Photo)