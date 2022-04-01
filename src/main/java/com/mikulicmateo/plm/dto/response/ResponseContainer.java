package com.mikulicmateo.plm.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ResponseContainer {
    private boolean success;
    private ResponseProductDto productDto;

    public ResponseContainer(boolean success){
        this.success = success;
    }
}
