package avivi.com.pollardi;

import io.realm.RealmObject;

/**
 * Created by Void on 07.05.2015.
 */
public class RealmGridCol extends RealmObject {
    private String id_grid;
    private String url_photo;
    private String url_det_pic;
    private String det_text;
    private String name_t;
    private boolean has_det_pic;

    public boolean isHas_det_pic() {
        return has_det_pic;
    }

    public void setHas_det_pic(boolean has_det_pic) {
        this.has_det_pic = has_det_pic;
    }

    public String getUrl_det_pic() {
        return url_det_pic;
    }

    public void setUrl_det_pic(String url_det_pic) {
        this.url_det_pic = url_det_pic;
    }

    public String getDet_text() {
        return det_text;
    }

    public void setDet_text(String det_text) {
        this.det_text = det_text;
    }

    public String getName_t() {
        return name_t;
    }

    public void setName_t(String name_t) {
        this.name_t = name_t;
    }

    public String getId_grid() {
        return id_grid;
    }

    public void setId_grid(String id_grid) {
        this.id_grid = id_grid;
    }

    public String getUrl_photo() {
        return url_photo;
    }

    public void setUrl_photo(String url_photo) {
        this.url_photo = url_photo;
    }
}
