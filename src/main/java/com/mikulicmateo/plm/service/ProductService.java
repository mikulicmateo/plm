package com.mikulicmateo.plm.service;

import com.mikulicmateo.plm.dto.request.RequestProductDto;
import com.mikulicmateo.plm.dto.response.ResponseMessageDto;
import com.mikulicmateo.plm.entity.Product;
import com.mikulicmateo.plm.mapper.ProductMapper;
import com.mikulicmateo.plm.repository.ProductRepository;
import com.mikulicmateo.plm.util.CurrencyClient;
import com.mikulicmateo.plm.util.ProductValidator;
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
        if(ProductValidator.validateProduct(productDto)){

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

    @Transactional
    public ResponseMessageDto updateProduct(long id, RequestProductDto productDto) {
        if(ProductValidator.validateProduct(productDto)){
            Product productToUpdate = productRepository.getById(id);
            if(productToUpdate == null){
                return new ResponseMessageDto(false, "Product does not exist.");
            }
            productRepository.save(updateProductFields(productToUpdate, productDto));

            return new ResponseMessageDto(true, "Product successfully updated.");
        }
        else{
            return new ResponseMessageDto(false, "Product not correctly defined.");
        }

    }

    private Product updateProductFields(Product product, RequestProductDto productDto){
        product.setCode(productDto.getCode());
        product.setName(productDto.getName());
        product.setPriceHrk(productDto.getPriceHrk());
        product.setPriceEur(productDto.getPriceHrk() / CurrencyClient.getEurCurrency());
        product.setDescription(productDto.getDescription());
        product.setAvailable(productDto.isAvailable());
        return product;
    }

}