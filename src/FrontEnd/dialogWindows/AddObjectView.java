package FrontEnd.dialogWindows;

import BackEnd.Object;
import FrontEnd.MainView;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.Objects;

public class AddObjectView {

    /**
     * Opens a dialog box that allows the user to add an object (with location and destination)
     * @param callback class to which the method should callback
     */
    public static void addNodeDialog(MainView callback) {
        addNodeDialog(callback, null);
    }

    /**
     * Opens a dialog box that allows the user to add or edit an object (with location and destination)
     * @param callback class to which the method should callback
     * @param object the object that needs to be edited (null if a new object will be made)
     *
     * @author Kerr
     */
    @SuppressWarnings("Duplicates") //TODO Lots of duplicate code in this section, might be worth it to fix
    public static void addNodeDialog(MainView callback, Object object) {

        // Create four labels and Spinners for the location X and Y and destination X and Y. Set the ValueFactory for
        // the spinners. If the parameter index is not -1, an existing object is to be changed and the spinner value is
        // set to the current value of that object.

        Label locationXLabel = new Label("location (X):");
        Spinner<Integer> locationXSpinner = new Spinner<>();
        SpinnerValueFactory<Integer> valueFactoryLocationX = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, callback.getSettingsView().gridWidth - 1);
        valueFactoryLocationX.setValue((Objects.isNull(object)) ? 0 : object.getLocationX());
        locationXSpinner.setValueFactory(valueFactoryLocationX);

        Label locationYLabel = new Label("location (Y):");
        Spinner<Integer> locationYSpinner = new Spinner<>();
        SpinnerValueFactory<Integer> valueFactoryLocationY = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, callback.getSettingsView().gridHeight - 1);
        valueFactoryLocationY.setValue((Objects.isNull(object)) ? 0 : object.getLocationY());
        locationYSpinner.setValueFactory(valueFactoryLocationY);

        Label destinationXLabel = new Label("destination (X):");
        Spinner<Integer> destinationXSpinner = new Spinner<>();
        SpinnerValueFactory<Integer> valueFactoryDestinationX = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, callback.getSettingsView().gridWidth - 1);
        valueFactoryDestinationX.setValue((Objects.isNull(object)) ? 0 : object.getDestinationX());
        destinationXSpinner.setValueFactory(valueFactoryDestinationX);

        Label destinationYLabel = new Label("destination (Y):");
        Spinner<Integer> destinationYSpinner = new Spinner<>();
        SpinnerValueFactory<Integer> valueFactoryDestinationY = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, callback.getSettingsView().gridHeight - 1);
        valueFactoryDestinationY.setValue((Objects.isNull(object)) ? 0 : object.getDestinationY());
        destinationYSpinner.setValueFactory(valueFactoryDestinationY);

        // Create a new GridPane and add all the previously created labels and spinners to it.
        GridPane mainView = new GridPane();
        mainView.setHgap(10);
        mainView.setVgap(10);

        mainView.add(locationXLabel, 0, 0);
        mainView.add(locationYLabel, 0, 1);
        mainView.add(locationXSpinner, 1, 0);
        mainView.add(locationYSpinner, 1, 1);
        mainView.add(destinationXLabel, 0, 2);
        mainView.add(destinationYLabel, 0, 3);
        mainView.add(destinationXSpinner, 1, 2);
        mainView.add(destinationYSpinner, 1, 3);

        // Create a new Dialog with an OK and CANCEL button and add the main layout
        Dialog dialog = new Dialog<>();
        dialog.setHeaderText(null);
        dialog.setGraphic(null);

        if (Objects.isNull(object)) {
            dialog.setTitle("Add object");
        } else {
            dialog.setTitle("Edit object");
        }


        DialogPane dialogPane = dialog.getDialogPane();
        ButtonType OkayButton = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
        dialogPane.getButtonTypes().addAll(OkayButton, ButtonType.CANCEL);
        dialogPane.setContent(mainView);

        // Add button functionality
        Button okButton = (Button) dialog.getDialogPane().lookupButton(OkayButton);
        okButton.addEventFilter(ActionEvent.ACTION, event -> {

            boolean succeeded = callback.onAddObjectEvent(locationXSpinner.getValue(), locationYSpinner.getValue(), destinationXSpinner.getValue(), destinationYSpinner.getValue(), object);

            if (!succeeded) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Invalid locations given, please try again!");
                errorAlert.showAndWait();
                event.consume();
            }
        });

        // Show the dialog and wait until the user has pressed cancel or okay
        dialog.showAndWait();
    }
}
