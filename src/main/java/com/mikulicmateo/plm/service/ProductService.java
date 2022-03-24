package com.mikulicmateo.plm.service;

import com.mikulicmateo.plm.dto.request.RequestProductDto;
import com.mikulicmateo.plm.dto.response.ResponseMessageDto;
import com.mikulicmateo.plm.entity.Product;
import com.mikulicmateo.plm.mapper.ProductMapper;
import com.mikulicmateo.plm.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Autowired
    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Transactional
    public ResponseMessageDto saveProduct(RequestProductDto productDto) {
        if(validateNewProduct(productDto)){

            if(productRepository.findByCode(productDto.getCode()).isPresent()){
                return new ResponseMessageDto(false, "Product already exists.");
            }

            Product product = productMapper.requestProductDtoToProduct(productDto);
            productRepository.save(product);

            return new ResponseMessageDto(true, "Product saved succesfully.");
        }else{
            return new ResponseMessageDto(false, "Product not correctly defined.");
        }
    }

    private boolean validateNewProduct(RequestProductDto productDto){
        System.out.println(productDto + " " + productDto.getCode() + " " + productDto.getPriceHrk() + " " + productDto.getCode().length());
        if(productDto == null || productDto.getCode() == null || productDto.getPriceHrk() < 0 || productDto.getCode().length() != 10)
            return false;

        return true;
    }
}