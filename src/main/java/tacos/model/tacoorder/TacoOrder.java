package tacos.model.tacoorder;

import lombok.Data;
import org.jetbrains.annotations.NotNull;
import tacos.model.taco.Taco;

import java.util.ArrayList;
import java.util.List;

@Data
public class TacoOrder {

    private String deliveryName;
    private String deliveryStreet;
    private String deliveryCity;
    private String deliveryState;
    private String deliveryZip;
    private String ccNumber;
    private String ccExpiration;
    private String ccCVV;
    private List<Taco> tacos = new ArrayList<>(); // maybe a map by name?

    public void addTaco(@NotNull Taco taco) {
        this.tacos.add(taco);
    }
}