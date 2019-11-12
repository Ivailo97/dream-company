package dreamcompany.domain.model.binding;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

public class TeamCreateBindingModel {

    private String name;

    //id
    private String office;

    private MultipartFile teamLogo;

    //ids
    private Set<String> employees;

    @NotNull(message = "Choose a name")
    @NotEmpty(message = "Choose a name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull(message = "Select a office")
    @NotEmpty(message = "Select a office")
    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    @Size(min = 2, message = "Select at least two employees")
    public Set<String> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<String> employees) {
        this.employees = employees;
    }

    @NotNull(message = "upload a logo")
    public MultipartFile getTeamLogo() {
        return teamLogo;
    }

    public void setTeamLogo(MultipartFile teamLogo) {
        this.teamLogo = teamLogo;
    }
}
