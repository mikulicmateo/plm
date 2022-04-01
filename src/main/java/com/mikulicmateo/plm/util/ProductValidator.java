package com.mikulicmateo.plm.util;

import com.mikulicmateo.plm.dto.request.RequestProductDto;

public class ProductValidator {

    public static boolean validateProduct(RequestProductDto productDto){

        if(productDto == null || productDto.getCode() == null || productDto.getPriceHrk() < 0 || productDto.getCode().length() != 10)
            return false;

        return true;
    }
}
