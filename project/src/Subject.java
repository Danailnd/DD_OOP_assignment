import java.util.List;
import java.util.UUID;

//Дисциплина
public class Subject {
    private UUID id;
    private String name;
    private boolean isMandatory;
    private List<Integer> availableYears;

    public Subject() {
    }

    public Subject(String name, boolean isMandatory, List<Integer> availableYears) {
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

    public List<Integer> getAvailableYears() {
        return availableYears;
    }

    // Setters
    public void setId(UUID id){this.id = id;}
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
