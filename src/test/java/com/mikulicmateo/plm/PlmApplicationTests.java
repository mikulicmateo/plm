package com.mikulicmateo.plm;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.mikulicmateo.plm.dto.request.PageRequestDto;
import com.mikulicmateo.plm.dto.request.RequestProductDto;
import com.mikulicmateo.plm.resource.ProductResource;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;



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

    WireMockServer wireMockServer = new WireMockServer(8089);

    private ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;

    @Value("${curr.path}")
    private String path;

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

        wireMockServer.stubFor(get(path)).setResponse(aResponse().withBody(body).withStatus(200).withHeader("Content-type", "application/json").build());
        wireMockServer.start();
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
    void saveExists() throws Exception {
        RequestProductDto productDto = new RequestProductDto("0123456789", "potato", 7.5, "Krumpir", true);
        mockMvc.perform(MockMvcRequestBuilders.post("/product/save")
                        .content(objectMapper.writeValueAsString(productDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Product already exists."))
                .andDo(print());
    }

    @Test
    @Order(3)
    void saveBadCode() throws Exception {
        RequestProductDto productDto = new RequestProductDto("01234567891", "potato", 7.5, "Krumpir", true);
        mockMvc.perform(MockMvcRequestBuilders.post("/product/save")
                        .content(objectMapper.writeValueAsString(productDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Product not correctly defined."))
                .andDo(print());
    }

    @Test
    @Order(4)
    void saveNotCorrectlyDefined() throws Exception {
        RequestProductDto productDto = new RequestProductDto("1123456789", null, -7.5, "Krumpir", true);
        mockMvc.perform(MockMvcRequestBuilders.post("/product/save")
                        .content(objectMapper.writeValueAsString(productDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Product not correctly defined."))
                .andDo(print());
    }

    @Test
    @Order(5)
    void updateOk() throws Exception {
        RequestProductDto productDto = new RequestProductDto("1234567890", "Banana", 1, "Zuta", true);
        mockMvc.perform(MockMvcRequestBuilders.put("/product/update/1")
                        .content(objectMapper.writeValueAsString(productDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Product successfully updated."))
                .andDo(print());
    }

    @Test
    @Order(6)
    void updateNotCorrectlyDefined() throws Exception {
        RequestProductDto productDto = new RequestProductDto("1234567890", "Banana", -1, "Zuta", true);
        mockMvc.perform(MockMvcRequestBuilders.put("/product/update/1")
                        .content(objectMapper.writeValueAsString(productDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Product not correctly defined."))
                .andDo(print());
    }


    @Test
    @Order(7)
    void updateNotFound() throws Exception {
        RequestProductDto productDto = new RequestProductDto("1234567890", "Banana", 1, "Zuta", true);
        mockMvc.perform(MockMvcRequestBuilders.put("/product/update/6")
                        .content(objectMapper.writeValueAsString(productDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Product does not exist."))
                .andDo(print());
    }


    @Test
    @Order(8)
    void getByCodeOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/product/get-code/0123456789"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.code").value("0123456789"))
                .andExpect(jsonPath("$.name").value("potato"))
                .andExpect(jsonPath("$.priceHrk").value(7.5))
                .andExpect(jsonPath("$.priceEur").value(1.00))
                .andExpect(jsonPath("$.description").value("Krumpir"))
                .andExpect(jsonPath("$.available").value(true))
                .andDo(print());
    }

    @Test
    @Order(9)
    void getByCodeBadCode() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/product/get-code/012346789"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(print());
    }

    @Test
    @Order(10)
    void getByCodeNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/product/get-code/0123456780"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(print());
    }

    @Test
    @Order(11)
    void getByBadId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/product/get-id/0"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(print());
    }

    @Test
    @Order(12)
    void getByIdNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/product/get-id/100"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(print());
    }

    @Test
    @Order(13)
    void getByIdOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/product/get-id/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.code").value("1234567890"))
                .andExpect(jsonPath("$.name").value("Banana"))
                .andExpect(jsonPath("$.priceHrk").value(1))
                .andExpect(jsonPath("$.priceEur").value(0.13333333333333333))
                .andExpect(jsonPath("$.description").value("Zuta"))
                .andExpect(jsonPath("$.available").value(true))
                .andDo(print());
    }


    @Test
    @Order(14)
    void getByNameOk() throws Exception {
        PageRequestDto pageRequestDto = new PageRequestDto(0,1);
        mockMvc.perform(MockMvcRequestBuilders.get("/product/get-name/Banana")
                        .content(objectMapper.writeValueAsString(pageRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.content.[*].code").value("1234567890"))
                .andExpect(jsonPath("$.content.[*].name").value("Banana"))
                .andExpect(jsonPath("$.content.[*].priceHrk").value(1.0))
                .andExpect(jsonPath("$.content.[*].description").value("Zuta"))
                .andExpect(jsonPath("$.content.[*].available").value(true))
                .andExpect(jsonPath("$.numberOfElements").value(1))
                .andDo(print());
    }

    @Test
    @Order(15)
    void getByHalfNameOk() throws Exception {
        PageRequestDto pageRequestDto = new PageRequestDto(0,1);
        mockMvc.perform(MockMvcRequestBuilders.get("/product/get-name/Bana")
                        .content(objectMapper.writeValueAsString(pageRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.content.[*].code").value("1234567890"))
                .andExpect(jsonPath("$.content.[*].name").value("Banana"))
                .andExpect(jsonPath("$.content.[*].priceHrk").value(1.0))
                .andExpect(jsonPath("$.content.[*].description").value("Zuta"))
                .andExpect(jsonPath("$.content.[*].available").value(true))
                .andExpect(jsonPath("$.numberOfElements").value(1))
                .andDo(print());
    }

    @Test
    @Order(16)
    void getByNoResultOk() throws Exception {
        PageRequestDto pageRequestDto = new PageRequestDto(0,1);
        mockMvc.perform(MockMvcRequestBuilders.get("/product/get-name/koka")
                        .content(objectMapper.writeValueAsString(pageRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.numberOfElements").value(0))
                .andDo(print());
    }

    @Test
    @Order(17)
    void getBadLimit() throws Exception {
        PageRequestDto pageRequestDto = new PageRequestDto(0,0);
        mockMvc.perform(MockMvcRequestBuilders.get("/product/get-name/koka")
                        .content(objectMapper.writeValueAsString(pageRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Limit must not be less than one!"))
                .andDo(print());
    }

    @Test
    @Order(18)
    void deleteById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/product/delete-id/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Product successfully deleted."))
                .andDo(print());
    }

    @Test
    @Order(19)
    void deleteByIdNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/product/delete-id/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Product does not exist."))
                .andDo(print());
    }

    @Test
    @Order(19)
    void deleteByBadId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/product/delete-id/0")
                        .characterEncoding(StandardCharsets.UTF_8)
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Please give valid ID."))
                .andDo(print());
    }


    @Test
    @Order(20)
    void deleteByCode() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/product/delete-code/0123456789")
                        .characterEncoding(StandardCharsets.UTF_8)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Product successfully deleted"))
                .andDo(print());
    }


    @Test
    @Order(21)
    void deleteByCodeNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/product/delete-code/0123456789")
                        .characterEncoding(StandardCharsets.UTF_8)
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Please specify product code for existing product."))
                .andDo(print());
    }

    @Test
    @Order(22)
    void deleteByBadCode() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/product/delete-code/0")
                        .characterEncoding(StandardCharsets.UTF_8)
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Please specify valid product code."))
                .andDo(print());
    }


    @Test
    @Order(23)
    void saveNull() throws Exception {
        RequestProductDto productDto = null;
        mockMvc.perform(MockMvcRequestBuilders.post("/product/save")
                        .content(objectMapper.writeValueAsString(productDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Body not readable."))
                .andDo(print());
    }

    @Test
    @Order(24)
    void updateNull() throws Exception {
        RequestProductDto productDto = null;
        mockMvc.perform(MockMvcRequestBuilders.put("/product/update/1")
                        .content(objectMapper.writeValueAsString(productDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Body not readable."))
                .andDo(print());
    }

}
