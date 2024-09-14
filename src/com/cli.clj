(ns com.cli
  "Command line interface implementation"
  (:require [clojure.string :as str]))


(defn get_answ [answ, available_variants single]
  (if (and (.contains answ ",") (not single))
    (let [answs (str/split answ #",")]
      (mapv (fn [item]
              (let [item (str/replace item #"\s+" "")]
                (if (contains? available_variants item)
                  item
                  (throw (Exception. "Bad answer provided")))))
            answs))
    (if (contains? available_variants answ)
      [answ]
      (if (= answ "") [] (throw (Exception. "Bad answer provided"))))))


(defn ask [question, answ_variants, error-message, single]
  (println question (interpose "," (map identity answ_variants)))
  (flush)
  (let [answ (read-line)]
    (try
      (get_answ answ answ_variants single)
      (catch Exception _ (do
                           (println error-message)
                           (ask question answ_variants error-message single))))))

(defrecord Form [newbie, part_of_the_world, nation, key_aspect])

(def leaders_dict {"Peter I" "Петр 1", "Gandhi" "Ганди", "Catherin de Medici" "Катерина Медичи", "Cleopatra" "Клеопатра", "Saladin" "Саладин", "Gilgamesh" "Гильгамеш", "Harald Hardrada" "Харальд Суровый", "Hojo Tokimune" "Ходжо Токимунэ", "Pedro II" "Педро II", "Philip II" "Филипп II", "Qin Shi Huang" "Кин Ши Хуанг", "Theodore Roosevelt" "Теодор Рузвельт", "Tomyris" "Томирис", "Trajan" "Траян", "Victoria" "Виктория", "Shaka" "Чака"})
(def newbie_dict {"Да" true, "Нет" false, nil false})
(def part_of_the_world_dict {"Европа" "Europe", "Азия" "Asia", "Америка" "America", "Африка" "Africa"})
(def nation_dict {"Россия" "Russia", "Индия" "India", "Франция" "France", "Египт" "Egypt", "Аравия" "Arabia", "Шумеры" "Sumeria", "Норвегия" "Norway", "Япония" "Japan", "Бразилия" "Brazil", "Испания" "Spain", "Китай" "China", "Америка" "USA", "Скифия" "Scythia", "Рим" "Rome", "Англия" "England","Зулусы" "Zulu"})
(def key_aspect_dict {"Наука" "Science", "Война" "War", "Дипломатия" "Diplomation", "Религия" "Faith", "Культура" "Culture"})

(defn find_key_by_value [dict value]
  (first (keys (filter #(= (val %) value) dict))))

(defn translate_answs [^Form form]
  (->Form (newbie_dict (:newbie form)),
          (map part_of_the_world_dict (:part_of_the_world form)),
          (map nation_dict (:nation form))
          (map key_aspect_dict (:key_aspect form))))

(defn ask_iteration_loop [skip_instruction]
  (when (not skip_instruction)
    (println "Привет! Я помогу вам с выбором персонажа в игре Civilization VI")
    (println "Для помощи с подбором от вас потребуется ответить на ряд вопросов")
    (println "Если какой либо вопрос вы считаете \"не важным\", просто оставьте ответ пустым"))
  (let [
        newbie (ask "Вы новичок в игре Civilization?"
                 #{"Да", "Нет"}
                 "Я не понял ваш ответ :("
                  true)
        part_of_the_world (ask "В какой части света вам бы хотелось стартовать? Вы можете указать несколько вариантов через запятую."
                            #{"Европа" "Азия" "Америка" "Африка"}
                            "Ответ не понят, повторите ввод."
                             false)
        nation (ask "За какие державы вам было бы интересно сыграть? Вы можете указать несколько вариантов через запятую."
                 #{"Россия" "Индия" "Франция" "Египт" "Аравия" "Шумеры" "Норвегия" "Япония" "Бразилия" "Испания" "Китай" "Америка" "Скифия" "Рим" "Англия" "Зулусы"}
                 "Я не понял ваш ответ :("
                  false)
        key_aspect (ask "Какой аспект игры для вас наиболее интересен? Вы можете указать несколько вариантов через запятую."
                        #{"Наука" "Война" "Дипломатия" "Культура" "Религия"}
                        "Я не понял ваш ответ :("
                        false)]
    (translate_answs (->Form (first newbie), part_of_the_world, nation, key_aspect))))

(defn answ_iteration_loop [leaders]
  (if (> (count leaders) 0)
    (do
      (println "На выбор для вас следующий набор лидеров:")
      (doseq [[num leader] (map-indexed vector leaders)]
        (println (str (+ 1 num)) (:leader-name leader))
        (println "Данный лидер управляет державой" (:nation-name leader) ", ее стартовая часть света это " (:location-name leader))
        (println "Уровень удобства игры за данного лидера в различных аспектах игры: ")
        (println "Наука" (:science-aspect leader))
        (println "Дипломатия" (:diplomation-aspect leader))
        (println "Война" (:war-aspect leader))
        (println "Религия" (:faith-aspect leader))
        (println "Культура" (:culture-aspect leader))
        (println "---------------------------------------------------------------------")))
    (println "К сожалению по вашим параметрам не удалось найти подходящего лидера... :("))

  (let [retry (ask "Хотите попробовать подобрать ещё раз?"
                   #{"Да" "Нет"}
                   "Я не понял ваш ответ :("
                   true)]
    (and (first retry) (= (first retry) "Да"))))

(defn fake_iteration_loop []
  (translate_answs (->Form (first ["Нет"]), ["Азия" "Африка" "Европа"], ["Рим" "Америка" "Аравия" "Китай" "Зулусы"], ["Война" "Наука" "Религия"])))