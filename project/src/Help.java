/**
 * Команда за извеждане на списък с всички налични команди и тяхното предназначение.
 *
 * Употреба: help
 */
public class Help extends Operation {

    @Override
    public void execute(String[] args) {
        System.out.println("""
Списък с налични команди:

open <път_до_файл>        – Зарежда файл с данни.
open --new                – Създава ново празно хранилище.
close                     – Затваря текущото хранилище.
save                      – Запазва текущите данни във вече отворения файл.
saveas <път_до_файл>      – Запазва текущите данни в нов файл.
exit                      – Излиза от програмата (с потвърждение).

enroll <fn> <spec> <group> <name>
                          – Записва нов студент с дадени параметри.

change <fn> <option> <value>
                          – Променя специалност, курс или група на студент.
                            Опции: specialty, group, year

graduate <fn>             – Отбелязва студент като завършил (ако има всички оценки).
interrupt <fn>            – Прекъсва студент.
resume <fn>               – Възстановява студент.

advance <fn>              – Повишава студент в следващ курс.

enrollin <fn> <course>    – Записва студент в дисциплина от програмата му.
grade <fn> <course> <grade>
                          – Добавя оценка по дисциплина на студент.

print <fn>                – Извежда информация за конкретен студент.
printall <spec> <year>    – Показва всички студенти в специалност и курс.

protocol <course>         – Извежда протоколи за дисциплината по групи.
report <fn>               – Извежда академичен отчет на студент.

help                      – Показва този списък с команди.
        """);
    }
}
