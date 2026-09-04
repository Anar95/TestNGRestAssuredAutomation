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

    private String hesabA; //pulu gonderen
    private String hesabB;  // pulu alan

    /*
    bu metod tokenle ısleyır ona gorede Base Test auth  ondan sonra ıslemeldıır
    TestNg bu ardıcılıgı ozu tenzımleyır
     */

    @BeforeClass(alwaysRun = true,dependsOnMethods = "authenticate")
    public void hesablarıHazırla(){
        Response hesab = given().spec(authSpec(adminToken))
                .when()
                .get("/accounts");
        hesab.then().statusCode(200);

        List<Map<String, Objects>> siyahi = hesab.jsonPath().getList("hesaba");

        Assert.assertTrue(siyahi.size() >=2);

        hesabA = hesab.jsonPath().getString("[0].id");
        hesabB = hesab.jsonPath().getString("[1].id");

        System.out.println("hesabA: "+hesabA +" hesabB: "+hesabB);

    }



        //Musbet senarı kocurmeden sonra  balans kocurulen qeder azalmalıdır

        @Test(groups = {"smoke","transfer"},description = "Balans kocurulen mebleg geder azalmalıdır")

            public void balansDuzgunAzalır(){
            double evvel = balansOxu(hesabA);
            double mebleg = 10.0;

            System.out.println("Kocurmeden oncekı balans deyeri: " + evvel);

            // 1 Kocurme
        given().spec(authSpec(adminToken))
                .body(TransferRequest.of(hesabA,hesabB,mebleg))
        .when()
                .post("/transfers")
        .then()
                .statusCode(201)
                .time(lessThan(MAX_TIME_MS));


        //2 tesdiqleme
        double sonra =balansOxu(hesabA);
            System.out.println("Kecurmede sonrakı balans: "+ sonra);

            //0.01= delta double muqayısesınde mutleq qoyulmalıdır
        // Beraberdırmı "sonrakı mebleg= 969799"   evvel=979799-mebleg=10000
            Assert.assertEquals(sonra,evvel-mebleg,0.01,
                    "Balans Duzgun Deyısmedı " +
                            "Evvel: "+ evvel +
                             "Sonra: " + sonra +
                             "Köçürülen: "+ mebleg +
                            "Gözlenilen: "+ (evvel-mebleg));

     // evvel 10
        // gonderılen 5 ıdıse
        // qalan 5
        // assertıon evvel-gonderılen = galan

        }



    // Hesabın carı balansın qaytarır
    //Balans harda saxlanılır   sualına cavab bu metoddur
    //
    private  double balansOxu(String hesabID){
        Response hesab = given()
                .spec(authSpec(adminToken))
                .pathParams("id",hesabID)
                .when()
                .get("/accounts/{id}");

        hesab.then().statusCode(200);

        Object balance =  hesab.jsonPath().get("balance");
        Assert.assertNotNull(balance,"balans sahesi tapılmadı! ");

        return Double.parseDouble(balance.toString());

    }


    }

