(ns meth-search-reloaded.config-spec
  (:require [meth-search-reloaded.config :as c]
            [speclj.core :refer :all]))

(describe "Testing configuration module"
          (before-all (println "A config specs is about to be evaluated"))
          (after-all (println "\nA config specs has just been evaluated"))
          
          (it "check app-config status"
              (should= (:profile @c/app-configs) :spec))

          (it "check cfg status"
              (let [app-configs (stub :c/app-configs {:return {:profile :spec :port 8888 :thread 1 :queue 1024}})]
                (should= (c/cfg :profile) :spec)))

          (it "fail with unknown key and raise RuntimeException with correct message"
              (should= "caught exception: unknown config key failed-key"
                       (try
                         (c/cfg :failed-key)
                         (catch RuntimeException e (str "caught exception: " (.getMessage e)))))))
