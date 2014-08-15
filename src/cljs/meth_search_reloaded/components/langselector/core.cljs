(ns meth-search-reloaded.components.langselector.core
  (:require-macros [schema.macros :refer [defschema]])
  (:require [om.core :as om :include-macros true]
            [om-tools.core :refer-macros [defcomponentk]]
            [om-tools.dom :as dom :include-macros true]
            [schema.core :as s]
            [cljs.core.async :as async :refer [put! chan]]))

(defn handle-click [selected state]
  (swap! state assoc :current selected)
  (put! (:lang-channel @state) selected)
  (.setLanguage js/Date.i18n selected))

(defschema LangSelector
  {:available [js/String]
   :current js/String
   :lang-channel (type chan)})

(defcomponentk langselector
  "Language selector"
  [[:data available current lang-channel] :- LangSelector state opts owner]
  (display-name [_]
                (or (:react-name opts) "LangSelector"))
  (init-state [_]
              {:current current
               :available available
               :lang-channel lang-channel})
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
                                    "en-US"]
                        :lang-channel (chan)})
           {:target (.getElementById js/document root)}))

