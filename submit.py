#!/bin/python3
import requests
import json
import sys
import os
from getFiles import get_files

configJson = os.path.join(os.path.dirname(os.path.abspath(sys.argv[0])), 'config.json')

with open(configJson, 'r') as f:
    config = json.loads(f.read())


def login():
    if 'USER' not in config:
        config['USER'] = input('[user]: ')
    if 'PASSWD' not in config:
        config['PASSWD'] = input('[password]: ')

    url = 'https://judge.in.tum.de/theo/public/login.php'
    payload = {
        'cmd': 'login',
        'login': config['USER'],
        'passwd': config['PASSWD']
    }

    r = requests.post(url, data=payload, allow_redirects=False)

    if r.status_code != 302:
        print('wrong credentials')
        exit(1)

    config['PHP_COOKIE'] = r.cookies['PHPSESSID']

    with open(configJson, 'w') as f:
        f.write(json.dumps(config))


def submit():
    if 'PHP_COOKIE' not in config:
        login()

    url = 'https://judge.in.tum.de/theo/team/upload.php'
    files = map(lambda f: ('code[]', (f, open(f, 'rb'))), get_files(sys.argv[3], './'))
    data = {
        'probid': sys.argv[2],
        'langid': 'java',
        'submit': 'submit'
    }
    cookies = {
        'PHPSESSID': config['PHP_COOKIE'],
        'domjudge_cid': sys.argv[1]
    }
    r = requests.post(url, data=data, files=files, cookies=cookies, allow_redirects=False)

    if r.status_code != 302:
        print('http code %d, expected 302' % r.status_code)

        if str(r.text).find('Password:'):
            print('trying to login again')
            login()
            submit()
            return
        else:
            exit(1)

    print('upload seemed to work')


submit()
