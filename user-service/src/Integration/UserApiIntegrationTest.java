package com.project.Integration;
import io.restassured.path.json.JsonPath;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class UserApiIntegrationTest {
    public String retrieveToken() {
        String authorizationHeader = RestAssured.given()
                .baseUri("http://localhost:8081")
                .contentType("application/json")
                .body("{\"username\":\"hieuhg123456\",\"password\":\"hieuhg123456\"}")
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
}
