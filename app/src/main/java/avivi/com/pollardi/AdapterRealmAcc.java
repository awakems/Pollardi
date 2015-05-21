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
 * Created by Void on 06.05.2015.
 */
public class AdapterRealmAcc  extends RealmBaseAdapter<RealmAcces> implements ListAdapter {

    public AdapterRealmAcc(Context context, RealmResults<RealmAcces> realmResults, boolean automaticUpdate) {
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


        RealmAcces item = realmResults.get(position);

//        Typeface type = Typeface.createFromAsset(context.getAssets(),"fonts/fine.ttf");
//        viewHolder.coltitle.setTypeface(type);
        viewHolder.acc_title.setText(item.getAcc_title());
        viewHolder.acc_desc.setText(Html.fromHtml(item.getAcc_desc()));
        viewHolder.acc_id.setText(item.getId());


        return convertView;
    }
}
