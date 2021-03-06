# Introduction

This project is intended for me to learn to write a [Language Server
Protocol](https://microsoft.github.io/language-server-protocol/) (LSP)
server, using the programming language [Clojure](https://clojure.org/)
as the language to implement the LSP server process.

I started this project not knowing anything about how to implement an
LSP server, so if you are in a similar situation, perhaps this
repository could help you learn how to do so, too.


# Motivation

I am not trying to learn and implement every feature of LSP.  My
eventual purpose is to implement enough of an LSP for the
[P4_16](https://p4.org/) programming language to enable this
functionality in two editors/IDEs of interest to me:

+ In [Visual Studio Code](https://code.visualstudio.com/) (aka
  VSCode):
  + Create an [Outline
    giew](https://code.visualstudio.com/docs/getstarted/userinterface#_outline-view)
    of objects defined in the program, where clicking on a name in the
    outline view will jump to that name's definition in the program.
  + Implement the [Go To
    Definition](https://code.visualstudio.com/docs/editor/editingevolved#_go-to-definition)
    feature for names that the cursor is on/near.
  + Implement the [Peek
    definition](https://docs.microsoft.com/en-us/visualstudio/ide/how-to-view-and-edit-code-by-using-peek-definition-alt-plus-f12?view=vs-2022#use-peek-definition)
    feature for names that the cursor is on/near.

+ In [GNU Emacs](https://www.gnu.org/software/emacs/):
  + Create a [treemacs](https://github.com/emacs-lsp/lsp-treemacs)
    symbols view using `M-x lsp-treemacs-symbols` (see [here in the
    docs](https://github.com/emacs-lsp/lsp-treemacs#lsp-treemacs-symbols)),
    similar to VSCode's Outline View.
  + Implement jump to definition of the name the cursor is on/near.
  + If there is a convenient way to implement something like VSCode's
    Peek definition feature, do that.  Alternately if there is a way
    to jump to the definition, but in a different buffer/frame from
    where the cursor is now, and the "jump from" place remains visible
    while the jumped-to location also becomes visible, that would be
    wonderful

As a step towards reaching that goal, I want to first implement the
features above for a tiny "toy" language that I am calling `toylang`.
`toylang` is not a real programming language.  It has just enough
syntax to be able to represent these kinds of things:

+ "definitions" for two kinds of names:
  + "variables" and "functions".
+ "uses" or "references" for these names.

This is just enough to be able to implement the features above for
`toylang`, and test this implementation on simple `toylang` source
files.

TODO: Consider adding comments, e.g. from "//" to the end of a line,
to see how to start moving a little bit more in the direction of a
real language syntax.

TODO: Also consider adding the option to use the C preprocessor for
toylang, and having the LSP server run the C preprocessor, and parse
the output of the C preprocessor, keeping track of file names and line
numbers within each file on disk across uses of #include between
files.


# Possibly useful sources for more info

+ Emacs `lsp-mode`
  [documentation](https://github.com/emacs-lsp/lsp-mode/blob/master/docs/page/adding-new-language.md)
  on adding support for a new programming language
+ [`lsp4j`](https://github.com/eclipse/lsp4j) - A Java library
  intended to help implement the Language Server Protocol (LSP)


# License

Copyright ?? 2022 Andy Fingerhut

Distributed under the EPL License, same as Clojure. See LICENSE.
