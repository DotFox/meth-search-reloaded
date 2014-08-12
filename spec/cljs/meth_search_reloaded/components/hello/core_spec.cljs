(ns meth-search-reloaded.components.hello.core-spec
  (:require [speclj.core]
            [meth-search-reloaded.components.hello.core :as hello])
  (:require-macros [speclj.core :refer (describe it should=)]))

(describe "Testing hello component"
          (it "should inject hello component"
              (hello/inject "hello")))

