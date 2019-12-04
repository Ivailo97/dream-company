package dreamcompany.domain.model.binding;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Getter
@Setter
public class TeamCreateBindingModel {

    private String name;

    //id
    private String office;

    private MultipartFile logo;

    //ids
    private Set<String> employees;
}
