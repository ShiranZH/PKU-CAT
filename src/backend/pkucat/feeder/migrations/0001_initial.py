from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    initial = True

    dependencies = [
        ('user', '0003_auto_20200413_0921'),
        ('archive', '0001_initial'),
    ]

    operations = [
        migrations.CreateModel(
            name='Feed',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('cat', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='archive.Cat')),
                ('feeder', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='user.User')),
            ],
            options={
                'unique_together': {('feeder', 'cat')},
            },
        ),
        migrations.CreateModel(
            name='Application_Feeder',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('cat', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='archive.Cat')),
                ('feeder', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='user.User')),
            ],
            options={
                'unique_together': {('feeder', 'cat')},
            },
        ),
    ]
