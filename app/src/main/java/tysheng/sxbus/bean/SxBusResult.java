package tysheng.sxbus.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Sty
 * Date: 16/9/20 15:38.
 */

public class SxBusResult implements Parcelable {
    public static final Parcelable.Creator<SxBusResult> CREATOR = new Parcelable.Creator<SxBusResult>() {
        @Override
        public SxBusResult createFromParcel(Parcel source) {
            return new SxBusResult(source);
        }

        @Override
        public SxBusResult[] newArray(int size) {
            return new SxBusResult[size];
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

    public SxBusResult() {
    }

    protected SxBusResult(Parcel in) {
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
