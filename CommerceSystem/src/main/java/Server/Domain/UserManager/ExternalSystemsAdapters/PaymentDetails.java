package Server.Domain.UserManager.ExternalSystemsAdapters;

public class PaymentDetails {

    private String card_number;
    private String month;
    private String year;
    private String holder;
    private String ccv;
    private String id;

    public PaymentDetails(String card_number, String month, String year, String holder, String ccv, String id) {
        this.card_number = card_number;
        this.month = month;
        this.year = year;
        this.holder = holder;
        this.ccv = ccv;
        this.id = id;
    }

    public String getCard_number() {
        return card_number;
    }

    public String getMonth() {
        return month;
    }

    public String getYear() {
        return year;
    }

    public String getHolder() {
        return holder;
    }

    public String getCcv() {
        return ccv;
    }

    public String getId() {
        return id;
    }

}
