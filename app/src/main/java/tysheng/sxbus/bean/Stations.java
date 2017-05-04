package tysheng.sxbus.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Sty
 * Date: 16/9/17 19:46.
 */
public class Stations implements Parcelable, MapInfo {
    public static final Creator<Stations> CREATOR = new Creator<Stations>() {
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
    /**
     * Description:到达状态
     * Creator:  shengtianyang
     * Update Date:  2017/3/12 14:27
     */
    public ArriveState arriveState = ArriveState.NotArrive;
    /**
     * Description: 查看地图时定位于此
     * Creator:  shengtianyang
     * Update Date:  2017/3/12 14:28
     */
    public boolean isLocatedHere;

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
        this.isLocatedHere = in.readByte() != 0;
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
        dest.writeByte(this.isLocatedHere ? (byte) 1 : (byte) 0);
    }

    @Override
    public String getName() {
        return stationName;
    }

    @Override
    public double getLat() {
        return lat;
    }

    @Override
    public double getLng() {
        return lng;
    }

    public enum ArriveState {
        NotArrive, Arriving, Arrived
    }
}
