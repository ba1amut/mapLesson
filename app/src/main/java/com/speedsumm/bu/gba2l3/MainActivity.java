package com.speedsumm.bu.gba2l3;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    LocationManager locationManager;
    GoogleMap map;
    TextView CellID;
    TextView LAC;
    Location location;

    String URLStr;
    CellLocation cellLocation;
    TelephonyManager manager;
    int cid;
    int lac;
    int mcc;
    int mnc;
    static double cellLatitude;
    static double cellLongitude;
    Button btnCellUpdate;
    HashSet<Marker> hashMarkers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_layout);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
//        map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        hashMarkers = new HashSet<>();

        PhoneListener phoneListener = new PhoneListener();
        manager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        manager.listen(phoneListener, PhoneListener.LISTEN_CELL_INFO);
        manager.listen(phoneListener, PhoneListener.LISTEN_SIGNAL_STRENGTHS);
        CellID = (TextView) findViewById(R.id.CellID);
        LAC = (TextView) findViewById(R.id.LAC);
        btnCellUpdate = (Button) findViewById(R.id.btnUpddate);
        btnCellUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    cellLocation = manager.getCellLocation();
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }
                if (cellLocation instanceof GsmCellLocation) {
                    GsmCellLocation gsmCellLocation = (GsmCellLocation) cellLocation;
                    cid = gsmCellLocation.getCid();
                    lac = gsmCellLocation.getLac();
                    String networkOperator = manager.getNetworkOperator();
                    mcc = Integer.parseInt(networkOperator.substring(0, 3));
                    mnc = Integer.parseInt(networkOperator.substring(3));
                    URLStr = "http://mobile.maps.yandex.net/cellid_location/?&cellid=" + cid + "&operatorid=" + mnc + "&countrycode=" + mcc + "&lac=" + lac;
                    GetLocation getLocation;
                    try {
                        getLocation = (GetLocation) new GetLocation().execute(URLStr);
                        String qqq = getLocation.get(2, TimeUnit.SECONDS);
                        Log.d("...", "CELL LAT " + Double.toString(cellLatitude));
                        Log.d("...", "CELL LON " + Double.toString(cellLongitude));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    }
                }

            }
        });


    }


    @Override
    public void onMapReady(GoogleMap map) {
//        LatLng sydney = new LatLng(-33.867, 151.206);

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(cellLatitude, cellLongitude), 12));
        map.addMarker(new MarkerOptions()
                .anchor(0.0f, 0.0f)
                .position(new LatLng(cellLatitude, cellLongitude))
                .icon(BitmapDescriptorFactory.fromResource(android.R.drawable.star_on))
                .title("CELL " + String.valueOf(cid)));


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        map.getUiSettings().setMyLocationButtonEnabled(false);


//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));
//
//        map.addMarker(new MarkerOptions()
//                .title("Sydney")
//                .snippet("The most populous city in Australia.")
//                .position(sydney));
    }


}



