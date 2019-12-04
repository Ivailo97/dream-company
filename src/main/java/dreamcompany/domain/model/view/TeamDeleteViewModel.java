package dreamcompany.domain.model.view;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class TeamDeleteViewModel {

    private String id;

    private String name;

    //address
    private String office;

    //full names
    private Set<String> employees;
}
