package org.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ElectricityPrice(
        double SEK_per_kWh,
        String time_start,
        String time_end
) {
}
