(ns sniffkill.core-test
  (:require [clojure.test :refer :all]
            [sniffkill.core :refer :all]))

(deftest parse-int-test
  (testing "should pares a string into an integer when valid"
    (is (= 1 (parse-int "1"))))
  (testing "should return the string when it's not a valid number"
    (is (= "a" (parse-int "a")))))

(deftest parse-name-test
  (testing "should parse out the host, port and type from a name string"
    (is (= {:host "127.0.0.1"
            :port 56217
            :type "LISTEN"}
           (parse-name "127.0.0.1:56217 (LISTEN)"))))
  (testing "should ignore the -> content in port and parse ports properly"
    (is (= {:host "127.0.0.1"
            :port 56217
            :type "LISTEN"}
           (parse-name "127.0.0.1:56217->127.0.0.1 (LISTEN)"))))
  (testing "should ignore type if it's not present"
    (is (= {:host "127.0.0.1"
            :port 56217
            :type nil}
           (parse-name "127.0.0.1:56217->127.0.0.1 "))))
  (testing "should handle * host"
    (is (= {:host "*"
            :port 56217
            :type "LISTEN"}
           (parse-name "*:56217 (LISTEN)"))))
  (testing "should handle * port"
    (is (= {:host "*"
            :port "*"
            :type "LISTEN"}
           (parse-name "*:* (LISTEN)")))
    ))


