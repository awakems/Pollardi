package avivi.com.pollardi;

import io.realm.RealmObject;

/**
 * Created by Void on 12.05.2015.
 */
public class RealmColl extends RealmObject {
    private String coll_id;
    private String coll_name;
    private String coll_det_text;

    public String getColl_id() {
        return coll_id;
    }

    public void setColl_id(String coll_id) {
        this.coll_id = coll_id;
    }

    public String getColl_name() {
        return coll_name;
    }

    public void setColl_name(String coll_name) {
        this.coll_name = coll_name;
    }

    public String getColl_det_text() {
        return coll_det_text;
    }

    public void setColl_det_text(String coll_det_text) {
        this.coll_det_text = coll_det_text;
    }
}
