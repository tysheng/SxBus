package tysheng.sxbus.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tysheng
 * Date: 2017/5/4 11:03.
 * Email: tyshengsx@gmail.com
 */

public class BikeStation implements Parcelable, MapInfo {
    public static final Creator<BikeStation> CREATOR = new Creator<BikeStation>() {
        @Override
        public BikeStation createFromParcel(Parcel in) {
            return new BikeStation(in);
        }

        @Override
        public BikeStation[] newArray(int size) {
            return new BikeStation[size];
        }
    };
    public int id;
    public String name;
    public double lat;
    public double lng;
    public int capacity;
    public int availBike;
    public String address;

    protected BikeStation(Parcel in) {
        id = in.readInt();
        name = in.readString();
        lat = in.readDouble();
        lng = in.readDouble();
        capacity = in.readInt();
        availBike = in.readInt();
        address = in.readString();
    }

    public BikeStation() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeDouble(lat);
        dest.writeDouble(lng);
        dest.writeInt(capacity);
        dest.writeInt(availBike);
        dest.writeString(address);
    }

    @Override
    public String getName() {
        return name + " \n全部 " + capacity + " 可借 " + availBike;
    }

    @Override
    public double getLat() {
        return lat;
    }

    @Override
    public double getLng() {
        return lng;
    }
}
