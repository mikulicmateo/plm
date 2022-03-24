package com.mikulicmateo.plm.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mikulicmateo.plm.exception.ResponseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;

public class CurrencyClient {

    private static final RestTemplate restTemplate = new RestTemplate();
    private static final String eurUrl = "https://api.hnb.hr/tecajn/v1?valuta=EUR";
    private static final ObjectMapper objectMapper  = new ObjectMapper();

    public static double getEurCurrency() {
        ResponseEntity<String> response = restTemplate.getForEntity(eurUrl, String.class);

        if(!response.getStatusCode().is2xxSuccessful()){
            throw new ResponseException("Unable to get currency.");
        }
        double currency;
        try {
            JsonNode root = objectMapper.readTree(response.getBody());
            currency = convertCommaSeparatedToDouble(root.get(0).path("Srednji za devize").asText());
        }
        catch (ParseException | JsonProcessingException | NullPointerException ex){
            throw new ResponseException("Unable to get currency");
        }
        if(currency == 0) throw new ResponseException("Unable to get currency");

        return currency;
    }

    private static double convertCommaSeparatedToDouble(String commaNumber) throws ParseException {
        DecimalFormat format = new DecimalFormat();
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(',');
        format.setDecimalFormatSymbols(symbols);
        Number number = format.parse(commaNumber);
        return number.doubleValue();
    }
}
