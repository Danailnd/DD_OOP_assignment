import java.util.List;
import java.util.UUID;

//Дисциплина
public class Course {

    private final UUID id;
    private String name;
    private boolean isMandatory;
    private List<Integer> availableYears;

    public Course(String name, boolean isMandatory, List<Integer> availableYears) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.isMandatory = isMandatory;
        this.availableYears = availableYears;
    }

    public boolean isAvailableForYear(int year) {
        return availableYears.contains(year);
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isMandatory() {
        return isMandatory;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setMandatory(boolean mandatory) {
        isMandatory = mandatory;
    }

    public void setAvailableYears(List<Integer> availableYears) {
        this.availableYears = availableYears;
    }
}
