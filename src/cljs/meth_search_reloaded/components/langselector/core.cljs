(ns cljs.meth-search-reloaded.components.langselector.core
  (:require-macros [schema.macros :refer [defschema]])
  (:require [om.core :as om :include-macros true]
            [om-tools.core :refer-macros [defcomponentk]]
            [om-tools.dom :as dom :include-macros true]
            [schema.core :as s]
            [cljs.utils.publisher :as p]
            [cljs.core.async :refer [put!]]))

(defn handle-click [selected state]
  (.setLanguage js/Date.i18n selected)
  (swap! state assoc :prev (:current @state) :current selected)
  (put! p/publisher {:topic :lang-change
                     :new (:current @state)
                     :old (:old @state)}))

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
               :available available
               :old nil})
  (render-state [_ _]
                (dom/ul
                 (for [i (:available @state)]
                   (dom/li
                    (dom/button
                     {:on-click #(handle-click i state)
                      :disabled (if (= i (:current @state)) true false)}
                     (str i)))))))

(defn inject [root initial]
  (om/root langselector
           (or initial {:current "en-US"
                        :available ["ru-RU"
                                    "th-TH"
                                    "en-US"]})
           {:target (.getElementById js/document root)}))

