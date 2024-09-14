(ns com.prolog
  "Prolog queries processor"
  (:require
            [clojure.java.io :as io])
  (:import (org.projog.api Projog QueryResult)
           (org.projog.core.event ProjogListener)))

(defn prolog-instance [path]
  (let [prolog (Projog. (into-array ProjogListener []))]
    (.consultReader prolog (io/reader path))
    prolog))


(defn get_value [q item]
  (let [value (case (:param-type item)
                :int-type (.getLong q (:param-name item))
                :atom-type (.getAtomName q (:param-name item))
                (.getAtomName q (:param-name item)))
        processor (:processor item)]
    (if processor
      (processor value)
      value)))


(defn mapper [^QueryResult q mapping]
  (mapv
    (fn [item]
      (assoc {} (:label item) (get_value q item)))
    mapping))

(defn parse-recursive [^QueryResult q query_result mapping]
  (if (.next q)
    (parse-recursive q (conj query_result (mapper q mapping)) mapping)
    query_result))

(defn query [prolog q mapping]
  (let [prolog_result (.executeQuery prolog q) result [] module (count mapping)]
    (mapv
      (fn [item] (reduce #(merge-with + %1 %2) item))
      (partition module (flatten (parse-recursive prolog_result result mapping))))))