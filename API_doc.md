# API Document for PKU-CAT

by TYJ, ZHD  
2020/04/05

## 相关数据结构

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

### 猫咪

``` json
Cat = {
    "name": string,
    "catID": number
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
        }],
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

// 方法错误：
{
    "code": 700,
    "data": {
        "msg": "wrong method"
    }
}

// 参数错误：
{
    "code": 300,
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
request method: GET

``` json
request.body = {
    "username": string,
    "password": string
}

// 登陆成功：
response.body = {
    "code": 200,
    "data": {
        "msg": "success"
    }
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
request method: GET

``` json
request.body = {
    "email": "1600012607"(string, pku邮箱名，不包括@pku.edu.cn)
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

// 成功注册：
response.body = {
    "code": 200,
    "data": {
        "msg": "success"
    }
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
request method: GET

``` json
request.body = {

}

```

### 动态相关

### 猫咪相关
