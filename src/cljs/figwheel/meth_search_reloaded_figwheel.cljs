(ns cljs.meth-search-reloaded-figwheel
  (:require [figwheel.client :as fw :include-macros true]))

(fw/watch-and-reload
 :websocket-url "ws://localhost:3449/figwheel-ws")
