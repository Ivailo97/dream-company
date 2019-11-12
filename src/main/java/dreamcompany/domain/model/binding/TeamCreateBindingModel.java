package dreamcompany.domain.model.binding;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

public class TeamCreateBindingModel {

    private String name;

    //id
    private String office;

    private MultipartFile logo;

    //ids
    private Set<String> employees;

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

    public MultipartFile getLogo() {
        return logo;
    }

    public void setLogo(MultipartFile logo) {
        this.logo = logo;
    }
}
