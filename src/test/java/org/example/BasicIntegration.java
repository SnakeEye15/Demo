package org.example;

import Files.ReUsableMethods;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import Files.Payload;
import org.testng.Assert;

public class BasicIntegration {
    //Add place -> Update Place -> Get updated address

    //Using given, when, then

    public static void main(String args[]) {
        RestAssured.baseURI="https://rahulshettyacademy.com";
        String response= given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
                .body(Payload.AddPlace())
                .when().post("/maps/api/place/add/json")
                .then().assertThat().statusCode(200).body("scope", equalTo("APP"))
                .header("server","Apache/2.4.52 (Ubuntu)").extract().response().asString();


        System.out.println(response);
        JsonPath js=ReUsableMethods.rawToJson(response); //for parsing Json
        String place_id=js.get("place_id");

        System.out.println(place_id);

        //Update Place
        String newAdress="VPO. Bhainswan Khurd";
        given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
                .body("{\n"
                        + "\"place_id\":\""+place_id+"\",\n"
                        + "\"address\":\""+newAdress+"\",\n"
                        + "\"key\":\"qaclick123\"\n"
                        + "}")
                .when().put("/maps/api/place/update/json")
                .then().assertThat().log().all().statusCode(200).body("msg", equalTo("Address successfully updated"));


        //Get Place

        String response2=given().log().all().queryParam("key", "qaclick123").queryParam("place_id", place_id)
                .when().get("/maps/api/place/update/json")
                .then().assertThat().statusCode(200).extract().response().asString();


        JsonPath js1= ReUsableMethods.rawToJson(response2);
        String getAddress= js1.getString("address");
        System.out.println(getAddress);

        Assert.assertEquals(getAddress,newAdress);


    }

}
