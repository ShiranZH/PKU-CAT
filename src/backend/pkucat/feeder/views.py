#-*- coding:utf-8 _*-  
""" 
@author: tengyuanjian
@file: views.py 
@time: 2020/04/16
@contact: tyyy@pku.edu.cn
"""

from django.shortcuts import render
from django.http import HttpResponse, JsonResponse
from .models import Feed, ApplicationFeeder
from demo.config import CODE
from archive.models import Cat

def feeders(request):
    # 用户已登录
    if request.user.is_authenticated:
        # GET: 获取饲养员列表
        if request.method == 'GET':
            # param catID：获取某一猫咪的饲养员列表
            try:
                catID = request.GET['catID']
                users = []
                feeds = Feed.objects.filter(cat_id=catID)
                if feeds.exists():
                    for feed in feeds:
                        users.append(feed.feeder.id)
                    code = CODE['success']
                    msg = "success"
                else:
                    code = CODE['database_error']
                    msg = "no feeder for this cat"
                data = {
                    'msg': msg,
                    'feeders': users
                }

            # 无参数：获取所有饲养员列表
            except:
                users = []
                feeds = Feed.objects.all()
                if feeds.exists():
                    for feed in feeds:
                        users.append(feed.feeder.id)
                    code = CODE['success']
                    msg = "success"
                else:
                    code = CODE['database_error']
                    msg = "no feeder yet"
                data = {
                    'msg': msg,
                    'feeders': users
                }

            response = {
                'code': code,
                'data': data
            }

        # 其他method，报错
        else:
            response = {
                'code': CODE['method_error']
                'data': {
                    'msg': "wrong method"
                }
            }

    # 用户未登录
    else:
        code = CODE['user_error']
        msg = "not logged in"
        response = {
            'code': code,
            'data': {
                'msg': msg
            }
        }

    return JsonResponse(response)


def apply(request):
    # 用户已登录
    if request.user.is_authenticated:
        # POST: 添加申请记录
        if request.method == 'POST':
            catID = request.POST.get('catID')
            applyInfo = request.POST.get('apply_info')
            if catID is None or applyInfo is None:
                code = CODE['parameter_error']
                msg = "wrong parameters"
            else:
                userID = request.user.id
                cat = Cat.objects.filter(id=catID)
                if cat.exists():
                    newApply = ApplicationFeeder.objects.create(
                        feeder=request.user,
                        cat=cat
                    )
                    newApply.save()
                    code = CODE['success']
                    msg = "success"
                else:
                    code = CODE['database_error']
                    msg = "no match cat in database"
            response = {
                'code': code,
                'data': {
                    'msg': msg
                }
            }

        elif request.method == 'DELETE':
            applyID = request.DELETE.get('applyID')
            if applyID is None:
                code = CODE['parameter_error']
                msg = "wrong parameters"
            else:
                application = ApplicationFeeder.objects.filter(id=applyID)
                if application.exists():
                    # 删除申请记录
                    application.delete()
                    code = CODE['success']
                    msg = "success"
                else:
                    code = CODE['database_error']
                    msg = "no match application in database"
            response = {
                'code': code,
                'data': {
                    'msg': msg
                }
            }

        # 其他method，报错
        else:
            response = {
                'code': CODE['method_error'],
                'data': {
                    'msg': "wrong method"
                }
            }

    # 用户未登录
    else:
        code = CODE['user_error']
        msg = "not logged in"
        response = {
            'code': code,
            'data': {
                'msg': msg
            }
        }

    return JsonResponse(response)


def agree(request):
    # 用户已登录
    if request.user.is_authenticated:
        # 用户是管理员
        if request.user.is_superuser:
            # POST: 同意申请
            if request.method == 'POST':
                applyID = request.POST.get('applyID')
                if applyID is None:
                    code = CODE['parameter_error']
                    msg = "wrong parameters"
                else:
                    # 添加饲养员记录
                    application = ApplicationFeeder.objects.filter(id=applyID)
                    if application.exists():
                        newFeed = Feed.objects.create(
                            feeder = application.feeder,
                            cat = application.cat
                        )
                        newFeed.save()
                        # 删除申请记录
                        application.delete()
                        code = CODE['success']
                        msg = "success"
                    else:
                        code = CODE['database_error']
                        msg = "no match application in database"
                response = {
                    'code': code,
                    'data': {
                        'msg': msg
                    }
                }

            # 其他method，报错
            else:
                response = {
                    'code': CODE['method_error'],
                    'data': {
                        'msg': "wrong method"
                    }
                }

        # 用户不是管理员
        else:
           response = {
                'code': CODE['user_error'],
                'data': {
                    'msg': "user is not super user"
                }
            } 

    # 用户未登录
    else:
        code = CODE['user_error']
        msg = "not logged in"
        response = {
            'code': code,
            'data': {
                'msg': msg
            }
        }

    return JsonResponse(response)


def applys(request):
    # 用户已登录
    if request.user.is_authenticated:
        # 用户是管理员
        if request.user.is_superuser:
            # GET：查看所有申请记录
            if request.method == 'GET':
                applys = ApplicationFeeder.objects.all()
                try:
                    if len(applys) > 0:
                        dics = []
                        for apply in applys:
                            dic = dict()
                            dic['applyID'] = apply.id
                            dic['feederID'] = apply.feeder_id
                            dic['catID'] = apply.cat_id
                            dics.append(dic)
                        code = CODE['success']
                        msg = "success"

                        return JsonResponse({
                            'code': code,
                            'data': {
                                'msg': msg,
                                'applies': dics
                            }
                        })

                    else:
                        code = CODE['database_error']
                        msg = "no application in database"
                except:
                    code = CODE['database_error']
                    msg = "no application in database"
                
                response = {
                    'code': code,
                    'data': {
                        'msg': msg
                    }
                }

            # 其他method，报错
            else:
                response = {
                    'code': CODE['method_error'],
                    'data': {
                        'msg': "wrong method"
                    }
                }

        # 用户不是管理员
        else:
           response = {
                'code': CODE['user_error'],
                'data': {
                    'msg': "user is not super user"
                }
            } 

    # 用户未登录
    else:
        code = CODE['user_error']
        msg = "not logged in"
        response = {
            'code': code,
            'data': {
                'msg': msg
            }
        }

    return JsonResponse(response)