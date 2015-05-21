package avivi.com.pollardi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by Void on 06.05.2015.
 */
public class AccessoriesActivity extends Activity {

    private Realm realm;
    RealmResults<RealmAcces> collresultlist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accessories);

        realm = Realm.getInstance(this);

        if(isInternetAvailable()){

            //inet on
            new DownloadImageTask().execute();
            collresultlist = realm.where(RealmAcces.class).findAll();
            Toast toast = Toast.makeText(this, "connection ON inet"  , Toast.LENGTH_SHORT);
            toast.show();


        }else{

            //when inet off
            Toast toast = Toast.makeText(this, "No connection to inet"  , Toast.LENGTH_SHORT);
            toast.show();
            collresultlist = realm.where(RealmAcces.class).findAll();

        }

        //create adapter
        final AdapterRealmAcc adapter = new AdapterRealmAcc(this, collresultlist, true);
        ListView listView = (ListView) findViewById(R.id.accesor_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView itemtextView = (TextView) view.findViewById(R.id.accIDtv);
                String ID = itemtextView.getText().toString();
                TextView category = (TextView) view.findViewById(R.id.title_acc);
                String categ_name = category.getText().toString();


                Intent intent = new Intent(AccessoriesActivity.this, SimpleImageActivity.class);
                intent.putExtra(Constants.Extra.FRAGMENT_INDEX, ImageGridFragment.INDEX);
                intent.putExtra("ID", ID);
                intent.putExtra("prefix", "ornamentation/?ID=");
                intent.putExtra("categ_name", categ_name);
                startActivity(intent);
            }
        });
    }



    private class DownloadImageTask extends AsyncTask<Void, Void, String[]> {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";



        @Override
        protected String[] doInBackground(Void... params) {

            ArrayList<String> list = new ArrayList<String>();
            try {
                URL url = new URL("http://pollardi.com/ornamentation/");

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
                System.out.println("error downloat json string");
                e.printStackTrace();
            }
            JSONArray dataJsonArr = null;
            realm = Realm.getInstance(getApplicationContext());

            try {
                dataJsonArr = new JSONArray(resultJson);

                RealmQuery<RealmAcces> query = realm.where(RealmAcces.class);
                RealmResults<RealmAcces> result1 = query.findAll();

                realm.beginTransaction();
                result1.clear();
                for (int i = 0; i < dataJsonArr.length(); i++){
                    RealmAcces collName = realm.createObject(RealmAcces.class);
                    JSONObject c = dataJsonArr.getJSONObject(i);
                    String man = c.getString("NAME");
                    String text = c.getString("DESCRIPTION");
                    String id = c.getString("ID");
                    collName.setAcc_title(man);
                    collName.setAcc_desc(text);
                    collName.setId(id);
                    list.add(man);
//                    System.out.println(text + " ~~~~~~<><><>");
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


    public boolean isInternetAvailable() {

        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();

    }
}
