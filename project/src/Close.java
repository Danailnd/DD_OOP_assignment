/**
 * Команда за затваряне на текущото хранилище с данни.
 *
 * Изтрива обекта DataStore от паметта.
 */

public class Close extends Operation{
    public void execute(String[] args) {
        g_data_store = null;
    }
}
