# meth-search-reloaded

A Clojure library designed to ... well, that part is up to you.

## Workflow

1. clone repository
1. run: `lein conf`
1. run: `lein dev`
1. Wait for line - Figwheel: Starting server at http://localhost:3449
1. Open [dev_localhost](http://localhost:3449/index.html)
1. Change some piece of code in src dir
1. Watch how [dev_localhost](http://localhost:3449/index.html) change

## Connect IDE to browser

Use fireplace for VIM or Cider for Emacs

1. Start REPL from IDE:
  1. VIM - dont remember how but if you using VIM - you know )
  1. Emacs - `M-x cider-jake-in` then open REPL tab
1. In REPL run

  ```clojure
  (require 'cljs.repl.browser)
  (cemerick.piggieback/cljs-repl :repl-env (cljs.repl.browser/repl-env :port 9000))
  ```

1. Restart [dev_localhost](http://localhost:3449/index.html)
1. Eval in IDE some cljs code
  1. VIM - `:Eval`
  1. Emacs - `C-c C-c`

## Usage

FIXME

## License

Copyright © 2014 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
