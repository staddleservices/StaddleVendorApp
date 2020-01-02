package staddlevendor.com.staddlevendor.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class MenuListModule implements Parcelable {

    private String id;
    private String name;
    private String price;
    private String status;
    private String sid;
    private String subCatName;
  //  private int count;

  //  public int getCount() {
    //    return count;
   // }

  //  public void setCount(int count) {
   //     this.count = count;
   // }

    protected MenuListModule(Parcel in) {
        id = in.readString();
        name = in.readString();
        price = in.readString();
        sid = in.readString();
        subCatName = in.readString();
        status = in.readString();
     //   count = Integer.parseInt(in.readString());

    }

    public static final Creator<MenuListModule> CREATOR = new Creator<MenuListModule>() {
        @Override
        public MenuListModule createFromParcel(Parcel in) {
            return new MenuListModule(in);
        }

        @Override
        public MenuListModule[] newArray(int size) {
            return new MenuListModule[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getSubCatName() {
        return subCatName;
    }

    public void setSubCatName(String subCatName) {
        this.subCatName = subCatName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(price);
        parcel.writeString(status);
        parcel.writeString(sid);
        parcel.writeString(subCatName);
      //  parcel.writeString(String.valueOf(count));
    }
}
