(defproject sniffkill "0.1.0"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :plugins [[lein-binplus "0.6.6"]]
  :dependencies [[org.clojure/clojure "1.11.1"]]
  :main ^:skip-aot sniffkill.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}}
  :bin {:name "sk"
        :bin-path ".bin"
        :bootclasspath false})
