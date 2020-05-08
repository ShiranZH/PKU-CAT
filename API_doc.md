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
    "start": number,
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

### URI概览

```
/user
	/user/login
    /user/register
        /user/register/validation
    /user/logout
    /user/profile

/feeder
	/feeders
	/feeder/apply
	/feeder/applys
	
/post
	/posts
	/post/favor
	/post/comment
	/post/comments

/archive
	/archives
	
/file
```

### 上传文件

POST /file

```json
request.body = {
    "picture": [file1, file2, ...],
    "video": [file3, file4, ...]
}

// 上传成功：
response.body = {
    "code": 200,
    "data": {
        "msg": "success",
        "picture": [url1, url2, ...],
        "video": [url1, url2, ...],
    }
}

// 上传失败：
response.body = {
    "code": 700,
    "data": {
        "msg": "parameter error",
    }
}
```



### 用户相关

#### Login 登陆

POST /user/login

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

POST /user/register

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

POST /user/register/validation

``` json
request.body = {
    "username": "pkucat" (string),
    "password": "pkucat2020" (string),
    "verificationCode": "666666" (string),
}

// 用户名重复：
response.body = {
    "code": 300,
    "data": {
        "msg": "duplicate username"
    }
}

// 验证码错误：
response.body = {
    "code": 400,
    "data": {
        "msg": "verification code error"
    }
}
```

#### Logout 注销

POST /user/logout

``` json
request.body = { }
```

#### UserProfile 查看个人信息

GET /user/profile

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

PUT /user/profile

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

POST /feeder/apply

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

#### Feeder_list 查看饲养员列表

GET /feeders

``` json
// 成功返回
response.body = {
    "code": 200,
    "data": {
        "msg": "success",
        "feeders": [
            User,
        ]
    }
}
```

#### Feeder_apply_list 查看申请列表

GET /feeder/applys

``` json
// 成功
response.body = {
    "code": 200,
    "data": {
        "msg": "success",
        "applies": [
            {
                "applyID": number,
                "catID": number,
    			"apply_info": string,
            }
        ]
    }
}
```
#### Feeder_agree 同意饲养员申请

POST /feeders

``` json
request.body = {
    "applyID": number,
}

// 失败
response.body = {
    "code": 300,
    "data": {
        "msg": "wrong parameter",
    }
}
```
#### Feeder_refuse 拒绝饲养员申请

DELETE /feeder/apply

``` json
request.body = {
    "applyID": number,
}

// 失败
response.body = {
    "code": 300,
    "data": {
        "msg": "wrong parameter",
    }
}
```

### 动态相关

#### 发布动态

POST /post

``` json
request.body = {
    "post": Post,
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

DELETE /post?id=123

```json
// 成功
response.body = {
    "code": 200,
    "data": {
        "msg": "delete post successfully"
    }
}
```

#### 请求动态列表（时间顺序）

GET /posts?limit=10&start=10&comments=10

limit默认为10，start默认为1，表示从第一条动态开始，comments默认为10，表示下载10条最新评论

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

GET /posts/search?catid=123&userid=123&keyword=大威&comments=10

cat-id表示猫咪的id，user-id表示用户id，keyword表示关键词，这三个参数至少有一个不为空，都为空返回结果同`请求动态列表（时间顺序）`

comments默认为10，表示顺便下载10条最新评论

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

GET /post/comments?type=post&root-id=123&limit=10&start=10

``` json
response = {
    "code": 200,
    "data": {
        "commentList": CommentList
    }
}
```

#### 动态点赞

PUT /post/favor

``` json
request.body = {
    "postID": number
}
```

#### 取消点赞

DELETE /post/favor

``` json
request.body = {
    "postID": number
}
```

#### 评论动态

POST /post/comment

``` json
request.body = {
    "postID": number,
    "text": string,
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

GET /archives

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

GET /archive?catid=10

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

PUT /archive

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

POST /archive

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

GET /archive?keyword="大威"

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

