package callbacks;

public interface bluetoothCallback {
    void onBluetoothTransmitEvent(String command);
    void onBluetoothReceiveEvent(String command);
}