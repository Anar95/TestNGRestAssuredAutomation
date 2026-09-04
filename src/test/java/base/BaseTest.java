package base;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.apache.http.client.methods.RequestBuilder;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import utils.TokenManager;

public abstract class BaseTest {

    // Uı da  @BeforMethod -> brauzerı acırdı / AfterMethod bagla
    //BeforSuit - base url+ log
    // BeforClass  token almaq


    // Api ın esas unvanı.
    public static final  String BASE_URI =
            System.getProperty("baseUri","https://api.anarabbas.com");


    // Cavab müddet üçün yuxarı hedd
    public  static final long MAX_TIME_MS = 3000L;

    // Test istifadeçileri

    public  static final String ADMIN_EMAIL= "admin@test.com";
    public  static final String ADMIN_PASSWORD= "Admin1234" ;
    public  static final String USER_EMAIL= "user1@test.com";
    public  static final String USER_PASSWORD= "User1234" ;

    // Tokenler @BeforClass- da doldurulur

    protected String adminToken;
    protected String userToken;

// Bütün suitde bir defe istifade olunacaq
    @BeforeSuite(alwaysRun = true)
    public void globalSetup() {
        RestAssured.baseURI = BASE_URI;

        //Test fail tam sorgu + cavabı konsola yazsın
        // Uğurlu testlerde susur- loglar temiz

        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        System.out.println("---------------------------------------------");
        System.out.println("Apı auto - base URI: " + RestAssured.baseURI);

    }

    //her class ucun bır defe
    @BeforeClass(alwaysRun = true)
    public void authenticate() {
        adminToken = TokenManager.adminToken();
        userToken = TokenManager.userToken();
    }

    // Hazır sorgu sablonları

    // Tokensız sorgular ucun sablon
    protected RequestSpecification publicSpec(){
        return new RequestSpecBuilder()
                .setBaseUri(BASE_URI)
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .build();
    }

    // tokenlı sorgular ucun sablon

    protected RequestSpecification authSpec(String token){
        return new RequestSpecBuilder()
                .setBaseUri(BASE_URI)
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addHeader("Authorization","Bearer "+token)
                .build();
    }





}
