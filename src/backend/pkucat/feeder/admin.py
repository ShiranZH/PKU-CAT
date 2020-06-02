from django.contrib import admin
from feeder.models import Feed, ApplicationFeeder

class FeedAdmin(admin.ModelAdmin):
    fields = ['feeder', 'cat']
    list_display = ['feeder', 'cat']

admin.site.register(Feed, FeedAdmin)
admin.site.register(ApplicationFeeder)