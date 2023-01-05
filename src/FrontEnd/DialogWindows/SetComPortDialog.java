package FrontEnd.DialogWindows;

import FrontEnd.MainView;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import jssc.SerialPortList;

/**
 * Class that opens a window when the application launches asking the user to select a COMPORT
 */
public class SetComPortDialog {

    /**
     * Opens a dialog box that allows the user to set the comport for the bluetooth connection
     * @param callback class to which the method should callback
     *
     * @author Kerr
     */
    public SetComPortDialog(MainView callback) {

        // Get the available COMPORTS
        String[] portNames = SerialPortList.getPortNames();

        // Add a ComboBox containing the available COMPORTS
        ComboBox<String> comboBox = new ComboBox<>();

        for (String string : portNames) {
            comboBox.getItems().add(string);
        }

        // Create the main Layout and add the ComboBox
        VBox mainView = new VBox();
        mainView.getChildren().addAll(new Label("Welcome! Please select a COM port to begin."), comboBox);

        // Create a new Dialog with an OK and CANCEL button and add the main layout
        Dialog dialog = new Dialog<>();
        dialog.setHeaderText(null);
        dialog.setGraphic(null);
        dialog.setTitle("Set COMPORT");

        DialogPane dialogPane = dialog.getDialogPane();
        ButtonType OkayButton = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
        dialogPane.getButtonTypes().addAll(OkayButton);
        dialogPane.setContent(mainView);

        // Add button functionality
        Button okButton = (Button) dialog.getDialogPane().lookupButton(OkayButton);
        okButton.addEventFilter(ActionEvent.ACTION, event -> {


            if (comboBox.getValue() == null) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Please select a COM port");
                errorAlert.showAndWait();
                event.consume();
            } else {
                callback.getSettingsDialog().comPort = Integer.parseInt(comboBox.getValue().charAt(comboBox.getValue().length() - 1) + "");
                if(!callback.getBluetoothConnection().openPort()) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("Failed to connect bluetooth. It is recommended\nto select a different COM port or to make sure the\nbluetooth module is correctly paired to your PC");
                    errorAlert.showAndWait();
                    event.consume();
                }
            }
        });

//        dialog.setOnCloseRequest(event -> Platform.exit()); //TODO fix this

        // Show the dialog and wait until the user has pressed cancel or okay
        dialog.showAndWait();
    }

}
