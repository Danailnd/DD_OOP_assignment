import java.util.List;

/**
 * DTO (Data Transfer Object) клас за сериализация и десериализация на специалност.
 * <p>
 * Използва се при записване и зареждане на данни в/от файлове (например JSON).
 * Съдържа основните полета за специалността, включително списък с курсове (дисциплини).
 * </p>
 *
 * <p>Полета:</p>
 * <ul>
 *   <li><b>id</b> - уникален идентификатор на специалността (UUID като String)</li>
 *   <li><b>name</b> - име на специалността</li>
 *   <li><b>courses</b> - списък с дисциплини, които са част от специалността</li>
 * </ul>
 *
 * @author Данаил Димитров
 */
public class SpecialtyDTO {
    public String id;
    public String name;
    public List<Subject> courses;
}
