(ns meth-search-reloaded.components.datepicker.core
  (:require-macros [schema.macros :refer [defschema]])
  (:require [om.core :as om :include-macros true]
            [om-tools.core :refer-macros [defcomponent defcomponentk]]
            [om-tools.dom :as dom :include-macros true]
            [schema.core :as s]))

;(enable-console-print!)

;; (defn format-fn [format-string date]
;;   (.format date format-string))

;; (defschema DatePicker
;;   {(s/optional-key :date) js/Date
;;    :date-string js/String
;;    :format-string js/String})

;; (defcomponent widget [data :- DatePicker owner opts]
;;   (display-name [_]
;;                 (or (:react-name opts) "DatePickerWidget"))
;;   (will-mount [_]
;;               (om/set-state! owner :date-string (:date-string data)))
;;   (render-state [_ {:keys [date-string]}]
;;                 (dom/div
;;                  (dom/input date-string))))

(defn handle-change [e owner {:keys [text]}]
  (let [current-text (.. e -target -value)
        current-date (.parse js/Date current-text)]
    (om/set-state! owner :text current-text)
    (om/set-state! owner :date current-date)))

(defschema DatePickerInput
  {(s/optional-key :date) js/Date
   :format-string js/String
   :init-date js/Date})

(defcomponent input [data :- DatePickerInput owner opts]
  (display-name [_]
                (or (:react-name opts) "DatepickerInput"))
  (will-mount [_]
              (om/set-state! owner :text (.toString
                                          (:init-date data)
                                          (:format-string data)))
              (om/set-state! owner :date (:init-date data)))
  (render-state [_ {:keys [text date]}]
                (dom/div
                 (dom/input {:type "text"
                             :value text
                             :on-change #(handle-change % owner state)})
                 (dom/span (str "Date: " (if date (.toString date "F") ""))))))

(defn inject [root]
  (let [initial-params {:init-date (js/Date)
                        :format-string "d"}]
    (om/root input
             initial-params
             {:target (.getElementById js/document root)})))
