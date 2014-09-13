(ns cljs.utils.date)

(defn parse [string]
  (.parse js/Date string))

(defn format [date fmt]
  (if (= (type date) js/Date)
    (if-let [date-str (.toString date fmt)]
      date-str
      "")
    ""))

(defn formatter [fmt]
  #(format % fmt))
