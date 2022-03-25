package com.mikulicmateo.plm.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ResponseProductDto {

    private String code;
    private String name;
    private double priceHrk;
    private double priceEur;
    private String description;
    private boolean available;

}
