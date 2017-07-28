import json
import os
import platform
import traceback
import urllib
import urllib2
# import common

manager_ip = '127.0.0.1'
manager_port = 13101


class ServerId:
    def __init__(self):
        pass

    AUTH = 17104897
    GAME = 16842753


class OSType:
    def __init__(self):
        pass

    NONE = 0
    IOS = 1
    ANDROID = 2


class LoginPlatform:
    def __init__(self):
        pass

    TEST = 'test'
    WEIXIN = 'wx'


def dict_to_json(arg):
    return json.dumps(arg)


def get_product_version_args():
    args = dict()
    args['server_id'] = ServerId.AUTH
    args['handler'] = 'UpdateProductVersionHandler'
    args['product_version'] = '0'
    args['product_description'] = ''

    return args

def get_white_user():
    args = dict()
    args['server_id'] = ServerId.AUTH
    args['handler'] = 'SetWhiteListEnableHandler'
    args['enabled'] = '0'

    return args


def get_lib_version_args(login_platform):
    args = dict()
    args['server_id'] = ServerId.AUTH
    args['handler'] = 'InsertLibraryVersionHandler'
    args['library_version'] = '0.0.0.0'
    args['library_description'] = ''
    args['platform'] = str(login_platform)
    args['library_url'] = ''
    args['force_update'] = False

    return args


def get_login_platform_args(platform_id, login_platform):
    args = dict()
    args['server_id'] = ServerId.GAME
    args['handler'] = 'AllowLoginPlatformHandler'
    args['operation'] = 0
    args['id'] = platform_id
    args['allow_login'] = login_platform

    return args


def get_gmt_url(ip, port, args):
    url = 'http://'
    url += ip + ':' + str(port)
    url += '/gmtools?'

    normal_json = dict_to_json(args)
    quote_json = urllib.quote(normal_json)
    url += quote_json

    return url


def request_manager(args):
    url = get_gmt_url(manager_ip, manager_port, args)
    req = urllib2.Request(url)
    # common.request(url)
    print url
    res_data = urllib2.urlopen(req)
    res = res_data.read()
    print res


if __name__ == '__main__':
    try:
        product_version_args = get_product_version_args()
        request_manager(product_version_args)

        windows_lib_version_args = get_lib_version_args(OSType.NONE)
        request_manager(windows_lib_version_args)

        ios_lib_version_args = get_lib_version_args(OSType.IOS)
        request_manager(ios_lib_version_args)

        android_lib_version_args = get_lib_version_args(OSType.ANDROID)
        request_manager(android_lib_version_args)

        test_login_args = get_login_platform_args(1, LoginPlatform.TEST)
        request_manager(test_login_args)

        wx_login_args = get_login_platform_args(2, LoginPlatform.WEIXIN)
        request_manager(wx_login_args)

        white_user_args = get_white_user()
        request_manager(white_user_args)
    except:
        traceback.print_exc()
        # common.pause()
    finally:
        os.system("pause")
        exit()