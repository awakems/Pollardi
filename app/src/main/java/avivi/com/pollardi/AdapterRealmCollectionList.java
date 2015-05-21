package avivi.com.pollardi;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;

/**
 * Created by Void on 12.05.2015.
 */
public class AdapterRealmCollectionList extends RealmBaseAdapter<RealmColl> implements ListAdapter {
    public AdapterRealmCollectionList(Context context, RealmResults<RealmColl> realmResults, boolean automaticUpdate) {
        super(context, realmResults, automaticUpdate);
    }

    private static class ViewHolder {
        TextView acc_title;
        TextView acc_desc;
        TextView acc_id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_accessor_list, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.acc_title = (TextView) convertView.findViewById(R.id.title_acc);
            viewHolder.acc_desc = (TextView) convertView.findViewById(R.id.descript_acc);
            viewHolder.acc_id = (TextView) convertView.findViewById(R.id.accIDtv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        RealmColl item = realmResults.get(position);

//        Typeface type = Typeface.createFromAsset(context.getAssets(),"fonts/fine.ttf");
//        viewHolder.coltitle.setTypeface(type);
        viewHolder.acc_title.setText(item.getColl_name());
        viewHolder.acc_desc.setText(Html.fromHtml(item.getColl_det_text()));
        viewHolder.acc_id.setText(item.getColl_id());


        return convertView;
    }
}
