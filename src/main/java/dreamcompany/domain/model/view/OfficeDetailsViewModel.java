package dreamcompany.domain.model.view;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OfficeDetailsViewModel {

    private String address;

    private String phoneNumber;

    private List<TeamAllViewModel> teams;
}
