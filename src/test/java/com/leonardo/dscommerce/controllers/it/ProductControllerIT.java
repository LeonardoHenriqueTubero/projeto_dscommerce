package com.leonardo.dscommerce.controllers.it;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leonardo.dscommerce.dto.ProductDTO;
import com.leonardo.dscommerce.entities.Product;
import com.leonardo.dscommerce.tests.ProductFactory;
import com.leonardo.dscommerce.tests.TokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductControllerIT {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private String productName, adminUsername, clientUsername,
            adminPassword, clientPassword, adminBearerToken, clientBearerToken, fieldName, message;
    private Long existingId, nonExistingId, dependentId;

    private Product product;

    @BeforeEach
    void setUp() throws Exception{
        productName = "Macbook";
        product = ProductFactory.createProduct();

        adminUsername = "alex@gmail.com";
        adminPassword = "123456";

        clientUsername = "maria@gmail.com";
        clientPassword = "123456";

        adminBearerToken = tokenUtil.obtainAccessToken(mvc, adminUsername, adminPassword);
        clientBearerToken = tokenUtil.obtainAccessToken(mvc, clientUsername, clientPassword);

        existingId = 4L;
        nonExistingId = 100L;
        dependentId = 1L;
    }

    @Test
    void findAllShouldReturnPageWhenNameParamIsNotEmpty() throws Exception{
        mvc.perform(
                get("/products?name={productName}", productName).accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk(),
                jsonPath("$.content[0].id").value(3L),
                jsonPath("$.content[0].name").value("Macbook Pro"),
                jsonPath("$.content[0].price").value(1250.0),
                jsonPath("$.content[0].imgUrl").value("https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/3-big.jpg")
        );
    }

    @Test
    void findAllShouldReturnPageWhenNameParamIsEmpty() throws Exception{
        mvc.perform(
                get("/products").accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk(),
                jsonPath("$.content[0].id").value(1L),
                jsonPath("$.content[0].name").value("The Lord of the Rings"),
                jsonPath("$.content[0].price").value(90.5),
                jsonPath("$.content[0].imgUrl").value("https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/1-big.jpg")
        );
    }

    @Test
    void insertShouldReturnProductDTOWhenValidDataAndAdminLogged() throws Exception {
        product.setId(null);

        ProductDTO dto = new ProductDTO(product);

        String json = objectMapper.writeValueAsString(dto);

        Long expectedId = 26L;
        String expectedName = dto.getName();

        mvc.perform(
                post("/products")
                        .header("Authorization", "Bearer " + adminBearerToken)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isCreated(),
                jsonPath("$.id").value(expectedId),
                jsonPath("$.name").value(expectedName)
        );
    }

    @Test
    void insertShouldReturn422AndCustomMessageWhenInvalidNameAndAdminLogged() throws Exception {
        product.setId(null);
        product.setName("Me");

        ProductDTO dto = new ProductDTO(product);

        String json = objectMapper.writeValueAsString(dto);
        fieldName = "name";
        message = "Nome precisa ter entre 3 a 80 caracteres";

        mvc.perform(
                post("/products")
                        .header("Authorization", "Bearer " + adminBearerToken)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isUnprocessableEntity(),
                jsonPath("$.errors[0].fieldName").value(fieldName),
                jsonPath("$.errors[0].message").value(message)
        );
    }

    @Test
    void insertShouldReturn422AndCustomMessageWhenInvalidDescriptionAndAdminLogged() throws Exception {
        product.setId(null);
        product.setDescription("Me");

        ProductDTO dto = new ProductDTO(product);

        String json = objectMapper.writeValueAsString(dto);
        fieldName = "description";
        message = "Descrição precisa ter no mínimo 10 caracteres";

        mvc.perform(
                post("/products")
                        .header("Authorization", "Bearer " + adminBearerToken)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isUnprocessableEntity(),
                jsonPath("$.errors[0].fieldName").value(fieldName),
                jsonPath("$.errors[0].message").value(message)
        );
    }

    @Test
    void insertShouldReturn422AndCustomMessageWhenNegativePriceAndAdminLogged() throws Exception {
        product.setId(null);
        product.setPrice(-50.0);

        ProductDTO dto = new ProductDTO(product);

        String json = objectMapper.writeValueAsString(dto);
        fieldName = "price";
        message = "O preço deve ser positivo";

        mvc.perform(
                post("/products")
                        .header("Authorization", "Bearer " + adminBearerToken)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isUnprocessableEntity(),
                jsonPath("$.errors[0].fieldName").value(fieldName),
                jsonPath("$.errors[0].message").value(message)
        );
    }

    @Test
    void insertShouldReturn422AndCustomMessageWhenPriceIsZeroAndAdminLogged() throws Exception {
        product.setId(null);
        product.setPrice(0.0);

        ProductDTO dto = new ProductDTO(product);

        String json = objectMapper.writeValueAsString(dto);
        fieldName = "price";
        message = "O preço deve ser positivo";

        mvc.perform(
                post("/products")
                        .header("Authorization", "Bearer " + adminBearerToken)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isUnprocessableEntity(),
                jsonPath("$.errors[0].fieldName").value(fieldName),
                jsonPath("$.errors[0].message").value(message)
        );
    }

    @Test
    void insertShouldReturn422AndCustomMessageWhenNoCategoryAndAdminLogged() throws Exception {
        product.setId(null);
        product.getCategories().clear();

        ProductDTO dto = new ProductDTO(product);

        String json = objectMapper.writeValueAsString(dto);
        fieldName = "categories";
        message = "Deve ter pelo menos 1 categoria";

        mvc.perform(
                post("/products")
                        .header("Authorization", "Bearer " + adminBearerToken)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isUnprocessableEntity(),
                jsonPath("$.errors[0].fieldName").value(fieldName),
                jsonPath("$.errors[0].message").value(message)
        );
    }

    @Test
    void insertShouldReturn403WhenClientLogged() throws Exception {
        product.setId(null);

        ProductDTO dto = new ProductDTO(product);

        String json = objectMapper.writeValueAsString(dto);

        mvc.perform(
                post("/products")
                        .header("Authorization", "Bearer " + clientBearerToken)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isForbidden()
        );
    }

    @Test
    void insertShouldReturn401WhenNoUserLogged() throws Exception {
        product.setId(null);

        ProductDTO dto = new ProductDTO(product);

        String json = objectMapper.writeValueAsString(dto);

        mvc.perform(
                post("/products")
                        .header("Authorization", "Bearer ")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isUnauthorized()
        );
    }

    @Test
    void deleteShouldReturn204WhenProductExistsAndAdminLogged() throws Exception{
        mvc.perform(
                delete("/products/{id}", existingId)
                        .header("Authorization", "Bearer " + adminBearerToken)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isNoContent()
        );
    }

    @Test
    void deleteShouldReturn404WhenProductDoesNotExistAndAdminLogged() throws Exception{
        mvc.perform(
                delete("/products/{id}", nonExistingId)
                        .header("Authorization", "Bearer " + adminBearerToken)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isNotFound()
        );
    }

    @Test
    @Transactional(propagation = Propagation.SUPPORTS)
    void deleteShouldReturn400WhenDependentProductAndAdminLogged() throws Exception{
        mvc.perform(
                delete("/products/{id}", dependentId)
                        .header("Authorization", "Bearer " + adminBearerToken)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isBadRequest()
        );
    }

    @Test
    void deleteShouldReturn403WhenClientLogged() throws Exception{
        mvc.perform(
                delete("/products/{id}", existingId)
                        .header("Authorization", "Bearer " + clientBearerToken)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isForbidden()
        );
    }

    @Test
    void deleteShouldReturn401WhenNoUserLogged() throws Exception{
        mvc.perform(
                delete("/products/{id}", existingId)
                        .header("Authorization", "Bearer ")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isUnauthorized()
        );
    }
}
