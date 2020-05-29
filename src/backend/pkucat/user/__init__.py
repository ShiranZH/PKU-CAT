from .apps import UserConfig

default_app_config = 'user.apps.UserConfig'


def get_user_profile(user_id):
    user_profile = {}
    if User.objects.filter(id=user_id).exists():
        user = User.objects.get(id=user.id)
        user_profile['user'] = {'name':user.username, "userID":user.id}
        user_profile['avatar'] = user.avatar if user.avatar != '' else '/static/user/avatar_default.jpg'
        user_profile['mail'] = user.pku_mail 
        user_profile['whatsup'] = user.whatsup
    return user_profile