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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.NodeList;

import java.net.MalformedURLException;
import java.net.URL;
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
    URL url1;

    NodeList nodeList;
    CellLocation cellLocation;
    TelephonyManager manager;
    int cid;
    int lac;
    int mcc;
    int mnc;
    static double cellLatitude;
    static double cellLongitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_layout);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        CellID = (TextView) findViewById(R.id.CellID);
        LAC = (TextView) findViewById(R.id.LAC);
        manager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            cellLocation = manager.getCellLocation();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        if (cellLocation instanceof GsmCellLocation) {
            GsmCellLocation gsmCellLocation = (GsmCellLocation) cellLocation;
            cid = gsmCellLocation.getCid();
            lac = gsmCellLocation.getLac();
            String networkOperator = manager.getNetworkOperator();
            mcc = Integer.parseInt(networkOperator.substring(0, 3));
            mnc = Integer.parseInt(networkOperator.substring(3));
            URLStr = "http://mobile.maps.yandex.net/cellid_location/?&cellid=" + cid + "&operatorid=" + mnc + "&countrycode=" + mcc + "&lac=" + lac;

        }

        GetLocation getLocation;
        try {
            getLocation = (GetLocation) new GetLocation().execute(URLStr);
            Log.d("....", "Dsta from " + cellLongitude);
            String qwerqwe = getLocation.get(1, TimeUnit.SECONDS);
            Log.d("....", "Dsta from1 " + qwerqwe);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            Toast.makeText(this,"Не удалось получить даннеы с сервера",Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }


    }

//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        String provider = LocationManager.PASSIVE_PROVIDER;
//        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            location = locationManager.getLastKnownLocation(provider);
//        }else{
//            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},1);
//        }
//
//            CellID.setText(Double.toString(location.getLatitude()));
//            LAC.setText(Double.toString(location.getLongitude()));
////            dqdqwd = new PhoneStateListener(new )


    @Override
    public void onMapReady(GoogleMap map) {
//        LatLng sydney = new LatLng(-33.867, 151.206);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(cellLatitude, cellLongitude), 14));
        map.addMarker(new MarkerOptions()
                .anchor(0.0f, 1.0f)
                .position(new LatLng(cellLatitude, cellLongitude)));


//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            map.setMyLocationEnabled(true);
//        } else {
//            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//        }
//        map.getUiSettings().setMyLocationButtonEnabled(false);


//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));
//
//        map.addMarker(new MarkerOptions()
//                .title("Sydney")
//                .snippet("The most populous city in Australia.")
//                .position(sydney));
    }


}



