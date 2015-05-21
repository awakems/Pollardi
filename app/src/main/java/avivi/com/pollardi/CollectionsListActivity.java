package avivi.com.pollardi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
 * Created by Void on 28.04.2015.
 */
public class CollectionsListActivity extends Activity {

    private Realm realm;
    RealmResults<RealmColl> collresultlist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collections);

        realm = Realm.getInstance(this);

        if(isInternetAvailable()){

            //inet on
            new DownloadImageTask().execute();
            collresultlist = realm.where(RealmColl.class).findAll();
            Toast toast = Toast.makeText(this, "connection ON inet"  , Toast.LENGTH_SHORT);
            toast.show();


        }else{

            //when inet off
            Toast toast = Toast.makeText(this, "No connection to inet"  , Toast.LENGTH_SHORT);
            toast.show();
            collresultlist = realm.where(RealmColl.class).findAll();

        }

        //create adapter
        final AdapterRealmCollectionList adapter = new AdapterRealmCollectionList(this, collresultlist, true);
        ListView listView = (ListView) findViewById(R.id.listviewcollection);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView itemtextView = (TextView) view.findViewById(R.id.accIDtv);
                String ID = itemtextView.getText().toString();
                TextView category = (TextView) view.findViewById(R.id.title_acc);
                String categ_name = category.getText().toString();


                Intent intent = new Intent(CollectionsListActivity.this, SimpleImageActivity.class);
                intent.putExtra(Constants.Extra.FRAGMENT_INDEX, ImageGridFragment.INDEX);
                intent.putExtra("ID", ID);
                intent.putExtra("prefix", "collections/?ID=");
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

            InputStream inputStream =null;

            ArrayList<String> list = new ArrayList<String>();
            try {
                System.out.println("start doinback !!!");
                URL url = new URL("http://pollardi.com/collections/");

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
                Log.i("JSON Parser", resultJson);

            } catch (Exception e) {
                System.out.println("error downloat json string");
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
            RealmQuery<RealmColl> query = realm.where(RealmColl.class);
            RealmResults<RealmColl> result1 = query.findAll();

            realm.beginTransaction();

            try {
                System.out.println("start Jsonarray !!!");
                dataJsonArr = new JSONArray(resultJson);

                result1.clear();
                for (int i = 0; i < dataJsonArr.length(); i++){
                    RealmColl collName = realm.createObject(RealmColl.class);
                    JSONObject c = dataJsonArr.getJSONObject(i);
                    String name = c.getString("NAME");
                    String text = c.getString("DESCRIPTION");
                    String id = c.getString("ID");
                    collName.setColl_name(name);
                    collName.setColl_det_text(text);
                    collName.setColl_id(id);
                    list.add(name);
//                    System.out.println(text + " ~~~~~~<><><>");
                }
                realm.commitTransaction();


            } catch (JSONException e) {
                System.out.println("eror Fail JsonArrayCreate");
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


    public boolean isInternetAvailable() {

        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();

    }


}
