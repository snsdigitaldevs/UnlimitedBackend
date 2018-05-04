package com.simonschuster.pimsleur.unlimited.product;

import com.google.common.collect.ImmutableList;
import com.simonschuster.pimsleur.unlimited.BaseApiTest;
import io.restassured.http.ContentType;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

public class ProductApiTest extends BaseApiTest {

    @Test
    public void should_get_product_info_successfully_when_prodcut_is_pu_kitted() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/productInfo?isPUProductCode=true&productCode=9781508231516&sub=auth0|5ab1728e1d2fb71e499dde01")
                .then()
                .log().all()
                .assertThat()
                .body("productCode", is(ImmutableList.of("9781508231509", "9781442350403", "9781442350380", "9781442350397")))
                .body("courseName", is(ImmutableList.of("Pimsleur Spanish Unlimited Level 1", "Pimsleur Spanish Unlimited Level 4",
                        "Pimsleur Spanish Unlimited Level 2", "Pimsleur Spanish Unlimited Level 3")));
    }

    @Test
    public void should_get_product_info_successfully_when_product_is_pu_single() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/productInfo?isPUProductCode=true&productCode=9781508231431&sub=auth0|5ab1728e1d2fb71e499dde01")
                .then()
                .log().all()
                .assertThat()
                .body("productCode", is(ImmutableList.of("9781508231431")));
    }

    @Test
    public void should_get_product_info_successfully_when_product_is_pcm_kitted() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/productInfo?isPUProductCode=false&productCode=9781508205333&sub=auth0|5ab1728e1d2fb71e499dde01")
                .then()
                .log().all()
                .assertThat()
                .body("productCode", is(ImmutableList.of("9781442307674", "9781442308046", "9781442308411", "9781442367852", "9781442381834")))
                .body("courseName", is(ImmutableList.of("French Level 1, Lessons 1-30", "French Level 2, Lessons 1-30",
                        "French Level 3, Lessons 1-30", "French Level 4, Lessons 1-30",
                        "French Level 5, Lessons 1-30")));
    }

    @Test
    public void should_get_product_info_successfully_when_product_is_pcm_single() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/productInfo?isPUProductCode=false&productCode=9781442307674&sub=auth0|5ab1728e1d2fb71e499dde01")
                .then()
                .log().all()
                .assertThat()
                .body("productCode", is(ImmutableList.of("9781442307674")))
                .body("courseName", is(ImmutableList.of("French Level 1, Lessons 1-30")));
    }

    @Test
    public void should_get_product_info_successfully_when_product_is_pu_free_lesson() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/productInfo?isPUProductCode=true&productCode=9781508243328&sub=auth0|5ab1728e1d2fb71e499dde01")
                .then()
                .log().all();
    }
}
