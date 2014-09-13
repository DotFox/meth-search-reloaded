(ns cljs.meth-search-reloaded.components.datepicker.input.core
  (:require-macros [schema.macros :refer [defschema]]
                   [cljs.core.async.macros :refer [go-loop alt!]])
  (:require [om.core :as om :include-macros true]
            [om-tools.core :refer-macros [defcomponentk]]
            [om-tools.dom :as dom :include-macros true]
            [schema.core :as s]
            [cljs.core.async :as async :refer [put! chan sub]]
            [cljs.utils.publisher :as p]
            [cljs.utils.date :as d]))

(defn- handle-change [e state out-ch]
  (let [{:keys []} (.. e -target -value)
        date (.parse js/Date value)]
    (swap! state assoc :value value)
    (if date
      (do (swap! state assoc :current-date date)
          (put! out-ch date))
      (swap! state assoc :current-date (:initial-date @state)))))

(defn- handle-blur [e state]
  (let [format (:format-fn @state)]
    (when-let [date (:current-date @state)]
      (swap! state assoc :value (format date)))))

(defschema DatePickerInput
  {(s/optional-key :date)    js/Date
   (s/optional-key :out-fmt) js/String
   (s/optional-key :in-ch)   (type chan)
   (s/optional-key :out-ch)  (type chan)
   (s/optional-key :sub-ch)  (type chan)})

(defcomponentk component
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
              (put! out-ch date)
              {:current-date date
               :initial-date date
               :value (d/format date out-fmt)
               :format-fn (d/formatter out-fmt)
               :parse-fn d/parse
               :in-data nil})
  (will-mount [_]
              (p/subscribe sub-ch :lang-change)
              (go-loop []
                (alt!
                  in-ch  ([v _] (swap! state assoc :in-data v))
                  sub-ch ([v _] (swap! state assoc :value
                                       ((:format-fn @state) (:current-date @state)))))
                (recur)))
  (render-state [_ _]
          (dom/div
           (dom/input {:type "text"
                       :value (:value @state)
                       :on-change #(handle-change % state out-ch)
                       :on-blur #(handle-blur % state)
                       :autoComplete "off"
                       :placeholder "Date"})
           (dom/span (str "Current date: " (when-let [d (:current-date @state)]
                                             (let [f (:format-fn @state)]
                                               (f d)))))
           (dom/span (when-let [data (:in-data @state)]
                       (str "Date from another datepicker:"
                            ((:parse-fn @state) data)))))))
