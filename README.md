# meth-search-reloaded

A Clojure library designed to ... well, that part is up to you.

## Workflow

1. clone repository
1. run: lein conf
1. run: lein dev
1. Wait for line - Figwheel: Starting server at http://localhost:3449
1. Open http://localhost:3449
1. Change some piece of code in src dir
1. Watch how http://localhost:3449 change

## Connect IDE to browser

Use fireplace for VIM or Cider for Emacs

1. Start REPL from IDE:
1.1. VIM - dont remember how but if you using VIM - you know )
1.2. Emacs - M-x cider-jake-in then open REPL table
2. In REPL run

  (require 'cljs.repl.browser)
  (cemerick.piggieback/cljs-repl :repl-env (cljs.repl.browser/repl-env :port 9000)

3. Restart http://localhost:3449
4. Eval in IDE some cljs code
4.1. VIM - :Eval
4.2. Emacs - C-c C-c

## Usage

FIXME

## License

Copyright © 2014 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
