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
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public class ImagePagerFragment extends BaseFragment {

	public static final int INDEX = 2;

	String[] imageUrls;
    Realm realm;
    String about_text;
    RealmResults<RealmGridCol> collresultlist;
    ArrayList<String> photos = new ArrayList<>();
//    DisplayImageOptions options;


    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        realm = Realm.getInstance(getActivity().getApplicationContext());




        String name = getActivity().getIntent().getExtras().getString("name");

        RealmResults<RealmGridCol> query = realm.where(RealmGridCol.class).findAll();
        collresultlist = query.where().equalTo("name_t", name).findAll();

        RealmResults<RealmGridCol> result2 = query.where().equalTo("name_t", name).findAll();
        RealmResults<RealmGridCol> res = collresultlist.where().equalTo("has_det_pic", false).findAll();
        RealmGridCol obj = res.first();
        photos.add(obj.getUrl_photo());

        RealmResults<RealmGridCol> result = collresultlist.where().equalTo("has_det_pic", true).findAll();

        for (RealmGridCol x : result){
            photos.add(x.getUrl_det_pic());
        }

        about_text = obj.getDet_text();

        realm.close();

        imageUrls = photos.toArray(new String[photos.size()]);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fr_image_pager, container, false);
		ViewPager pager = (ViewPager) rootView.findViewById(R.id.pager);
		pager.setAdapter(new ImageAdapter(getActivity()));

		return rootView;
	}

	private class ImageAdapter extends PagerAdapter {

		private LayoutInflater inflater;
        private DisplayImageOptions options;

		ImageAdapter(Context context) {
			inflater = LayoutInflater.from(context);
            options = new DisplayImageOptions.Builder()
                    .showImageForEmptyUri(R.drawable.ic_empty)
                    .showImageOnFail(R.drawable.ic_error)
                    .resetViewBeforeLoading(true)
                    .cacheOnDisk(true)
                    .cacheInMemory(true)
                    .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
//				.bitmapConfig(Bitmap.Config.ARGB_8888)
                    .considerExifParams(true)
                    .displayer(new FadeInBitmapDisplayer(300))
                    .build();
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public int getCount() {
			return imageUrls.length;
		}

		@Override
		public Object instantiateItem(ViewGroup view, int position) {

            final String no_det_text = getString(R.string.no_det_text_toast);
			View imageLayout = inflater.inflate(R.layout.item_pager_image, view, false);
			assert imageLayout != null;
            TouchImageView imageView = (TouchImageView) imageLayout.findViewById(R.id.img);
			final ProgressBar spinner = (ProgressBar) imageLayout.findViewById(R.id.loading);
            ImageButton but_inf = (ImageButton) imageLayout.findViewById(R.id.imageButtonInfo);
            TextView num_photo = (TextView) imageLayout.findViewById(R.id.t_num_photo);
            but_inf.setImageResource(R.drawable.info_icon);
            but_inf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (about_text.equals("")) {
                        Toast t2_toast = Toast.makeText(getActivity().getApplicationContext(), no_det_text, Toast.LENGTH_LONG);
                        t2_toast.show();
                    } else {
                        Toast t_toast = Toast.makeText(getActivity().getApplicationContext(), Html.fromHtml(about_text), Toast.LENGTH_LONG);
                        t_toast.show();
                        System.out.println("=>>>>" + about_text + "<<<<<<");

                    }
                }
            });

            num_photo.setText(position+1 + " / " + getCount());

//            Picasso.with(getActivity().getApplicationContext()).load(imageUrls[position]).into(imageView);

			ImageLoader.getInstance().displayImage(imageUrls[position], imageView, options, new SimpleImageLoadingListener() {
				@Override
				public void onLoadingStarted(String imageUri, View view) {
					spinner.setVisibility(View.VISIBLE);
                    view.setVisibility(View.GONE);
				}

				@Override
				public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
					String message = null;
					switch (failReason.getType()) {
						case IO_ERROR:
							message = "Input/Output error";
							break;
						case DECODING_ERROR:
							message = "Image can't be decoded";
							break;
						case NETWORK_DENIED:
							message = "Downloads are denied";
							break;
						case OUT_OF_MEMORY:
							message = "Out Of Memory error";
							break;
						case UNKNOWN:
							message = "Unknown error";
							break;
					}

					Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

					spinner.setVisibility(View.GONE);
				}

				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					spinner.setVisibility(View.GONE);
                    view.setVisibility(View.VISIBLE);
				}
			});

			view.addView(imageLayout, 0);
			return imageLayout;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view.equals(object);
		}

		@Override
		public void restoreState(Parcelable state, ClassLoader loader) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}
	}


}