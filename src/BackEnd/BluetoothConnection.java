package BackEnd;

import FrontEnd.MainView;
import javafx.application.Platform;
import jssc.*;

/**
 * Class that controls the bluetooth connection with the boebot
 * Created with help from https://www.codeproject.com/Tips/801262/Sending-and-receiving-strings-from-COM-port-via-jS
 */
public class BluetoothConnection {

    private MainView callback;
    private SerialPort serialPort;
    private boolean isDisabled = false;

    private String receivedCommand = "";

    /**
     * Constructor of the class BluetoothConnection that is responsible for receiving and transmitting data over
     * bluetooth using a bluetooth module on the roboto
     * @param callback class to which the method should callback
     *
     * @author Kerr
     */
    public BluetoothConnection(MainView callback) {
        this.callback = callback;
    }

    /**
     * Method that opens a bluetooth connection using a Baudrate of 115200 and the COM port given by the user.
     * @return true = connection was successful or false = connection failed
     *
     * @author Kerr
     */
    public boolean openPort() {

        this.serialPort = new SerialPort("COM" + callback.getSettingsDialog().comPort);

        try {
            // Open the serial connection
            serialPort.openPort();

            // Configure the bluetooth connection
            serialPort.setParams(SerialPort.BAUDRATE_115200,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);

            // Add an event listeners that checks for an incoming signal
            serialPort.addEventListener(new PortReader(), SerialPort.MASK_RXCHAR);
        } catch (SerialPortException e) {
            return false;
        }
        return true;
    }

    /**
     * Method that closes the bluetooth connection.
     *
     * @author Kerr
     */
    public void closePort() {
        try {
            serialPort.closePort();
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method that enables the bluetooth connection.
     *
     * @author Kerr
     */
    public void enableBluetooth() {
        this.isDisabled = false;
    }


    // Transmit methods

    /**
     * Method that sends a signal over bluetooth and then disables the bluetooth connection.
     * Caution: the 'break' button will always function!
     * @param command command that will be send over bluetooth
     *
     * @author Kerr
     */
    public void sendManualControl(String command) {
        if (!isDisabled) {
            sendCommand("Application: " + command);
        }
        isDisabled = true;
    }

    /**
     * Method that sends a signal over bluetooth.
     * @param command command that will be send over bluetooth
     *
     * @author Kerr
     */
    public void sendAutomaticControl(String command) {
        sendCommand("Application: " + command);
    }

    /**
     * Helper method that a given command + prefix over bluetooth
     * @param command prefix + command that will be send over bluetooth
     */
    private void sendCommand(String command) {
            try {
                serialPort.writeString(command);
            } catch (SerialPortException e) {
                e.printStackTrace();
            }
    }

    // Receive methods

    /**
     * Method that only passes on a command once the entire command has been send (the end of an command is marked by *)
     *
     * @author Kerr
     */
    private void receiveCommand() {
        // For as long as the received command does not contain a *, do not do anything. Else, send the command to the callback
        if(receivedCommand.contains("*")) {
            callback.onBluetoothReceiveEvent(receivedCommand.substring(0, receivedCommand.length() - 1));

            // Clear the command
            receivedCommand = "";
        }
    }

    /**
     * helper class that interprets an incoming bluetooth signal. Made with the help of:
     * https://www.codeproject.com/Tips/801262/Sending-and-receiving-strings-from-COM-port-via-j
     * and
     * https://stackoverflow.com/questions/17850191/why-am-i-getting-java-lang-illegalstateexception-not-on-fx-application-thread
     *
     * @author Kerr
     */
    private class PortReader implements SerialPortEventListener {

        @Override
        public void serialEvent(SerialPortEvent event) {
            if (event.isRXCHAR() && event.getEventValue() > 0) {
                try {
                    String receivedData = serialPort.readString(event.getEventValue());
                    receivedCommand += receivedData;
                    Platform.runLater(BluetoothConnection.this::receiveCommand);
                } catch (SerialPortException ex) {
                    System.out.println("Error in receiving string from COM-port: " + ex);
                }
            }
        }
    }
}
