package com.mikulicmateo.plm.mapper;

import com.mikulicmateo.plm.dto.request.RequestProductDto;
import com.mikulicmateo.plm.entity.Product;
import com.mikulicmateo.plm.util.CurrencyClient;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mappings({
            @Mapping(target="code", source="code"),
            @Mapping(target="name", source="name"),
            @Mapping(target="priceHrk", source="priceHrk"),
            @Mapping(target="description", source="description"),
            @Mapping(target="available", source="available")
    })
    Product requestProductDtoToProduct(RequestProductDto productDto);

    @BeforeMapping
    default void addEurPriceToProduct(RequestProductDto productDto, @MappingTarget Product product){
        product.setPriceEur(calculatePrice(productDto.getPriceHrk(), CurrencyClient.getEurCurrency()));
    }

    default double calculatePrice(double priceHrk, double eurValue){
        return priceHrk / eurValue;
    }
}
