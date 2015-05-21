package avivi.com.pollardi;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Void on 05.05.2015.
 */
public class NewsDetailActivity extends Activity {

    private Realm realm;
    TextView textViewTitle;
    TextView textViewText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        setTitle("Новости");

        realm = Realm.getInstance(this);
        textViewTitle = (TextView) findViewById(R.id.title_news_d);
        textViewText = (TextView) findViewById(R.id.text_news_d);

        String tester = getIntent().getExtras().getString("title");

        RealmResults<RealmCollName> query = realm.where(RealmCollName.class)
                .contains("col_name", tester)
                .findAll();

        textViewTitle.setText(tester);
        textViewText.setText(Html.fromHtml(query.first().getCol_det_text()));

        realm.close();

    }
}
