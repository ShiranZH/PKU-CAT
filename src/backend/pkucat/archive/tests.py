from django.test import TestCase
from django.http import HttpResponse, JsonResponse
from .models import Cat, Archive, Photo, Relationship

# Create your tests here.
class ArchiveTests(TestCase):
    #def test_demo(self):
    #    response = self.client.get('/archive/demo')
    #    self.assertEqual(response.context, 'archive demo ')

    def test_get_archive_list(self):
        Cat.objects.create(name='maomao', avatar='/home/photos/picture1.jpg')

        #错误方法
        response = self.client.post('/archive/archives')
        self.assertEqual(type(response), JsonResponse)
        response = response.json()
        self.assertEqual(response['code'], 600)

        response = self.client.delete('/archive/archives')
        self.assertEqual(type(response), JsonResponse)
        response = response.json()
        self.assertEqual(response['code'], 600)

        #成功
        response = self.client.get('/archive/archives')
        self.assertEqual(type(response), JsonResponse)
        response = response.json()
        self.assertEqual(response['code'], 200)
        self.assertEqual(response['data']['msg'], 'success')
        self.assertEqual(response['data']['catList'][0]['name'], 'maomao')
        self.assertEqual(response['data']['catList'][0]['catID'], Cat.objects.get(name='maomao').id)
        self.assertEqual(response['data']['catList'][0]['avatar'], '/home/photos/picture1.jpg')

    def test_get_archive_detail(self):
        cat0 = Cat(name='maomao', avatar='/home/photos/maomao.jpg')
        cat0.save()
        cat1 = Cat(name='xiaobai', avatar='/home/photos/xiaobai.jpg')
        cat1.save()
        cat1id = cat1.id
        print('cat1id='+str(cat1id))
        archive1 = Archive()
        archive1.name = 'xiaobai'
        archive1.introduction = "这是一只小白猫"
        #catMaomao = Cat(name='maomao', avatar='/home/photos/picture1.jpg')
        relatedCatName1 = 'maomao'
        relatedCat1 = Cat.objects.filter(name=relatedCatName1).first()
        print('???'+str(relatedCat1.id))
        relatedCat1id = relatedCat1.id
        
        archive1.save()
        archive1.relatedCats.add(relatedCat1)
        rr = Relationship.objects.filter(archive=archive1, cat=relatedCat1).first()
        rr.relation = 'brother'
        rr.save()

        #Relationship.objects.create(archive=archive1, cat=relatedCat1, relation='brother')
       
        print(Relationship.objects.all())
        #r2 = Relationship.objects.create(archive=archive1, cat=relatedCat1, relation='brother')
        #print("r2 "+ r2.relation)
        #print(Relationship.objects.all())
        print(Relationship.objects.all()[0].relation)
        for r in Relationship.objects.all():
            print(r.archive.name, r.cat.name, r.relation)
        r = Relationship.objects.filter(archive=archive1, cat=relatedCat1).first()
        print('archive_name='+r.archive.name)
        print('cat_name='+r.cat.name)
        print('relation='+r.relation)

        #成功
        response = self.client.get('/archive/archive', {'catid': cat1id})
        self.assertEqual(type(response), JsonResponse)
        response = response.json()
        self.assertEqual(response['code'], 200)
        self.assertEqual(response['data']['msg'], 'success')
        self.assertEqual(response['data']['archive']['catName'], 'xiaobai')
        self.assertEqual(response['data']['archive']['introduction'], "这是一只小白猫")
        self.assertEqual(response['data']['archive']['relatedCats'][0]['relatedCat'], relatedCat1id)
        self.assertEqual(response['data']['archive']['relatedCats'][0]['relation'], 'brother')

        #catid找不到对应的archive
        response = self.client.get('/archive/archive', {'catid': 3})#maomao没建archive
        self.assertEqual(type(response), JsonResponse)
        response = response.json()
        self.assertEqual(response['code'], 300)
        #self.assertEqual(response['code']['msg'], 'archive not exist')

        #catid数据类型不正确
        response = self.client.get('/archive/archive', {'catid': 'not_id'})
        self.assertEqual(type(response), JsonResponse)
        response = response.json()
        print(response['data']['msg'])
        self.assertEqual(response['code'], 700)
        self.assertEqual(response['data']['msg'], 'wrong parameter')

    def test_search_cat(self):
        Cat.objects.create(name='maomao', avatar='/home/photos/maomao.jpg')
        Cat.objects.create(name='xiaobai', avatar='/home/photos/xiaobai.jpg')
        
        '''
        #keyword类型不正确
        response = self.client.get('/archive/archive', {'keyword': -1})
        self.assertEqual(type(response), JsonResponse)
        response = response.json()
        self.assertEqual(response['code'], 700)
        self.assertEqual(response['code']['msg'], 'wrong parameter')
        '''

        #keyword搜索无果
        response = self.client.get('/archive/archive', {'keyword': 'ch'})
        self.assertEqual(type(response), JsonResponse)
        response = response.json()
        self.assertEqual(response['code'], 300)
        self.assertEqual(response['code']['msg'], 'fail to search with keyword')

        #成功
        response = self.client.get('/archive/archive', {'keyword': 'ao'})
        self.assertEqual(type(response), JsonResponse)
        response = response.json()
        self.assertEqual(response['code']['msg']['results'][0]['name'], 'maomao')
        self.assertEqual(response['code']['msg']['results'][0]['catID'], Cat.objects.get(name='maomao').id)
        self.assertEqual(response['code']['msg']['results'][0]['avatar'], '/home/photos/picture1.jpg')
        self.assertEqual(response['code']['msg']['results'][1]['name'], 'xiaobai')
        self.assertEqual(response['code']['msg']['results'][1]['catID'], Cat.objects.get(name='xiaobai').id)
        self.assertEqual(response['code']['msg']['results'][1]['avatar'], '/home/photos/xiaobai.jpg')

        

    #def test_modify_archive(self):
        
        
        #response = self.client.put('/archive', {'catID':'', '':''})

