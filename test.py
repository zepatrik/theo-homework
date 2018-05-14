#!/bin/python3
import os
import sys
from subprocess import call, run, PIPE
from getFiles import get_files, get_main
from colorama import Fore, Style

answ_linestart = 'Answer: '


def compile_java(task, output_path='.', source_path=''):
    cmd = ['javac', '-d', output_path]
    cmd.extend(get_files(task, source_path))
    call(cmd)


def run_tests(task, out_path='out', src_path='src', test_path='test'):
    compile_java(task, out_path, src_path)
    for fn in os.listdir(test_path):
        if fn.endswith('.theotest'):
            with open(os.path.join(test_path, fn), 'r') as f:
                test_case = f.read().split('\n')
                answer = test_case.pop()
                p = run(['java', get_main(task)], cwd=out_path, input='\n'.join(test_case)+'\n', stdout=PIPE, universal_newlines=True)

                if answer.startswith(answ_linestart):
                    output = str(p.stdout)[:-1]
                    if output == answer[len(answ_linestart):]:
                        print(f'{Fore.GREEN}pass {fn}{Style.RESET_ALL}')
                    else:
                        print(f'{Fore.RED}fail {fn}: expected {answer[len(answ_linestart):]}, got {output}{Style.RESET_ALL}')


run_tests(*sys.argv[1:])
