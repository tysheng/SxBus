package tysheng.sxbus.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Sty
 * Date: 16/9/17 19:46.
 */
public class Stations implements Parcelable {
    public static final Parcelable.Creator<Stations> CREATOR = new Parcelable.Creator<Stations>() {
        @Override
        public Stations createFromParcel(Parcel source) {
            return new Stations(source);
        }

        @Override
        public Stations[] newArray(int size) {
            return new Stations[size];
        }
    };
    public String id;
    public int area;
    public double lat;
    public double lng;
    public String state;
    public String stationName;
    public String updateTime;
    public ArriveState arriveState = ArriveState.NotArrive;

    public Stations() {
    }

    protected Stations(Parcel in) {
        this.id = in.readString();
        this.area = in.readInt();
        this.lat = in.readDouble();
        this.lng = in.readDouble();
        this.state = in.readString();
        this.stationName = in.readString();
        this.updateTime = in.readString();
        int tmpArriveState = in.readInt();
        this.arriveState = tmpArriveState == -1 ? null : ArriveState.values()[tmpArriveState];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeInt(this.area);
        dest.writeDouble(this.lat);
        dest.writeDouble(this.lng);
        dest.writeString(this.state);
        dest.writeString(this.stationName);
        dest.writeString(this.updateTime);
        dest.writeInt(this.arriveState == null ? -1 : this.arriveState.ordinal());
    }

    public enum ArriveState {
        NotArrive, Arriving, Arrived
    }
}
