package test;

import base.BaseTest;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.TransferRequest;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.lessThan;

public class TransferTest extends BaseTest {


    //Transfer kocurme pozıtıve nqatıv testler

    private int hesabA; //pulu gonderen
    private int hesabB;  // pulu alan

    /*
    bu metod tokenle ısleyır ona gorede Base Test auth  ondan sonra ıslemeldıır
    TestNg bu ardıcılıgı ozu tenzımleyır
     */

    @BeforeClass(alwaysRun = true,dependsOnMethods = "authenticate")
    public void hesablarıHazırla(){
        Response hesab = given().spec(authSpec(userToken))
                .when()
                .get("/accounts");
        hesab.then().statusCode(200);

        List<Map<String, Objects>> siyahi = hesab.jsonPath().getList("hesaba");

        Assert.assertTrue(siyahi.size() >=2);

        hesabA = hesab.jsonPath().getInt("[0].id");
        hesabB = hesab.jsonPath().getInt("[1].id");

        System.out.println("hesabA: "+hesabA +" hesabB: "+hesabB);





        //Musbet senarı kocurmeden sonra  balans kocurulen qeder azalmalıdır

        @Test(groups = {"smoke","transfer"},description = "Balans kocurulen mebleg geder azalmalıdır")

                public void balansDuzgunAzalır(){
            double evvel = balansOxu(hesabA);
            double mebleg = 10.0;

            System.out.println("Koxurmeden oncekı balans deyeri: " + evvel);

            // 1 Kocurme
        given().spec(authSpec(userToken))
                .body(TransferRequest.of(hesabA,hesabB,mebleg))
                .when()
                .post("/transfer")
                .then()
                .statusCode(200)
                .time(lessThan(MAX_TIME_MS));


        //2 tesdiqleme
        double sonra =balansOxu(hesabA);
            System.out.println("Kecurmede sonrakı balans: "+ sonra);

            //0.01= delta double muqayısesınde mutleq qoyulmalıdır
            Assert.assertEquals(sonra,evvel-mebleg,0.01,
                    "Balans Duzgun Deyısmedı" +
                            "Evvel: "+ evvel +
                             "Sonra: " + sonra +
                             "Köçürülen: "+ mebleg +
                            "Gözlenilen: "+ (evvel-mebleg));



        }
    }




}
