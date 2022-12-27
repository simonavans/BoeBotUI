package Application;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.Optional;

class AddNodeView {

    // TODO fix errors when canceling
    // TODO fix conditions for getting an error message when nog filling in all values
    // TODO When adding an obstruction, give it a different label (an no destination)
    // TODO make labels functional
    // TODO make error message nicer (preferably in the same window)


    /**
     * Opens a dialog box that allows the user to add an object (with location and destination) or an
     * obstruction (with a location)
     */
    int[] generateAddNodeView() {
        // Create four labels and TextFields for the location X and Y and destination X and Y.
        Label locationXLabel = new Label("location (X):");
        TextField locationXTextfield = new TextField();

        Label locationYLabel = new Label("location (Y):");
        TextField locationYTextfield = new TextField();

        Label destinationXLabel = new Label("destination (X):");
        TextField destinationXTextfield = new TextField();

        Label destinationYLabel = new Label("destination (Y):");
        TextField destinationYTextfield = new TextField();

        // Create a checkbox for setting the object as an obstruction
        CheckBox obstructionCheckBox = new CheckBox("This object is an obstruction");

        // Create a new GridPane and add all the previously created labels and TextFields to it.
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);

        gridPane.add(locationXLabel, 0,0);
        gridPane.add(locationYLabel, 0,1);
        gridPane.add(locationXTextfield, 1,0);
        gridPane.add(locationYTextfield, 1,1);
        gridPane.add(destinationXLabel, 0,2);
        gridPane.add(destinationYLabel, 0,3);
        gridPane.add(destinationXTextfield, 1,2);
        gridPane.add(destinationYTextfield, 1,3);

        // Create the main layout and add all previously created components
        VBox mainView = new VBox();
        mainView.setSpacing(20);
        mainView.getChildren().addAll(gridPane, obstructionCheckBox);


        // Create a new Dialog with an OK and CANCEL button and add the main layout
        Dialog<int[]> dialog = new Dialog<>();
        dialog.setHeaderText(null);
        dialog.setGraphic(null);
        dialog.setTitle("Add object");

        DialogPane dialogPane = dialog.getDialogPane();
        ButtonType OkayButton = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE); //TODO do not fully understand this
        dialogPane.getButtonTypes().addAll(OkayButton, ButtonType.CANCEL);
        dialogPane.setContent(mainView);

        // If the checkbox is selected disable the destination TextFields
        obstructionCheckBox.setOnAction(e -> {
            boolean state = obstructionCheckBox.isSelected();
            destinationXTextfield.setDisable(state);
            destinationXLabel.setDisable(state);
            destinationYTextfield.setDisable(state);
            destinationYLabel.setDisable(state);
        });

        // TODO do not fully understand this
        // Check if all required values are filled in and if not, display an error message
        Button okButton = (Button) dialog.getDialogPane().lookupButton(OkayButton);
        okButton.addEventFilter(ActionEvent.ACTION, event -> {
            if (locationXTextfield.getText().equals("")) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Please fill in all values!");
                errorAlert.showAndWait();
                event.consume();
            }
        });


            // TODO do not fully understand this
        // If everything is correctly filled in, return values
        Optional<int[]> result = dialog.showAndWait();
        if (result.isPresent()) {

            if (obstructionCheckBox.isSelected()) {
                return new int[]{
                        Integer.parseInt(locationXTextfield.getText()),
                        Integer.parseInt(locationYTextfield.getText()),
                        -1,
                        -1};
            }
            } else {

            return new int[]{
                    Integer.parseInt(locationXTextfield.getText()),
                    Integer.parseInt(locationYTextfield.getText()),
                    Integer.parseInt(destinationXTextfield.getText()),
                    Integer.parseInt(destinationYTextfield.getText())};
        }
        return null;
    }
}
