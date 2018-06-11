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
        '4c': 'CanonicDFA',
        '5a': 'AcceptPDA',
        '5b': 'Palindrome',
        '5c': 'PDAPrefix',
        '5d': 'CNF',
        '5e': 'CYK'
    }[task]


def get_files(task, path=''):
    automaton_common = lambda x: [os.path.join(path, a) for a in
                                  ['State.java', 'Transition.java', 'DFA.java', 'NFA.java', 'EpsilonNFA.java',
                                   'Parser.java', x + '.java']]
    grammar_common = lambda x: [os.path.join(path, a) for a in
                                ['Production.java', 'Grammar.java', x + '.java', 'Terminal.java', 'NonTerminal.java',
                                 'Atom.java', 'Util.java']]
    pda_common = lambda x: [os.path.join(path, a) for a in
                            ['PDA.java', 'PDAParser.java', 'PDATransition.java', 'State.java', x + '.java', 'Util.java',
                             'Transition.java', 'Atom.java']]

    main = get_main(task)

    if task in ['4a', '5d']:
        return grammar_common(main)
    elif task == '5e':
        lst = grammar_common(main)
        lst.append('CYKTable.java')
        return lst
    elif task in ['5a', '5b', '5c']:
        return pda_common(main)
    else:
        return automaton_common(main)
