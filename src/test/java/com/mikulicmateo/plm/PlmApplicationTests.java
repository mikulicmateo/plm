package com.mikulicmateo.plm;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.mikulicmateo.plm.dto.request.RequestProductDto;
import com.mikulicmateo.plm.resource.ProductResource;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WireMockTest
@ActiveProfiles("test")
class PlmApplicationTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ProductResource productResource;

    private ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;

    @BeforeEach
    public void setup(){
        mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @BeforeAll
    public void createStub(){
        String body = "[\n" +
                "    {\n" +
                "        \"Broj tečajnice\": \"63\",\n" +
                "        \"Datum primjene\": \"31.03.2022\",\n" +
                "        \"Država\": \"EMU\",\n" +
                "        \"Šifra valute\": \"978\",\n" +
                "        \"Valuta\": \"EUR\",\n" +
                "        \"Jedinica\": 1,\n" +
                "        \"Kupovni za devize\": \"7,500000\",\n" +
                "        \"Srednji za devize\": \"7,500000\",\n" +
                "        \"Prodajni za devize\": \"7,500000\"\n" +
                "    }\n" +
                "]";

        stubFor(get("/tecajn/v1?valuta=EUR").willReturn(ok().withBody(body)));
    }


    @Test
    @Order(1)
    void saveOk() throws Exception {
        RequestProductDto productDto = new RequestProductDto("0123456789", "potato", 7.5, "Krumpir", true);
        mockMvc.perform(MockMvcRequestBuilders.post("/product/save")
                .content(objectMapper.writeValueAsString(productDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Product saved succesfully."))
                .andDo(print());
    }

    @Test
    @Order(2)
    void getByCodeOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/product/get-code/0123456789"))
                .andExpect(jsonPath("$.code").value("0123456789"))
                .andExpect(jsonPath("$.name").value("potato"))
                .andExpect(jsonPath("$.priceHrk").value(7.5))
                .andExpect(jsonPath("$.priceEur").value(1.00))
                .andExpect(jsonPath("$.description").value("Krumpir"))
                .andExpect(jsonPath("$.available").value(true))
                .andDo(print());
    }

}
