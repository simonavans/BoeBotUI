package frontend.dialogwindows;

import backend.Obstruction;
import frontend.MainView;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.Optional;

public class ObstructionDialog {

    private MainView callback;
    private Dialog<int[]> mainDialog;
    private GridPane mainAddLayout;
    private GridPane mainEditLayout;

   private GridPane currentLayout;
    private Obstruction obstruction;

    /**
     * Sets up a dialog box that allows the user to add, edit or convert obstructions
     *
     * @param callback class to which the method should callback
     * @author Kerr
     */
    @SuppressWarnings("unchecked") // All casts are manual and do not cause issues
    public ObstructionDialog(MainView callback) {

        // Set the callback
        this.callback = callback;

        // Create two labels and Spinners for the location X and Y.

        Label locationXLabel = new Label("location (X):");
        Spinner<Integer> locationXSpinner = new Spinner<>();

        Label locationYLabel = new Label("location (Y):");
        Spinner<Integer> locationYSpinner = new Spinner<>();

        // Create a new GridPane and add all the previously created labels and spinners to it.
        this.mainAddLayout = new GridPane();
        mainAddLayout.setHgap(10);
        mainAddLayout.setVgap(10);

        mainAddLayout.add(locationXLabel, 0, 0);
        mainAddLayout.add(locationYLabel, 0, 1);
        mainAddLayout.add(locationXSpinner, 1, 0);
        mainAddLayout.add(locationYSpinner, 1, 1);


        // Create a secondary layout for editing obstructions
        // Create four labels and Spinners for the location and destination X and Y.
        Label locationXEditLabel = new Label("location (X):");
        Spinner<Integer> locationXEditSpinner = new Spinner<>();

        Label locationYEditLabel = new Label("location (Y):");
        Spinner<Integer> locationEditSpinner = new Spinner<>();

        Label destinationXLabel = new Label("destination (X):");
        destinationXLabel.setDisable(true);
        Spinner<Integer> destinationXSpinner = new Spinner<>();
        destinationXSpinner.setDisable(true);

        Label destinationYLabel = new Label("location (Y):");
        destinationYLabel.setDisable(true);
        Spinner<Integer> destinationYSpinner = new Spinner<>();
        destinationYSpinner.setDisable(true);

        // Add a checkbox that allows a obstruction to be converted to an obstruction
        CheckBox convertObstructionCheckbox = new CheckBox("Convert to obstruction");

        // Create a new GridPane and add all the previously created labels and spinners to it.
        this.mainEditLayout = new GridPane();
        mainEditLayout.setHgap(10);
        mainEditLayout.setVgap(10);

        mainEditLayout.add(locationXEditLabel, 0, 0);
        mainEditLayout.add(locationYEditLabel, 0, 1);
        mainEditLayout.add(locationXEditSpinner, 1, 0);
        mainEditLayout.add(locationEditSpinner, 1, 1);

        mainEditLayout.add(convertObstructionCheckbox, 0, 2);

        mainEditLayout.add(destinationXLabel, 0, 3);
        mainEditLayout.add(destinationYLabel, 0, 4);
        mainEditLayout.add(destinationXSpinner, 1, 3);
        mainEditLayout.add(destinationYSpinner, 1, 4);

        // Create a new Dialog with an OK and CANCEL button and add the main layout
        this.mainDialog = new Dialog<>();
        mainDialog.setHeaderText(null);
        mainDialog.setGraphic(null);

        DialogPane dialogPane = mainDialog.getDialogPane();
        ButtonType OkayButton = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
        dialogPane.getButtonTypes().addAll(OkayButton, ButtonType.CANCEL);
        dialogPane.getStylesheets().add("applicationStyle.css");

        // Add button functionality
        Button okButton = (Button) mainDialog.getDialogPane().lookupButton(OkayButton);

        okButton.addEventFilter(ActionEvent.ACTION, event -> {

            int locationX = ((Spinner<Integer>) currentLayout.getChildren().get(2)).getValue();
            int locationY = ((Spinner<Integer>) currentLayout.getChildren().get(3)).getValue();

            // If the obstruction is to be converted to an object, check if it will be a valid object, else check if the
            // created/edited obstruction is valid
            if (convertObstructionCheckbox.isSelected()) {
                int destinationX = ((Spinner<Integer>) currentLayout.getChildren().get(7)).getValue();
                int destinationY = ((Spinner<Integer>) currentLayout.getChildren().get(8)).getValue();

                // If it is not a valid object display an error message and consume the event
                if (!callback.isValidConversion(locationX, locationY, destinationX, destinationY, obstruction)) {
                    callback.displayError("Invalid locations given, please try again!");
                    event.consume();
                }
            } else {
                // If it is not a valid obstruction display an error message and consume the event
                if (!callback.isValidObstruction(locationX, locationY, obstruction)) {
                    callback.displayError("Invalid location given, please try again!");
                    event.consume();
                }
            }
        });

        mainDialog.setResultConverter(dialogButton -> {
            if (dialogButton == OkayButton) {
                boolean checkboxIsSelected = convertObstructionCheckbox.isSelected();
                convertObstructionCheckbox.setSelected(false);

                int locationX = ((Spinner<Integer>) currentLayout.getChildren().get(2)).getValue();
                int locationY = ((Spinner<Integer>) currentLayout.getChildren().get(3)).getValue();

                // If the obstruction is to be converted to an object, return a location and destination else return only
                // the location
                if (checkboxIsSelected) {
                    int destinationX = ((Spinner<Integer>) currentLayout.getChildren().get(7)).getValue();
                    int destinationY = ((Spinner<Integer>) currentLayout.getChildren().get(8)).getValue();

                    return new int[] {locationX, locationY, destinationX, destinationY};

                } else {
                    return new int[]{locationX, locationY};
                }
            } else {
                return null;
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
    }

    /**
     * Opens a dialog box that allows the user to add an obstruction (with location and destination)
     *
     * @return an Array of the format {locationX, locationY, destinationX, destinationY} with the user input
     */

    public Optional<int[]> openAddDialog() {
        mainDialog.setResult(null);
        currentLayout = mainAddLayout;

        // Set the ValueFactory for the spinners.
        setSpinnerValues(0, 0);

        // Set the dialog title and open the dialog
        mainDialog.setTitle("Add obstruction");
        mainDialog.getDialogPane().setContent(mainAddLayout);

        this.obstruction = null;
        return mainDialog.showAndWait();
    }

    /**
     * Opens a dialog box that allows the user to edit an obstruction (with location and destination)
     *
     * @param obstruction the obstruction that will be edited
     * @return an Array of the format {locationX, locationY, destinationX, destinationY} with the user input
     * @author Kerr
     */
    public Optional<int[]> openEditDialog(Obstruction obstruction) {
        mainDialog.setResult(null);
        currentLayout = mainEditLayout;

        // Set the ValueFactory for the spinners.
        setSpinnerValues(obstruction.getLocationX(), obstruction.getLocationY());

        // Reset the checkbox and destination spinners
        ((CheckBox) mainEditLayout.getChildren().get(4)).setSelected(false);
        mainEditLayout.getChildren().get(5).setDisable(true);
        mainEditLayout.getChildren().get(6).setDisable(true);
        mainEditLayout.getChildren().get(7).setDisable(true);
        mainEditLayout.getChildren().get(8).setDisable(true);

        // Set the dialog title and open the dialog
        mainDialog.setTitle("Edit obstruction");


        mainDialog.getDialogPane().setContent(mainEditLayout);

        this.obstruction = obstruction;
        return mainDialog.showAndWait();
    }


    /**
     * Helper method that sets the default value for the spinners of the obstructionDialog
     *
     * @param locationX the default value for the location X spinner
     * @param locationY the default value for the location Y spinner
     * @author Kerr
     */
    @SuppressWarnings("unchecked") // All casts are manual and do not cause issues
    private void setSpinnerValues(int locationX, int locationY) {

        if (currentLayout.equals(mainAddLayout)) {
            // Location X
            SpinnerValueFactory<Integer> valueFactoryLocationX = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, callback.getSettingsDialog().gridWidth - 1);
            valueFactoryLocationX.setValue(locationX);
            ((Spinner<Integer>) mainAddLayout.getChildren().get(2)).setValueFactory(valueFactoryLocationX);

            // Location X
            SpinnerValueFactory<Integer> valueFactoryLocationY = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, callback.getSettingsDialog().gridHeight - 1);
            valueFactoryLocationY.setValue(locationY);
            ((Spinner<Integer>) mainAddLayout.getChildren().get(3)).setValueFactory(valueFactoryLocationY);

        } else {
            // Location X
            SpinnerValueFactory<Integer> valueFactoryLocationX = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, callback.getSettingsDialog().gridWidth - 1);
            valueFactoryLocationX.setValue(locationX);
            ((Spinner<Integer>) mainEditLayout.getChildren().get(2)).setValueFactory(valueFactoryLocationX);

            // Location y
            SpinnerValueFactory<Integer> valueFactoryLocationY = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, callback.getSettingsDialog().gridHeight - 1);
            valueFactoryLocationY.setValue(locationY);
            ((Spinner<Integer>) mainEditLayout.getChildren().get(3)).setValueFactory(valueFactoryLocationY);

            // Destination X
            SpinnerValueFactory<Integer> valueFactoryDestinationX = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, callback.getSettingsDialog().gridWidth - 1);
            valueFactoryDestinationX.setValue(0);
            ((Spinner<Integer>) mainEditLayout.getChildren().get(7)).setValueFactory(valueFactoryDestinationX);

            // Destination Y
            SpinnerValueFactory<Integer> valueFactoryDestinationY = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, callback.getSettingsDialog().gridHeight - 1);
            valueFactoryDestinationY.setValue(0);
            ((Spinner<Integer>) mainEditLayout.getChildren().get(8)).setValueFactory(valueFactoryDestinationY);
        }
    }
}