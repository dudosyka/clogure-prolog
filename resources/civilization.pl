/*
    leader - факт описывающий лидеров представленных в игре
*/
leader('Peter I').
leader('Gandhi').
leader('Catherin de Medici').
leader('Cleopatra').
leader('Saladin').
leader('Gilgamesh').
leader('Harald Hardrada').
leader('Hojo Tokimune').
leader('Pedro II').
leader('Philip II').
leader('Qin Shi Huang').
leader('Theodore Roosevelt').
leader('Tomyris').
leader('Trajan').
leader('Victoria').
leader('Shaka').


/*
    nation - факт описывающий во главе какой державы стоит каждый из лидеров
*/
nation('Peter I', 'Russia').
nation('Gandhi', 'India').
nation('Catherin de Medici', 'France').
nation('Cleopatra', 'Egypt').
nation('Saladin', 'Arabia').
nation('Gilgamesh', 'Sumeria').
nation('Harald Hardrada', 'Norway').
nation('Hojo Tokimune', 'Japan').
nation('Pedro II', 'Brazil').
nation('Philip II', 'Spain').
nation('Qin Shi Huang', 'China').
nation('Theodore Roosevelt', 'USA').
nation('Tomyris', 'Scythia').
nation('Trajan', 'Rome').
nation('Victoria', 'England').
nation('Shaka', 'Zulu').


/*
    location - факт описывающий местоположение, в котором начинает каждая из держав
*/
location('Russia', 'Europe').
location('India', 'Asia').
location('France', 'Europe').
location('Egypt', 'Africa').
location('Arabia', 'Asia').
location('Sumeria', 'Asia').
location('Norway', 'Europe').
location('Japan', 'Asia').
location('Brazil', 'America').
location('Spain', 'Europe').
location('China', 'Asia').
location('USA', 'America').
location('Scythia', 'Europe').
location('Rome', 'Europe').
location('England', 'Europe').
location('Zulu', 'Africa').

/*
    leader_aspects_raiting это факт описывающий наклонности лидеров относительно различных аспектов игры

    leader_aspects_raiting(Лидер, Наука, Дипломатия, Война, Вера, Культура)
    
    Каждый аспект оценивается от 1 до 10
    Сумма всех аспектов для каждого лидера равна 30

    aspect - факт описывающий аспект, при описании лидера, вторым термом вводится порядковый номер при описании рейтинга лидера
*/

aspect('Science', 0).
aspect('Diplomation', 1).
aspect('War', 2).
aspect('Faith', 3).
aspect('Culture', 4).

leader_aspects_raiting('Peter I', 10, 4, 5, 3, 8).
leader_aspects_raiting('Gandhi', 6, 5, 2, 10, 7).
leader_aspects_raiting('Catherin de Medici', 5, 7, 5, 4, 9).
leader_aspects_raiting('Cleopatra', 4, 5, 6, 5, 10).
leader_aspects_raiting('Saladin', 7, 3, 7, 9, 4).
leader_aspects_raiting('Gilgamesh', 9, 4, 7, 2, 8).
leader_aspects_raiting('Harald Hardrada', 7, 2, 10, 4, 7).
leader_aspects_raiting('Hojo Tokimune', 4, 5, 10, 6, 5).
leader_aspects_raiting('Pedro II', 6, 4, 7, 4, 9).
leader_aspects_raiting('Philip II', 5, 5, 7, 8, 5).
leader_aspects_raiting('Qin Shi Huang', 7, 6, 6, 5, 6).
leader_aspects_raiting('Theodore Roosevelt', 8, 5, 7, 4, 6).
leader_aspects_raiting('Tomyris', 5, 6, 9, 5, 5).
leader_aspects_raiting('Trajan', 6, 6, 8, 5, 5).
leader_aspects_raiting('Victoria', 5, 6, 8, 4, 7).
leader_aspects_raiting('Shaka', 6, 5, 8, 5, 6).

/*
    start_difficulty - это факт описывающий то, насколько легко начинать игру за того или иного лидера
    Оценка производится по шкале 1-5
*/

start_difficulty('Peter I', 1).
start_difficulty('Gandhi', 3).
start_difficulty('Catherin de Medici', 4).
start_difficulty('Cleopatra', 3).
start_difficulty('Saladin', 3).
start_difficulty('Gilgamesh', 5).
start_difficulty('Harald Hardrada', 3).
start_difficulty('Hojo Tokimune', 4).
start_difficulty('Pedro II', 4).
start_difficulty('Philip II', 2).
start_difficulty('Qin Shi Huang', 2).
start_difficulty('Theodore Roosevelt', 3).
start_difficulty('Tomyris', 4).
start_difficulty('Trajan', 1).
start_difficulty('Victoria', 2).
start_difficulty('Shaka', 5).


/*
    Предикат для проверки есть ли в массиве эллементы больше заданного 
*/
have_grater([H|_], I) :- H > I, !.
have_grater([_|T], I) :- have_grater(T, I).


/*
    Предикат для получения максимального элемента в массиве и его индекса.
*/
get_max_indexed([H|T], Max, Index) :- max_indexed(T, H, Max, 1, 0, Index).
/*
    "Базовое" правило к которому все сводится, при переборе массива
    Все будет сводиться к нему, так как в процессе перебора 
    мы каждый раз "отрезаем" голову массива и сравниваем ее с текущим максимумом
    Перебор заканчивает ровно тогда, когда массив опустеет.
*/
max_indexed([], CurMax, CurMax, _, Index, Index).
max_indexed([H|T], CurMax, Max, CountIndex, CurIndex, MaxIndex) :- 
    (
        H =< CurMax, 
        NextIndex is CountIndex + 1,
        max_indexed(T, CurMax, Max, NextIndex, CurIndex, MaxIndex)
    ) ; 
    (
        H > CurMax, 
        NextIndex is CountIndex + 1,
        max_indexed(T, H, Max, NextIndex, CountIndex, MaxIndex)
    ).


/*
    Правило связывающее лидера и местоположение на котором он начинает.
*/
leader_location(Leader, Location) :-
    nation(Leader, Nation), 
    location(Nation, Location).

/*
    Правило описывающее лидеров, которые начинают в местоположении географически отделенном от основной массы игроков.
*/
separated_location(Leader) :-
    leader_location(Leader, Location), 
    (Location = 'America' ;  Location = 'Africa').

/*
    Правило описывающее лидеров, которые лучше всего себя показывают на старте игры.
*/
best_for_easy_start(Leader) :- 
    start_difficulty(Leader, X), 
    X =< 3, leader_location(Leader, Location), 
    Location = 'Europe'.

/*
    Правило описывающее лидеров, которые, при правильном отыгрыше, лучше всего себя показывают ближе к концу игры.
*/
best_for_lategame(Leader) :- 
    separated_location(Leader),
    leader_aspects_raiting(Leader, S, D, W, _, _),
    S > 4, D > 4, W > 4.

/*
    Правило описывающее лидеров, которые лучше всего подходят для новичков в Civilization.
    Такими лидерами считаются обладатели упора на какой то конкретный аспект.
*/
best_for_newbie(Leader) :-
    leader_aspects_raiting(Leader, S, D, W, F, C), 
    have_grater([S, D, W, F, C], 8), 
    best_for_easy_start(Leader).

/*
    Правило описывающее максимально развитый аспект у того или иного лидера
*/
leader_best_aspect(Leader, Aspect) :-
    leader_aspects_raiting(Leader, S, D, W, F, C), 
    get_max_indexed([S, D, W, F, C], _, Index), 
    aspect(Aspect, Index).

/*
    Правило описывающее лидера, как идеально подходящего для игры через стратегию научной победы.
*/
best_science_leader(Leader) :-
    leader_best_aspect(Leader, 'Science'),
    (leader_location(Leader, 'Europe') ; leader_location(Leader, 'Asia')).
