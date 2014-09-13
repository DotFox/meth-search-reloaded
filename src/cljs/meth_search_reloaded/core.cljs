(ns cljs.meth-search-reloaded.core
  (:use-macros [dommy.macros :only [node sel sel1]])
  (:require [om.core :as om :include-macros true]
            [cljs.meth-search-reloaded.components.datepicker.core :as datepicker]
            [cljs.meth-search-reloaded.components.langselector.core :as langselector]
            [dommy.utils :as utils]
            [dommy.core :as dommy]
            [cljs.core.async :as async :refer [chan]]))

(def application
  (let [first->second (chan)
        second->first (chan)
        lang-> (chan)]
    (atom {:datepicker [{:id "date1"
                         :initial {:date (.addWeeks (js/Date.) 1)
                                   :out-fmt "d MMM yyyy"
                                   :in-ch second->first
                                   :out-ch first->second}
                         :inject datepicker/inject}
                        {:id "date2"
                         :initial {:date (.addWeeks (js/Date.) 2)
                                   :out-fmt "d MMM yyyy"
                                   :in-ch first->second
                                   :out-ch second->first}
                         :inject datepicker/inject}]
           :langselector {:id "lang"
                          :initial {:current "en-US"
                                    :available ["ru-RU"
                                                "th-TH"
                                                "en-US"]}
                          :inject langselector/inject}})))

(defn prepare-component-root [what where]
  (let [what-node (node [:div {:id what}])
        where-node (sel1 where)]
    (dommy/append! where-node what-node)))

(defn prepare-component [component-params]
  (if (= (type component-params) (type []))
    (doall (map prepare-component component-params))
    (let [id (:id component-params)
          initial (:initial component-params)
          injector (:inject component-params)]
      (prepare-component-root id "body")
      (injector id initial))))

(defn prepare-application []
  (doseq [[_ value] @application]
    (prepare-component value)))

(prepare-application)

