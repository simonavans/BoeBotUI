package callbacks;

public interface BluetoothCallback {
    void onBluetoothTransmitEvent(String command);
    void onBluetoothReceiveEvent(String command);
}