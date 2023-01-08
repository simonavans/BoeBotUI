package FrontEnd.DialogWindows;

import BackEnd.Object;
import FrontEnd.MainView;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.Optional;

public class ObjectDialog {

    private MainView callback;
    private Dialog<int[]> mainDialog;
    private GridPane mainLayout;
    private Object object;

    /**
     * Sets up a dialog box that allows the user to add or edit objects
     *
     * @param callback class to which the method should callback
     * @author Kerr
     */
    @SuppressWarnings("unchecked") // All casts are manual and do not cause issues
    public ObjectDialog(MainView callback) {

        // Set the callback
        this.callback = callback;

        // Create four labels and Spinners for the location X and Y and destination X and Y.

        Label locationXLabel = new Label("location (X):");
        Spinner<Integer> locationXSpinner = new Spinner<>();

        Label locationYLabel = new Label("location (Y):");
        Spinner<Integer> locationYSpinner = new Spinner<>();

        Label destinationXLabel = new Label("destination (X):");
        Spinner<Integer> destinationXSpinner = new Spinner<>();

        Label destinationYLabel = new Label("destination (Y):");
        Spinner<Integer> destinationYSpinner = new Spinner<>();

        // Create a new GridPane and add all the previously created labels and spinners to it.
        this.mainLayout = new GridPane();
        mainLayout.setHgap(10);
        mainLayout.setVgap(10);

        mainLayout.add(locationXLabel, 0, 0);
        mainLayout.add(locationYLabel, 0, 1);
        mainLayout.add(locationXSpinner, 1, 0);
        mainLayout.add(locationYSpinner, 1, 1);
        mainLayout.add(destinationXLabel, 0, 2);
        mainLayout.add(destinationYLabel, 0, 3);
        mainLayout.add(destinationXSpinner, 1, 2);
        mainLayout.add(destinationYSpinner, 1, 3);

        // Create a new Dialog with an OK and CANCEL button and add the main layout
        this.mainDialog = new Dialog<>();
        mainDialog.setHeaderText(null);
        mainDialog.setGraphic(null);

        DialogPane dialogPane = mainDialog.getDialogPane();
        ButtonType OkayButton = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
        dialogPane.getButtonTypes().addAll(OkayButton, ButtonType.CANCEL);
        dialogPane.setContent(mainLayout);
        dialogPane.getStylesheets().add("applicationStyle.css");


        // Add button functionality
        Button okButton = (Button) mainDialog.getDialogPane().lookupButton(OkayButton);

        okButton.addEventFilter(ActionEvent.ACTION, event -> {
            int locationX = ((Spinner<Integer>)mainLayout.getChildren().get(2)).getValue();
            int locationY = ((Spinner<Integer>)mainLayout.getChildren().get(3)).getValue();
            int destinationX = ((Spinner<Integer>)mainLayout.getChildren().get(6)).getValue();
            int destinationY = ((Spinner<Integer>)mainLayout.getChildren().get(7)).getValue();


            // If it is not a valid object display an error message and consume the event
            if (!callback.isValidObject(locationX, locationY, destinationX, destinationY, object)) {

                callback.displayError("Invalid locations given, please try again!");
                event.consume();
            }
        });

        mainDialog.setResultConverter(dialogButton -> {
            if (dialogButton == OkayButton) {
                int locationX = ((Spinner<Integer>)mainLayout.getChildren().get(2)).getValue();
                int locationY = ((Spinner<Integer>)mainLayout.getChildren().get(3)).getValue();
                int destinationX = ((Spinner<Integer>)mainLayout.getChildren().get(6)).getValue();
                int destinationY = ((Spinner<Integer>)mainLayout.getChildren().get(7)).getValue();
                return new int[] {locationX, locationY, destinationX, destinationY};
            } else {
                return null;
            }
        });
    }

    /**
     * Opens a dialog box that allows the user to add an object (with location and destination)
     * @return an Array of the format {locationX, locationY, destinationX, destinationY} with the user input
     */
    public Optional<int[]> openAddDialog() {

        mainDialog.setResult(null);

        // Set the ValueFactory for the spinners.
        setSpinnerValues(0, 0, 0, 0);

        // Set the dialog title and open the dialog
        mainDialog.setTitle("Add object");

        this.object = null;
        return mainDialog.showAndWait();
    }

    /**
     * Opens a dialog box that allows the user to edit an object (with location and destination)
     * @param object the object that will be edited
     * @return an Array of the format {locationX, locationY, destinationX, destinationY} with the user input
     *
     * @author Kerr
     */
    public Optional<int[]> openEditDialog(Object object) {
        mainDialog.setResult(null);

        // Set the ValueFactory for the spinners.
        setSpinnerValues(object.getLocationX(), object.getLocationY(), object.getDestinationX(), object.getDestinationY());

        // Set the dialog title and open the dialog
        mainDialog.setTitle("Edit object");

        this.object = object;
        return mainDialog.showAndWait();
    }


    /**
     * Helper method that sets the default value for the spinners of the objectDialog
     * @param locationX the default value for the location X spinner
     * @param locationY the default value for the location Y spinner
     * @param destinationX the default value for the destination X spinner
     * @param destinationY the default value for the destination Y spinner
     *
     * @author Kerr
     */
    @SuppressWarnings("unchecked") // All casts are manual and do not cause issues
    private void setSpinnerValues(int locationX, int locationY, int destinationX, int destinationY) {
        // Location X
        SpinnerValueFactory<Integer> valueFactoryLocationX = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, callback.getSettingsDialog().gridWidth - 1);
        valueFactoryLocationX.setValue(locationX);
        ((Spinner<Integer>)mainLayout.getChildren().get(2)).setValueFactory(valueFactoryLocationX);

        // Location X
        SpinnerValueFactory<Integer> valueFactoryLocationY = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, callback.getSettingsDialog().gridHeight - 1);
        valueFactoryLocationY.setValue(locationY);
        ((Spinner<Integer>)mainLayout.getChildren().get(3)).setValueFactory(valueFactoryLocationY);

        // Location X
        SpinnerValueFactory<Integer> valueFactoryDestinationX = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, callback.getSettingsDialog().gridWidth - 1);
        valueFactoryDestinationX.setValue(destinationX);
        ((Spinner<Integer>)mainLayout.getChildren().get(6)).setValueFactory(valueFactoryDestinationX);

        // Location X
        SpinnerValueFactory<Integer> valueFactoryDestinationY = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, callback.getSettingsDialog().gridHeight - 1);
        valueFactoryDestinationY.setValue(destinationY);
        ((Spinner<Integer>)mainLayout.getChildren().get(7)).setValueFactory(valueFactoryDestinationY);
    }
}