package org.example;
import Pojos.GetCourses;
import Pojos.WebAutomation;
import Pojos.api;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;

public class oAuth {

    public static void main(String[] args) {
        String response=given().formParams("client_id","692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
                .formParams("client_secret","erZOWM9g3UtwNRj340YYaK_W")
                .formParams("grant_type","client_credentials")
                .formParams("scope","trust").
                when().log().all().post("https://rahulshettyacademy.com/oauthapi/oauth2/resourceOwner/token").then().extract().asString();

        JsonPath js= new JsonPath(response);
        String accessToken = js.getString("access_token");


        //now getting course details

        Pojos.GetCourses gc  =given().queryParam("access_token",accessToken)
                .when().get("https://rahulshettyacademy.com/oauthapi/getCourseDetails").as(GetCourses.class);


        System.out.println(gc.getInstructor());
        System.out.println(gc.getLinkedIn());

        //we have to find out the price of course title "SoapUI Webservices Testing" under api course

        List<api> apiCourse=gc.getCourses().getApi();

        for(int i=0;i<apiCourse.size();i++){
            if(apiCourse.get(i).getCourseTitle().equalsIgnoreCase("SoapUI Webservices Testing")){
                System.out.println(apiCourse.get(i).getCourseTitle());
                System.out.println(apiCourse.get(i).getPrice());
            }
        }

        //we have to print the all the course titles present in webAutomation course

        List<WebAutomation> webAutomationCourses= gc.getCourses().getWebAutomation();

        for(int i=0;i<webAutomationCourses.size();i++){
            System.out.println(webAutomationCourses.get(i).getCourseTitle());
        }

        String [] old= {"Selenium Webdriver Java","Cypress","Protractor"};
        //Let's create for dynamic number of courses in web automation.
        ArrayList<String> actual =new ArrayList<>();

        for(int i=0;i<webAutomationCourses.size();i++){
            actual.add(webAutomationCourses.get(i).getCourseTitle());
        }

        List<String> expectedList= Arrays.asList(old);

        Assert.assertEquals(expectedList,actual);


    }
}
