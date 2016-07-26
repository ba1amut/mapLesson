package com.speedsumm.bu.gba2l3;

import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.CellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

import java.util.List;

/**
 * Created by bu on 26.07.2016.
 */
public class PhoneListener extends PhoneStateListener{


    @Override
    public void onCellInfoChanged(List<CellInfo> cellInfo) {
        super.onCellInfoChanged(cellInfo);
        Log.d(".....", "onCellInfoChanged: " + cellInfo);

    }

    @Override
    public void onSignalStrengthsChanged(SignalStrength signalStrength) {
        super.onSignalStrengthsChanged(signalStrength);
        Log.d(".....", "signalStrength: " + signalStrength);
    }
}
