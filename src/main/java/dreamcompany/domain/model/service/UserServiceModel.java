package dreamcompany.domain.model.service;

import dreamcompany.domain.enumeration.Position;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
public class UserServiceModel extends BaseServiceModel {

    private String firstName;

    private String lastName;

    private Position position;

    private String imageUrl;

    private String imageId;

    private String email;

    private Integer credits;

    private BigDecimal salary;

    private LocalDateTime hiredOn;

    private String username;

    private String password;

    private TeamServiceModel team;

    private Set<RoleServiceModel> authorities;

    private Set<UserServiceModel> friends;

    private Set<FriendRequestServiceModel> friendRequests;
}
