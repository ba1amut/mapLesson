package com.speedsumm.bu.gba2l3;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.CellInfo;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
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
    ArrayList<Marker> hashMarkers;
    PhoneListener phoneListener;
    DBHandler dbHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_layout);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        hashMarkers = new ArrayList<>();
        dbHandler = new DBHandler(this);


//        hashMarkers.add(new Marker(59.8592796, 30.3920956, 236614));
//        hashMarkers.add(new Marker(59.8652349, 30.4039557, 236002));
//        hashMarkers.add(new Marker(59.9592796, 31.3920956, 537715));

        dbHandler.getAllMarkers(hashMarkers);

        phoneListener = new PhoneListener();
        manager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
//        manager.listen(phoneListener, PhoneListener.LISTEN_CELL_INFO);
        manager.listen(phoneListener, PhoneListener.LISTEN_SIGNAL_STRENGTHS);
        manager.listen(phoneListener, PhoneListener.LISTEN_CELL_LOCATION);


//        CellID = (TextView) findViewById(R.id.CellID);
//        LAC = (TextView) findViewById(R.id.LAC);
//        btnCellUpdate = (Button) findViewById(R.id.btnUpddate);
//
//        if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            cellLocation = manager.getCellLocation();
//        } else {
//            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//        }
//        if (cellLocation instanceof GsmCellLocation) {
//            GsmCellLocation gsmCellLocation = (GsmCellLocation) cellLocation;
//            cid = gsmCellLocation.getCid();
//            lac = gsmCellLocation.getLac();
//            String networkOperator = manager.getNetworkOperator();
//            mcc = Integer.parseInt(networkOperator.substring(0, 3));
//            mnc = Integer.parseInt(networkOperator.substring(3));
//            URLStr = "http://mobile.maps.yandex.net/cellid_location/?&cellid=" + cid + "&operatorid=" + mnc + "&countrycode=" + mcc + "&lac=" + lac;
//            GetLocation getLocation;
//            try {
//                getLocation = (GetLocation) new GetLocation().execute(URLStr);
//                String qqq = getLocation.get(2, TimeUnit.SECONDS);
//                Log.d("...", "CELL LAT " + Double.toString(cellLatitude));
//                Log.d("...", "CELL LON " + Double.toString(cellLongitude));
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            } catch (TimeoutException e) {
//                e.printStackTrace();
//            }
//        }
//        btnCellUpdate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                    cellLocation = manager.getCellLocation();
//                } else {
//                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//                }
//                if (cellLocation instanceof GsmCellLocation) {
//                    GsmCellLocation gsmCellLocation = (GsmCellLocation) cellLocation;
//                    cid = gsmCellLocation.getCid();
//                    lac = gsmCellLocation.getLac();
//                    String networkOperator = manager.getNetworkOperator();
//                    mcc = Integer.parseInt(networkOperator.substring(0, 3));
//                    mnc = Integer.parseInt(networkOperator.substring(3));
//                    URLStr = "http://mobile.maps.yandex.net/cellid_location/?&cellid=" + cid + "&operatorid=" + mnc + "&countrycode=" + mcc + "&lac=" + lac;
//                    GetLocation getLocation;
//                    try {
//                        getLocation = (GetLocation) new GetLocation().execute(URLStr);
//                        String qqq = getLocation.get(2, TimeUnit.SECONDS);
//                        Log.d("...", "CELL LAT " + Double.toString(cellLatitude));
//                        Log.d("...", "CELL LON " + Double.toString(cellLongitude));
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    } catch (ExecutionException e) {
//                        e.printStackTrace();
//                    } catch (TimeoutException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//            }
//        });


    }


    @Override
    public void onMapReady(GoogleMap map) {

        for (int i = 0; i < hashMarkers.size(); i++) {
            map.addMarker(new MarkerOptions()

                    .anchor(0.5f, 0.5f)
                    .position(new LatLng(hashMarkers.get(i).getCellLat(), hashMarkers.get(i).getCellLon()))
                    .icon(BitmapDescriptorFactory.fromResource(android.R.drawable.star_off))
                    .title("CELL " + String.valueOf(hashMarkers.get(i).getCellID())));
//            Log.d("....", "Сформрован маркер с кооринатами " + String.valueOf(iterator.next().getCellLat()) + " " + String.valueOf(iterator.next().getCellLon()));

        }
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(hashMarkers.get(hashMarkers.size() - 1).getCellLat(), hashMarkers.get(hashMarkers.size() - 1).getCellLon()), 10));


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        map.getUiSettings().setMyLocationButtonEnabled(false);

    }

    public class PhoneListener extends PhoneStateListener {



        @Override
        public void onCellLocationChanged(CellLocation location) {
            super.onCellLocationChanged(location);
            Toast.makeText(MainActivity.this,"смена базовой соты "+ location,Toast.LENGTH_LONG).show();

            Log.d(".....", "onCellInfoChanged: " + location);

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
            dbHandler.addMMarker(new Marker(cellLatitude, cellLongitude, cid));
            Toast.makeText(MainActivity.this,"в базу добалена информация о вышке "+ String.valueOf(cid)+" "+String.valueOf(cellLatitude)+" "+String.valueOf(cellLongitude),Toast.LENGTH_LONG).show();
            Log.d(".....","в базу добалена информация о вышке "+ String.valueOf(cid)+" "+String.valueOf(cellLatitude)+" "+String.valueOf(cellLongitude));


        }

        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
//            Toast.makeText(MainActivity.this,"смена силы сигнала "+ signalStrength,Toast.LENGTH_LONG).show();

//            Log.d(".....", "signalStrength: " + signalStrength);
        }


    }
}





