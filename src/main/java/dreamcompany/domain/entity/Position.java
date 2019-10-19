package dreamcompany.domain.entity;

import java.math.BigDecimal;

public enum Position {

    INTERN(800),
    JUNIOR(1500),
    SENIOR(3000),
    TEAM_LEADER(4000),
    PROJECT_MANAGER(6000);

    int salary;

    Position(int salary) {
        this.salary = salary;
    }

    public BigDecimal getSalary() {
        return BigDecimal.valueOf(salary);
    }
}
