package dreamcompany.domain.model.binding;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class TeamEditBindingModel {

    private String id;

    private String name;

    private MultipartFile logo;

    //id
    private String office;
}
