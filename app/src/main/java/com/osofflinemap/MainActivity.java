
package com.osofflinemap;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

import uk.co.ordnancesurvey.android.maps.BitmapDescriptor;
import uk.co.ordnancesurvey.android.maps.BitmapDescriptorFactory;
import uk.co.ordnancesurvey.android.maps.GridPoint;
import uk.co.ordnancesurvey.android.maps.MapFragment;
import uk.co.ordnancesurvey.android.maps.MarkerOptions;
import uk.co.ordnancesurvey.android.maps.OSMap;
import uk.co.ordnancesurvey.android.maps.OSTileSource;

public class MainActivity extends AppCompatActivity implements OSMap.OnMapClickListener {

    private final static String TAG = MainActivity.class.getSimpleName();

    private OSMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MapFragment mapFragment = ((MapFragment) getFragmentManager().findFragmentById(R.id.map_fragment));
        mMap = mapFragment.getMap();

        // set list of tileSources
        ArrayList<OSTileSource> sources = new ArrayList<>();
        sources.addAll(mMap.localTileSourcesInDirectory(this, getLocalMapTilesDirectory()));
        mMap.setTileSources(sources);

        // register as OnMapClickListener
        mMap.setOnMapClickListener(this);
    }

    @Override
    public boolean onMapClick(GridPoint gp) {
        final String locationMessage = String.format("Map tapped at OS GridPoint\n{%.0f, %.0f}", gp.x, gp.y);
        Log.v(TAG, locationMessage);
        BitmapDescriptor icon = BitmapDescriptorFactory.defaultMarker();
        mMap.addMarker(new MarkerOptions()
                .gridPoint(gp)
                .title("Map clicked here!")
                .snippet(locationMessage)
                .icon(icon));
        return true;
    }

    public static File getLocalMapTilesDirectory() {
        return new File(Environment.getExternalStorageDirectory().getPath() + "/spargonet/mapping");
    }
}
