package dreamcompany.domain.model.service;

import dreamcompany.domain.entity.Position;

import java.math.BigDecimal;
import java.util.Set;

public class TeamServiceModel extends BaseServiceModel {

    private String name;

    private BigDecimal profit;

    private String logoUrl;

    private String logoId;

    private OfficeServiceModel office;

    private Position teamLeaderPreviousPosition;

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

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getLogoId() {
        return logoId;
    }

    public void setLogoId(String logoId) {
        this.logoId = logoId;
    }

    public Position getTeamLeaderPreviousPosition() {
        return teamLeaderPreviousPosition;
    }

    public void setTeamLeaderPreviousPosition(Position teamLeaderPreviousPosition) {
        this.teamLeaderPreviousPosition = teamLeaderPreviousPosition;
    }
}
