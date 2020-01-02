package staddlevendor.com.staddlevendor.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class ProductListModel{
    private String id;
    private String uid;
    private String vid;
    private String vfname;
    private String vlname;
    private String vmobile;
    private String vemail;
    private String vlocation;
    private String subcategory;
    private String vimage;
    private String order_price;
    private String discount;
    private String discount_price;
    private String commission;
    private String create_date;
    private String status;
    private String payment;
    private String user_name;
    private String user_email;
    private String user_mobile;
    private String house_number;
    private String city;
    private String state;
    private String landmark;
    private String booking_slot;
    private String booked_date;
    ArrayList<OrderParsedListModel> data = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getVfname() {
        return vfname;
    }

    public void setVfname(String vfname) {
        this.vfname = vfname;
    }

    public String getVlname() {
        return vlname;
    }

    public void setVlname(String vlname) {
        this.vlname = vlname;
    }

    public String getVmobile() {
        return vmobile;
    }

    public void setVmobile(String vmobile) {
        this.vmobile = vmobile;
    }

    public String getVemail() {
        return vemail;
    }

    public void setVemail(String vemail) {
        this.vemail = vemail;
    }

    public String getVlocation() {
        return vlocation;
    }

    public void setVlocation(String vlocation) {
        this.vlocation = vlocation;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public String getVimage() {
        return vimage;
    }

    public void setVimage(String vimage) {
        this.vimage = vimage;
    }

    public String getOrder_price() {
        return order_price;
    }

    public void setOrder_price(String order_price) {
        this.order_price = order_price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDiscount_price() {
        return discount_price;
    }

    public void setDiscount_price(String discount_price) {
        this.discount_price = discount_price;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_mobile() {
        return user_mobile;
    }

    public void setUser_mobile(String user_mobile) {
        this.user_mobile = user_mobile;
    }

    public String getHouse_number() {
        return house_number;
    }

    public void setHouse_number(String house_number) {
        this.house_number = house_number;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getBooking_slot() {
        return booking_slot;
    }

    public void setBooking_slot(String booking_slot) {
        this.booking_slot = booking_slot;
    }

    public String getBooked_date() {
        return booked_date;
    }

    public void setBooked_date(String booked_date) {
        this.booked_date = booked_date;
    }

    public ArrayList<OrderParsedListModel> getData() {
        return data;
    }

    public void setData(ArrayList<OrderParsedListModel> data) {
        this.data = data;
    }
}
