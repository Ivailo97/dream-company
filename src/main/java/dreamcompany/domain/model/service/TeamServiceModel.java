package dreamcompany.domain.model.service;

import dreamcompany.domain.enumeration.Position;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class TeamServiceModel extends BaseServiceModel {

    private String name;

    private BigDecimal profit;

    private String logoUrl;

    private String logoId;

    private OfficeServiceModel office;

    private Position teamLeaderPreviousPosition;

    private ProjectServiceModel project;

    private Set<UserServiceModel> employees;
}
