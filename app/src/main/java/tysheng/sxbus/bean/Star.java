package tysheng.sxbus.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by Sty
 * Date: 16/9/11 11:44.
 */
@Entity
public class Star implements Cloneable {
    @Id
    public Long mainId;
    public String tableName;
    public Boolean isStar = false;
    public Long sortId;
//    ----------------

    public String id;
    public String localLineId;
    public String endStationName;
    public String lineName;
    public String startStationName;
    public String updateTime;

    @Generated(hash = 361015167)
    public Star(Long mainId, String tableName, Boolean isStar, Long sortId, String id,
                String localLineId, String endStationName, String lineName,
                String startStationName, String updateTime) {
        this.mainId = mainId;
        this.tableName = tableName;
        this.isStar = isStar;
        this.sortId = sortId;
        this.id = id;
        this.localLineId = localLineId;
        this.endStationName = endStationName;
        this.lineName = lineName;
        this.startStationName = startStationName;
        this.updateTime = updateTime;
    }

    @Generated(hash = 249476133)
    public Star() {
    }

    public Long getMainId() {
        return this.mainId;
    }

    public void setMainId(Long mainId) {
        this.mainId = mainId;
    }

    public String getTableName() {
        return this.tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public boolean getIsStar() {
        return this.isStar;
    }

    public void setIsStar(Boolean isStar) {
        this.isStar = isStar;
    }

    public void setIsStar(boolean isStar) {
        this.isStar = isStar;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocalLineId() {
        return this.localLineId;
    }

    public void setLocalLineId(String localLineId) {
        this.localLineId = localLineId;
    }

    public String getEndStationName() {
        return this.endStationName;
    }

    public void setEndStationName(String endStationName) {
        this.endStationName = endStationName;
    }

    public String getLineName() {
        return this.lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public String getStartStationName() {
        return this.startStationName;
    }

    public void setStartStationName(String startStationName) {
        this.startStationName = startStationName;
    }

    public String getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Long getSortId() {
        return this.sortId;
    }

    public void setSortId(Long sortId) {
        this.sortId = sortId;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}