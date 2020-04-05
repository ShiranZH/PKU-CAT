# API Document for PKU-CAT

by TYJ, ZHD  
2020/04/05

## 相关数据结构

### 猫咪

``` json
Cat = {
    "name": string,
    "catID": number,
    "avatar": url (string)
}
```

### 猫咪档案

``` json
Archive = {
    "name": string,
    "photos": [
        url (string),
    ],
    "introduction": string,
    "relatedCats": [{
            "relatedCat": Cat,
            "relation": string
        }]
}
```

### 用户

``` json
User = {
    "name": string,
    "userID": number,
}
```

### 用户档案

``` json
UserProfile = {
    "user": User,
    "avatar": url (string),
    "mail": string,
    "whatsup": string
}
```

### 饲养员申请

``` json
Feeder_application = {
    "applicationID": number,
    "user": User,
    "apply_info": string,
    "cat": Cat
}
```

### 动态

``` json
Post = {
    "publisher": PublisherInfo,
    "time": "2020-04-05 16:02" (string),
    "text": string,
    "multimediaContent": [
        url (string),
    ],
    "commentList": [
        string,
    ],
    "favorCnt": int,
}

PublisherInfo = {
    "userID": number,
    "username": string,
    "avatar": url (string)
}

```

## Codes

body中code相关约定  
见src/backend/config.py  

``` python
CODE = {
    "success": 200,
    "database_error": 300,
    "user_error": 400,
    "method_error": 600,
    "parameter_error": 700,
}
```

## Response Body Fudanmental

服务器响应body的基本数据格式  
其中data为json

``` json
// 通用：
{
    "code": xxx,
    "data": {
        "msg": string,
        xxx: xxx
    }
}

// 成功：
{
    "code": 200,
    "data": {
        "msg": "success"
    }
}

// 用户无权限
response.body = {
    "code": 400,
    "data": {
        "msg": "not authorized"
    }
}

// 用户未登录
response.body = {
    "code": 400,
    "data": {
        "msg": "user not logged in"
    }
}

// 方法错误：
{
    "code": 600,
    "data": {
        "msg": "wrong method"
    }
}

// 参数错误：
{
    "code": 700,
    "data": {
        "msg": "wrong parameter"
    }
}

```

## APIs

每个接口的具体设计

### 用户相关

#### Login 登陆

uri: /login  
request method: POST

``` json
request.body = {
    "username": string,
    "password": string
}

// 登陆失败：
response.body = {
    "code": 300,
    "data": {
        "msg": "username error" / "password error"
    }
}
```

#### Register 注册

uri: /register
request method: POST

``` json
request.body = {
    "email": "1600012607" (string, pku邮箱名，不包括@pku.edu.cn)
}


// 邮件已注册：
response.body = {
    "code": 300,
    "data": {
        "msg": "email address already registered"
    }
}
```

#### Register_validation 注册验证

uri: /register/validation
request method: POST

``` json
request.body = {
    "username": "pkucat" (string),
    "password": "pkucat2020" (string)
}

// 用户名重复：
response.body = {
    "code": 300,
    "data": {
        "msg": "duplicate username"
    }
}
```

#### Logout 注销

uri: /logout
request method: POST

``` json
request.body = { }
```

#### UserProfile 查看个人信息

uri: /userProfile
request method: GET

``` json
request.body = { }

// 成功返回
response.body = {
    "code": 200,
    "data": {
        "msg": "success",
        "profile": Userprofile
    }
}
```

#### UserProfile_modify 修改个人信息

uri: /userProfile/modify
request method: PUT

``` json
request.body = {
    "avatar": image,
    "whatsup": string,
    "username": string
}

// 用户名重复
response.body = {
    "code": 300,
    "data": {
        "msg": "duplicate username"
    }
}

```

#### Feeder_apply 申请成为饲养员

uri: /feeder/apply
request method: POST

``` json
request.body = {
    "catID": number,
    "apply_info": string
}

// 已成为饲养员
response.body = {
    "code": 300,
    "data": {
        "msg": "already been feeder"
    }
}

```

#### Feeder_list 查看申请列表

uri: /feeder/list
request method: GET

``` json
request.body = { }

// 成功返回
response.body = {
    "code": 200,
    "data": {
        "msg": "success",
        "applications": [
            Feeder_application,
        ]
    }
}
```

#### Feeder_accept 同意饲养员申请

uri: /feeder/accept
request method: POST

``` json
request.body = {
    "applicationID": number
}
```

### 动态相关

### 猫咪相关

#### Archive_list 查看猫咪档案列表

uri: /archive/list
request method: GET

``` json
request.body = { }

// 成功返回
resonse.body = {
    "code": 200,
    "data": {
        "msg": "success",
        "catList": [
            Cat,
        ]
    }
}
```

#### Archive_detail 查看猫咪档案详情

uri: /archive/detail
request method: GET

``` json
request.body = {
    "catID": number
}

// 成功返回
resonse.body = {
    "code": 200,
    "data": {
        "msg": "success",
        "archive": Archive
    }
}
```

#### Archive_modify 修改猫咪档案

uri: /archive/modify
request method: PUT

``` json
request.body = {
    "catID": number,
    "introcudtion": string,
    "addPhotos": [
        image,
    ],
    "deleteImages": [
        url (string),
    ],
    "relatedCats": [{
            "relatedCat": Cat,
            "relation": string
        }]
}
```

#### Archive_add 添加猫咪档案

uri: /archive/add
request method: POST

``` json
request.body = {
    "catName": string,
    "introduction": string,
    "photos": [
        image,
    ],
    "relatedCats": [{
            "relatedCat": Cat,
            "relation": string
        }]
}

// 成功添加
response.body = {
    "code": 200,
    "data": {
        "msg": "success",
        "catID": number
    }
}
```

#### Archive_search 搜索猫咪

uri: /archive/search
request method: GET

``` json
request.body = {
    "keyword": string
}

// 搜索结果
response.body = {
    "code": 200,
    "data": {
        "msg": "success",
        "results": [
            Cat,
        ]
    }
}

```
