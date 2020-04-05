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
    // 这里的postID在创建post时无效
    "postID": number,
    "publisher": PublisherInfo,
    "time": "2020-04-05 16:02" (string),
    "text": string,
    "multimediaContent": [
        url (string),
    ],
    "commentList": CommentList,
    "favor": {
        "self": number,     // 1表示自己点赞了
        "favorCnt": number,
    }
}

PublisherInfo = {
    "userID": number,
    "username": string,
    "avatar": url (string)
}
```

### 评论列表

``` json
CommentList = {
    "rootType": string,     // cat or post
    "rootID", number,
    "totalCount": number,
    "downloadCount": number,
    "begin": number,
    "comments": [
        Comment,
    ]
}
```

### 评论

``` json
// 暂时没有考虑评论嵌套
Comment = {
    "commentID": number,
    "user": User,
    "text": string
}
```

---

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

---

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

#### 发布动态

uri: /post

request method: POST

``` json
request.body = {
    "post": Post,
}

// 成功
response.body = {
    "code": 200,
    "data": {
        "msg": "create post successfully",
        "postID": number,
    }
}

// 失败
response.body = {
    "code": 700,
    "data": {
        "msg": "wrong parameter",
    }
}
```

#### 删除动态

uri: /post?id=123

request method: DELETE

```json
// 成功
response.body = {
    "code": 200,
    "data": {
        "msg": "delete post successfully"
    }
}

// 失败: 动态不存在
response.body = {
    "code": 300,
    "data": {
        "msg": "post not exists",
    }
}

// 失败: 没有权限
response.body = {
    "code": 400,
    "data": {
        "msg": "user permission error",
    }
}
```

#### 请求动态列表（时间顺序）

uri: /posts?num=10&begin=10&comments=10

num默认为10，begin默认为1，表示从第一条动态开始，comments默认为10，表示顺便下载10条最新评论

request method: GET

``` json
response = {
    "code": 200,
    "data": {
        "downloadCount": number,
        "data": [
            Post,
        ]
    }
}
```

#### 查找动态

uri: /posts/search?cat-id=123&user-id=123&keyword=大威&comments=10

cat-id表示猫咪的id，user-id表示用户id，keyword表示关键词，这三个参数至少有一个不为空，都为空返回结果同`请求动态列表（时间顺序）`

comments默认为10，表示顺便下载10条最新评论

request method: GET

``` json
response = {
    "code": 200,
    "data": {
        "downloadCount": number,
        "data": [
            Post,
        ]
    }
}
```

#### 请求动态评论

uri: /comments?type=post&root-id=123&num=10&begin=10
request method: GET

``` json
response = {
    "code": 200,
    "data": {
        "commentList": CommentList
    }
}
```

#### 动态点赞

uri: /post/favor
request method: POST

``` json
request.body = {
    "postID": number
}

// 已点赞
response = {
    "code": 300,
    "data": {
        "msg": "favor exists",
    }
}
```

#### 评论动态

uri: /post/comment
request method: POST

``` json
request.body = {
    "rootType": string,
    "rootID": number,
    "userID": number,
    "text": string,
}

// 评论成功
response = {
    "code": 200,
    "data": {
        "msg": "comment successfully",
        "commentID": number,
    }
}

// 动态不存在
response = {
    "code": 300,
    "data": {
        "msg": "post not exists",
    }
}
```

### 猫咪相关

#### Archive_list 查看猫咪档案列表

uri: /archive/list
request method: GET

``` json
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

uri: /archive/detail?catid=10
request method: GET

``` json
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

uri: /archive/search?keyword="大威"
request method: GET

``` json
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
