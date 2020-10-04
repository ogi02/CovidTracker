package cc.holdinga.covidtracker.utils;

import android.bluetooth.BluetoothAdapter;

public class BluetoothUtils {
    public static final String currentDeviceName = getCurrentDeviceName();

    private static String getCurrentDeviceName() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            return null;
        }
        return bluetoothAdapter.getName();
    }
}
