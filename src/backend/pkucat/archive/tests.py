from django.test import TestCase
from django.http import HttpResponse, JsonResponse
from .models import Cat, Archive, Photo, Relationship

# Create your tests here.
class ArchiveTests(TestCase):
	def test_demo(self):
		response = self.client.get('/archive/demo')
		self.assertEqual(response, 'archive demo ')

	def test_get_archive_list(self):
		Cat.objects.create(name='maomao', avatar='/home/photos/picture1.jpg')

		#错误方法
		response = self.client.post('/archives')
		self.assertEqual(type(response), JsonResponse)
		response = response.json()
		self.assertEqual(response['code'], '600')

		response = self.client.delete('/archives')
		self.assertEqual(type(response), JsonResponse)
		response = response.json()
		self.assertEqual(response['code'], '600')

		#成功
		response = self.client.get('/archives')
		self.assertEqual(type(response), JsonResponse)
		response = response.json()
		self.assertEqual(response['code'], '200')
		self.assertEqual(response['data']['msg'], 'success')
		self.assertEqual(response['data']['catList'][0]['name'], 'maomao')
		self.assertEqual(response['data']['catList'][0]['catID'], Cat.objects.get(name='maomao').id)
		self.assertEqual(response['data']['catList'][0]['avatar'], '/home/photos/picture1.jpg')

	def test_get_archive_detail(self):
		cat1 = Cat(name='xiaobai', avatar='/home/photos/xiaobai.jpg')
		cat1.save()
		cat1id = cat1.id
		archive1 = Archive()
        archive1.name = 'xiaobai'
        archive1.introduction = "这是一只小白猫"
		#catMaomao = Cat(name='maomao', avatar='/home/photos/picture1.jpg')
		relatedCatName1 = 'maomao'
		relatedCat1 = Cat.objects.filter(name='maomao').first()
		ralatedCat1id = relatedCat1.id
		archive1.relatedCats.add(relatedCat1)
		archive1.save()
		Relationship.objects.create(archive=archive1, cat=relatedCat1, relation='brother')

		#成功
		response = self.client.get('/archive', {'catid': cat1id})
		self.assertEqual(type(response), JsonResponse)
		response = response.json()
		self.assertEqual(response['code'], '200')
		self.assertEqual(response['data']['msg'], 'success')
		self.assertEqual(response['data']['archive']['catName'], 'xiaobai')
		self.assertEqual(response['data']['archive']['introduction'], "这是一只小白猫")
		self.assertEqual(response['data']['archive']['relatedCats'][0]['relatedCat'], relatedCat1id)
		self.assertEqual(response['data']['archive']['relatedCats'][0]['relation'], 'brother')

		#catid找不到对应的archive
		response = self.client.get('/archive', {'catid': relatedCat1id})#maomao没建archive
		self.assertEqual(type(response), JsonResponse)
		response = response.json()
		self.assertEqual(response['code'], '300')
		self.assertEqual(response['code']['msg'], 'archive not exist')

		#catid数据类型不正确
		response = self.client.get('/archive', {'catid': 'not_id'})
		self.assertEqual(type(response), JsonResponse)
		response = response.json()
		self.assertEqual(response['code'], '700')
		self.assertEqual(response['code']['msg'], 'wrong parameter')

	def test_search_cat(self):
		#keyword类型不正确
		response = self.client.get('/archive', {'keyword': -1})
		self.assertEqual(type(response), JsonResponse)
		response = response.json()
		self.assertEqual(response['code'], '700')
		self.assertEqual(response['code']['msg'], 'wrong parameter')

		#keyword搜索无果
		response = self.client.get('/archive', {'keyword': 'ch'})
		self.assertEqual(type(response), JsonResponse)
		response = response.json()
		self.assertEqual(response['code'], '300')
		self.assertEqual(response['code']['msg'], 'fail to search with keyword')

		#成功
		response = self.client.get('/archive', {'keyword': 'ao'})
		self.assertEqual(type(response), JsonResponse)
		response = response.json()
		self.assertEqual(response['code']['msg']['results'][0]['name'], 'maomao')
		self.assertEqual(response['code']['msg']['results'][0]['catID'], Cat.objects.get(name='maomao').id)
		self.assertEqual(response['code']['msg']['results'][0]['avatar'], '/home/photos/picture1.jpg')
		self.assertEqual(response['code']['msg']['results'][1]['name'], 'xiaobai')
		self.assertEqual(response['code']['msg']['results'][1]['catID'], Cat.objects.get(name='xiaobai').id)
		self.assertEqual(response['code']['msg']['results'][1]['avatar'], '/home/photos/xiaobai.jpg')

	def test_modify_archive(self):
		
		response = self.client.put('/archive', {'catID':'', '':''})

