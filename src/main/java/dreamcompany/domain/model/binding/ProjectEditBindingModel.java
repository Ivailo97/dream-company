package dreamcompany.domain.model.binding;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ProjectEditBindingModel {

    private String id;

    private String name;

    private String description;

    private BigDecimal reward;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @NotEmpty(message = "Name can't be empty")
    @Length(min = 5, max = 15, message = "Length must be between 5 and 15 symbols")
    @NotNull(message = "Name can't be null")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotEmpty(message = "Description can't be empty")
    @Length(min = 10, max = 300, message = "Length must be between 10 and 300 symbols")
    @NotNull(message = "Description can't be null")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @NotNull(message = "Reward can't be null")
    @DecimalMin(value = "0.0001", message = "Reward can't be a negative value")
    public BigDecimal getReward() {
        return reward;
    }

    public void setReward(BigDecimal reward) {
        this.reward = reward;
    }
}
