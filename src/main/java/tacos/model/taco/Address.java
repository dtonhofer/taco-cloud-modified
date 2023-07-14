package tacos.model.taco;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import tacos.model.helpers.Helpers;

@Slf4j
@Data
public class Address {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Street is required")
    private String street;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "State is required")
    private String state;

    @NotBlank(message = "Zip code is required")
    private String zip;

    // In order to pre-fill form fields, deliver valid data immediately!

    public Address() {
        log.info(">>> {} created", Helpers.makeLocator(this));
        this.name = "Whoever";
        this.street = "Ammonia Ave.";
        this.city = "Carcosa";
        this.state = "Lowbar";
        this.zip = "B-7676";
    }
}
