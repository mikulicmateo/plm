package com.mikulicmateo.plm.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestProductDto {

    private String code;
    private String name;
    private double priceHrk;
    private String description;
    private boolean available;
}
