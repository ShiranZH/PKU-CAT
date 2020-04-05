# API Document for PKU-CAT

by TYJ, ZHD  
2020/04/05

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

``` text
通用：
{
    "code": xxx,
    "data": {
        "msg": string,
        xxx: xxx
    }
}

成功：
{
    "code": 200,
    "data": {
        "msg": "success"
    }
}

方法错误：
{
    "code": 700,
    "data": {
        "msg": "wrong method"
    }
}

参数错误：
{
    "code": 300,
    "data": {
        "msg": "wrong parameter"
    }
}

```

## APIs

每个接口的具体设计

### Login 登陆

uri: /login  
request method: GET

``` text
request.body = {
    "username": string,
    "password": string
}

登陆成功：
response.body = {
    "code": 200,
    "data": {
        "msg": "success"
    }
}

登陆失败：
response.body = {
    "code": 300,
    "data": {
        "msg": "username error" / "password error"
    }
}
```

### Register 注册

uri: /register
request method: GET

``` text
request.body = {
    "email": string: pku邮箱名（不包括@pku.edu.cn）
}


邮件已注册：
response.body = {
    "code": 300,
    "data": {
        "msg": "email address already registered"
    }
}

```

### Register_validation 注册验证

uri: /register/validation
request method: POST

``` text
request.body = {
    "username": string,
    "password": string
}

成功注册：
response.body = {
    "code": 200,
    "data": {
        "msg": "success"
    }
}

用户名重复：
response.body = {
    "code": 300,
    "data": {
        "msg": "duplicate username"
    }
}

```
