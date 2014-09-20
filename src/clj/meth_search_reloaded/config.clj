(ns meth-search-reloaded.config)

(defonce app-configs (atom {:profile :dev}))

(defn cfg [key & [default]]
  (if-let [v (or (key @app-configs) default)]
    v
    (when-not (contains? @app-configs key)
      (throw (RuntimeException. (str "unknown config key " (name key)))))))
