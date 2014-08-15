(ns meth-search-reloaded.core
  (:use-macros [dommy.macros :only [node sel sel1]])
  (:require [om.core :as om :include-macros true]
            [meth-search-reloaded.components.hello.core :as hello]
            [meth-search-reloaded.components.datepicker.core :as datepicker]
            [meth-search-reloaded.components.langselector.core :as langselector]
            [dommy.utils :as utils]
            [dommy.core :as dommy]
            [cljs.core.async :as async :refer [chan]]))

(def left-to-right
  (chan))

(def right-to-left
  (chan))

(def lang-channel
  (chan))

(def application
  (atom {:datepicker [{:id "date1"
                       :initial {:value ""
                                 :output-format "d MMMM yyyy"
                                 :date nil
                                 :in-channel right-to-left
                                 :out-channel left-to-right
                                 :lang-channel lang-channel}}
                      {:id "date2"
                       :initial {:value ""
                                 :output-format "d MMMM yyyy"
                                 :date nil
                                 :in-channel left-to-right
                                 :out-channel right-to-left
                                 :lang-channel lang-channel}}]
         :langselector {:id "lang"
                        :initial {:current "en-US"
                                  :available ["ru-RU"
                                              "th-TH"
                                              "en-US"]
                                  :lang-channel lang-channel}}}))

(defn prepare-component-root [what where]
  (let [what-node (node [:div {:id what}])
        where-node (sel1 where)]
    (dommy/append! where-node what-node)))

(prepare-component-root "hello" "body")

(doall (map (fn [datepicker-params]
              (prepare-component-root (:id datepicker-params) "body")
              (datepicker/inject (:id datepicker-params)
                                 (:initial datepicker-params)))
            (:datepicker @application)))

((fn [langselector-params]
      (prepare-component-root (:id langselector-params) "body")
      (langselector/inject (:id langselector-params)
                           (:initial langselector-params)))
    (:langselector @application))

(defn add [a b]
  (+ a b))

