package frontend.dialogwindows;

import frontend.MainView;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import jssc.SerialPortList;

/**
 * Class that opens a window when the application launches asking the user to select a COMPORT
 */
public class SetComPortDialog {

    private Dialog mainDialog;

    /**
     * Configures a dialog box that allows the user to set the comport for the bluetooth connection
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
        mainView.setSpacing(10);
        mainView.getChildren().addAll(new Label("Welcome! Please select a COM port to begin:"), comboBox);

        // Create a new Dialog with an OK and CANCEL button and add the main layout
        mainDialog = new Dialog<>();
        mainDialog.setHeaderText(null);
        mainDialog.setGraphic(null);
        mainDialog.setTitle("Set COMPORT");

        DialogPane dialogPane = mainDialog.getDialogPane();
        ButtonType OkayButton = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
        ButtonType CancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialogPane.getButtonTypes().addAll(OkayButton, CancelButton);
        dialogPane.setContent(mainView);
        dialogPane.getStylesheets().add("applicationStyle.css");

        // Add button functionality
        Button okButton = (Button) mainDialog.getDialogPane().lookupButton(OkayButton);
        okButton.addEventFilter(ActionEvent.ACTION, event -> {
            if (comboBox.getValue() == null) {
                callback.displayError("Please select a COM port");
                event.consume();
            } else {
                callback.getSettingsDialog().comPort = Integer.parseInt(comboBox.getValue().charAt(comboBox.getValue().length() - 1) + "");
                if(!callback.getBluetooth().openPort()) {
                    callback.displayError("Failed to connect bluetooth. It is recommended to select a different COM port or to make sure the bluetooth module is correctly paired to your PC.");
                    event.consume();
                }
            }
        });

        Button closeButton = (Button) mainDialog.getDialogPane().lookupButton(CancelButton);
        closeButton.addEventFilter(ActionEvent.ACTION, event -> Platform.exit());
    }

    /**
     * Getter method that returns the dialog window of this class
     * @return the dialog window of this class
     *
     * @author Kerr
     */
    public Dialog getMainDialog() {return mainDialog; }
}