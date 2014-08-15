(ns meth-search-reloaded.utils.date)

(defn set-language [ieft]
  (. js/Date (setLanguage ieft)))

(defn new []
  (js/Date.))

