package avivi.com.pollardi;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import io.realm.Realm;


public class SplashActivity extends Activity {
    String[] Addreses;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        Realm.deleteRealmFile(getApplicationContext());


        //Create file.txt in cache directory
        File f = new File(this.getExternalCacheDir(), "file.txt");
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Init and write into file list of Address strings
//        try {
//            Addreses =  new DownloadImageTask().execute().get();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }



        Thread background = new Thread() {
            public void run() {

                try {
                    // Thread will sleep for 5 seconds
                    sleep(1*1000);
                    Intent intent = new Intent(getBaseContext(), TestActivity.class);
                    startActivity(intent);
                    finish();

                } catch (Exception e) {

                }
            }
        };

        // start thread
        background.start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class DownloadImageTask extends AsyncTask<Void, Void, String[]> {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";



        @Override
        protected String[] doInBackground(Void... params) {

            ArrayList<String> list = new ArrayList<String>();
            try {
//                URL url = new URL("http://artosyad.hol.es/angular/partials/all_vadania.php");
                URL url = new URL("http://pollardi.com/json/");

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
            realm = Realm.getInstance(getApplicationContext());

            try {
                dataJsonArr = new JSONArray(resultJson);
                Log.d("LOOOG", dataJsonArr.toString());


                realm.beginTransaction();
                for (int i=0; i < dataJsonArr.length(); i++){
                    RealmCollName collName = realm.createObject(RealmCollName.class);
                    JSONObject c = dataJsonArr.getJSONObject(i);
                    String man = c.getString("DETAIL_PICTURE");
                    String text = c.getString("NAME");
                    collName.setCol_name(man);
                    collName.setCol_text(text);
                    list.add(man);
                    System.out.println(man + " ==============>");
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

            //Writing to file result of parsing
            File dir = Environment.getExternalStorageDirectory();
            File f2 = null;

            f2 = new File(getApplicationContext().getExternalCacheDir(), "file.txt");

            try {
                FileWriter fw = new FileWriter(f2);
                for (String x : strings){
                    fw.write(x+"\n");
                }
                fw.flush();
                fw.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

            super.onPostExecute(strings);
        }
    }

}
