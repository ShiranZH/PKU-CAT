from django.contrib import admin
from post.models import Post, Comment, Favor, Photo

class PostAdmin(admin.ModelAdmin):
    # fields = ['publisher', 'time', 'text', 'is_video', "video"]
    list_display = ['post_id', 'publisher', 'time', 'text', 'is_video', "video"]

admin.site.register(Post, PostAdmin)
admin.site.register(Comment)
admin.site.register(Favor)
admin.site.register(Photo)