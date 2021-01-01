This folder contains a set of examples/tests, also used as regression tests at each release.

Each different source file type is kept in a separate folder, to avoid confusion between
original (that is test written by developers) and files generated during compilation process
(e.g. when testing a .fbf file, we also generate a .bf file that we do not want to be tested).

`ca65` folder includes a complete functional test for the 6502bf emulator.

For each test, a `.ref` file is provided which contains "reference" output for the test.
A test program is supposed to run successfully if its outpur matches content of corresponding `.ref` file.