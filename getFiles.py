#!/bin/python2
import os


def get_main(task):
    return {
        '3a': 'IsFinite',
        '3b': 'IsEmpty',
        '3c': 'Intersection',
        '3d': 'Operations',
        '3e': 'Equivalent'
    }[task]


def get_files(task, path=''):
    automaton_common = lambda x: [os.path.join(path, a) for a in
                                  ['State.java', 'Transition.java', 'DFA.java', 'NFA.java', 'EpsilonNFA.java',
                                   'Parser.java', x]]
    return automaton_common(get_main(task) + '.java')
