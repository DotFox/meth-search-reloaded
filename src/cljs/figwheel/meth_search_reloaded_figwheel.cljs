(ns meth-search-reloaded-figwheel
  (:require [figwheel.client :as fw :include-macros true]
            [om.core :as om :include-macros true]))

(.log js/console "!!!!!")

(fw/watch-and-reload)
