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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.*;

public class ECommerceAPITest {
    public static void main(String[] args) {
        //Login
        RequestSpecification req= new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").setContentType(ContentType.JSON)
                .build();

        RequestLogin login= new RequestLogin();
        login.setUserEmail("sainidheeraj913@gmail.com");
        login.setUserPassword("@Dksharmais1908");

        RequestSpecification request=given().log().all().spec(req).body(login);

        Pojos.ResponseLogin res =request.when().post("/api/ecom/auth/login").then().extract().response().as(ResponseLogin.class);
        System.out.println(res.getToken());
        System.out.println(res.getUserId());
        String userId=res.getUserId();
        String token=res.getToken();

        // ✅ Validation
        Assert.assertNotNull(token, "Login failed - token is null");
        Assert.assertNotNull(userId, "Login failed - userId is null");
        System.out.println("Login successful ✅");


        /***
        //Creating product
        RequestSpecification AddProduct=new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
                .addHeader("Authorization",token)
                .build();

        RequestSpecification requestAddProduct=given().log().all().spec(AddProduct).params("productName","qwerty")
                .params("productAddedBy",userId)
                .params("productCategory","fashion")
                .params("productSubCategory","shirts")
                .params("productPrice","11500")
                .params("productDescription","Addias Originals")
                .params("productFor","women")
                .multiPart("productImage", new File("src/test/resources/Demo_screenshot.png"));

        String AddProductResponse=requestAddProduct.when().post("/api/ecom/product/add-product")
                .then().extract().response().asString();

        JsonPath js= new JsonPath(AddProductResponse);
        String productId = js.getString("productId");
        System.out.println("Product ID: " + productId);
        ***/

        //Create product
        File productImage = new File("src/test/resources/Demo_screenshot.png");
        if (!productImage.exists()) {
            throw new RuntimeException("File not found: " + productImage.getAbsolutePath());
        }

        RequestSpecification AddProduct = new RequestSpecBuilder()
                .setBaseUri("https://rahulshettyacademy.com")
                .addHeader("Authorization", token)
                .setContentType("multipart/form-data")
                .build();

        RequestSpecification requestAddProduct = given().log().all()
                .spec(AddProduct)
                .multiPart("productName", "qwerty")
                .multiPart("productAddedBy", userId)
                .multiPart("productCategory", "fashion")
                .multiPart("productSubCategory", "shirts")
                .multiPart("productPrice", "11500")
                .multiPart("productDescription", "Adidas Originals")
                .multiPart("productFor", "women")
                .multiPart("productImage", productImage);

        String AddProductResponse = requestAddProduct.when()
                .post("/api/ecom/product/add-product")
                .then().log().all()
                .extract().asString();

        JsonPath js = new JsonPath(AddProductResponse);
        String productId = js.getString("productId");
        System.out.println("Product ID: " + productId);
        String productMsg = js.getString("message");

        // ✅ Validation
        Assert.assertEquals(productMsg, "Product Added Successfully", "Product not added successfully!");
        Assert.assertNotNull(productId, "Product ID is null after adding product");
        System.out.println("Product added successfully ✅");


        //Create order
        RequestSpecification createOrder= new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").addHeader("Authorization",token)
                .setContentType(ContentType.JSON).build();
        OrderDetails orderDetails= new OrderDetails();
        orderDetails.setCountry("India");
        orderDetails.setProductOrderedId(productId);

        List<OrderDetails> orderDetailsList= new ArrayList<OrderDetails>();
        orderDetailsList.add(orderDetails);

        Order order= new Order();
        order.setOrders(orderDetailsList);

       RequestSpecification CreateOrders=given().log().all().spec(createOrder).body(order);

        String createResponse= CreateOrders.when().post("/api/ecom/order/create-order")
                .then().log().all().extract().response().asString();

        System.out.println(createResponse);
        JsonPath orderJson = new JsonPath(createResponse);
        String orderMsg = orderJson.getString("message");

        // ✅ Validation
        Assert.assertTrue(orderMsg.contains("Order Placed Successfully"), "Order not placed successfully!");
        System.out.println("Order placed successfully ✅");


        //Delete product

        RequestSpecification DeleteProduct= new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").setContentType(ContentType.JSON)
                .addPathParams("productId",productId)
                .addHeader("Authorization",token)
                .build();

        RequestSpecification deleteProductReq=given().log().all().spec(DeleteProduct);

        String deleteResponse=deleteProductReq.when().delete("/api/ecom/product/delete-product/{productId}")
                .then().log().all().extract().response().asString();

        JsonPath deleteJson = new JsonPath(deleteResponse);
        String deleteMsg = deleteJson.getString("message");

        // ✅ Validation
        Assert.assertEquals(deleteMsg, "Product Deleted Successfully", "Product not deleted!");
        System.out.println("Product deleted successfully ✅");








    }
}
