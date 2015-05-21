package avivi.com.pollardi;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Void on 02.04.2015.
 */
public class DownloadListURL extends AsyncTask<Void, Void, String[]> {
    HttpURLConnection urlConnection = null;
    BufferedReader reader = null;
    String resultJson = "";
    Realm realm;
    String partAdres;
    Context context;

    public DownloadListURL(Context context, String partAdres) {
        this.context = context;
        this.partAdres = partAdres;
    }

    @Override
    protected String[] doInBackground(Void... params) {

        ArrayList<String> list = new ArrayList<String>();

        try {
//            URL url = new URL("http://artosyad.hol.es/angular/partials/all_vadania.php");
            URL url = new URL("http://pollardi.com/ornamentation/?ID="+ partAdres);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            resultJson = buffer.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONArray dataJsonArr = null;
        realm = Realm.getInstance(context);

        try {
            dataJsonArr = new JSONArray(resultJson);
//                Log.d("LOOOG", dataJsonArr.toString());

//            RealmQuery<RealmGridCol> query = realm.where(RealmGridCol.class);
            RealmResults<RealmGridCol> query = realm.where(RealmGridCol.class).findAll();
            RealmResults<RealmGridCol> result1 = query.where().contains("id_grid", partAdres).findAll();

            realm.beginTransaction();
            result1.clear();
            for (int i = 0; i < dataJsonArr.length(); i++){
                RealmGridCol collName = realm.createObject(RealmGridCol.class);
                JSONObject c = dataJsonArr.getJSONObject(i);
//                String man = c.getString("ID");
                String text = c.getString("DETAIL_PICTURE");
                String det_text = c.getString("DETAIL_TEXT");
                String pict2 = c.getString("PROPERTY_ATT_IMEGES_VALUE");
                String name_t = c.getString("NAME");
                collName.setDet_text(det_text);
                collName.setName_t(name_t);
                collName.setId_grid(partAdres);
                collName.setUrl_photo(text);
                collName.setUrl_det_pic(pict2);
                list.add(text);
                System.out.println(text + " <~~~~~~~in Download~~~~~~~~~~>");
            }
            realm.commitTransaction();
            realm.close();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String[] arr = list.toArray(new String[list.size()]);

        return arr;
    }

    @Override
    protected void onPostExecute(String[] strings) {

        super.onPostExecute(strings);
    }
}