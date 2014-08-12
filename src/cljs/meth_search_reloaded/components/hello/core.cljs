(ns meth-search-reloaded.components.hello.core
  (:require [om.core :as om :include-macros true]
            [om-tools.core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]))

(defcomponent widget [data :- {:init js/Number} owner opts]
  (display-name [_]
                (or (:react-name opts) "HelloWidget"))
  (will-mount [_]
              (om/set-state! owner :n (:init data)))
  (render-state [_ {:keys [n]}]
                (dom/div
                 (dom/blockquote {:class "_left_"}
                                 (dom/p "Hello World!"
                                        (dom/small "Someone famouse in "
                                                   (dom/cite {:title "Source Title"}
                                                             "Source Title"))))
                 (dom/div
                  (dom/span (str "Count: " n))
                  (dom/button
                   {:on-click #(om/set-state! owner :n (inc n))}
                   "+")
                  (dom/button
                   {:on-click #(om/set-state! owner :n (dec n))}
                   "-")))))

(defn inject [root]
  (om/root widget {:init 5} {:target (.getElementById js/document root)}))

