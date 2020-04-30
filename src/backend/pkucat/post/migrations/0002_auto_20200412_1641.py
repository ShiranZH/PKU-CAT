# Generated by Django 3.0.5 on 2020-04-12 16:41

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('post', '0001_initial'),
    ]

    operations = [
        migrations.AlterField(
            model_name='photo',
            name='photo',
            field=models.FileField(upload_to='static/photos/'),
        ),
        migrations.AlterField(
            model_name='post',
            name='video',
            field=models.FileField(null=True, upload_to='static/videos/'),
        ),
        migrations.AlterField(
            model_name='publisherinfo',
            name='avatar',
            field=models.FileField(upload_to='static/avatars/'),
        ),
    ]
