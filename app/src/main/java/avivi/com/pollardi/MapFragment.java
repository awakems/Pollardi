package avivi.com.pollardi;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Void on 06.04.2015.
 */
public class MapFragment extends FragmentActivity implements OnMapReadyCallback {

    static final LatLng HAMBURG = new LatLng(53.558, 9.927);
    static final LatLng KHMELNITSKIY = new LatLng(49.4254695,26.9856546);

    SupportMapFragment mapFragment;
    GoogleMap map;
    Marker marker;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fr_map);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap map) {
        map.addMarker(new MarkerOptions()
                .position(new LatLng(49.4254695,26.9856546))
                .title("Свадебный салон 'CHERRY' Грушевского 40"));
       // map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
       map.moveCamera(CameraUpdateFactory.newLatLngZoom(KHMELNITSKIY, 6));
        map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
    }
}
