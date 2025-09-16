package Pojos;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class SpecBuilderTest {
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

        RequestSpecification request= new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").addQueryParam("Key","qaclick123").setContentType(ContentType.JSON).build();
        ResponseSpecification res= new ResponseSpecBuilder().expectStatusCode(200).expectContentType(ContentType.JSON).build();


        RequestSpecification req=given().spec(request);

        Response response= req. body(ad)
                .when().post("/maps/api/place/add/json").
                then().spec(res).extract().response();

        String responseString=response.asString();
        System.out.println(responseString);

    }
}
