package avivi.com.pollardi;

import io.realm.RealmObject;

/**
 * Created by Void on 28.04.2015.
 */
public class RealmCollName extends RealmObject {

    private String col_name;
    private String col_text;
    private String col_det_text;

    public String getCol_det_text() {
        return col_det_text;
    }

    public void setCol_det_text(String col_det_text) {
        this.col_det_text = col_det_text;
    }

    public String getCol_name() {
        return col_name;
    }

    public String getCol_text() {
        return col_text;
    }

    public void setCol_name(String col_name) {
        this.col_name = col_name;
    }

    public void setCol_text(String col_text) {
        this.col_text = col_text;
    }
}
