package dreamcompany.domain.model.service;

import java.math.BigDecimal;
import java.util.Set;

public class TeamServiceModel extends BaseServiceModel {

    private String name;

    private BigDecimal profit;

    private OfficeServiceModel office;

    private ProjectServiceModel project;

    private Set<UserServiceModel> employees;

    public TeamServiceModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }

    public OfficeServiceModel getOffice() {
        return office;
    }

    public void setOffice(OfficeServiceModel office) {
        this.office = office;
    }

    public Set<UserServiceModel> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<UserServiceModel> employees) {
        this.employees = employees;
    }

    public ProjectServiceModel getProject() {
        return project;
    }

    public void setProject(ProjectServiceModel project) {
        this.project = project;
    }
}
