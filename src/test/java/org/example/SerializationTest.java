package org.example;

import Pojos.AddLocation;
import Pojos.Location;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.*;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.*;

public class SerializationTest {
    public static void main(String[] args) {
        AddLocation ad= new AddLocation();
        ad.setAccuracy(50);
        ad.setName("Frontline house");
        ad.setPhone_number("(+91) 983 893 3937");
        ad.setAddress("29, side layout, cohen 09");
        ad.setWebsite("http://google.com");
        ad.setLanguage("French-IN");

        List<String> types=new ArrayList<String>();
        types.add("shoe park");
        types.add("shop");

        ad.setTypes(types);

        Location l= new Location();
        l.setLat(-38.383494);
        l.setLng(33.427362);

        ad.setLocation(l);

        RequestSpecification req= new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").addQueryParam("key","qaclick123")
                .setContentType(ContentType.JSON).build();

        ResponseSpecification res= new ResponseSpecBuilder().expectContentType(ContentType.JSON).expectStatusCode(200).build();

        RequestSpecification request=given().spec(req).body(ad);

        Response response= request.when().post("/maps/api/place/add/json").
                then().spec(res).extract().response();

        String responseString=response.asString();
        System.out.println(responseString);

    }
}
