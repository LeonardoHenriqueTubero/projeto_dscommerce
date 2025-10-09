package com.leonardo.dscommerce.controllers.it;

import com.leonardo.dscommerce.tests.TokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class OrderControllerIT {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private TokenUtil tokenUtil;

    private String productName, adminUsername, clientUsername,
            adminPassword, clientPassword, adminBearerToken, clientBearerToken;
    private Long clientOrderId, otherClientOrderId, nonExistingOrderId;

    @BeforeEach
    void setUp() throws Exception {
        adminUsername = "alex@gmail.com";
        adminPassword = "123456";

        clientUsername = "maria@gmail.com";
        clientPassword = "123456";

        adminBearerToken = tokenUtil.obtainAccessToken(mvc, adminUsername, adminPassword);
        clientBearerToken = tokenUtil.obtainAccessToken(mvc, clientUsername, clientPassword);

        clientOrderId = 1L;
        otherClientOrderId = 2L;
        nonExistingOrderId = 100L;
    }

    @Test
    void findByIdReturnsExistingOrderDTOWhenExistingOrderAndAdminLogged() throws Exception {
        String status = "PAID";
        String moment = "2022-07-25T13:00:00Z";
        Double total = 1431.0;

        mvc.perform(
                get("/orders/{id}", clientOrderId)
                        .header("Authorization", "Bearer " + adminBearerToken)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk(),
                jsonPath("$.id").value(clientOrderId),
                jsonPath("$.status").value(status),
                jsonPath("$.moment").value(moment),
                jsonPath("$.client").exists(),
                jsonPath("$.payment").exists(),
                jsonPath("$.items").exists(),
                jsonPath("$.total").value(total)
        );
    }

    @Test
    void findByIdReturnsExistingOrderDTOWhenExistingOrderAndClientLoggedAndOrderBelongsToClient() throws Exception {
        String status = "PAID";
        String moment = "2022-07-25T13:00:00Z";
        Double total = 1431.0;

        mvc.perform(
                get("/orders/{id}", clientOrderId)
                        .header("Authorization", "Bearer " + clientBearerToken)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk(),
                jsonPath("$.id").value(clientOrderId),
                jsonPath("$.status").value(status),
                jsonPath("$.moment").value(moment),
                jsonPath("$.client").exists(),
                jsonPath("$.payment").exists(),
                jsonPath("$.items").exists(),
                jsonPath("$.total").value(total)
        );
    }

    @Test
    void findByIdReturns403WhenExistingOrderAndClientLoggedAndOrderDoesNotBelongToClient() throws Exception {
        mvc.perform(
                get("/orders/{id}", otherClientOrderId)
                        .header("Authorization", "Bearer " + clientBearerToken)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isForbidden()
        );
    }

    @Test
    void findByIdReturns404AdminLoggedAndOrderDoesNotExist() throws Exception {
        mvc.perform(
                get("/orders/{id}", nonExistingOrderId)
                        .header("Authorization", "Bearer " + adminBearerToken)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isNotFound()
        );
    }

    @Test
    void findByIdReturns404ClientLoggedAndOrderDoesNotExist() throws Exception {
        mvc.perform(
                get("/orders/{id}", nonExistingOrderId)
                        .header("Authorization", "Bearer " + clientBearerToken)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isNotFound()
        );
    }

    @Test
    void findByIdReturns401WhenOrderDoesExistAndNoUserLogged() throws Exception {
        mvc.perform(
                get("/orders/{id}", clientOrderId)
                        .header("Authorization", "Bearer ")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isUnauthorized()
        );
    }
}
