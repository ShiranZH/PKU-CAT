# Generated by Django 3.0.5 on 2020-06-02 12:53

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('archive', '0002_auto_20200601_2123'),
    ]

    operations = [
        migrations.AddField(
            model_name='archive',
            name='catID',
            field=models.IntegerField(default=-1),
        ),
        migrations.AlterField(
            model_name='photo',
            name='photo_url',
            field=models.CharField(max_length=128),
        ),
    ]
