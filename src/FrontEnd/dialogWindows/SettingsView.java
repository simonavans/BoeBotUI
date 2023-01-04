package FrontEnd.dialogWindows;

import BackEnd.Object;
import FrontEnd.MainView;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class SettingsView {

    private MainView callback;

    public int gridWidth = 8;
    public int gridHeight = 8;

    public int boebotX = 0;
    public int boebotY = 0;
    public int boebotVX = 1;
    public int boebotVY = 0;

    public int turnWeight = 40;
    public int forwardWeight = 20;

    public int comPort = 8;

    /**
     * Contructor for the settings class
     * @param callback class to which the method should callback
     *
     * @author Kerr
     */
    public SettingsView(MainView callback) {
        this.callback = callback;
    }

    /**
     * Opens a dialog box that allows the user to change the settings of the robot
     *
     * @author Kerr
     */
    public void settingsDialog() {

        // Grid settings
        // Create new Spinners
        Label gridWidthLabel = new Label("Grid Width");
        Spinner<Integer> gridWidthSpinner = new Spinner<>();
        SpinnerValueFactory<Integer> valueFactoryGridWidth = new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 15);
        valueFactoryGridWidth.setValue(gridWidth);
        gridWidthSpinner.setValueFactory(valueFactoryGridWidth);

        Label gridHeightLabel = new Label("Grid Height");
        Spinner<Integer> gridHeightSpinner = new Spinner<>();
        SpinnerValueFactory<Integer> valueFactoryGridHeight = new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 15);
        valueFactoryGridHeight.setValue(gridHeight);
        gridHeightSpinner.setValueFactory(valueFactoryGridHeight);

        // Boebot location settings
        // Create new Spinners
        Label boebotXLabel = new Label("Boebot Start X");
        Spinner<Integer> boebotXSpinner = new Spinner<>();
        SpinnerValueFactory.IntegerSpinnerValueFactory valueFactoryBoebotX = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, gridWidth - 1);
        valueFactoryBoebotX.setValue(boebotX);
        boebotXSpinner.setValueFactory(valueFactoryBoebotX);

        gridWidthSpinner.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            valueFactoryBoebotX.setMax(newValue - 1);
            boebotXSpinner.setValueFactory(valueFactoryBoebotX);
        });


        Label boebotYLabel = new Label("Boebot Start Y");
        Spinner<Integer> boebotYSpinner = new Spinner<>();
        SpinnerValueFactory.IntegerSpinnerValueFactory valueFactoryBoebotY = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, gridHeight - 1);
        valueFactoryBoebotY.setValue(boebotY);
        boebotYSpinner.setValueFactory(valueFactoryBoebotY);

        gridHeightSpinner.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            valueFactoryBoebotY.setMax(newValue - 1);
            boebotXSpinner.setValueFactory(valueFactoryBoebotX);
        });

        // Boebot orientation settings
        Label boebotOrientationLabel = new Label("Boebot Orientation:");

        // Create new RadioButtons
        RadioButton boebotOrientationNorth = new RadioButton("North");
        boebotOrientationNorth.setSelected(boebotVX == 0 && boebotVY == 1);
        RadioButton boebotOrientationEast = new RadioButton("East");
        boebotOrientationEast.setSelected(boebotVX == 1 && boebotVY == 0);
        RadioButton boebotOrientationSouth = new RadioButton("South");
        boebotOrientationSouth.setSelected(boebotVX == 0 && boebotVY == -1);
        RadioButton boebotOrientationWest = new RadioButton("West");
        boebotOrientationWest.setSelected(boebotVX == -1 && boebotVY == 0);

        // Add all the RadioButtons to the same group
        ToggleGroup boebotOrientationGroup = new ToggleGroup();
        boebotOrientationNorth.setToggleGroup(boebotOrientationGroup);
        boebotOrientationEast.setToggleGroup(boebotOrientationGroup);
        boebotOrientationSouth.setToggleGroup(boebotOrientationGroup);
        boebotOrientationWest.setToggleGroup(boebotOrientationGroup);

        // Path weight settings
        // Create sliders
        Label pathWeightForwardLabel = new Label("Path Weight (Forward): " + forwardWeight);
        Slider pathWeightForward = new Slider(0, 100, forwardWeight);
        pathWeightForward.setMajorTickUnit(5);
        pathWeightForward.setMinorTickCount(0);
        pathWeightForward.setSnapToTicks(true);

        pathWeightForward.valueProperty().addListener((observableValue, oldValue, newValue) ->
                pathWeightForwardLabel.setText("Path Weight (Forward): " + newValue.intValue()));

        Label pathWeightCornerLabel = new Label("Path Weight (Corner): " + turnWeight);
        Slider pathWeightCorner = new Slider(0, 100, turnWeight);
        pathWeightCorner.setMajorTickUnit(5);
        pathWeightCorner.setMinorTickCount(0);
        pathWeightCorner.setSnapToTicks(true);

        pathWeightCorner.valueProperty().addListener((observableValue, oldValue, newValue) ->
                pathWeightCornerLabel.setText("Path Weight (Corner): " + newValue.intValue()));

        // Create a new GridPane and add all the previously created settings to it.
        GridPane gridSettings = new GridPane();
        gridSettings.setHgap(15);
        gridSettings.setVgap(15);

        gridSettings.add(gridWidthLabel, 0, 0);
        gridSettings.add(gridWidthSpinner, 1, 0);
        gridSettings.add(gridHeightLabel, 0, 1);
        gridSettings.add(gridHeightSpinner, 1, 1);

        gridSettings.add(boebotXLabel, 0, 2);
        gridSettings.add(boebotXSpinner, 1, 2);
        gridSettings.add(boebotYLabel, 0, 3);
        gridSettings.add(boebotYSpinner, 1, 3);

        gridSettings.add(boebotOrientationLabel, 0, 4);
        gridSettings.add(boebotOrientationNorth, 0, 5);
        gridSettings.add(boebotOrientationEast, 0, 6);
        gridSettings.add(boebotOrientationSouth, 0, 7);
        gridSettings.add(boebotOrientationWest, 0, 8);

        // Create the main layout and add all the settings to it
        VBox mainLayout = new VBox();
        mainLayout.setSpacing(10);
        mainLayout.getChildren().addAll( gridSettings, pathWeightForwardLabel, pathWeightForward, pathWeightCornerLabel, pathWeightCorner);

        // Create a new Dialog with an OK and CANCEL button and add the main layout
        Dialog dialog = new Dialog<>();
        dialog.setHeaderText(null);
        dialog.setGraphic(null);
        dialog.setTitle("Application Settings");

        DialogPane dialogPane = dialog.getDialogPane();
        ButtonType OkayButton = new ButtonType("Confirm changes", ButtonBar.ButtonData.OK_DONE);
        dialogPane.getButtonTypes().addAll(OkayButton, ButtonType.CANCEL);
        dialogPane.setContent(mainLayout);

        // Check if all required values are (correctly) filled in and if not, display an error message
        Button okButton = (Button) dialog.getDialogPane().lookupButton(OkayButton);
        okButton.addEventFilter(ActionEvent.ACTION, event -> {

            // Check if illegal inputs are found
            boolean isValid = true;

            for (Object existingObject : callback.getObjectListView().getObjectList()) {
                if (boebotXSpinner.getValue() == existingObject.getLocationX() && boebotYSpinner.getValue() == existingObject.getLocationY()) {
                    isValid = false;
                }
            }

            if (!isValid) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("You cannot place the boebot on an object!");
                errorAlert.showAndWait();
                event.consume();
            } else {
                this.gridWidth = gridWidthSpinner.getValue();
                this.gridHeight = gridHeightSpinner.getValue();
                this.boebotX = boebotXSpinner.getValue();
                this.boebotY = boebotYSpinner.getValue();

                if (boebotOrientationNorth.isSelected()) {
                    boebotVX = 0;
                    boebotVY = 1;
                }
                if (boebotOrientationEast.isSelected()) {
                    boebotVX = 1;
                    boebotVY = 0;
                }
                if (boebotOrientationSouth.isSelected()) {
                    boebotVX = 0;
                    boebotVY = -1;
                }
                if (boebotOrientationWest.isSelected()) {
                    boebotVX = -1;
                    boebotVY = 0;
                }

                this.turnWeight = (int) pathWeightCorner.getValue();
                this.forwardWeight = (int) pathWeightForward.getValue();

                callback.onSettingsEvent();
            }
        });

        // Show the dialog and wait until the user has pressed cancel or okay
        dialog.showAndWait();
    }
}