/**
 * DTO (Data Transfer Object) клас за сериализация и десериализация на
 * връзката между студент и предмет с оценка.
 * <p>
 * Използва се при записване и зареждане на данни в/от файлове (например JSON).
 * Съдържа само основните данни като идентификатори и оценка.
 * </p>
 *
 * <p>Полета:</p>
 * <ul>
 *   <li><b>studentId</b> - уникален идентификатор на студента (UUID като String)</li>
 *   <li><b>subjectId</b> - уникален идентификатор на предмета (UUID като String)</li>
 *   <li><b>grade</b> - оценка за съответния студентски предмет</li>
 * </ul>
 *
 * @author Данаил Димитров
 */
public class StudentSubjectDTO {
    public String studentId;
    public String subjectId;
    public float grade;

    public static StudentSubjectDTO from(StudentSubject ss) {
        StudentSubjectDTO dto = new StudentSubjectDTO();
        dto.studentId = ss.getStudent().getId().toString();
        dto.subjectId = ss.getSubject().getId().toString();
        dto.grade = ss.getGrade();
        return dto;
    }
}
