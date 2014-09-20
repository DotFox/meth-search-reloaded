(ns meth-search-reloaded.main
  (:gen-class)
  (:use [org.httpkit.server :only [run-server]]
        [clojure.tools.cli :only [cli]]
        [meth-search-reloaded.config :only [app-configs cfg]]
        [taoensso.timbre :as timbre]
        [meth-search-reloaded.route :only [application]]))

(timbre/refer-timbre)

(defn- to-int [s]
  (Integer/parseInt s))

(defonce server (atom nil))

(defn start-server []
  ;; stop it if started
  (when-not (nil? @server) (@server))
  (reset! server (run-server (application) {:port (cfg :port)
                                            :thread (cfg :thread)
                                            :queue-size (cfg :queue)})))

(defn stop-server []
  (when-not (nil? @server)
    (@server :timeout 100)
    (reset! server nil)))

(defn -main [& args]
  (let [[options _ banner]
        (cli args
             ["-p" "--port" "Port to listen" :default 3000 :parse-fn to-int]
             ["--thread" "Http worker thread count" :default 4 :parse-fn to-int]
             ["--profile" "dev or prod" :default :dev :parse-fn keyword]
             ["-q" "--queue" "Max queue size" :default 204800 :parse-fn to-int]
             ["--[no-]help" "Print this help"])]
    (when (:help options) (println banner) (System/exit 0))
    ;; Configure application
    ;; Config can be accessed by (cfg :key)
    (swap! app-configs merge options)
    (spy :info (start-server))
    (info (color-str :yellow (str "Server started. Listen on 0.0.0.0:" (cfg :port)) " with profile " (cfg :profile) " and queue size " (cfg :queue)))))
