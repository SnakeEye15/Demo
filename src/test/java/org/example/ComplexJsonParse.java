package org.example;

import Files.Payload;
import groovy.json.JsonOutput;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;

public class ComplexJsonParse {

    public static void main(String args[]){
        JsonPath js = new JsonPath(Payload.CoursePrice());
    /***
     * 1. Print No of courses returned by API
     *
     * 2.Print Purchase Amount
     *
     * 3. Print Title of the first course
     *
     * 4. Print All course titles and their respective Prices
     *
     * 5. Print no of copies sold by RPA Course
     *
     * 6. Verify if Sum of all Course prices matches with Purchase Amount
     */


        //Print No of courses returned by API
        int count = js.getInt("courses.size()");
        System.out.println(count);

        //Print Purchase Amount
        int amount= js.getInt("dashboard.purchaseAmount");
        System.out.println(amount);

        //Print Title of the first course
        String firstTitle=js.getString("courses[0].title");
        System.out.println(firstTitle);

        //Print All course titles and their respective Prices

        for(int i=0;i<count;i++){
            String courseTitles=js.getString("courses["+i+"].title");
            int price=js.getInt("courses["+i+"].price");
            System.out.println(courseTitles+" : "+price);
        }

        //Print no of copies sold by RPA Course
        for(int i=0;i<count;i++){
            String courseTitles=js.getString("courses["+i+"].title");
            if(courseTitles.equalsIgnoreCase("RPA")){
                int counts=js.getInt("courses["+i+"].copies");
                System.out.println(counts);
                break;
            }
        }

        //Verify if Sum of all Course prices matches with Purchase Amount
        int totalAmount=0;
        for(int i=0;i<count;i++){
            int prices=js.getInt("courses["+i+"].price");
            int copies=js.getInt("courses["+i+"].copies");
            int amounts=prices*copies;
            totalAmount+=amounts;

        }
        System.out.println(totalAmount);
        Assert.assertEquals(amount,totalAmount);

}

}
