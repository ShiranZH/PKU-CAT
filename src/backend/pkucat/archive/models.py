from django.db import models

# Create your models here.
class Cat(models.Model):
    name = models.CharField(max_length=30, unique=True)
    catID = models.AutoField(primary_key=True)
    avatar = models.URLField()


    def __str__(self):
        return self.name



class Archive(models.Model):
    name = models.CharField(max_length=30, unique=True)
    introduction = models.TextField()
    relatedCats = models.ManyToManyField(
        Cat,
        related_name='archive_this_cat_related_to',
        through='Relationship',
        through_fields=('archive', 'cat'),
    )

    def __str__(self):
        return self.name

class Photo(models.Model):
    photo_url = models.URLField()
    containing_archive = models.ForeignKey(
        'Archive',
        on_delete=models.CASCADE,
    )



class Relationship(models.Model):
    archive = models.ForeignKey(Archive, on_delete=models.CASCADE)
    cat = models.ForeignKey(Cat, on_delete=models.CASCADE)
    relation = models.CharField(max_length=100)

