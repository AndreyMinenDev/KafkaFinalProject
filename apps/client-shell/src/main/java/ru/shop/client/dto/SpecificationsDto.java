package ru.shop.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpecificationsDto {

    private String weight;
    private String dimensions;

    @JsonProperty("battery_life")
    private String battery_Life;

    @JsonProperty("water_resistance")
    private String waterResistance;

}
