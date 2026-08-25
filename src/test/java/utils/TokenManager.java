package utils;



// Sessıyada token yaradır
// token hardcode yaradıla bılmez- vaxt bıtır  testler 401
// her testde tokenalmaq sıstemı yorur ve yavasladır
//emaılı kesleyırık 50 testde 1 logın


import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeMethod;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static base.BaseTest.*;

public class TokenManager {

    // adı hashmap paralel muhıtde  data pozulması yaranacaq
    // ConcurrentHashMap - cunkı testler paralel ısleye bıler

    private static final Map<String, String> CACHE = new ConcurrentHashMap<>();

    // Utilityde class - obyect yaradır
    private TokenManager() {}


    // Hazır metodlar
    public static String adminToken (){
        return tokenFor(ADMIN_EMAIL,ADMIN_PASSWORD);
    }


    public static String userToken (){
        return tokenFor(USER_EMAIL,USER_PASSWORD);
    }


    //Keşde varsa onu qaytarır, yoxdursa login olur ve saxlayır

    public static String tokenFor(String email, String password){
        return CACHE.computeIfAbsent(email,key->login(email,password));
    }

    // Kesı temızleyır __ token vaxtı bıtdı senarılerı test ederken lazım olur

    public static void  clearCache(){
        CACHE.clear();
    }

    // Esas mentıq login

    private  static String login(String email, String password){
        Map<String,String> body = new HashMap<>();
        body.put("email",email);
        body.put("password",password);

        Response response = RestAssured
                .given()
                .baseUri(BASE_URI)
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/auth/login");

// Aydın xeta mesajı - saatlarla debuq vaxtı alır. vaxta qenaet ucun
        if (response.statusCode()!=200){
            throw new IllegalStateException(
                    "Login uğursuz oldu!"
                    + " İstifadeçi : " +email
                    + "Status : " +  response.statusCode()
                    + " Cavab : " + response.asString());
        }


        String token = response.jsonPath().getString("token");


        if (token==null || token.isBlank()){
            throw new IllegalStateException(
                    "Login 200 qaytardı, amma cavabda token sahesi tapılmadı"
                    + " Cavab : " +  response.asString());
        }
        return token;
    }


}
