package staddlevendor.com.staddlevendor.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class GetVendorInfoModel implements Parcelable {

    private String vid;
    private String venderfname;
    private String venderlname;
    private String mobile;
    private String email;
    private String create_date;
    private String status;
    private String image;
    private String location;
    private String adharcard_no;
    private String gst_no;

    protected GetVendorInfoModel(Parcel in) {
        vid = in.readString();
        venderfname = in.readString();
        venderlname = in.readString();
        mobile = in.readString();
        email = in.readString();
        create_date = in.readString();
        status = in.readString();
        image = in.readString();
        location = in.readString();
        adharcard_no = in.readString();
        gst_no = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(vid);
        dest.writeString(venderfname);
        dest.writeString(venderlname);
        dest.writeString(mobile);
        dest.writeString(email);
        dest.writeString(create_date);
        dest.writeString(status);
        dest.writeString(image);
        dest.writeString(location);
        dest.writeString(adharcard_no);
        dest.writeString(gst_no);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GetVendorInfoModel> CREATOR = new Creator<GetVendorInfoModel>() {
        @Override
        public GetVendorInfoModel createFromParcel(Parcel in) {
            return new GetVendorInfoModel(in);
        }

        @Override
        public GetVendorInfoModel[] newArray(int size) {
            return new GetVendorInfoModel[size];
        }
    };

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getVenderfname() {
        return venderfname;
    }

    public void setVenderfname(String venderfname) {
        this.venderfname = venderfname;
    }

    public String getVenderlname() {
        return venderlname;
    }

    public void setVenderlname(String venderlname) {
        this.venderlname = venderlname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAdharcard_no() {
        return adharcard_no;
    }

    public void setAdharcard_no(String adharcard_no) {
        this.adharcard_no = adharcard_no;
    }

    public String getGst_no() {
        return gst_no;
    }

    public void setGst_no(String gst_no) {
        this.gst_no = gst_no;
    }
}
