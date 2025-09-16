package org.example;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import java.io.File;

import static io.restassured.RestAssured.*;
public class BaseTest {
    public static void main(String[] args) {

        //Creating issue in Jira using Rest Assured


        RestAssured.baseURI="https://sainidheeraj913.atlassian.net/";

        String response= given().header("Content-Type","application/json")
                .header("Authorization","Basic c2FpbmlkaGVlcmFqOTEzQGdtYWlsLmNvbTpBVEFUVDN4RmZHRjBLT0tHeXZ1TjRQdkFiM01TWHhpcWpvSHR6T1VFMS16NHdNUUh3RVBkSDA3TjdGRlhNZndxMVlZZWV3UDI0ZEl0Wk5tOXZiWlNPLWhTbDBTQlZqUXJXXzRCcEFCbzNMbGowT2h4QzAzQ2JmNVVSeHpXY1pLTERxRWt5aktiX0lxbnRDUjhwa2dwei11V2NuOUlPU0pqNEM3ZkVjM2FZNkphaENpa0xRNjRqSms9QTE4QjE0Q0M=")
                .body("{\n" +
                        "    \"fields\": {\n" +
                        "       \"project\":\n" +
                        "       {\n" +
                        "          \"key\": \"SAINI913\"\n" +
                        "       },\n" +
                        "       \"summary\": \"This is created from Rest Assured- Demo issue\",\n" +
                        "       \"issuetype\": {\n" +
                        "          \"name\": \"Bug\"\n" +
                        "       }\n" +
                        "   }\n" +
                        "}").log().all()
                .when().post("rest/api/3/issue")
                .then().assertThat().statusCode(201).extract().response().asString();


        JsonPath js= new JsonPath(response);
        String issueId=js.getString("id");


        //adding attachments to newly created bug

        given().pathParams("key",issueId)
                .header("Authorization","Basic c2FpbmlkaGVlcmFqOTEzQGdtYWlsLmNvbTpBVEFUVDN4RmZHRjBLT0tHeXZ1TjRQdkFiM01TWHhpcWpvSHR6T1VFMS16NHdNUUh3RVBkSDA3TjdGRlhNZndxMVlZZWV3UDI0ZEl0Wk5tOXZiWlNPLWhTbDBTQlZqUXJXXzRCcEFCbzNMbGowT2h4QzAzQ2JmNVVSeHpXY1pLTERxRWt5aktiX0lxbnRDUjhwa2dwei11V2NuOUlPU0pqNEM3ZkVjM2FZNkphaENpa0xRNjRqSms9QTE4QjE0Q0M=")
                .header("X-Atlassian-Token","no-check")
                .multiPart("file",new File("/Users/dheerajv/Desktop/Demo_screenshot.png"))
                .when().post("rest/api/3/issue/{key}/attachments")
                .then().assertThat().statusCode(200);
    }
}
