package avivi.com.pollardi;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

//import android.support.v4.widget.DrawerLayout;

/**
 * Created by Void on 02.04.2015.
 */
public class MainActivity extends Activity {

   // private DrawerLayout mDrawer;
    private ListView mListView;
    private String[] menuItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        mListView = (ListView) findViewById(R.id.left_drawer);
//        menuItems = getResources().getStringArray(R.array.menu_items);
//
//
//        mListView.setAdapter(new ArrayAdapter<String>(this,
//                android.R.layout.simple_list_item_1, menuItems));
//
//
//
//
//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                Fragment fragment=null;
//                switch (position){
//                    case 0:
//                        //open new activity or fragment
//                        break;
//                    case 1:
//                        Intent intent = new Intent(MainActivity.this, SimpleImageActivity.class);
//                        intent.putExtra(Constants.Extra.FRAGMENT_INDEX, ImageGridFragment.INDEX);
//                        startActivity(intent);
//                        break;
//                    case 2:
//                        break;
//                    case 3:
//                      //  fragment=new ProfileActivity();
//                        break;
//                    case 4:
//                        Intent intent2 = new Intent(MainActivity.this, MapFragment.class);
//                        startActivity(intent2);
//                        break;
//                }
//                transaction.replace(R.id.content_fragment, fragment);
//                transaction.commit();
//               mDrawer.closeDrawers();
//            }
//        });
    }
}
