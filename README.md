Lab 61 
Написать программу эмуляции работы прерываний от клавиатуры, то есть по нажатию заданной клавиши
прервать работу программы. 3 функции которые выводят постоянно сообщения.
Во время ввода нужно ввести клавиши прерывания (например, если нажать 1 то перекидывает на 1ю функцию,
если 2 на 2ю и тд, а если нажать на клавишу текущей функции то она останавливается).

Lab 62
Во 2м задании нужно чтобы через какой-то определенный промежуток времени
выводилось на экран текст который за это время был записан.
А также иногда(например раз в 10 секунд) чтобы производились работа с ранее введенными данными.
Проблема в том, что текст должен сохраняться неазависимо от нажатия Enter, поэтому я создаю форму (да, Swing).
Через консоль это можно сделать только переведя её в raw mode, для этого нужно использовать странные
вещи (net.java.dev.jna).

Lab 5 - Файловая система
Создать программу, эмулирующую файловую систему. Должны быть базовые функции: перемещение, удаление,
добавление, переименование, чтение и тд. Создаётся какой-то контейнер,
разбитый на блоки, где хранятся файлы в виде 0 и 1. Если файл заполняет блок не полностью,
то остаток блока заполняется 0. Разумеется, программа должны уметь
переводить текст в двоичное представление. Файл может превышать размер блока.

Lab 4 - Память компьютера
Есть физическая память, жесткий диск(ВП), различные процессы.
Структура хранения на ФП и ВП двойной
список, где первые значения это номер (или название) процесса, а второй номер страницы в процессе
[“процесс 1”,1], [0,0]…].
Структура хранения в процессах тройной список (хранение в ВП или ФП, номер страницы,
некоторые данные [[“ФП”,1,”некоторые данные стр1”],…].
ФП должна в какой-то момент переполнится для просмотра работы с ВП.
Если ФП заполнена, то случайная строчка из ФП переносится в ВП
(меняются записи в процессе с “ФП” на “ВП”) и на ее место становится новая строчка.
При удалении: если процесс находится в ВП, то сначала переносится на ФП и затем удаляется
(перенесённый в ВП процесс остаётся там).

Lab 3 - Потоки
Процессы борются за семафор. Есть несколько контрольных точек.
Поток пришедший первый в контрольную точку, порождает новые потоки, остальные потоки останавливаются.
Дано: какие потоки должны порождаться на каждом из этапов и на каком этапе они пытаются захватить семафор
(может быть такое, что поток порождён на первом этапе,
но за семафор он будет бороться с потоками порождёнными на третьем этапе).
