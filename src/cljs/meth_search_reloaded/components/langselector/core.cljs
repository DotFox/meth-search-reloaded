(ns cljs.meth-search-reloaded.components.langselector.core
  (:require-macros [schema.macros :refer [defschema]]
                   [cljs.core.async.macros :refer [go]])
  (:require [om.core :as om :include-macros true]
            [om-tools.core :refer-macros [defcomponentk]]
            [om-tools.dom :as dom :include-macros true]
            [schema.core :as s]
            [cljs.utils.publisher :as p]
            [cljs.core.async :refer [>! put!]]))

(defn handle-click [selected state]
  (.setLanguage js/Date.i18n selected)
  (put! p/publisher {:topic :lang-change :new selected})
  (swap! state assoc :current selected))

(defschema LangSelector
  {:available [js/String]
   :current js/String})

(defcomponentk langselector
  "Language selector"
  [[:data available current] :- LangSelector state opts owner]
  (display-name [_]
                (or (:react-name opts) "LangSelector"))
  (init-state [_]
              {:current current
               :available available})
  (render-state [_ _]
                (dom/ul
                 (for [i (filter (fn [x]
                                   (not= x (:current @state)))
                                 (:available @state))]
                   (dom/li
                    (dom/button
                     {:on-click #(handle-click i state)}
                     (str i)))))))

(defn inject [root initial]
  (om/root langselector
           (or initial {:current "en-US"
                        :available ["ru-RU"
                                    "th-TH"
                                    "en-US"]})
           {:target (.getElementById js/document root)}))

