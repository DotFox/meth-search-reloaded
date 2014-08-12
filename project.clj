(defproject meth-search-reloaded "0.1.0-SNAPSHOT"
  :description "Reloaded workflow for clojure and clojurescript."
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  ;; Highly recomended option, for cljsbuild
  :jvm-opts ["-Xmx1G"]

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2311"]
                 [figwheel "0.1.3-SNAPSHOT"]
                 [om "0.7.1"]
                 [secretary "1.2.1-SNAPSHOT"]
                 [speclj "3.0.2"]
                 [prismatic/dommy "0.1.3"]
                 [prismatic/om-tools "0.3.2" :exclusions [[org.clojure/clojure]]]]

  :plugins [[lein-cljsbuild "1.0.4-SNAPSHOT"]
            [lein-figwheel "0.1.3-SNAPSHOT"]
            [lein-pdo "0.1.1"]
            [com.cemerick/piggieback "0.1.4-SNAPSHOT"]
            [lein-marginalia "0.8.0-SNAPSHOT"]
            [lein-npm "0.4.0"]
            [lein-shell "0.4.0"]
            [cider/cider-nrepl "0.8.0-SNAPSHOT"]
            [speclj "3.0.2"]]

  :figwheel {:http-server-root "public"
             :port 3449
             :css-dirs ["resources/public/css"]}

  ;; Sad but true - in clojure world nothing can compile stylus :(
  :node-dependencies [[stylus "0.47.3"
                       jeet "5.3.0"
                       boy "0.0.1"
                       phantomjs "1.9.7-15"]]

  :source-paths ["src/clj" "spec/clj"]

  :cljsbuild {:builds [{;; Task for dev, add to compiled JS static webserver with reloader and browser repl
                        :id "dev"
                        :source-paths ["src/cljs/meth_search_reloaded" "src/cljs/figwheel" "src/cljs/brepl"]
                        :compiler {:output-to "resources/public/js/meth_search_reloaded.js"
                                   :output-dir "resources/public/js/out"
                                   :optimizations :none
                                   :source-map true}}

                       {;; Release task, min output. Show all warnings from closure compiler.
                        :id "release"
                        :source-paths ["src/cljs/meth_search_reloaded"]
                        :compiler {:output-to "resources/public/js/meth_search_reloaded.min.js"
                                   :output-dir "resources/public/js/prod-out"
                                   :optimizations :advanced
                                   :pretty-print false
                                   :source-map "resources/public/js/meth_search_reloaded.min.js.map"
                                   :preamble ["react/react.min.js"]
                                   :externs ["react/externs/react.js"]}}

                       {;; Fun specs task :) Build JS with specs and send it to PhantomJS.
                        :id "spec"
                        :notify-command ["./node_modules/.bin/phantomjs"
                                         "resources/private/unit-tests.js"
                                         "resources/private/specs.html"]
                        :source-paths ["src/cljs/meth_search_reloaded" "spec/cljs"]
                        :compiler {:output-to "resources/private/js/specs.js"
                                   :output-dir "resources/private/js/specs-out"
                                   :pretty-print true
                                   :optimizations :whitespace
                                   :preamble ["react/react.js"]
                                   :externs ["react/externs/react.js"]}}]}

  :aliases {;; Run it after cloning repository
            "conf" ["do"
                    ["pdo"
                     ["npm" "install"]
                     ["marg"]]]

            ;; First start will be sloooow, but after that all your sources will be loaded
            ;; Just keep this task running
            "dev" ["do"
                   ["cljsbuild" "clean"]
                   ["pdo"
                    ["figwheel" "dev"]
                    ["cljsbuild" "auto" "dev"]
                    ["cljsbuild" "auto" "spec"]
                    ["cljsbuild" "auto" "release"]
                    ["shell" "./node_modules/.bin/stylus" "-u" "jeet" "-w" "src/stylus/style.styl" "-o" "resources/public/css/"]]]

            "prod" ["do"
                    ["cljsbuild" "clean"]
                    ["pdo"
                     ["shell" "./node_modules/.bin/stylus" "-u" "jeet" "src/stylus/style.styl" "-o" "resources/public/css/"]
                     ["cljsbuild" "once" "release"]]]

            ;; Use only with lein-ancient plugin, for dev only !!!
            "test-ancient" ["do"
                            ["cljsbuild" "clean"]
                            ["cljsbuild" "once" "spec"]]})

