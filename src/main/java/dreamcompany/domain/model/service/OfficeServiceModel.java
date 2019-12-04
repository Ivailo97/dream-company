package dreamcompany.domain.model.service;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OfficeServiceModel extends BaseServiceModel {

    private String address;

    private String phoneNumber;

    private String town;

    private String country;
}
