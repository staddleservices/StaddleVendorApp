package staddlevendor.com.staddlevendor.bean;

public class MyPromoListModel {

    String vid;
    String promo_name;
    String description;
    String promo_value;
    String minimum_price;
    String promo_type;
    String create_date;

    public MyPromoListModel(String vid,String promo_name, String description, String promo_value, String minimum_price, String promo_type, String create_date) {
        this.vid = vid;
        this.description = description;
        this.promo_value = promo_value;
        this.minimum_price = minimum_price;
        this.promo_type = promo_type;
        this.create_date = create_date;
        this.promo_name = promo_name;
    }

    public String getPromo_name() {
        return promo_name;
    }

    public void setPromo_name(String promo_name) {
        this.promo_name = promo_name;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPromo_value() {
        return promo_value;
    }

    public void setPromo_value(String promo_value) {
        this.promo_value = promo_value;
    }

    public String getMinimum_price() {
        return minimum_price;
    }

    public void setMinimum_price(String minimum_price) {
        this.minimum_price = minimum_price;
    }

    public String getPromo_type() {
        return promo_type;
    }

    public void setPromo_type(String promo_type) {
        this.promo_type = promo_type;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }
}
