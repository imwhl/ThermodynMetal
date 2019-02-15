(defproject thermodynmetal "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [seesaw/seesaw "1.5.0"]
                 [org.knowm.xchart/xchart "3.5.2"]
                 ;; [incanter/incanter-core "1.9.3"]
                 ;; [incanter/incanter-charts "1.9.3"]
                 ;; https://mvnrepository.com/artifact/com.panayotis/javaplot
                 ;;[com.panayotis/javaplot "0.5.0"]
                 ]
  :main ^:skip-aot thermodynmetal.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
;;todo: use incanter to replace xchart
