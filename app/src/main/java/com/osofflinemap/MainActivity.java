package com.osofflinemap;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;

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

    private static final String OSTILE_FOLDER = "/maps";

    private OSMap osMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map_fragment);
        osMap = mapFragment.getMap();

        ArrayList<OSTileSource> sources = new ArrayList<>();
        sources.addAll(osMap.localTileSourcesInDirectory(this,
                new File(Environment.getExternalStorageDirectory().getPath() + OSTILE_FOLDER)));
        osMap.setTileSources(sources);
        osMap.setOnMapClickListener(this);
    }

    @Override
    public boolean onMapClick(GridPoint gridPoint) {
        String locationMessage = String.format("Map tapped at OS GridPoint\n{%.0f, %.0f}", gridPoint.x, gridPoint.y);
        BitmapDescriptor icon = BitmapDescriptorFactory.defaultMarker();
        osMap.addMarker(new MarkerOptions()
                .gridPoint(gridPoint)
                .title("Map clicked here!")
                .snippet(locationMessage)
                .icon(icon));
        return true;
    }
}
