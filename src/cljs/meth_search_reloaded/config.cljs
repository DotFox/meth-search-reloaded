(ns cljs.meth-search-reloaded.config
  (:require [om/core :as om :include-macros true]))

(defn config
  ([owner]
     (or (om/get-shared owner :config) {}))
  ([owner key]
     (get (config owner) key)))

(def state
  (atom {}))

