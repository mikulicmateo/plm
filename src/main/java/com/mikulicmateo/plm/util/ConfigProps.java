package com.mikulicmateo.plm.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConfigProps {

    @Value("${curr.hostname}")
    private String hostname;

    @Value(("${curr.path}"))
    private String path;
}
