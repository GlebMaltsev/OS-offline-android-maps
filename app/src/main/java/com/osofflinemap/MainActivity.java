package com.osofflinemap;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;

import uk.co.ordnancesurvey.android.maps.BitmapDescriptor;
import uk.co.ordnancesurvey.android.maps.BitmapDescriptorFactory;
import uk.co.ordnancesurvey.android.maps.CameraPosition;
import uk.co.ordnancesurvey.android.maps.GridPoint;
import uk.co.ordnancesurvey.android.maps.MapProjection;
import uk.co.ordnancesurvey.android.maps.MarkerOptions;
import uk.co.ordnancesurvey.android.maps.OSMap;
import uk.co.ordnancesurvey.android.maps.OSMapOptions;
import uk.co.ordnancesurvey.android.maps.OSTileSource;
import uk.co.ordnancesurvey.android.maps.SupportMapFragment;

public class MainActivity extends AppCompatActivity implements OSMap.OnMapClickListener {

    private static final String OSTILE_FOLDER = "/maps";
    private static final String[] LAYERS = new String[]{"50K-660DPI", "50K-165DPI"};

    private OSMap osMap;
    private SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        OSMapOptions options = new OSMapOptions();
        options.products(LAYERS);

        FragmentManager fragmentManager = getSupportFragmentManager();
        mapFragment = (SupportMapFragment) fragmentManager.findFragmentById(R.id.map_container);
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance(options);
            fragmentManager.beginTransaction().replace(R.id.map_container, mapFragment).commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        setUpSources();
        goToTile();
    }

    private void goToTile() {
        MapProjection mapProjection = MapProjection.getDefault();
        GridPoint localPark = mapProjection.toGridPoint(53.385000, -2.936567);
        setMarker(localPark);
        osMap.moveCamera(new CameraPosition(localPark, 1f), true);
    }

    private void setUpMapIfNeeded() {
        if (osMap == null) {
            osMap = mapFragment.getMap();
            osMap.setOnMapClickListener(this);
        }
    }

    private void setUpSources() {
        ArrayList<OSTileSource> sources = new ArrayList<>();
        sources.addAll(osMap.localTileSourcesInDirectory(this,
                new File(Environment.getExternalStorageDirectory().getPath() + OSTILE_FOLDER)));
        osMap.setTileSources(sources);
    }

    @Override
    public boolean onMapClick(GridPoint gridPoint) {
        setMarker(gridPoint);
        return true;
    }

    private void setMarker(GridPoint gridPoint) {
        String locationMessage = String.format("Map tapped at OS GridPoint\n{%.0f, %.0f}", gridPoint.x, gridPoint.y);
        BitmapDescriptor icon = BitmapDescriptorFactory.defaultMarker();
        osMap.addMarker(new MarkerOptions()
                .gridPoint(gridPoint)
                .title("Map clicked here!")
                .snippet(locationMessage)
                .icon(icon));
    }
}
