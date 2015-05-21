package avivi.com.pollardi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * Created by Void on 07.04.2015.
 */
public class TestActivity extends Activity {

    ListView list;
    Context context;
    String [] names;

    int[] imageId = {
            R.drawable.bg_111,
            R.drawable.bg_222,
            R.drawable.bg_333,
            R.drawable.bg_444,
            R.drawable.bg_555,
            R.drawable.bg_666,
            R.drawable.bg_777
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_act);

        context = this;
        list = (ListView) findViewById(R.id.listView);
        names = getResources().getStringArray(R.array.menu_items);

        View footerView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_layout, null, false);
        list.addFooterView(footerView);

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mView = inflater.inflate(R.layout.item_list_main, null);
        final ImageView imageView = (ImageView) mView.findViewById(R.id.imageViewList);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position){
                    case 0:
                        Intent intent3 = new Intent(TestActivity.this, CollectionsListActivity.class);
                        startActivity(intent3);
                        overridePendingTransition(R.anim.slidein, R.anim.slideout);
                        //open new activity or fragment
                        break;
                    case 1:
//                        Intent intent = new Intent(TestActivity.this, SimpleImageActivity.class);
//                        intent.putExtra(Constants.Extra.FRAGMENT_INDEX, ImageGridFragment.INDEX);
//                        intent.putExtra("frag", "this is sparta");
//                        startActivity(intent);
                        Intent intent = new Intent(TestActivity.this, AccessoriesActivity.class);
                        startActivity(intent);


                        overridePendingTransition(R.anim.slidein, R.anim.slideout);
                        break;
                    case 2:
                        Intent intent4 = new Intent(TestActivity.this, FindStoreActivity.class);
                        startActivity(intent4);
                        overridePendingTransition(R.anim.slidein, R.anim.slideout);
                        break;
                    case 3:
//                        Intent intent2 = new Intent(TestActivity.this, MapFragment.class);
//                        startActivity(intent2);

                        Intent intent2 = new Intent(TestActivity.this, CooperationActivity.class);
                        startActivity(intent2);

                        overridePendingTransition(R.anim.slidein, R.anim.slideout);
                        //  fragment=new ProfileActivity();
                        break;
                    case 4:
                        Intent intent6 = new Intent(TestActivity.this, NewsActivity.class);
                        startActivity(intent6);
                        overridePendingTransition(R.anim.slidein, R.anim.slideout);
                        //  fragment=new ProfileActivity();
                        break;
                    case 5:
                        Intent intent7 = new Intent(TestActivity.this, AboutUsActivity.class);
                        startActivity(intent7);
                        overridePendingTransition(R.anim.slidein, R.anim.slideout);
                        //  fragment=new ProfileActivity();
                        break;
                    case 6:
                        Intent intent8 = new Intent(TestActivity.this, ContactsActivity.class);
                        startActivity(intent8);
                        overridePendingTransition(R.anim.slidein, R.anim.slideout);
                        //  fragment=new ProfileActivity();
                        break;
                }

            }
        });

        list.setAdapter(new CustomAdapter(TestActivity.this, imageId, names));

    }

    public void onFBClick(View view)
    {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/daria.karlozi.3?fref=hovercard"));
        startActivity(browserIntent);
    }

    public void onTwitterClick(View view)
    {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/PollardiGroup"));
        startActivity(browserIntent);
    }

    public void onVKClick(View view)
    {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://vk.com/pollardi"));
        startActivity(browserIntent);
    }

    public void onGoogleClick(View view)
    {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/101865313408069619436/posts?hl=ru"));
        startActivity(browserIntent);
    }

    public void onPinClick(View view)
    {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://ru.pinterest.com/pollardifashion/"));
        startActivity(browserIntent);
    }

    public void onInsClick(View view)
    {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://instagram.com/pollardifashiongroup/"));
        startActivity(browserIntent);
    }

    public void onYoutClick(View view)
    {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UCA3bZyEkK4e3GjAMLpjUVrw"));
        startActivity(browserIntent);
    }


}
