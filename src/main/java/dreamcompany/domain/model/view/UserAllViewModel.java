package dreamcompany.domain.model.view;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserAllViewModel {

    private String id;

    private String username;

    private String email;

    private Set<String> authorities;
}
