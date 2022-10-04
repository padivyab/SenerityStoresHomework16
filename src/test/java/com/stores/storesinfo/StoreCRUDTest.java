package com.stores.storesinfo;

import com.stores.constants.EndPoints;
import com.stores.model.StoresPojo;
import com.stores.testbase.TestBase;
import com.stores.utils.TestUtils;
import io.restassured.http.ContentType;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Title;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;

import static net.serenitybdd.rest.RestRequests.given;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasValue;

public class StoreCRUDTest extends TestBase {

    static String name = "di" + TestUtils.getRandomValue();
    static String type = "box" + TestUtils.getRandomValue();
    static String address = "ce23" + TestUtils.getRandomValue();

    static String address2 = "";
    static String city = "na" + TestUtils.getRandomValue();
    static String state = "MN";
    static String zip = "55303";
    static float lat = 44.969655f;
    static float lag = -93.449539f;

    static int storeId;

    @Title("This will create a new student")
    @Test
    public void test001()
    {
        StoresPojo storesPojo =new StoresPojo();
        storesPojo.setName(name);
        storesPojo.setType(type);
        storesPojo.setAddress(address);
        storesPojo.setAddress2(address2);
        storesPojo.setCity(city);
        storesPojo.setState(state);
        storesPojo.setZip(zip);
        storesPojo.setLat(lat);
        storesPojo.setLng(lag);
        SerenityRest.given().log().all()
                .contentType(ContentType.JSON)
                .body(storesPojo)
                .when()
                .post()
                .then().log().all().statusCode(201);

    }
    @Title("Verify if student was created")
    @Test
    public void test002()
    {
        String s1 = "data.findAll{it.name='";
        String s2 = "'}.get(0)";

        HashMap<String,Object> storeMap = SerenityRest.given().log().all()
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .path(s1+name+s2);
        Assert.assertThat(storeMap, hasValue(name));
        storeId = (int) storeMap.get("id");



    }

    @Title("Update the user and verify the updated information")
    @Test
    public void test003()
    {
        name = name+"update";

        StoresPojo storesPojo =new StoresPojo();
        storesPojo.setName(name);
        storesPojo.setType(type);
        storesPojo.setAddress(address);
        storesPojo.setAddress2(address2);
        storesPojo.setCity(city);
        storesPojo.setState(state);
        storesPojo.setZip(zip);
        storesPojo.setLat(lat);
        storesPojo.setLng(lag);

        SerenityRest.given().log().all()
                .contentType(ContentType.JSON)
                //.header("Content-Type","application/json; charset=UTF-8")
                .pathParam("storeId",storeId)
                .body(storesPojo)
                .when()
                .put(EndPoints.UPDATE_STORES_BY_ID)
                .then().log().all().statusCode(200);

        String s1 = "data.findAll{it.name='";
        String s2 = "'}.get(0)";

        HashMap<String,Object> storeMap =SerenityRest.given().log().all()
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .path(s1+name+s2);
        Assert.assertThat(storeMap, hasValue(name));

    }

    @Title("Delete the student and verify if the student is deleted")
    @Test
    public void test004()
    {
        SerenityRest.given()
                .pathParam("storeId",storeId)
                .when()
                .delete(EndPoints.DELETE_STORES_BY_ID)
                .then()
                .statusCode(200);
        given().log().all()
                .pathParam("storeId",storeId)
                .when()
                .get()
                .then()
                .statusCode(404);

    }

}
