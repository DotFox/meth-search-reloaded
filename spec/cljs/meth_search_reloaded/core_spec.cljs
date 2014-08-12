(ns meth-search-reloaded.core-spec
  (:require [speclj.core]
            [meth-search-reloaded.core :as ms]
            [dommy.utils :as utils]
            [dommy.core :as dommy])
  (:use-macros [dommy.macros :only [node sel sel1]])
  (:require-macros [speclj.core :refer (describe it should=
                                                 should==
                                                 should-not=)]))

(describe "Testing core module"
          (it "tests add function"
              (should= 4 (ms/add 1 3)))
          (it "test prepare-component-root function"
              (let [not-injected (sel1 "#test")
                    body (ms/prepare-component-root "test" "body")
                    injected (sel1 "#test")]
                (should= injected (sel1 "#test"))
                (should= not-injected nil)
                (should-not= injected not-injected))))

