# hasm-java
**H**ack **AS**e**M**bler written in Java.

This is an assembler written for the Hack computer designed in the book *The Elements of Computing Systems*.

# Overview
*The Elements of Computing Systems* is one of the best books on computer science that I have ever found. In 12 chapters, it describes how to get from a simple `nand` logic gate to a full computer (called Hack) with an operating system written in a high level programming language (Jack).

This is my implementation of the assembler for the Hack platform.

For an overview of Hack assembly, see chapters 4 and 6 of *The Elements of Computing Systems*.

## Compiling
```
$ make
```

## Usage
```
$ java Hasm file.asm
```

Assembled file is `file.hack`.


