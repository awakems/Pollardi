package avivi.com.pollardi;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Void on 09.04.2015.
 */
public class ContactsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        Resources res = getResources();
        String[] titles = res.getStringArray(R.array.name_titles);
        String[] phones = res.getStringArray(R.array.phones);
        String[] emails = res.getStringArray(R.array.emails);

        ListView list = (ListView) findViewById(R.id.list_contacts);
        MyAdapter adapter = new MyAdapter(this, titles, phones, emails);
        list.setAdapter(adapter);
    }

    private static class MyAdapter extends BaseAdapter {
        String[] title_a;
        String[] pho_a;
        String[] mai_a;
        Context ctx;
        LayoutInflater inflater;


        private static class ViewHolder {
            TextView title_c;
            TextView tel_c;
            TextView email_c;
            TextView tel_pref;
            ImageView icon;
        }

        private MyAdapter(Context ctx, String[] tit, String[] tel, String[] mail) {
            this.title_a = tit;
            this.pho_a = tel;
            this.mai_a = mail;
            this.ctx = ctx;
            this.inflater = LayoutInflater.from(ctx);
        }



        @Override
        public int getCount() {
            return title_a.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            final ViewHolder viewHolder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_list_contact, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.title_c = (TextView) convertView.findViewById(R.id.text_contact_it_titles);
                viewHolder.tel_c = (TextView) convertView.findViewById(R.id.tel_text);
                viewHolder.email_c = (TextView) convertView.findViewById(R.id.mail_text);
                viewHolder.tel_pref = (TextView) convertView.findViewById(R.id.textView3);
                viewHolder.icon = (ImageView) convertView.findViewById(R.id.imageView);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            if (position % 2 == 0){ convertView.setBackgroundResource(android.R.color.darker_gray);}
                    else {convertView.setBackgroundResource(android.R.color.background_light);}

            if (pho_a[position].equals("empty")){
                viewHolder.tel_c.setVisibility(View.INVISIBLE);
                viewHolder.icon.setVisibility(View.INVISIBLE);
                viewHolder.tel_pref.setVisibility(View.INVISIBLE);
            }else {
                viewHolder.tel_c.setText(pho_a[position]);
                viewHolder.tel_c.setVisibility(View.VISIBLE);
                viewHolder.icon.setVisibility(View.VISIBLE);
                viewHolder.tel_pref.setVisibility(View.VISIBLE);
            }

            viewHolder.title_c.setText(title_a[position]);
            viewHolder.email_c.setText(mai_a[position]);

//            viewHolder.tel_c.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent
//                }
//            });

            return convertView;
        }
    }


}
