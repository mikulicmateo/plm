package com.mikulicmateo.plm.resource;

import com.mikulicmateo.plm.dto.request.RequestProductDto;
import com.mikulicmateo.plm.dto.response.ResponseContainer;
import com.mikulicmateo.plm.dto.response.ResponseMessageDto;
import com.mikulicmateo.plm.dto.response.ResponseProductDto;
import com.mikulicmateo.plm.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("product")
public class ProductResource {

    private final ProductService productService;

    @Autowired
    public ProductResource(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/save")
    public ResponseEntity<ResponseMessageDto> saveProduct(@RequestBody RequestProductDto productDto){
        ResponseMessageDto responseMessage = productService.saveProduct(productDto);
        if(responseMessage.isSuccess()){
            return new ResponseEntity<>(responseMessage, HttpStatus.CREATED);
        }else{
            return  new ResponseEntity<>(responseMessage, HttpStatus.OK);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseMessageDto> updateProduct(@PathVariable long id, @RequestBody RequestProductDto productDto){
        ResponseMessageDto responseMessageDto = productService.updateProduct(id, productDto);
        return new ResponseEntity<>(responseMessageDto, HttpStatus.OK);
    }

    @DeleteMapping("/delete-id/{id}")
    public ResponseEntity<ResponseMessageDto> updateProduct(@PathVariable long id){
        ResponseMessageDto responseMessageDto = productService.deleteProductById(id);
        if(responseMessageDto.isSuccess()){
            return new ResponseEntity<>(responseMessageDto, HttpStatus.OK);
        }else return new ResponseEntity<>(responseMessageDto, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/delete-code/{code}")
    public ResponseEntity<ResponseMessageDto> updateProduct(@PathVariable String code){
        ResponseMessageDto responseMessageDto = productService.deleteProductByCode(code);
        if(responseMessageDto.isSuccess()){
            return new ResponseEntity<>(responseMessageDto, HttpStatus.OK);
        }else return new ResponseEntity<>(responseMessageDto, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/get-id/{id}")
    public ResponseEntity<ResponseProductDto> getProductById(@PathVariable long id){
        ResponseContainer responseContainer = productService.getProductById(id);
        if(responseContainer.isSuccess()){
            return new ResponseEntity<>(responseContainer.getProductDto(), HttpStatus.OK);
        }else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/get-code/{code}")
    public ResponseEntity<ResponseProductDto> getProductByCode(@PathVariable String code){
        ResponseContainer responseContainer = productService.getProductByCode(code);
        if(responseContainer.isSuccess()){
            return new ResponseEntity<>(responseContainer.getProductDto(), HttpStatus.OK);
        }else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


}