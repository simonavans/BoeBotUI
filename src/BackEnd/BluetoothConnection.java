package BackEnd;

import FrontEnd.MainView;
import jssc.SerialPort;
import jssc.SerialPortException;

import java.util.ArrayList;

public class BluetoothConnection {

    private  MainView callback;
    private SerialPort serialPort;
    private int comPortNumber;

    public BluetoothConnection(MainView callback) {
        this.callback = callback;
        updateBluetoothConnection();

//        try {
//            serialPort.openPort(); // Open the serial connection
//
//            serialPort.setParams(SerialPort.BAUDRATE_115200,
//                    SerialPort.DATABITS_8,
//                    SerialPort.STOPBITS_1,
//                    SerialPort.PARITY_NONE);
//        } catch (
//                SerialPortException e) {
//            e.printStackTrace();
//        }
    }

    public int getComPortNumber() {return comPortNumber;}

    public void updateBluetoothConnection() {
        this.serialPort = new SerialPort("COM" + callback.getSettingsView().comPort);
    }

    public void sendRoute(ArrayList<ArrayList<String>> route) {
//        String instructions = "Route ";
//        for (ArrayList<String> arrayList : route) {
//            for (String instruction : arrayList) {
//                instructions += instruction;
//            }
//        }
//
//        System.out.println(instructions);
//
//        try {
//            serialPort.writeString(instructions);
//        } catch (SerialPortException e) {
//            e.printStackTrace();
//        }
    }

    public void sendManualControl(String command) {
//        try {
//            serialPort.writeString("Control " + command);
//        } catch (SerialPortException e) {
//            e.printStackTrace();
//        }
    }
}
