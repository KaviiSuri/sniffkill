(ns sniffkill.core
  (:require [clojure.java.shell :refer [sh]]
            [clojure.string :as str])
  (:gen-class))

(defn get-raw-processses []
  (let [output (:out (sh "lsof" "-i" "-P" "-n"))
        lines (str/split-lines output)
        header (first lines)
        rows (rest lines)
        columns [:command :pid :user :id :type :device :size-off :node :name]]
    (map (fn [row]
           (zipmap columns (str/split row #"\s+" 9)))  ;; Parse each row into a map
         rows)))

(defn parse-int
  "Attempts to parse a string into an integer. Returns the string if it's not a valid number."
  [s]
  (try
    (Integer/parseInt s)
    (catch Exception _ s)))

(defn parse-name
  "Parses out the name, the host, the port and the protocol from a name string
  e.g. \"127.0.0.1:56217 (LISTEN)\" 
          => {:host \"127.0.0.1\"
              :port 56217
              :type (\"LISTEN\")}"
  [name]
  (let [[address type-str] (str/split name #"\s+")
        [host port] (str/split address #":")]
    {:host host
     :port (parse-int (first (str/split port #"->")))
     :type (when type-str (str/trim (str/replace type-str #"[()]" "")))}))

(defn is-relevant? [process]
  (let [name (:name process)]
    (= (:type name) "LISTEN")))

(defn kill-process [pid]
  (sh "kill" "-9" (str pid)))

(defn get-processes []
  (->> (get-raw-processses)
       (map #(update % :name parse-name))
       (filter is-relevant?)))

(defn kill-process-by-port [port]
  (let [processes (get-processes)
        process (first (filter #(= port (:port (:name %))) processes))]
    (when process
      (kill-process (:pid process)))
    process))

(defn fzf-installed? []
  (zero? (:exit (sh "command" "-v" "fzf"))))

(defn fzf-select-process []
  (let [processes (get-processes)
        formatted (map #(str (:port (:name %)) " - " (:command %)) processes)
        input-str (str/join "\n" formatted)
        {:keys [out err exit]} (sh "fzf" :in input-str)]
    (when (zero? exit)
      (-> out
          (str/split #" - ")
          first
          parse-int))))

(defn print-usage-and-exit []
  (println "fzf not found. Use the following syntax instead:
Usage: sniffkill <port>\nKills the process listening on the given port.")
  (System/exit 1))

(defn -main
  [& args]
  (let [port (if (empty? args)
               (if (fzf-installed?)
                 (fzf-select-process)
                 (print-usage-and-exit))
               (parse-int (first args)))]
    (if (number? port)
      (do
        (println (str "Attempting to kill process on port " port "..."))
        (if-let [process (kill-process-by-port port)]
          (println (str "Killed process "
                        (:command process) " with PID "
                        (:pid process)))
          (println (str "No process found on port " port)))
        (System/exit 0))
      (do
        (println "Invalid port number.")
        (System/exit 1)))))
