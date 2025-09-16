package org.example;

import Files.Payload;
import Files.ReUsableMethods;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class DynamicJson {

    @Test(dataProvider = "BooksData")
    public void addBook(String isbn, String aisle){

        RestAssured.baseURI="http://216.10.245.166";
        String response=given().header("Content-Type","application/json")
                .body(Payload.AddBook(isbn,aisle)).log().all()
                .when().post("Library/Addbook.php")
                .then().log().all().assertThat().statusCode(200).extract().response().asString();

        JsonPath js= ReUsableMethods.rawToJson(response);
        String ID=js.get("ID");
        System.out.println(ID);
    }

    @DataProvider(name="BooksData")
    public Object[][] addData(){

        return new Object[][] {{"adsd","4322"},{"hdlskjfk","7983"},{"kkdjfs","943"},{"kdhflks","3232"}};
    }


}
