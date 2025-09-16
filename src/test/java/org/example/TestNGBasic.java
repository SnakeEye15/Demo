package org.example;

import Pojos.Order;
import Pojos.OrderDetails;
import Pojos.RequestLogin;
import Pojos.ResponseLogin;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class TestNGBasic {

    private static String token;
    private static String userId;
    private static String productId;

    private RequestSpecification baseReq;

    @BeforeClass
    public void setup() {
        baseReq = new RequestSpecBuilder()
                .setBaseUri("https://rahulshettyacademy.com")
                .setContentType(ContentType.JSON)
                .build();

        // ========== LOGIN ==========
        RequestLogin login = new RequestLogin();
        login.setUserEmail("sainidheeraj913@gmail.com");
        login.setUserPassword("@Dksharmais1908");

        ResponseLogin res = given().spec(baseReq).body(login)
                .when().post("/api/ecom/auth/login")
                .then().log().all()
                .extract().as(ResponseLogin.class);

        token = res.getToken();
        userId = res.getUserId();

        Assert.assertNotNull(token, "Login failed - token is null");
        Assert.assertNotNull(userId, "Login failed - userId is null");
        System.out.println("✅ Login successful");
    }

    @Test(priority = 1)
    public void testAddProduct() {
        File productImage = new File("src/test/resources/Demo_screenshot.png");
        Assert.assertTrue(productImage.exists(), "Product image file not found!");

        RequestSpecification addProductReq = new RequestSpecBuilder()
                .setBaseUri("https://rahulshettyacademy.com")
                .addHeader("Authorization", token)
                .setContentType("multipart/form-data")
                .build();

        String addProductResponse = given().spec(addProductReq)
                .multiPart("productName", "qwerty")
                .multiPart("productAddedBy", userId)
                .multiPart("productCategory", "fashion")
                .multiPart("productSubCategory", "shirts")
                .multiPart("productPrice", "11500")
                .multiPart("productDescription", "Adidas Originals")
                .multiPart("productFor", "women")
                .multiPart("productImage", productImage)
                .when().post("/api/ecom/product/add-product")
                .then().log().all()
                .extract().asString();

        JsonPath js = new JsonPath(addProductResponse);
        productId = js.getString("productId");
        String message = js.getString("message");

        Assert.assertEquals(message, "Product Added Successfully", "Product not added successfully!");
        Assert.assertNotNull(productId, "Product ID is null after adding product");
        System.out.println("✅ Product added successfully");
    }

    @Test(priority = 2, dependsOnMethods = "testAddProduct")
    public void testCreateOrder() {
        RequestSpecification createOrderReq = new RequestSpecBuilder()
                .setBaseUri("https://rahulshettyacademy.com")
                .addHeader("Authorization", token)
                .setContentType(ContentType.JSON)
                .build();

        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setCountry("India");
        orderDetails.setProductOrderedId(productId);

        List<OrderDetails> orderDetailsList = new ArrayList<>();
        orderDetailsList.add(orderDetails);

        Order order = new Order();
        order.setOrders(orderDetailsList);

        String createResponse = given().spec(createOrderReq).body(order)
                .when().post("/api/ecom/order/create-order")
                .then().log().all()
                .extract().asString();

        JsonPath orderJson = new JsonPath(createResponse);
        String orderMsg = orderJson.getString("message");

        Assert.assertTrue(orderMsg.contains("Order Placed Successfully"), "Order not placed successfully!");
        System.out.println("✅ Order placed successfully");
    }

    @AfterClass
    public void cleanup() {
        if (productId != null) {
            RequestSpecification deleteProductReq = new RequestSpecBuilder()
                    .setBaseUri("https://rahulshettyacademy.com")
                    .setContentType(ContentType.JSON)
                    .addPathParams("productId", productId)
                    .addHeader("Authorization", token)
                    .build();

            String deleteResponse = given().spec(deleteProductReq)
                    .when().delete("/api/ecom/product/delete-product/{productId}")
                    .then().log().all()
                    .extract().asString();

            JsonPath deleteJson = new JsonPath(deleteResponse);
            String deleteMsg = deleteJson.getString("message");

            Assert.assertEquals(deleteMsg, "Product Deleted Successfully", "Product not deleted!");
            System.out.println("✅ Product deleted successfully");
        }
    }
}
