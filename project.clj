(defproject meth-search-reloaded "0.1.0-SNAPSHOT"
  :description "Reloaded workflow for clojure and clojurescript."
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  ;; Highly recomended option, for cljsbuild
  ;; :jvm-opts ["-Xmx0.5G"]

  :dependencies [[figwheel "0.1.4-SNAPSHOT"]
                 [om "0.7.1"]
                 [org.clojure/clojure "1.7.0-alpha2"]
                 [org.clojure/clojurescript "0.0-2322"]
                 [org.clojure/core.async "0.1.338.0-5c5012-alpha"]
                 [prismatic/dommy "0.1.3"]
                 [prismatic/om-tools "0.3.3" :exclusions [[org.clojure/clojure]]]
                 [prismatic/schema "0.2.6"]
                 [secretary "1.2.2-SNAPSHOT"]
                 [speclj "3.1.0"]]

  :plugins [[lein-cljsbuild "1.0.4-SNAPSHOT"]
            [lein-figwheel "0.1.4-SNAPSHOT"]
            [lein-pdo "0.1.1"]
            [com.cemerick/piggieback "0.1.4-SNAPSHOT"]
            [lein-marginalia "0.8.0"]
            [lein-npm "0.4.0"]
            [lein-bower "0.5.1"]
            [lein-shell "0.4.0"]
            [speclj "3.1.0"]]

  :figwheel {:http-server-root "public"
             :port 3449
             :css-dirs ["resources/public/css"
                        "dev-resources/public/css"]}

  ;; Sad but true - in clojure world nothing can compile stylus :(
  :node-dependencies [[stylus "0.47.3"]
                      [jeet "5.3.0"]
                      [boy "0.0.1"]
                      [phantomjs "1.9.7-15"]
                      [bower "latest"]]

  :bower-dependencies [[datejs "git@github.com:abritinthebay/datejs.git#master"
                        react "0.11.1"]]
  :bower {:directory "dev-resources/public/vendor"}

  :source-paths ["src/clj" "spec/clj"]

  :cljsbuild {:builds [{;; Task for dev, add to compiled JS static webserver with reloader and browser repl
                        :id "dev"
                        :source-paths ["src/cljs/meth_search_reloaded" "src/cljs/utils" "src/cljs/figwheel" "src/cljs/brepl"]
                        :compiler {:output-to "dev-resources/public/js/meth_search_reloaded.js"
                                   :output-dir "dev-resources/public/js/out"
                                   :optimizations :none
                                   :source-map true}}

                       {;; Release task, min output. Show all warnings from closure compiler.
                        :id "release"
                        :source-paths ["src/cljs/meth_search_reloaded" "src/cljs/utils"]
                        :compiler {:output-to "resources/public/js/meth_search_reloaded.min.js"
                                   :output-dir "resources/public/js/out"
                                   :optimizations :advanced
                                   :pretty-print false
                                   ;;
                                   :warnings false
                                   :source-map "resources/public/js/meth_search_reloaded.min.js.map"
                                   :preamble ["react/react.min.js"
                                              "public/vendor/datejs/build/production/date.min.js"
                                              "public/vendor/datejs/build/production/i18n/ru-RU.js"
                                              "public/vendor/datejs/build/production/i18n/th-TH.js"]
                                   :externs ["react/externs/react.js"
                                             "externs/date.js"]}}

                       {;; Fun specs task :) Build JS with specs and send it to PhantomJS.
                        :id "spec"
                        :notify-command ["./node_modules/.bin/phantomjs"
                                         "spec-resources/public/unit-tests.js"
                                         "spec-resources/public/index.html"]
                        :source-paths ["src/cljs/meth_search_reloaded" "src/cljs/utils" "spec/cljs"]
                        :compiler {:output-to "spec-resources/public/js/meth_search_reloaded.specs.js"
                                   :output-dir "spec-resources/public/js/out"
                                   :optimizations :whitespace
                                   :pretty-print true
                                   ;;
                                   :warnings false
                                   :preamble ["react/react.js"
                                              "public/vendor/datejs/build/date.js"
                                              "public/vendor/datejs/build/i18n/ru-RU.js"
                                              "public/vendor/datejs/build/i18n/th-TH.js"]
                                   :externs ["react/externs/react.js"
                                             "externs/date.js"]}}]}

  :aliases {;; Run it after cloning repository
            "conf" ["do"
                    ["pdo"
                     ["npm" "install"]
                     ["bower" "install"]
                     ["marg"]]]

            ;; First start will be sloooow, but after that all your sources will be loaded
            ;; Just keep this task running
            "dev" ["do"
                   ["cljsbuild" "clean"]
                   ["pdo"
                    ["figwheel" "dev"]
                    ["cljsbuild" "auto" "dev"]
                    ;; ["cljsbuild" "auto" "spec"]
                    ["cljsbuild" "auto" "release"]
                    ["shell" "./node_modules/.bin/stylus" "-c" "-u" "jeet" "-w" "src/stylus/style.styl" "-o" "resources/public/css/"]]]

            "prod" ["do"
                    ["cljsbuild" "clean"]
                    ["pdo"
                     ["shell" "./node_modules/.bin/stylus" "-u" "jeet" "src/stylus/style.styl" "-o" "resources/public/css/"]
                     ["cljsbuild" "once" "release"]]
                    ["shell" "tar" "czf" "public.tar.gz" "resources/public/index_prod.html" "resources/public/js/meth_search_reloaded.min.js" "resources/public/js/meth_search_reloaded.min.js.map" "resources/public/css/style.css"]]

            ;; Use only with lein-ancient plugin, for dev only !!!
            "test-ancient" ["do"
                            ["cljsbuild" "clean"]
                            ["cljsbuild" "once" "spec"]]})
