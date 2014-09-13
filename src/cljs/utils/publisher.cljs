(ns cljs.utils.publisher
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs.core.async :as async :refer [chan pub sub]]))

(def publisher
  (chan))

(def publication
  (pub publisher #(:topic %)))

(defn subscribe [channel topic]
  (sub publication topic channel))
