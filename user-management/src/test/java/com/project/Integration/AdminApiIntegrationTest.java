package com.project.Integration;

import static io.restassured.RestAssured.given;
import io.restassured.path.json.JsonPath;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
    void getAllUsers() {
        String response_case_1 = given().
                header("Authorization", "Bearer " + jwtToken)
                .queryParam("")
                .when()
                .get("http://localhost:8081/api/admin/all")
                .then()
                .statusCode(200)
                .extract().asString();
        String response_case_2 = given().
                header("Authorization", "Bearer " + jwtToken)
                .queryParam("email","admin@admin.com")
                .when()
                .get("http://localhost:8081/api/admin/all")
                .then()
                .statusCode(200)
                .extract().asString();
        String response_case_3 = given().
                header("Authorization", "Bearer " + jwtToken)
                .queryParam("email","banyamanohg@gmail.com").
                queryParam("amount",1001600)
                .when()
                .get("http://localhost:8081/api/admin/all")
                .then()
                .statusCode(200)
                .extract().asString();
        String response_case_4 = given().
                header("Authorization", "Bearer " + jwtToken).
                queryParam("amount",1001600)
                .when()
                .get("http://localhost:8081/api/admin/all")
                .then()
                .statusCode(200)
                .extract().asString();
    }
}
