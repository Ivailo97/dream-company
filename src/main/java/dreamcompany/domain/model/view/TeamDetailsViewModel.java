package dreamcompany.domain.model.view;

import java.util.Set;

public class TeamDetailsViewModel {

    private Set<UserInTeamDetailsViewModel> employees;

    private String officeAddress;

    public String getOfficeAddress() {
        return officeAddress;
    }

    public void setOfficeAddress(String officeAddress) {
        this.officeAddress = officeAddress;
    }

    public Set<UserInTeamDetailsViewModel> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<UserInTeamDetailsViewModel> employees) {
        this.employees = employees;
    }
}
