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

    private static final String[] LAYERS = new String[]{"50K-660DPI", "50K-165DPI"};
    private static final String TILE_DIRECTORY = Environment.getExternalStorageDirectory().getPath() + "/OSofflineMap/tiles";
    private static final String TILE_NAME = "50K_10KSJ38.ostiles";
    private static final double TILE_LON = 53.385000;
    private static final double TILE_LAT = -2.936567;

    private OSMap osMap;
    private SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        mapFragment = (SupportMapFragment) fragmentManager.findFragmentById(R.id.map_container);
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance(new OSMapOptions().products(LAYERS));
            fragmentManager.beginTransaction().replace(R.id.map_container, mapFragment).commit();
        }
    }

    @Override
    protected void onResume() {
        setUpMapIfNeeded();
        setUpSources();
        goToTile();
        super.onResume();
    }

    private void setUpMapIfNeeded() {
        if (osMap == null) {
            osMap = mapFragment.getMap();
            osMap.setOnMapClickListener(this);
        }
    }

    private void setUpSources() {
        ArrayList<OSTileSource> sources = new ArrayList<>();
        File tileDirectory = new File(TILE_DIRECTORY);
        tileDirectory.mkdirs();
        File tile = new File(tileDirectory.getPath(), TILE_NAME);
        AssetsUtils.getFileFromAssets(this, TILE_NAME, tile);
        sources.addAll(osMap.localTileSourcesInDirectory(this, tileDirectory));
        osMap.setTileSources(sources);
    }

    private void goToTile() {
        MapProjection mapProjection = MapProjection.getDefault();
        GridPoint localPark = mapProjection.toGridPoint(TILE_LON, TILE_LAT);
        setMarker(localPark);
        osMap.moveCamera(new CameraPosition(localPark, 1f), true);
    }

    @Override
    public boolean onMapClick(GridPoint gridPoint) {
        setMarker(gridPoint);
        return true;
    }

    private void setMarker(GridPoint gridPoint) {
        BitmapDescriptor icon = BitmapDescriptorFactory.defaultMarker();
        osMap.addMarker(new MarkerOptions()
                .gridPoint(gridPoint)
                .snippet(gridPoint.x + "; " + gridPoint.y)
                .icon(icon));
    }
}
