package com.speedsumm.bu.gba2l3;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap map;
    TextView CellID;
    TextView LAC;
    final static String LOG = "NET_MONITOR LOG";

    String URLStr;
    CellLocation cellLocation;
    TelephonyManager manager;
    int cid;
    int lac;
    int mcc;
    int mnc;
    static double cellLatitude;
    static double cellLongitude;
    HashSet<Marker> hashMarkers;
    PhoneListener phoneListener;
    DBHandler dbHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_layout);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        hashMarkers = new HashSet<>();
        dbHandler = new DBHandler(this);
        CellID = (TextView) findViewById(R.id.CellID);
        LAC = (TextView) findViewById(R.id.LAC);


        dbHandler.getAllMarkers(hashMarkers);

        phoneListener = new PhoneListener();
        manager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        manager.listen(phoneListener, PhoneListener.LISTEN_SIGNAL_STRENGTHS);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            manager.listen(phoneListener, PhoneListener.LISTEN_CELL_LOCATION);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        this.map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        Iterator<Marker> iterator = hashMarkers.iterator();
        while (iterator.hasNext()) {
            Marker marker = iterator.next();

            map.addMarker(new MarkerOptions()

                    .anchor(0.5f, 0.5f)
                    .position(new LatLng(marker.getCellLat(), marker.getCellLon()))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.point1))
                    .title("CELL " + String.valueOf(marker.getCellID())));
            Log.d(LOG, "Сформрован маркер с кооринатами " + String.valueOf(marker.getCellLat()) + " " + String.valueOf(marker.getCellLon()));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(marker.getCellLat(), marker.getCellLon()), 12));
        }


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
            Log.d(LOG, "onCellInfoChanged: " + location);

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
                    Log.d(LOG, "CELL LAT " + Double.toString(cellLatitude));
                    Log.d(LOG, "CELL LON " + Double.toString(cellLongitude));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }
            boolean baseadd = hashMarkers.add(new Marker(mcc, mnc, lac, cid, cellLatitude, cellLongitude));
            if (baseadd) {
                dbHandler.addMMarker(new Marker(mcc, mnc, lac, cid, cellLatitude, cellLongitude));
                Log.d(LOG, "Обнаружена новая вышка " + cid + ", добавлена в базу");
                Toast.makeText(MainActivity.this, "Поздравляю обнаружена новая вышка " + String.valueOf(cid) + " с координатами " + String.valueOf(cellLatitude) + " " + String.valueOf(cellLongitude), Toast.LENGTH_SHORT).show();
            } else {
                Log.d(LOG, "Обнаруженная вышка " + cid + ", уже присутвует в базе");
            }

//            Toast.makeText(MainActivity.this,"в базу добалена информация о вышке "+ String.valueOf(cid)+" "+String.valueOf(cellLatitude)+" "+String.valueOf(cellLongitude),Toast.LENGTH_LONG).show();
//            Log.d(".....", "в базу добалена информация о вышке " + String.valueOf(cid) + " " + String.valueOf(cellLatitude) + " " + String.valueOf(cellLongitude));
            CellID.setText("Cell ID " + cid);
            LAC.setText("LAC " + lac);


        }

        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
//            Toast.makeText(MainActivity.this,"смена силы сигнала "+ signalStrength,Toast.LENGTH_LONG).show();

//            Log.d(".....", "signalStrength: " + signalStrength);
        }


    }
}





