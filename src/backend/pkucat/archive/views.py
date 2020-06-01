from django.shortcuts import render

from django.views.decorators.csrf import csrf_exempt, csrf_protect
from django.shortcuts import HttpResponse
from django.http import QueryDict
from .models import Cat, Archive, Photo, Relationship

def index(request):
    return HttpResponse('index')
    
def demo(request):
    return HttpResponse('archive demo ')

def archives(request):
    if request.method == 'GET':
    #Archive_list 查看猫咪档案列表      GET /archives
        cats = Cat.objects.all()#QuerySet
        catlist = []
        for c in cats:
            catInfo = {
                "name": c.name,
                "catID": c.id,
                "avatar": c.avatar
            }
            catlist.append(catInfo)
        response = {
            "code": 200,
            "data": {
                "msg": "success",
                "catList": catlist
            }
        }
        return JsonResponse(response)
    response = {
        "code": 600,
        "data": {
            "msg": "wrong method"
        } 
    }
    return JsonResponse(response)

@csrf_exempt
def archive(request):
    if request.method == 'GET':
    #Archive_detail 查看猫咪档案详情    GET /archive?catid=10  
    #Archive_search 搜索猫咪           GET /archive?keyword="大威"
        catid = request.GET.get('catid', default=None)
        keyword = request.GET.get('keyword', default=None)
        #查看猫咪档案详情
        if catid:
            try:
                catid = int(catid)
            except:
                response = {
                    'code': 700,
                    'data': {
                        'msg': 'wrong parameter'
                    }
                }
            try:
                cat_name = Cat.objects.get(id=catid).name
            except:
                response = {
                    'code': 300,
                    'data': {
                        'msg': 'archive not exist'
                    }
                }
                return JsonResponse(response)
            archive_one = Archive.objects.get(name=cat_name)
            related_cats_list = []
            for c in archive_one.relatedCats.all():
                relateCatInfo = {
                    "relatedCat": c.cat,
                    "relation": Relationship.objects.filter(archive=archive_one, cat=c).first().relation
                }
                related_cats_list.append(relateCatInfo)
            archive_detail = {
                "catName": archive_one.name,
                "introduction": archive_one.introduction,
                "relatedCats": related_cats_list
            }
            response = {
                "code": 200,
                "data": {
                    "msg": "success",
                    "archive": archive_detail
                }
            }
        elif keyword:
        #搜索猫咪
            search_cats = Cat.objects.filter(name__icontains=keyword).all()#QuerySet
            search_results = []
            for search_cat in search_cats:
                catInfo = {
                    "name": search_cat.name,
                    "catID": search_cat.id,
                    "avatar": search_cat.avatar
                }
                search_results.append(catInfo)
            response = {
                "code": 200,
                "data": {
                    "msg": "success",
                    "results": search_results
                }
            }
        else:
            response = {
                "code": 700,
                "data": {
                    "msg": "wrong parameter"
                }
            }
        return JsonResponse(response)
    
        
    if request.method == 'PUT':
    #Archive_modify 修改猫咪档案        PUT /archive
        put = QueryDict(request.body)
        catid_modify = put.get('id')
        introduction_modify = put.get('introduction')
        add_photos = put.getlist('addPhotos')#python列表   image是个啥类型？暂时写成urlstring
        delete_images = put.getlist('deleteImages')#python列表
        related_cat_list = put.getlist('relatedCats')

        target_cat_name = Cat.objects.get(id=catid_modify).name
        target_archive = Archive.objects.get(name=target_cat_name)
        target_archive.introduction=introduction_modify
        #relatedCats操作
        for related_cat in related_cat_list:
            cat_related = related_cat["relatedCat"]#catID
            relationship = related_cat["relation"]
            #target_archive.relationship_set.
            if not target_archive.relatedCats.all().filter(id=cat_related).exists():#cat可以是catID么？
                target_cat = Cat.objects.filter(id=cat_related).first()
                target_archive.relatedCats.add(target_cat)#add(Cat对象) 直接cat_related?
                Relationship.objects.filter(archive=target_archive, cat=target_cat).first().relation = relationship
        
        target_archive.save()
        #photos增减
        for add_photo in add_photos:
            Photo.objects.create(photo_url=add_photo, containing_archive=target_archive)
        for delete_image in delete_images:
            Photo.objects.filter(photo_url=delete_image).delete()

        response = {
            "code": 200,
            "data": {
                "msg": "success"
            }
        }
        return JsonResponse(response)


    if request.method == 'POST':
    #Archive_add 添加猫咪档案       POST /archive
        cat_name_add = request.POST.get('catName')
        introduction_add = request.POST.get('introduction')
        photos_add = request.POST.getlist('photos')
        related_cat_list_add = put.getlist('relatedCats')

        archive_add = Archive()
        archive_add.name = cat_name_add
        archive_add.introduction = introduction_add
        for c in related_cat_list_add:
            cat_related = c["relatedCat"]#catID
            relationship = c["relation"]
            related_cat_add = Cat.objects.filter(id=cat_related).first()
            archive_add.relatedCats.add(related_cat_add)#
            Relationship.objects.create(archive=archive_add, cat=related_cat_add, relation=relationship)
        archive_add.save()

        cat_add = Cat()
        cat_add.name = cat_name_add 
        #avatar暂且不设？
        cat_add.save()

        response = {
            "code": 200,
            "data": {
                "msg": "success",
                "catID": cat_add.id
            }
        }
        return JsonResponse(response)
    response = {
        "code": 600,
        "data": {
            "msg": "wrong method"
        } 
    }
    return JsonResponse(response)


        
        



