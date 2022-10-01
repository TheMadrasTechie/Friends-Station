package radson.findpeople.recyclercardview;

import android.graphics.Bitmap;

/**
 * Created by Belal on 10/29/2015.
 */
public class ListItem {

    private String name;
    private String url;
    private String Reg_id;
    private Bitmap image;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String get_reg_id() {
        return Reg_id;
    }

    public void setreg_id(String Reg_id) {
        this.Reg_id = Reg_id;
    }
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

}
