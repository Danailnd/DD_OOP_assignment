/**
 * Препис на статусите, в които може да се намира студент.
 *
 * <ul>
 *   <li>{@link #ENROLLED} - Записан студент.</li>
 *   <li>{@link #GRADUATED} - Завършил студент.</li>
 *   <li>{@link #SUSPENDED} - Прекъснал студент.</li>
 * </ul>
 *
 * Използва се за проследяване на академичния статус на студента в системата.
 *
 * @author Данаил Димитров
 */
public enum StudentStatus {
    ENROLLED, GRADUATED, SUSPENDED
}
