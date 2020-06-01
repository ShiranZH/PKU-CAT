from django.contrib import admin
from archive.models import *

admin.site.register(Cat)
admin.site.register(Archive)
admin.site.register(Photo)
admin.site.register(Relationship)