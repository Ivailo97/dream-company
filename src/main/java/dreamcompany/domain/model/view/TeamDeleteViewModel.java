package dreamcompany.domain.model.view;

import java.util.Set;

public class TeamDeleteViewModel {

    private String id;

    private String name;

    //address
    private String office;

    //full names
    private Set<String> employees;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public Set<String> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<String> employees) {
        this.employees = employees;
    }
}
