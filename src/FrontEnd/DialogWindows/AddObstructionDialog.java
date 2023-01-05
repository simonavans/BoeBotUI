package FrontEnd.DialogWindows;

import BackEnd.Obstruction;
import FrontEnd.MainView;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.Objects;

public class AddObstructionDialog {

    /**
     * Opens a dialog box that allows the user to add an Obstruction (with location)
     * @param callback class to which the method should callback
     */
    public static void addNodeDialog(MainView callback) {
        addNodeDialog(callback, null);
    }

    /**
     * Opens a dialog box that allows the user to add or edit an obstruction (with location)
     * @param callback class to which the method should callback
     * @param obstruction the obstruction that needs to be edited (null if a new obstruction will be made)
     *
     * @author Kerr
     */
    @SuppressWarnings("Duplicates") //TODO Lots of duplicate code in this section, might be worth it to fix
    public static void addNodeDialog(MainView callback, Obstruction obstruction) {

        // Create two labels and Spinners for the location X and Y. Set the ValueFactory for
        // the spinners. If the parameter Object is not null, an existing obstruction is to be changed and the spinner value is
        // set to the current value of that obstruction.
        Label locationXLabel = new Label("location (X):");
        Spinner<Integer> locationXSpinner = new Spinner<>();
        SpinnerValueFactory<Integer> valueFactoryLocationX = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, callback.getSettingsDialog().gridWidth - 1);
        valueFactoryLocationX.setValue((Objects.isNull(obstruction)) ? 0 : obstruction.getLocationX());
        locationXSpinner.setValueFactory(valueFactoryLocationX);

        Label locationYLabel = new Label("location (Y):");
        Spinner<Integer> locationYSpinner = new Spinner<>();
        SpinnerValueFactory<Integer> valueFactoryLocationY = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, callback.getSettingsDialog().gridHeight - 1);
        valueFactoryLocationY.setValue((Objects.isNull(obstruction)) ? 0 : obstruction.getLocationY());
        locationYSpinner.setValueFactory(valueFactoryLocationY);

        // Create a new GridPane and add all the previously created labels and spinners to it.
        GridPane mainView = new GridPane();
        mainView.setHgap(10);
        mainView.setVgap(10);

        mainView.add(locationXLabel, 0, 0);
        mainView.add(locationYLabel, 0, 1);
        mainView.add(locationXSpinner, 1, 0);
        mainView.add(locationYSpinner, 1, 1);

        // Create a new Dialog with an OK and CANCEL button and add the main layout
        Dialog dialog = new Dialog<>();
        dialog.setHeaderText(null);
        dialog.setGraphic(null);

        // If an existing obstruction is to be edited, add a few more UI elements
        // Add a checkbox that allows a obstruction to be converted to an object
        CheckBox convertObstructionCheckbox = new CheckBox("Convert to object");

        // Create two labels and Spinners for the destination X and Y. Set the ValueFactory for
        // the spinners.
        Label destinationXLabel = new Label("destination (X):");
        destinationXLabel.setDisable(true);
        Spinner<Integer> destinationXSpinner = new Spinner<>();
        SpinnerValueFactory<Integer> valueFactoryDestinationX = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, callback.getSettingsDialog().gridWidth - 1);
        valueFactoryDestinationX.setValue((0));
        destinationXSpinner.setValueFactory(valueFactoryDestinationX);
        destinationXSpinner.setDisable(true);

        Label destinationYLabel = new Label("location (Y):");
        destinationYLabel.setDisable(true);
        Spinner<Integer> destinationYSpinner = new Spinner<>();
        SpinnerValueFactory<Integer> valueFactoryDestinationY = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, callback.getSettingsDialog().gridHeight - 1);
        valueFactoryDestinationY.setValue((0));
        destinationYSpinner.setValueFactory(valueFactoryDestinationY);
        destinationYSpinner.setDisable(true);


        if (Objects.isNull(obstruction)) {
            dialog.setTitle("Add obstruction");
        } else {
            dialog.setTitle("Edit obstruction");
            // Add the spinners and labels to the existing mainView
            mainView.add(convertObstructionCheckbox,0 , 2);
            mainView.add(destinationXLabel, 0, 3);
            mainView.add(destinationYLabel, 0, 4);
            mainView.add(destinationXSpinner, 1, 3);
            mainView.add(destinationYSpinner, 1, 4);
        }

        DialogPane dialogPane = dialog.getDialogPane();
        ButtonType OkayButton = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
        dialogPane.getButtonTypes().addAll(OkayButton, ButtonType.CANCEL);
        dialogPane.setContent(mainView);

        // Add button functionality
        Button okButton = (Button) dialog.getDialogPane().lookupButton(OkayButton);
        okButton.addEventFilter(ActionEvent.ACTION, event -> {
            boolean succeeded;

            // If an the obstruction is to be converted to an object, try to convert it, else add/edit the obstruction
            if (convertObstructionCheckbox.isSelected()) {
                succeeded = callback.onAddObjectEvent(locationXSpinner.getValue(), locationYSpinner.getValue(), destinationXSpinner.getValue(), destinationYSpinner.getValue(), obstruction);
            } else {
                succeeded = callback.onAddObstructionEvent(locationXSpinner.getValue(), locationYSpinner.getValue(), obstruction);
            }
            if (!succeeded) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Invalid location given, please try again!");
                errorAlert.showAndWait();
                event.consume();
            }
        });


        // Add checkbox functionality
        convertObstructionCheckbox.setOnAction(e -> {
            // if the destination label is disabled, enable all destination spinners/labels and vice versa
            boolean isEnabled = destinationXLabel.isDisabled();
            destinationXLabel.setDisable(!isEnabled);
            destinationXSpinner.setDisable(!isEnabled);
            destinationYLabel.setDisable(!isEnabled);
            destinationYSpinner.setDisable(!isEnabled);
        });

        // Show the dialog and wait until the user has pressed cancel or okay
        dialog.showAndWait();
    }
}
