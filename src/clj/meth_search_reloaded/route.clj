(ns meth-search-reloaded.route
  (:use [compojure.core :only [defroutes GET POST DELETE ANY context]]
        (ring.middleware [keyword-params :only [wrap-keyword-params]]
                         [params :only [wrap-params]]
                         [session :only [wrap-session]])
        [meth-search-reloaded.config :only [cfg]]
        [meth-search-reloaded.middleware :only [wrap-request-logging]]
        [meth-search-reloaded.templates :only [index]])
  (:require [compojure.route :as route]))

(defroutes server-routes*
  (GET "/" [] (clojure.string/join (index)))
  (route/resources "/")
  (route/not-found "Page not found"))

(defn application [] (-> #'server-routes*
                         wrap-session
                         wrap-keyword-params
                         wrap-params
                         wrap-request-logging))
