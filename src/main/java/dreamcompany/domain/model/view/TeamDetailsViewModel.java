package dreamcompany.domain.model.view;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class TeamDetailsViewModel {

    private String name;

    private Set<UserInTeamDetailsViewModel> employees;

    private String officeAddress;
}
