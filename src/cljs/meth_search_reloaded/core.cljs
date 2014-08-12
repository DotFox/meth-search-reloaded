(ns meth-search-reloaded.core
  (:use-macros [dommy.macros :only [node sel sel1]])
  (:require [om.core :as om :include-macros true]
            [meth-search-reloaded.components.hello.core :as hello]
            [dommy.utils :as utils]
            [dommy.core :as dommy]))

(defn prepare-component-root [what where]
  (let [what-node (node [:div {:id what}])
        where-node (sel1 where)]
    (dommy/append! where-node what-node)))

(hello/inject "hello")

(defn add [a b]
  (+ a b))

