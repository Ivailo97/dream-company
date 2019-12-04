package dreamcompany.domain.model.view;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class UserProfileViewModel {

    private String username;

    private String id;

    private String firstName;

    private String lastName;

    private String imageUrl;

    private String email;

    private String position;

    private Integer credits;

    private BigDecimal salary;

    private List<FriendViewModel> friends;
}
