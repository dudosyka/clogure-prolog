(ns com.query
  "Query builder"
  (:require [clojure.string :as str]
            [com.prolog :as prolog]))

(defn process_newbie [newbie]
  (str (if newbie
         "best_for_newbie(X)"
         "leader(X)") ", leader_aspects_raiting(X, S, D, W, F, C), nation(X, Y), location(Y, Z)"))


(defn append_rule_recursive [query values rule]
  (if (= 0 (count values))
    (str/replace query #";$" "")
    (let [on_append (str/replace (str "'"(peek values)"'") #"\s+" "")
          query (str query rule on_append ") ;")]
      (append_rule_recursive query (pop values) rule))))

(defn process_part_of_the_world [query parts]
  (if (not (= 0 (count parts)))
    (str query ", (" (append_rule_recursive "" parts "leader_location(X,") ")")
    query))


(defn process_nation [query nations]
  (if (not (= 0 (count nations)))
    (str query ", (" (append_rule_recursive "" nations "nation(X,") ")")
    query))


(defn process_key_aspect [query aspects]
  (if (not (= 0 (count aspects)))
    (str query ", (" (append_rule_recursive "" aspects "leader_best_aspect(X,") ")")
    query))

(defn lazy_to_stack [lazy]
  (reduce conj [] lazy))

(defn find_leaders [form database mapper]
  (let [query (str (process_key_aspect
                     (process_part_of_the_world
                       (process_nation (process_newbie (:newbie form))
                                       (lazy_to_stack (:nation form)))
                       (lazy_to_stack (:part_of_the_world form)))
                     (lazy_to_stack (:key_aspect form))) ".")
        prolog (prolog/prolog-instance database)]
    (prolog/query prolog query mapper)))


