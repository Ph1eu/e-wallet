package com.project.Integration;

import io.restassured.path.json.JsonPath;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class AdminApiIntegrationTest {
    private String jwtToken;

    public String retrieveToken() {
        String authorizationHeader = given()
                .baseUri("http://localhost:8081")
                .contentType("application/json")
                .body("{\"username\":\"admin\",\"password\":\"admin\"}")
                .when()
                .post("/api/auth/signin")
                .then()
                .statusCode(200)
                .extract()
                .header("Authorization");

        // Extract the token by removing the "Bearer " prefix
        String jwtToken = authorizationHeader.replace("Bearer ", "");

        System.out.println("JWT Token: " + jwtToken);
        return jwtToken;
    }
    @BeforeEach
    public void setUp() {
        // Retrieve the JWT token and store it in the instance variable
        this.jwtToken = retrieveToken();
    }
    @Test
    void getHistoryofUser(){
        String response_case_1 =given()
                .header("Authorization", "Bearer " + jwtToken)
                .queryParam("username", "hieuhg123456")
                .when()
                .get("http://localhost:8082/api/admin/history")
                .then()
                .statusCode(200)
                .extract().asString();
        JsonPath jsonPath_1 = new JsonPath(response_case_1);
        List<Map<String, ?>> dataArray_1 = jsonPath_1.getList("data");

        Assert.assertEquals(dataArray_1.size(),7);

        String response_case_2 =given()
                .header("Authorization", "Bearer " + jwtToken)
                .queryParam("username", "hieuhg123456")
                .queryParam("page",0).
                queryParam("size",3)
                .when()
                .get("http://localhost:8082/api/admin/history")
                .then()
                .statusCode(200)
                .extract().asString();
        JsonPath jsonPath_2 = new JsonPath(response_case_2);
        List<Map<String, ?>> dataArray_2 = jsonPath_2.getList("data");
        Assert.assertEquals(dataArray_2.size(),3);

    }
}
