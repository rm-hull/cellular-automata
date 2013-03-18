(defproject cellular-automata "0.1.0-SNAPSHOT"
  :url "http://cellular-automata.destructuring-bind.org"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [noir "1.3.0"]
                 [jayq "2.1.0"]
                 [rm-hull/ring-gzip-middleware "0.1.5-SNAPSHOT"]
                 [rm-hull/monet "0.1.6-SNAPSHOT"]]
  :cljsbuild
    {:builds
     [{:source-paths ["src/ca/client"],
       :compiler
       {:pretty-print true,
        :output-to "resources/public/cljs/ca.js",
        :externs ["externs/jquery.js"],
        :optimizations :advanced,
        :print-input-delimiter true}}]}
  :hooks [;leiningen.dalap 
          leiningen.cljsbuild]
  :plugins [[rm-hull/lein-cljsbuild "0.3.1-SNAPSHOT"]
            [com.birdseye-sw/lein-dalap "0.1.0"] ]
  :profiles {:dev {:dependencies [[vimclojure/server "2.3.6"]]}}
  :main ca.server
  :min-lein-version "2.0.0"
  :warn-on-reflection true
  :description "A web-based cellular-automata animator")
