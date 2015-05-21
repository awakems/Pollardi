package avivi.com.pollardi;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;

/**
 * Created by Void on 28.04.2015.
 */
public class AdapterRealmCol extends RealmBaseAdapter<RealmCollName> implements ListAdapter {

    DisplayImageOptions options;
    ImageLoader imageLoader;

    private static class ViewHolder {
        TextView coltitle;
        ImageView imageicon;
    }

    public AdapterRealmCol(Context context, int listviewcollection, RealmResults<RealmCollName> realmResults, boolean automaticUpdate) {
        super(context, realmResults, automaticUpdate);
        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_list_news, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.coltitle = (TextView) convertView.findViewById(R.id.news_list_item_text);
            viewHolder.imageicon = (ImageView) convertView.findViewById(R.id.news_image_icon);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        RealmCollName item = realmResults.get(position);

//        Typeface type = Typeface.createFromAsset(context.getAssets(),"fonts/fine.ttf");
//        viewHolder.coltitle.setTypeface(type);
        viewHolder.coltitle.setText(item.getCol_name());

        imageLoader = ImageLoader.getInstance();
        String imageUri = item.getCol_text();
        imageLoader.displayImage(imageUri, viewHolder.imageicon, options);

        return convertView;
    }

    public RealmResults<RealmCollName> getRealmResults() {
        return realmResults;
    }

}
