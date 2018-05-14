# Theo Homework
A repo to use as a skeleton for your THEO homework with a view helping scripts.

The scripts can be used with any other project structure as well, have a look below for a quick explanation.

## submit.py
Submits files to TUMJudge

Usage:
```
$ ./submit.py tumjudge_cid tumjudge_probid [...files]
```
The contest and problem IDs can be found by inspecting the source code of TUMJudge.
You can also extract the IDs from the `MAKEFILE` or run `$ make submit3a`

## test.py
Compiles Java code and then runs all tests found in `test_dir`. Test have to follow the format defined by the parser
followed by the line `Answer: false` respectively the expected value.

Usage:
```
$ ./test.py task out_dir src_dir test_dir
```
For an example have a look at the `Makefile`.

## Contributions

Please open a pull requests to add templates and/or update the makefile accordingly.
Improvements are welcome.

I will also write a script to automatically detect the type of an exception sometime soon. You can watch the repository
to get notified.
