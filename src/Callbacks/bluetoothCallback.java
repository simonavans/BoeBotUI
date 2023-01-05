package Callbacks;

public interface bluetoothCallback {
    void onAutomaticControlEvent(String command);
    void onManualControlEvent(String command);
    void onBluetoothReceiveEvent(String command);
}
