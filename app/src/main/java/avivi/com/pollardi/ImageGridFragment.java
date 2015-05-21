/*******************************************************************************
 * Copyright 2011-2014 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package avivi.com.pollardi;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

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
import io.realm.RealmResults;


/**
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public class ImageGridFragment extends AbsListViewBaseFragment {

    public static final int INDEX = 1;

    ProgressBar proBar;

	String[] imageUrls;
    String[] names_towar;
    private Realm realm;
    RealmResults<RealmGridCol> collresultlist;
	DisplayImageOptions options;
    ArrayList<String> list = new ArrayList<String>();
    ArrayList<String> list_names = new ArrayList<String>();
    ArrayList<String> list_det_photo = new ArrayList<String>();


    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        realm = Realm.getInstance(getActivity().getApplicationContext());


        String tester = getActivity().getIntent().getExtras().getString("frag");
//        Toast toast = Toast.makeText(getActivity(), tester + " = " + partpath  , Toast.LENGTH_SHORT);
//        toast.show();



    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fr_image_grid, container, false);
		listView = (GridView) rootView.findViewById(R.id.grid);
        proBar = (ProgressBar) rootView.findViewById(R.id.progressBarGrid);
        proBar.setVisibility(View.VISIBLE);
        proBar.animate();

        String partpath = getActivity().getIntent().getExtras().getString("ID");
        String prefix = getActivity().getIntent().getExtras().getString("prefix");

        if(isInternetAvailable(getActivity().getApplicationContext()))
        {
            //inet on
            new DownloadImageTask(getActivity().getApplicationContext(),prefix ,partpath).execute();
//            Toast toast = Toast.makeText(getActivity().getApplicationContext(), "inet +++++"  , Toast.LENGTH_SHORT);
//            toast.show();
            System.out.println("after Post exec!!!!");
        } else {
            //when inet off
            RealmResults<RealmGridCol> query = realm.where(RealmGridCol.class).findAll();
            collresultlist = query.where().equalTo("id_grid", partpath).findAll();
            RealmResults<RealmGridCol> result = collresultlist.where().equalTo("has_det_pic", false).findAll();


            for (RealmGridCol x : result) {
                list.add(x.getUrl_photo());
                list_names.add(x.getName_t());
                System.out.println("<<<<<<" + x.getUrl_photo());
            }
            imageUrls = list.toArray(new String[list.size()]);
            names_towar = list_names.toArray(new String[list_names.size()]);
            ((GridView) listView).setAdapter(new ImageAdapter(getActivity()));

        }

        realm.close();

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = names_towar[position];
				startImagePagerActivity(position, imageUrls, name);
			}
		});

        return rootView;
	}

	public class ImageAdapter extends BaseAdapter {

		private LayoutInflater inflater;

		ImageAdapter(Context context) {
			inflater = LayoutInflater.from(context);
            options = new DisplayImageOptions.Builder()
//				.showImageOnLoading(R.drawable.ic_stub)
                    .showImageOnLoading(R.drawable.back_logo)
                    .showImageForEmptyUri(R.drawable.ic_empty)
                    .showImageOnFail(R.drawable.ic_error)
                    .resetViewBeforeLoading(true)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
//				.bitmapConfig(Bitmap.Config.)
                    .displayer(new FadeInBitmapDisplayer(300))
                    .build();
		}

		@Override
		public int getCount() {
			return imageUrls.length;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			View view = convertView;
			if (view == null) {
				view = inflater.inflate(R.layout.item_grid_image, parent, false);
				holder = new ViewHolder();
				assert view != null;
				holder.imageView = (ImageView) view.findViewById(R.id.image);
				holder.progressBar = (ProgressBar) view.findViewById(R.id.progress);
                holder.gridtext = (TextView) view.findViewById(R.id.gridtextitem);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

            ImageLoader.getInstance()
					.displayImage(imageUrls[position], holder.imageView, options, new SimpleImageLoadingListener() {
						@Override
						public void onLoadingStarted(String imageUri, View view) {
							holder.progressBar.setProgress(0);
							holder.progressBar.setVisibility(View.VISIBLE);
						}

						@Override
						public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
							holder.progressBar.setVisibility(View.GONE);
						}

						@Override
						public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
							holder.progressBar.setVisibility(View.GONE);
						}
					}, new ImageLoadingProgressListener() {
						@Override
						public void onProgressUpdate(String imageUri, View view, int current, int total) {
							holder.progressBar.setProgress(Math.round(100.0f * current / total));
						}
					});
            holder.gridtext.setText(names_towar[position]);

			return view;
		}
	}

	static class ViewHolder {
		ImageView imageView;
		ProgressBar progressBar;
        TextView gridtext;
	}


    public static boolean isInternetAvailable(Context context) {

//        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo netInfo = cm.getActiveNetworkInfo();
//        return netInfo != null && netInfo.isConnectedOrConnecting();

        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);

        // Check for network connections
        if ( connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {

            // if connected with internet

            Toast.makeText(context, " Connected ", Toast.LENGTH_LONG).show();
            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) {

            Toast.makeText(context, " Not Connected ", Toast.LENGTH_LONG).show();
            return false;
        }
        return false;

    }

    private class DownloadImageTask extends AsyncTask<Void, Void, String[]> {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";
        Realm realm;
        String partAdres;
        String prefix;
        Context context;

        public DownloadImageTask(Context context,String prefix ,String partAdres) {
            this.context = context;
            this.partAdres = partAdres;
            this.prefix = prefix;
        }

        @Override
        protected String[] doInBackground(Void... params) {

            ArrayList<String> list = new ArrayList<String>();
            InputStream inputStream = null;
            System.out.println("Start do in background");

            try {
                URL url = new URL("http://pollardi.com/" + prefix + partAdres);

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
                System.out.println(" = = = connection failed");
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
            realm = Realm.getInstance(context);

            RealmResults<RealmGridCol> query = realm.where(RealmGridCol.class).findAll();
            RealmResults<RealmGridCol> result1 = query.where().equalTo("id_grid", partAdres).findAll();

            realm.beginTransaction();

            try {
                dataJsonArr = new JSONArray(resultJson);

                result1.clear();
                System.out.println("clear result base");
                for (int i = 0; i < dataJsonArr.length(); i++){
                    RealmGridCol collName = realm.createObject(RealmGridCol.class);
                    JSONObject c = dataJsonArr.getJSONObject(i);
                    String text = c.getString("DETAIL_PICTURE");
//                    String det_text = c.getString("DETAIL_TEXT");
                    String det_text = c.getString("DET_T");
                    String name_t = c.getString("NAME");

                    System.out.println("Before check");
                    if (!c.get("ADDITIONAL_PICTURES").equals(null)) {
                        System.out.println("in array mas check");
                        JSONArray ad = c.getJSONArray("ADDITIONAL_PICTURES");
                        int length = ad.length();
                        if (length > 0) {
                            for (int j = 0; j < length; j++) {
                                RealmGridCol collName2 = realm.createObject(RealmGridCol.class);
                                collName2.setUrl_det_pic(ad.getString(j));
                                collName2.setName_t(name_t);
                                collName2.setId_grid(partAdres);
                                collName2.setHas_det_pic(true);
                            }
                        }
                    }
                    System.out.println("after check");

                    collName.setDet_text(det_text);
                    collName.setName_t(name_t);
                    collName.setId_grid(partAdres);
                    collName.setUrl_photo(text);
                    collName.setHas_det_pic(false);
//                    collName.setUrl_det_pic(arr);
                    list.add(text);
//                    System.out.println(text + " <~~~~~~~in Download~~~~~~~~~~>");
                }
                realm.commitTransaction();


            } catch (JSONException e) {
                System.out.println("not done init base");
                realm.cancelTransaction();
                e.printStackTrace();
            }

            String[] arr = list.toArray(new String[list.size()]);
            realm.close();
            return arr;
        }

        @Override
        protected void onPostExecute(String[] strings) {

//            progressDialog.dismiss();
            proBar.setVisibility(View.GONE);
            Realm realm2 = Realm.getInstance(getActivity().getApplicationContext());

            String partpath = getActivity().getIntent().getExtras().getString("ID");

            RealmResults<RealmGridCol> query = realm2.where(RealmGridCol.class).findAll();
            collresultlist = query.where().equalTo("id_grid", partpath).findAll();
            RealmResults<RealmGridCol> result = collresultlist.where().equalTo("has_det_pic", false).findAll();


            for(RealmGridCol x : result){
                list.add(x.getUrl_photo());
                list_names.add(x.getName_t());
//                System.out.println("<<<<<<"+ x.getUrl_photo());
            }
            imageUrls = list.toArray(new String[list.size()]);
            names_towar = list_names.toArray(new String[list_names.size()]);
//            System.out.println("Post execute DONE!!!");
            realm2.close();

            ((GridView) listView).setAdapter(new ImageAdapter(getActivity()));
//            System.out.println("set adapter ");

            super.onPostExecute(strings);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressDialog = ProgressDialog.show(getActivity().getBaseContext(), "Wait", "Downloading...");

        }
    }

}