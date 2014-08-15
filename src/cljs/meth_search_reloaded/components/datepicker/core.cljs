(ns meth-search-reloaded.components.datepicker.core
  (:require-macros [schema.macros :refer [defschema]]
                   [cljs.core.async.macros :refer [go]])
  (:require [om.core :as om :include-macros true]
            [om-tools.core :refer-macros [defcomponentk]]
            [om-tools.dom :as dom :include-macros true]
            [schema.core :as s]
            [cljs.core.async :as async :refer [<! put! chan]]))

(def in-channel
  (chan))

(def out-channel
  (chan))

(defn insert-into-channel [text]
  (put! in-channel text))

(defn handle-input [e state]
  (let [value (.. e -target -value)
        date (.parse js/Date value)]
    (swap! state assoc :value value)
    (if date
      (do (swap! state assoc :current-date date)
          (put! (:out-channel @state) date))
      (swap! state assoc :current-date (:initial-date @state)))))

(defn handle-events [e state]
  (let [format (:format-fn @state)]
    (if-let [date (:current-date @state)]
      (swap! state assoc :value (format date)))))

(defschema DatePickerInput
  {:value js/String
   (s/optional-key :date) js/Date
   :output-format js/String
   (s/optional-key :in-channel) (type chan)
   (s/optional-key :out-channel) (type chan)
   (s/optional-key :lang-channel) (type chan)})

(defcomponentk datepicker-input
  "Complex datepicker input"
  [[:data value output-format date in-channel out-channel lang-channel] :- DatePickerInput state opts owner]
  (display-name [_]
                (or (:react-name opts) "DatePickerInput"))
  (init-state [_]
              {:current-date date
               :initial-date date
               :value value
               :format-fn #(. % (toString output-format))
               :parse-fn #(.parse js/Date %)
               :now (js/Date. (.now js/Date))
               :in-data ""
               :out-channel out-channel
               :lang ""})
  (will-mount [_]
              (go (loop []
                    (let [data (<! in-channel)]
                      (om/set-state! owner :in-data data)
                      (recur))))
              (go (loop []
                    (let [lang (<! lang-channel)]
                      (when (and lang (not= lang (:lang @state)))
                        (om/refresh! owner))
                      (recur)))))
  (render [_]
          (dom/div
           (dom/input {:type "text"
                       :value (:value @state)
                       :on-change #(handle-input % state)
                       :on-blur #(handle-events % state)
                       :autoComplete "off"
                       :placeholder "Date"})
           (dom/span (str "Current date: " (when (:current-date @state)
                                             ((:format-fn @state) (:current-date @state)))))
           (dom/span (when-let [data (:in-data @state)]
                       (str "Date from another datepicker:"
                            ((:parse-fn @state) data)))))))

(defn inject [root initial]
  (om/root datepicker-input
           (or initial {:value ""
                        :output-format "d MMMM yyyy"
                        :date nil
                        :in-channel in-channel
                        :out-channel out-channel
                        :lang-channel (chan)})
           {:target (.getElementById js/document root)}))
