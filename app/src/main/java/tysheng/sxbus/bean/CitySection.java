package tysheng.sxbus.bean;

import com.chad.library.adapter.base.entity.SectionEntity;

/**
 * Created by Sty
 * Date: 16/9/15 10:21.
 */
public class CitySection extends SectionEntity<String> {
    public CitySection(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public CitySection(String s) {
        super(s);
    }
}
