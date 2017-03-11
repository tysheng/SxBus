package tysheng.sxbus.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Sty
 * Date: 16/9/20 15:38.
 */

public class YueChenBusResult implements Parcelable {
    public static final Parcelable.Creator<YueChenBusResult> CREATOR = new Parcelable.Creator<YueChenBusResult>() {
        @Override
        public YueChenBusResult createFromParcel(Parcel source) {
            return new YueChenBusResult(source);
        }

        @Override
        public YueChenBusResult[] newArray(int size) {
            return new YueChenBusResult[size];
        }
    };
    public String busId;
    public double lng;//经度
    public double lat;//纬度
    public double velocity;
    public int stationSeqNum;
    public String buslineId;
    public String actTime;
    public String cardId;
    public boolean isArriveDest;

    public YueChenBusResult() {
    }

    protected YueChenBusResult(Parcel in) {
        this.busId = in.readString();
        this.lng = in.readDouble();
        this.lat = in.readDouble();
        this.velocity = in.readDouble();
        this.stationSeqNum = in.readInt();
        this.buslineId = in.readString();
        this.actTime = in.readString();
        this.cardId = in.readString();
        this.isArriveDest = in.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.busId);
        dest.writeDouble(this.lng);
        dest.writeDouble(this.lat);
        dest.writeDouble(this.velocity);
        dest.writeInt(this.stationSeqNum);
        dest.writeString(this.buslineId);
        dest.writeString(this.actTime);
        dest.writeString(this.cardId);
        dest.writeByte(this.isArriveDest ? (byte) 1 : (byte) 0);
    }
}
