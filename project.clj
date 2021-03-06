(defproject meth-search-reloaded "0.1.0-SNAPSHOT"
  :description "Reloaded workflow for clojure and clojurescript."
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :jvm-opts ["-server"
             "-Xmx3G"
             "-Xms3G"
             "-Dfile.encoding=UTF-8"
             "-Dsun.jnu.encoding=UTF-8"]

  :main meth-search-reloaded.main

  :dependencies [;; Server
                 [http-kit "2.2.0-SNAPSHOT"]
                 [compojure "1.2.0-SNAPSHOT"]
                 [ring/ring-core "1.3.1"]
                 [com.taoensso/timbre "3.3.1" :exclusions [org.clojure/tools.reader]]
                 [org.clojure/tools.cli "0.3.1"]
                 [figwheel "0.1.4-SNAPSHOT" :exclusions [org.clojure/tools.reader]]
                 [enlive "1.1.5"]
                 [com.datomic/datomic-free "0.9.4899"]
                 ;; Client
                 [om "0.7.3"]
                 [org.clojure/clojure "1.7.0-alpha2"]
                 [org.clojure/clojurescript "0.0-2342" :exclusions [org.clojure/tools.reader]]
                 [org.clojure/core.async "0.1.338.0-5c5012-alpha"]
                 [prismatic/dommy "0.1.3"]
                 [prismatic/om-tools "0.3.3" :exclusions [org.clojure/clojure]]
                 [prismatic/schema "0.2.7-SNAPSHOT"]
                 [secretary "1.2.2-SNAPSHOT"]
                 ;; Specs
                 [speclj "3.1.0"]
                 ;; Tools
                 [com.cemerick/piggieback "0.1.4-SNAPSHOT"]]

  :plugins [[lein-cljsbuild "1.0.4-SNAPSHOT"]
            [lein-figwheel "0.1.4-SNAPSHOT" :exclusions [org.clojure/clojurescript]]
            [lein-pdo "0.1.1"]
            [lein-marginalia "0.8.0"]
            [lein-npm "0.4.0"]
            [lein-bower "0.5.1"]
            [lein-shell "0.4.0"]
            [speclj "3.1.0"]
            [lein-npm "0.4.0"]
            [lein-bower "0.5.1"]]

  :source-paths ["src/clj"]

  :profiles {:conf {:dependencies [[org.clojure/clojure "1.6.0"]]
                    :bower {:directory "foreign-libs/public/vendor"
                            :scripts {:postinstall "./node_modules/.bin/wiredep -s resources-dev/templates/index.html && ./node_modules/.bin/wiredep -s resources/templates/index.html"}}
                    :bower-dependencies [[datejs "git@github.com:abritinthebay/datejs.git#master"]
                                         [react "0.11.1"]
                                         [maxmertkit "1.0.5"]]
                    :node-dependencies [[stylus "0.47.3"]
                                        [kouto-swiss "0.10.2"]
                                        [phantomjs "1.9.7-15"]
                                        [bower "latest"]
                                        [wiredep "1.8.5"]]}
             :dev {:resource-paths ^:replace ["resources-dev" "foreign-libs"]
                   :cljsbuild ^:replace {:builds [{:source-paths ["src/cljs/meth_search_reloaded" "src/cljs/utils" "src/cljs/figwheel" "src/cljs/brepl"]
                                                   :compiler {:output-to "resources-dev/public/js/meth_search_reloaded.js"
                                                              :output-dir "resources-dev/public/js/out"
                                                              :optimizations :none
                                                              :source-map true}}]}
                   :figwheel {:http-server-root "public"
                              :port 3449
                              :css-dirs ["resources-dev/public/css"]}}
             :prod {:aot :all
                    :source-paths ^:replace ["src/clj"]
                    :resource-paths ^:replace ["resources" "foreign-libs"]
                    :cljsbuild ^:replace {:builds [{:source-paths ["src/cljs/meth_search_reloaded" "src/cljs/utils"]
                                                    :compiler {:output-to "resources/public/js/meth_search_reloaded.min.js"
                                                               :optimizations :advanced
                                                               :pretty-print false
                                                               :preamble ["react/react.min.js"
                                                                          "public/vendor/datejs/build/production/date.min.js"
                                                                          "public/vendor/datejs/build/production/i18n/ru-RU.js"
                                                                          "public/vendor/datejs/build/production/i18n/th-TH.js"]
                                                               :externs ["react/externs/react.js"
                                                                         "externs/date.js"]}}]}}
             :build {:resource-paths ^:replace ["resources"]
                     :jar-name "meth-search-reloaded.jar"
                     :uberjar-name "meth-search-reloaded-standalone.jar"
                     :uberjar-exclusions [#"META-INF/DUMMY.SF"
                                          #"(?:^|/)out/"]}
             :spec {:source-paths ^:replace ["src/clj" "spec/clj"]
                    :test-paths ["spec"]
                    :resource-paths ^:replace ["resources-spec" "foreign-libs"]
                    :cljsbuild ^:replace {:builds [{:notify-command ["./node_modules/.bin/phantomjs"
                                                                     "resources-spec/public/unit-tests.js"
                                                                     "resources-spec/public/index.html"]
                                                    :source-paths ["src/cljs/meth_search_reloaded" "src/cljs/utils" "spec/cljs"]
                                                    :compiler {:output-to "resources-spec/public/js/meth_search_reloaded.specs.js"
                                                               :output-dir "resources-spec/public/js/out"
                                                               :optimizations :whitespace
                                                               :pretty-print true
                                                               :warnings false
                                                               :preamble ["react/react.js"
                                                                          "public/vendor/datejs/build/date.js"
                                                                          "public/vendor/datejs/build/i18n/ru-RU.js"
                                                                          "public/vendor/datejs/build/i18n/th-TH.js"]
                                                               :externs ["react/externs/react.js"
                                                                         "externs/date.js"]}}]}}}
  
  :aliases {"configure" ["with-profile" "conf" ["do"
                                                ["npm" "install"]
                                                ["bower" "-f" "install"]
                                                ["marg"]
                                                ["shell" "ln -sF $PWD/resources-dev/templates/index.html $PWD/resources-dev/public/index_test.html"]
                                                ["shell" "ln -sF $PWD/resources/templates/index.html $PWD/resources/public/index_test.html"]]]

            "dev-start" ["with-profile" "dev" ["do"
                                               ["pdo"
                                                ["clean"]
                                                ["cljsbuild" "clean"]]
                                               ["pdo"
                                                ["figwheel"]
                                                ["shell" "./node_modules/.bin/stylus" "-c"
                                                 "-u" "kouto-swiss"
                                                 "-w"
                                                 "src/stylus/style.styl" "-o" "resources-dev/public/css/"]
                                                ["run"]]]]

            "prod-start" ["with-profile" "prod" ["do"
                                                 ["pdo"
                                                  ["clean"]
                                                  ["cljsbuild" "clean"]]
                                                 ["pdo"
                                                  ["cljsbuild" "auto"]
                                                  ["shell" "./node_modules/.bin/stylus"
                                                   "-u" "kouto-swiss"
                                                   "-w"
                                                   "src/stylus/style.styl" "-o" "resources/public/css/"]
                                                  ["run" "--profile" "prod" "--port" "3001"]]]]

            "prod-build" ["do"
                          ["with-profile" "prod" ["do"
                                                  ["pdo"
                                                   ["cljsbuild" "clean"]
                                                   ["cljsbuild" "once"]
                                                   ["shell" "./node_modules/.bin/stylus" "-c"
                                                    "-u" "kouto-swiss"
                                                    "src/stylus/style.styl" "-o" "resources/public/css/"]]]]
                          ["with-profiles" "prod,build" ["do"
                                                         ["clean"]
                                                         ["compile"]
                                                         ["uberjar"]]]]

            "tests-auto" ["with-profile" "spec" ["pdo"
                                                 ["spec" "-a"]
                                                 ["cljsbuild" "auto"]]]

            "tests-once" ["with-profile" "spec" ["do"
                                                 ["spec"]
                                                 ["cljsbuild" "once"]]]

            "clean-all" ["do"
                         ["with-profile" "dev" ["do"
                                                ["cljsbuild" "clean"]
                                                ["clean"]]]
                         ["with-profile" "prod" ["do"
                                                 ["cljsbuild" "clean"]
                                                 ["clean"]]]
                         ["with-profile" "spec" ["do"
                                                 ["cljsbuild" "clean"]
                                                 ["clean"]]]]})
