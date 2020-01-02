package staddlevendor.com.staddlevendor.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class OrderParsedListModel implements Parcelable {
    private String count;
    private String id;
    private String idd;
    private String menu_name;
    private String menu_price;
    private String vid;

    protected OrderParsedListModel(Parcel in) {
        count = in.readString();
        id = in.readString();
        idd = in.readString();
        menu_name = in.readString();
        menu_price = in.readString();
        vid = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(count);
        dest.writeString(id);
        dest.writeString(idd);
        dest.writeString(menu_name);
        dest.writeString(menu_price);
        dest.writeString(vid);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OrderParsedListModel> CREATOR = new Creator<OrderParsedListModel>() {
        @Override
        public OrderParsedListModel createFromParcel(Parcel in) {
            return new OrderParsedListModel(in);
        }

        @Override
        public OrderParsedListModel[] newArray(int size) {
            return new OrderParsedListModel[size];
        }
    };

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdd() {
        return idd;
    }

    public void setIdd(String idd) {
        this.idd = idd;
    }

    public String getMenu_name() {
        return menu_name;
    }

    public void setMenu_name(String menu_name) {
        this.menu_name = menu_name;
    }

    public String getMenu_price() {
        return menu_price;
    }

    public void setMenu_price(String menu_price) {
        this.menu_price = menu_price;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }
}
