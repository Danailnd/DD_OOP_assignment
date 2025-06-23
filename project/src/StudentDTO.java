/**
 * DTO (Data Transfer Object) клас за сериализация и десериализация на студент.
 * <p>
 * Използва се при записване и зареждане на студентски данни в/от файлове (например JSON).
 * Съдържа основните свойства на студента, включително референция към специалността по нейния идентификатор.
 * </p>
 *
 * <p>Полета:</p>
 * <ul>
 *   <li><b>id</b> - уникален идентификатор на студента (UUID като String)</li>
 *   <li><b>name</b> - име на студента</li>
 *   <li><b>facultyNumber</b> - факултетен номер на студента</li>
 *   <li><b>course</b> - текущ курс, в който се обучава студентът</li>
 *   <li><b>specialtyId</b> - идентификатор на специалността (UUID като String)</li>
 *   <li><b>group</b> - група, към която принадлежи студентът</li>
 *   <li><b>status</b> - статус на студента (напр. записан, прекъснал, завършил)</li>
 *   <li><b>averageGrade</b> - среден успех на студента</li>
 * </ul>
 *
 * @author Данаил Димитров
 */
public class StudentDTO {
    public String id;
    public String name;
    public String facultyNumber;
    public int course;
    public String specialtyId;
    public int group;
    public String status;
    public double averageGrade;

    public static StudentDTO from(Student student) {
        StudentDTO dto = new StudentDTO();
        dto.id = student.getId().toString();
        dto.name = student.getName();
        dto.facultyNumber = student.getFacultyNumber();
        dto.course = student.getCourse();
        dto.group = student.getGroup();
        dto.status = student.getStatus().name();
        dto.averageGrade = student.getAverageGrade();
        dto.specialtyId = student.getSpecialty().getId().toString();
        return dto;
    }
}
