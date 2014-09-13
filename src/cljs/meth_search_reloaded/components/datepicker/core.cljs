(ns cljs.meth-search-reloaded.components.datepicker.core
  (:require [om.core :as om :include-macros true]
            [cljs.meth-search-reloaded.components.datepicker.input.core :as input]
            [om-tools.core :refer-macros [defcomponentk]]))

(enable-console-print!)

(defcomponentk datepicker
  [[:data initial] state opts owner]
  (display-name [_]
                "DatePicker")
  (render [_]
          (om/build input/component initial)))

(defn inject [root initial extra]
  (om/root datepicker (atom {:initial initial}) {:target (.getElementById js/document root)}))
