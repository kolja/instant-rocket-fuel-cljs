(defproject irf "1.2.3"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.562"]]
  :plugins [[lein-cljsbuild "1.1.6"]
            [org.clojure/tools.reader "1.0.0"]
            [lein-figwheel "0.5.10"]
            [lein-doo "0.1.7"]]
  :cljsbuild
  {:builds [{:id "test"
             :source-paths ["src/irf" "test/irf"]
             :compiler {:output-to "target/test/irf.js"
                        :output-dir "target"
                        :main irf.runner
                        :target :nodejs
                        :optimizations :none}}
            {:id "repl"
             :source-paths ["src/irf"]
             :figwheel true
             :compiler {:main irf.core
                        :output-to "resources/public/js/out/irf.js"
                        :output-dir "resources/public/js/out"
                        :target :nodejs
                        :optimizations :none
                        :source-map true}}]}
  :figwheel {})

