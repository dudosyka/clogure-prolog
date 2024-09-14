(ns com.core
  "Main namespace"
  (:require
    [com.cli :as cli]
    [com.query :as query]))

(defn run [opts]
  (let [form (cli/ask_iteration_loop opts)
        leaders (query/find_leaders
                  form
                  "./resources/civilization.pl"
                  [{:param-name "X", :label :leader-name, :processor #(get cli/leaders_dict %)}
                   {:param-name "S", :label :science-aspect, :param-type :int-type}
                   {:param-name "D", :label :diplomation-aspect, :param-type :int-type}
                   {:param-name "W", :label :war-aspect, :param-type :int-type}
                   {:param-name "F", :label :faith-aspect, :param-type :int-type}
                   {:param-name "C", :label :culture-aspect, :param-type :int-type}
                   {:param-name "Y", :label :nation-name, :processor #(cli/find_key_by_value cli/nation_dict %)}
                   {:param-name "Z", :label :location-name, :processor #(cli/find_key_by_value cli/part_of_the_world_dict %)}])
        repeat (cli/answ_iteration_loop leaders)]
    (when repeat (run true))))
