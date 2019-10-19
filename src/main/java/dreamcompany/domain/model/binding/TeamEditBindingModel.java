package dreamcompany.domain.model.binding;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

public class TeamEditBindingModel {

    private String id;

    private String name;

    //id
    private String office;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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
}
