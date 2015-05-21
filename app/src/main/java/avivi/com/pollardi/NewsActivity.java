package avivi.com.pollardi;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by Void on 09.04.2015.
 */
public class NewsActivity extends Activity {

    private Realm realm;
    RealmResults<RealmCollName> collresultlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        setTitle("Новости");

        realm = Realm.getInstance(this);

        if(isInternetAvailable()){

            //inet on
            new DownloadImageTask().execute();
            collresultlist = realm.where(RealmCollName.class).findAll();
            Toast toast = Toast.makeText(this, "connection ON inet"  , Toast.LENGTH_SHORT);
            toast.show();


        }else{

            //when inet off
            Toast toast = Toast.makeText(this, "No connection to inet"  , Toast.LENGTH_SHORT);
            toast.show();
            collresultlist = realm.where(RealmCollName.class).findAll();
            for(RealmCollName x : collresultlist){
                System.out.println("LLLLLLLLLLLLLL" + x.getCol_name());
            }

        }

        //create adapter
        final AdapterRealmCol adapter = new AdapterRealmCol(this, R.id.listViewNews, collresultlist, true);
        ListView listView = (ListView) findViewById(R.id.listViewNews);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView itemtextView = (TextView) view.findViewById(R.id.news_list_item_text);
                String sendtext = itemtextView.getText().toString();

                if (sendtext.equals("International size")){
                    Intent intent1 = new Intent(NewsActivity.this, InternationalSize.class);
                    startActivity(intent1);
                } else {
                    Intent intent = new Intent(NewsActivity.this, NewsDetailActivity.class);
                    intent.putExtra("title", sendtext);
                    startActivity(intent);
                }
            }
        });

    }

    public boolean isInternetAvailable() {

//        ConnectivityManager cm =
//                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo netInfo = cm.getActiveNetworkInfo();
//        return netInfo != null && netInfo.isConnectedOrConnecting();

        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if ( connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {

            // if connected with internet

            Toast.makeText(this, " Connected ", Toast.LENGTH_LONG).show();
            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) {

            Toast.makeText(this, " Not Connected ", Toast.LENGTH_LONG).show();
            return false;
        }
        return false;

    }


    private class DownloadImageTask extends AsyncTask<Void, Void, String[]> {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";



        @Override
        protected String[] doInBackground(Void... params) {

            InputStream inputStream = null;
            ArrayList<String> list = new ArrayList<String>();
            try {
//                URL url = new URL("http://artosyad.hol.es/angular/partials/all_vadania.php");
                URL url = new URL("http://pollardi.com/json/");

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                resultJson = buffer.toString();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (inputStream != null){
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            JSONArray dataJsonArr = null;
            realm = Realm.getInstance(getApplicationContext());
            RealmQuery<RealmCollName> query = realm.where(RealmCollName.class);
            RealmResults<RealmCollName> result1 = query.findAll();

            realm.beginTransaction();

            try {
                dataJsonArr = new JSONArray(resultJson);
//                Log.d("LOOOG", dataJsonArr.toString());

                result1.clear();
                for (int i=0; i < dataJsonArr.length(); i++){
                    RealmCollName collName = realm.createObject(RealmCollName.class);
                    JSONObject c = dataJsonArr.getJSONObject(i);
//                    String man = c.getString("id");
//                    String text = c.getString("manager");
                    String man = c.getString("NAME");
                    String text = c.getString("DETAIL_PICTURE");
                    String text_d = c.getString("DETAIL_TEXT");
                    collName.setCol_name(man);
                    collName.setCol_text(text);
                    collName.setCol_det_text(text_d);
                    list.add(man);
                    System.out.println(text + " ~~~~~~<><><>");
                }
                realm.commitTransaction();


            } catch (JSONException e) {
                realm.cancelTransaction();
                e.printStackTrace();
            }

            String[] arr = list.toArray(new String[list.size()]);
            realm.close();
            return arr;
        }

        @Override
        protected void onPostExecute(String[] strings) {

            super.onPostExecute(strings);
        }
    }

}
