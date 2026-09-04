package model;

import com.fasterxml.jackson.annotation.JsonInclude;

//Deyeri null olan saheleri Jsona Hec salmır

//JsonInclude olmazsa  { "status": "completed", "amount": 200,"currency": null}
//JsonInclude olsa  { "status": "completed", "amount": 200,}
@JsonInclude(JsonInclude.Include.NON_NULL)

public class TransferRequest {

    /*
      "senderAccountId": "acc_001",
      "receiverAccountId": "acc_002",
      "amount": 200,
      "description": "Test köçürməsi"
     */

    private String senderAccountId;   // pul cıxan hesab
    private String receiverAccountId;    // pul geden hesab
    private Double amount;          // gınderılen mebleg
    private String description;     // acıqlaması


    // Bos Construcot -Jackson ucun mecburıdı
    // Json->obyecte cevıresınde (Responsu pojoya map edecek)
    //Jackson evvelce bos obyect yaradır , sonra setter lerle doldurur

    // Java heç bir constructor yazamasan avto boş yaradılır


    public TransferRequest() {}


    // tam Constructor obyectını bır setırde  dolu yaratmaq ucun

    public TransferRequest(String senderAccountId, String receiverAccountId, Double amount, String description) {
        this.senderAccountId = senderAccountId;
        this.receiverAccountId = receiverAccountId;
        this.amount = amount;
        this.description = description;
    }

   // Statıc Factory -sadece qısa yoldu  obyect yaratmadan cagırılır

    //TransferRequest(1,2,100.0);
    // evezi
    // new TransferRequest(1,2,100.0,"Kamunal"
    // descrıptonu her testde el ıle yazmamaq ucun

    public static  TransferRequest of (String senderAccountId, String receiverAccountId, Double amount) {
        return new TransferRequest(senderAccountId,receiverAccountId,amount,"Auto test transfer");
    }



    // Getter/ setter metodlar
    // Getter Jackson ""getAmount" metodunu goturur ->get atır
    // ilk herfi kiçildir ->json açarı "amount" olur
    //Getter olmasa hemin sahe Jsona umımıetle dusmur

    //Setter  eks ıstıqametde (Json->Obyecte) + testde obyectı deyısır
    // req.setAmount(null);   _> sahenı gondermır
    // req.setAmmount(-50.0)-> menfı mebleg testı




    public String getSenderAccountId() {return senderAccountId;}
    public void setSenderAccountId(String v) {this.senderAccountId = v;}

    public String getReceiverAccountId() {return receiverAccountId;}
    public void setReceiverAccountId(String v) {this.receiverAccountId = v;}

    public Double getAmount() {return amount;}
    public void setAmount(Double v) {this.amount = v;}

    public String getDescription() {return description;}
    public void setDescription(String v) {this.description = v;}
}
