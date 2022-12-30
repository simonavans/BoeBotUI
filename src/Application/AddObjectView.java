package Application;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

class AddObjectView {

    // TODO make error message nicer (preferably in the same window)
    // TODO make the spinner maximum dependent on the width/height
    // TODO check if there is already an objectList on/going to that location/destination (if statement construct is already present)
    // TODO check if we can work with callbacks for this
    // TODO check if showAndWait causes problems like boebot.Wait

    /**
     * Opens a dialog box that allows the user to add an object (with location and destination)
     * @param gridView the gridView of the application
     * @param objectListView table to add the values to
     */
    static void addNodeDialog(GridView gridView, ObjectListView objectListView) {
        addNodeDialog(gridView ,objectListView, -1);
    }

    /**
     * Opens a dialog box that allows the user to add an objectList (with location and destination)
     * @param gridView the gridView of the application
     * @param objectListView table to add the values to
     * @param index index of the row to add values to (-1 when creating a new row)
     *
     * @author Kerr
     */
    static void addNodeDialog(GridView gridView, ObjectListView objectListView, int index) {

        ObservableList<Object> objectList = objectListView.getObjectList();

        // Create four labels and Spinners for the location X and Y and destination X and Y. Set the ValueFactory for
        // the spinners. If the parameter index is not -1, an existing object is to be changed and the spinner value is
        // set to the current value of that object.
        Label locationXLabel = new Label("location (X):");
        Spinner<Integer> locationXSpinner = new Spinner<>();
        SpinnerValueFactory<Integer> valueFactoryLocationX = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10);
        valueFactoryLocationX.setValue((index == -1) ? 0 : objectList.get(index).getLocationX());
        locationXSpinner.setValueFactory(valueFactoryLocationX);

        Label locationYLabel = new Label("location (Y):");
        Spinner<Integer> locationYSpinner = new Spinner<>();
        SpinnerValueFactory<Integer> valueFactoryLocationY = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10);
        valueFactoryLocationY.setValue((index == -1) ? 0 :  objectList.get(index).getLocationY());
        locationYSpinner.setValueFactory(valueFactoryLocationY);

        Label destinationXLabel = new Label("destination (X):");
        Spinner<Integer> destinationXSpinner = new Spinner<>();
        SpinnerValueFactory<Integer> valueFactoryDestinationX = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10);
        valueFactoryDestinationX.setValue((index == -1) ? 0 :  objectList.get(index).getDestinationX());
        destinationXSpinner.setValueFactory(valueFactoryDestinationX);

        Label destinationYLabel = new Label("destination (Y):");
        Spinner<Integer> destinationYSpinner = new Spinner<>();
        SpinnerValueFactory<Integer> valueFactoryDestinationY = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10);
        valueFactoryDestinationY.setValue((index == -1) ? 0 :  objectList.get(index).getDestinationY());
        destinationYSpinner.setValueFactory(valueFactoryDestinationY);

        // Create a new GridPane and add all the previously created labels and TextFields to it.
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
        dialog.setTitle("Add objectList");

        DialogPane dialogPane = dialog.getDialogPane();
        ButtonType OkayButton = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
        dialogPane.getButtonTypes().addAll(OkayButton, ButtonType.CANCEL);
        dialogPane.setContent(mainView);

        // Check if all required values are (correctly) filled in and if not, display an error message
        Button okButton = (Button) dialog.getDialogPane().lookupButton(OkayButton);
        okButton.addEventFilter(ActionEvent.ACTION, event -> {

            // Get the values of the spinners
            int locationXInput = locationXSpinner.getValue();
            int locationYInput = locationYSpinner.getValue();
            int destinationXInput = destinationXSpinner.getValue();
            int destinationYInput = destinationYSpinner.getValue();


            // Check if illegal inputs are found
            if (false) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("There is already an objectList here!");
                errorAlert.showAndWait();
                event.consume();

            } else if (index == -1) {

                // If index is set to -1, a new object is to be added
                String objectLabel = "A" + (objectList.size() + 1);

                // Update the gridView
                gridView.markObjectLocation(locationXInput, locationYInput, objectLabel);
                gridView.markObjectDestination(destinationXInput, destinationYInput, objectLabel);

                // Update the tableView
                Object object = new Object(objectLabel, locationXInput, locationYInput, destinationXInput, destinationYInput);
                objectListView.getObjectTable().getItems().add(object);

            } else {

                // If index is not set to -1, an existing object is to be changed

                // Update the gridView
                gridView.deletePointOfInterest( objectList.get(index).getLocationX(),  objectList.get(index).getLocationY());
                gridView.deletePointOfInterest( objectList.get(index).getDestinationX(),  objectList.get(index).getDestinationY());
                gridView.markObjectLocation(locationXInput, locationYInput, objectList.get(index).getLabel());
                gridView.markObjectDestination(destinationXInput, destinationYInput, objectList.get(index).getLabel());

                // Update the tableView
                Object object = new Object(objectList.get(index).getLabel(), locationXInput, locationYInput, destinationXInput, destinationYInput);
                objectListView.getObjectTable().getItems().remove(index);
                objectListView.getObjectTable().getItems().add(object);
            }
        });

        // Show the dialog and wait until the user has pressed cancel or okay
        dialog.showAndWait();
    }
}
