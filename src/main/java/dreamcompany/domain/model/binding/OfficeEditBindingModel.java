package dreamcompany.domain.model.binding;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class OfficeEditBindingModel {

    private String id;

    private String address;

    private String phoneNumber;

    private String town;

    private String country;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @NotEmpty(message = "Address can't be empty")
    @Length(min = 5, max = 50, message = "Length must be between 5 and 50 symbols")
    @NotNull(message = "Address can't be null")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @NotEmpty(message = "Phone number cant be empty")
    @Length(min = 5, max = 10, message = "Length must be between 5 and 10 symbols")
    @NotNull(message = "Phone number can't be null")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @NotEmpty(message = "Town cant be empty")
    @NotNull(message = "Town can't be null")
    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    @NotEmpty(message = "Country cant be empty")
    @NotNull(message = "Country can't be null")
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
