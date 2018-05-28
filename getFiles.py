#!/bin/python2
import os


def get_main(task):
    return {
        '3a': 'IsFinite',
        '3b': 'IsEmpty',
        '3c': 'Intersection',
        '3d': 'Operations',
        '3e': 'Equivalent',
        '4a': 'CanProduce',
        '4b': 'Minimize',
        '4c': 'CanonicDFA'
    }[task]


def get_files(task, path=''):
    automaton_common = lambda x: [os.path.join(path, a) for a in
                                  ['State.java', 'Transition.java', 'DFA.java', 'NFA.java', 'EpsilonNFA.java',
                                   'Parser.java', x + '.java']]
    grammar_common = lambda x: [os.path.join(path, a) for a in
                                ['Production.java', 'Grammar.java', x + '.java']]

    return automaton_common(get_main(task)) if task != '4a' else grammar_common(get_main(task))
