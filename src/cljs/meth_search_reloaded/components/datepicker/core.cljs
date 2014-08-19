(ns cljs.meth-search-reloaded.components.datepicker.core
  (:require-macros [schema.macros :refer [defschema]]
                   [cljs.core.async.macros :refer [go-loop alt!]])
  (:require [om.core :as om :include-macros true]
            [om-tools.core :refer-macros [defcomponentk]]
            [om-tools.dom :as dom :include-macros true]
            [schema.core :as s]
            [cljs.core.async :as async :refer [put! chan sub]]
            [cljs.utils.publisher :as p]))

(enable-console-print!)

(defn- handle-input [e state out-ch]
  (let [value (.. e -target -value)
        date (.parse js/Date value)]
    (swap! state assoc :value value)
    (if date
      (do (swap! state assoc :current-date date)
          (put! out-ch date))
      (swap! state assoc :current-date (:initial-date @state)))))

(defn- handle-events [e state]
  (let [format (:format-fn @state)]
    (when-let [date (:current-date @state)]
      (swap! state assoc :value (format date)))))

(defschema DatePickerInput
  {(s/optional-key :date) js/Date
   (s/optional-key :out-fmt) js/String
   (s/optional-key :in-ch) (type chan)
   (s/optional-key :out-ch) (type chan)
   (s/optional-key :sub-ch) (type chan)})

(defcomponentk date-picker-input
  "Complex datepicker input"
  [[:data
    {date (js/Date.)}
    {out-fmt "d MMMM yyyy"}
    {in-ch (chan)}
    {out-ch (chan)}
    {sub-ch (chan)}] :- DatePickerInput state opts owner]
  (display-name [_]
                (or (:react-name opts) "DatePickerInput"))
  (init-state [_]
              (let [parse-fn (fn [date-str]
                               (.parse js/Date date-str))
                    format-fn (fn [date-obj]
                                (.toString date-obj out-fmt))]
                (put! out-ch date)
                {:current-date date
                 :initial-date date
                 :value (format-fn date)
                 :format-fn format-fn
                 :parse-fn parse-fn
                 :in-data nil}))
  (will-mount [this]
              (p/subscribe sub-ch :lang-change)
              (go-loop []
                (alt!
                  in-ch  ([v _] (swap! state assoc :in-data v))
                  sub-ch ([v _] (swap! state assoc :value ((:format-fn @state) (:current-date @state)))))
                (recur)))
  (render [_]
          (dom/div
           (dom/input {:type "text"
                       :value (:value @state)
                       :on-change #(handle-input % state out-ch)
                       :on-blur #(handle-events % state)
                       :autoComplete "off"
                       :placeholder "Date"})
           (dom/span (str "Current date: " (when-let [d (:current-date @state)]
                                             (let [f (:format-fn @state)]
                                               (f d)))))
           (dom/span (when-let [data (:in-data @state)]
                       (str "Date from another datepicker:"
                            ((:parse-fn @state) data)))))))

(defn inject [root initial]
  (om/root date-picker-input
           initial
           {:target (.getElementById js/document root)}))
