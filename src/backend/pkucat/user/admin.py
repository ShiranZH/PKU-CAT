from django.contrib import admin
import django.contrib.auth.admin
import django.contrib.auth.models

from .models import User, Verification


class UserAdmin(admin.ModelAdmin):
    fields = ['username', 'pku_mail', 'avatar', 'whatsup', 'is_superuser', "sha256_password"]
    list_display = ['username', 'pku_mail', 'is_superuser', "sha256_password"]


class VerificationAdmin(admin.ModelAdmin):
    fields = ['pku_mail', 'verification_code', 'update_date']
    list_display = ['pku_mail', 'verification_code', 'update_date']


admin.site.unregister(django.contrib.auth.models.User)
admin.site.unregister(django.contrib.auth.models.Group)
admin.site.register(User, UserAdmin)
admin.site.register(Verification, VerificationAdmin)