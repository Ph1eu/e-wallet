package com.project.integration;

import io.restassured.path.json.JsonPath;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
class UserApiIntegrationTest {
    private String jwtToken;

    public String retrieveToken() {
        String authorizationHeader = given()
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
    @BeforeEach
    public void setUp() {
        // Retrieve the JWT token and store it in the instance variable
         this.jwtToken = retrieveToken();
    }
    @Test
    void getOnlineBalance() {
    }

    @Test
    void depositMoney() {

        String response =given()
                .header("Authorization", "Bearer " + jwtToken)
                .pathParam("username", "hieuhg123456")
                .queryParam("amount", 10000)
                .when()
                .get("http://localhost:8082/api/user/{username}/deposit")
                .then()
                .statusCode(200)
                .extract().asString();

        JsonPath jsonPath = new JsonPath(response);
        List<Map<String, ?>> dataArray = jsonPath.getList("data");
        int amount = (int) dataArray.get(0).get("amount");
        String senderid = (String) dataArray.get(0).get("senderid");


        Assert.assertEquals(amount, 10000);
        Assert.assertEquals(senderid, "Hieupmbi11-090@gmail.com");

    }
    @Test
    void withdrawalMoney() {
        String response = given()
                .header("Authorization", "Bearer " + jwtToken)
                .pathParam("username", "hieuhg123456")
                .queryParam("amount", 100)
                .when()
                .get("http://localhost:8082/api/user/{username}/withdrawal")
                .then()
                .statusCode(200)
                .extract().asString();

        JsonPath jsonPath = new JsonPath(response);
        List<Map<String, ?>> dataArray = jsonPath.getList("data");
        int amount = (int) dataArray.get(0).get("amount");
        String senderId = (String) dataArray.get(0).get("senderid");

        Assert.assertEquals(amount, 100);
        Assert.assertEquals(senderId, "Hieupmbi11-090@gmail.com");
        List<Map<String, ?>> linkArray = jsonPath.getList("link");

        Assert.assertEquals(linkArray.size(), 4);
        Assert.assertEquals(linkArray.get(0).get("rel"), "self");
        Assert.assertEquals(linkArray.get(1).get("rel"), "deposit money to wallet example : 10000");
        Assert.assertEquals(linkArray.get(2).get("rel"), "withdrawal money to wallet example : 10000");
        Assert.assertEquals(linkArray.get(3).get("rel"), "transfer money to someone example : 10000");
    }

    @Test
    void transferMoney() {
        String response = given()
                .header("Authorization", "Bearer " + jwtToken)
                .pathParam("username", "hieuhg123456")
                .queryParam("amount", 100)
                .queryParam("phone", "0372771999")
                .when()
                .get("http://localhost:8082/api/user/{username}/transfer")
                .then()
                .statusCode(200)
                .extract().asString();

        JsonPath jsonPath = new JsonPath(response);
        List<Map<String, ?>> dataArray = jsonPath.getList("data");
        int amount = (int) dataArray.get(0).get("amount");
        String senderId = (String) dataArray.get(0).get("senderid");
        String recipientId = (String) dataArray.get(0).get("recipientid");

        Assert.assertEquals(amount, 100);
        Assert.assertEquals(senderId, "Hieupmbi11-090@gmail.com");
        Assert.assertEquals(recipientId, "banyamanohg@gmail.com");

        List<Map<String, ?>> linkArray = jsonPath.getList("link");
        // Asserting the "link" section
        Assert.assertEquals(linkArray.size(), 4);
        Assert.assertEquals(linkArray.get(0).get("rel"), "self");
        Assert.assertEquals(linkArray.get(1).get("rel"), "deposit money to wallet example : 10000");
        Assert.assertEquals(linkArray.get(2).get("rel"), "withdrawal money to wallet example : 10000");
        Assert.assertEquals(linkArray.get(3).get("rel"), "transfer money to someone example : 10000");
    }

    @Test
    void getHistory() {
        String response = given()
                .header("Authorization", "Bearer " + jwtToken)
                .when()
                .get("http://localhost:8082/api/user/hieuhg123456/balance/history")
                .then()
                .statusCode(200)
                .extract().asString();

        JsonPath jsonPath = new JsonPath(response);


        List<Map<String, ?>> linkArray = jsonPath.getList("link");

        // Asserting the "link" section
        Assert.assertEquals(linkArray.size(), 1);
        Assert.assertEquals(linkArray.get(0).get("rel"), "self");


        // Asserting the "next" link URL
        String nextLinkUrl = (String) linkArray.get(0).get("href");
        Assert.assertEquals(nextLinkUrl, "http://localhost:8082/api/user/hieuhg123456/balance/history");
    }
}