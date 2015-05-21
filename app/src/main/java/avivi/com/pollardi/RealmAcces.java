package avivi.com.pollardi;

import io.realm.RealmObject;

/**
 * Created by Void on 06.05.2015.
 */
public class RealmAcces extends RealmObject {
    private String acc_title;
    private String acc_desc;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAcc_title() {
        return acc_title;
    }

    public void setAcc_title(String acc_title) {
        this.acc_title = acc_title;
    }

    public String getAcc_desc() {
        return acc_desc;
    }

    public void setAcc_desc(String acc_desc) {
        this.acc_desc = acc_desc;
    }
}
