(defproject meth-search-reloaded "0.1.0-SNAPSHOT"
  :description "Reloaded workflow for clojure and clojurescript."
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :jvm-opts ["-Xmx1G"]

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2280"]
                 [figwheel "0.1.3-SNAPSHOT"]
                 [garden "1.2.1"]
                 [om "0.7.1"]
                 [secretary "1.2.0"]
                 [speclj "3.0.2"]
                 [prismatic/dommy "0.1.3"]
                 [prismatic/om-tools "0.3.0" :exclusions [[org.clojure/clojure]]]]

  :plugins [[lein-cljsbuild "1.0.3"]
            [lein-figwheel "0.1.3-SNAPSHOT"]
            [lein-pdo "0.1.1"]
            [com.cemerick/austin "0.1.4"]
            [lein-marginalia "0.8.0-SNAPSHOT"]
            [speclj "3.0.2"]]

  :figwheel {:http-server-root "public"
             :port 3449
             :css-dirs ["resources/public/css"]}

  :node-dependencies [[stylus "0.47.3"
                       jeet "5.3.0"
                       boy "0.0.1"
                       phantomjs "1.9.7-15"]]

  :source-paths ["src/clj" "spec/clj"]

  :cljsbuild {:builds [{:id "dev"
                        :source-paths ["src/cljs/meth_search_reloaded" "src/cljs/figwheel" "src/cljs/brepl"]
                        :compiler {:output-to "resources/public/js/meth_search_reloaded.js"
                                   :output-dir "resources/public/js/out"
                                   :optimizations :none
                                   :source-map true}}

                       {:id "spec"
                        :notify-command ["./node_modules/.bin/phantomjs"
                                         "resources/private/unit-tests.js"
                                         "resources/private/specs.html"]
                        :source-paths ["src/cljs/meth_search_reloaded" "spec/cljs"]
                        :compiler {:output-to "resources/private/js/specs.js"
                                   :output-dir "resources/private/js/specs-out"
                                   :pretty-print true
                                   :optimizations :whitespace
                                   :preamble ["react/react.js"]
                                   :externs ["react/externs/react.js"]}}

                       {:id "release"
                        :source-paths ["src/cljs/meth_search_reloaded"]
                        :compiler {:output-to "resources/public/js/meth_search_reloaded.min.js"
                                   :output-dir "resources/public/js/prod-out"
                                   :optimizations :advanced
                                   :pretty-print false
                                   :source-map "resources/public/js/meth_search_reloaded.min.js.map"
                                   :preamble ["react/react.min.js"]
                                   :externs ["react/externs/react.js"]}}]}

  :aliases {"conf" ["do"
                    ["npm" "install"]]

            "dev" ["do"
                   ["pdo"
                    ["figwheel" "dev"]
                    ["cljsbuild" "auto" "dev"]
                    ["cljsbuild" "auto" "spec"]
                    ["cljsbuild" "auto" "release"]
                    ["shell" "./node_modules/.bin/stylus" "-u" "jeet" "-w" "src/stylus/style.styl" "-o" "resources/public/css/"]]]

            "prod" ["do"
                    ["pdo"
                     ["shell" "./node_modules/.bin/stylus" "-u" "jeet" "src/stylus/style.styl" "-o" "resources/public/css/"]
                     ["cljsbuild" "once" "release"]]]})

