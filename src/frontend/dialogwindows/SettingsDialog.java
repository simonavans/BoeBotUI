package frontend.dialogwindows;

import backend.Object;
import backend.Obstruction;
import frontend.ApplicationMain;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class SettingsDialog {

    // Main layout items
    private Dialog mainDialog;
    private VBox mainLayout;

    // Sub layout items
    private GridPane gridSettings;
    private GridPane locationSettings;

    public int gridWidth;
    public int gridHeight;
    public int boebotX;
    public int boebotY;
    public int boebotVX;
    public int boebotVY;
    public int turnWeight;
    public int forwardWeight;
    public int comPort;

    /**
     * Constructor for the settings dialog
     * @param gridWidth sets the default grid width
     * @param gridHeight sets the default grid height
     * @param boebotX sets the default boebot X location
     * @param boebotY sets the default boebot y location
     * @param boebotVX sets the default X component of the boebot orientation
     * @param boebotVY sets the default Y component of the boebot orientation
     * @param turnWeight sets the default turn weight of the pathfinder
     * @param forwardWeight sets the default  forward weight of the pathfinder
     *
     * @author Kerr
     */
    public SettingsDialog(ApplicationMain callback, int gridWidth, int gridHeight, int boebotX, int boebotY, int boebotVX, int boebotVY, int turnWeight, int forwardWeight) {

        // Set the default settings
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        this.boebotX = boebotX;
        this.boebotY = boebotY;
        this.boebotVX = boebotVX;
        this.boebotVY = boebotVY;
        this.turnWeight = turnWeight;
        this.forwardWeight = forwardWeight;

        // Grid settings
        // Create new Spinners
        Label gridWidthLabel = new Label("Grid Width");
        Spinner<Integer> gridWidthSpinner = new Spinner<>();
        SpinnerValueFactory<Integer> valueFactoryGridWidth = new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 15);
        gridWidthSpinner.setValueFactory(valueFactoryGridWidth);

        Label gridHeightLabel = new Label("Grid Height");
        Spinner<Integer> gridHeightSpinner = new Spinner<>();
        SpinnerValueFactory<Integer> valueFactoryGridHeight = new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 15);
        gridHeightSpinner.setValueFactory(valueFactoryGridHeight);

        // Create a new GridPane and add all the previously created settings to it.
        gridSettings = new GridPane();
        gridSettings.setHgap(15);
        gridSettings.setVgap(15);

        gridSettings.add(gridWidthLabel, 0, 0);
        gridSettings.add(gridWidthSpinner, 1, 0);
        gridSettings.add(gridHeightLabel, 0, 1);
        gridSettings.add(gridHeightSpinner, 1, 1);

        // Boebot location settings
        // Create new Spinners
        Label boebotXLabel = new Label("Boebot X   ");
        Spinner<Integer> boebotXSpinner = new Spinner<>();
        SpinnerValueFactory.IntegerSpinnerValueFactory valueFactoryBoebotX = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, gridWidth - 1);
        boebotXSpinner.setValueFactory(valueFactoryBoebotX);

        gridWidthSpinner.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            valueFactoryBoebotX.setMax(newValue - 1);
            boebotXSpinner.setValueFactory(valueFactoryBoebotX);
        });


        Label boebotYLabel = new Label("Boebot Y   ");
        Spinner<Integer> boebotYSpinner = new Spinner<>();
        SpinnerValueFactory.IntegerSpinnerValueFactory valueFactoryBoebotY = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, gridHeight - 1);
        valueFactoryBoebotY.setValue(boebotY);
        boebotYSpinner.setValueFactory(valueFactoryBoebotY);

        gridHeightSpinner.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            valueFactoryBoebotY.setMax(newValue - 1);
            boebotYSpinner.setValueFactory(valueFactoryBoebotY);
        });

        // Create a new GridPane and add all the previously created settings to it.
        locationSettings = new GridPane();
        locationSettings.setHgap(15);
        locationSettings.setVgap(15);

        locationSettings.add(boebotXLabel, 0, 0);
        locationSettings.add(boebotXSpinner, 1, 0);
        locationSettings.add(boebotYLabel, 0, 1);
        locationSettings.add(boebotYSpinner, 1, 1);

        // Create new RadioButtons
        RadioButton boebotOrientationNorth = new RadioButton("North");
        RadioButton boebotOrientationEast = new RadioButton("East");
        RadioButton boebotOrientationSouth = new RadioButton("South");
        RadioButton boebotOrientationWest = new RadioButton("West");

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

        // Create the main layout and add all the settings to it, with headers
        int spacing = 15;

        Label headerGrid = new Label("Grid size");
        headerGrid.getStylesheets().add("headerStyle.css");

        Label headerLocation = new Label("Boebot Location");
        headerLocation.getStylesheets().add("headerStyle.css");
        Region spacer1 = new Region();
        spacer1.setPrefHeight(spacing);

        Label headerOrientation = new Label("Boebot Orientation");
        headerOrientation.getStylesheets().add("headerStyle.css");
        Region spacer2 = new Region();
        spacer2.setPrefHeight(spacing);

        Label headerPathfinder = new Label("Pathfinder Weights");
        headerPathfinder.getStylesheets().add("headerStyle.css");
        Region spacer3 = new Region();
        spacer3.setPrefHeight(spacing);

        mainLayout = new VBox();
        mainLayout.setSpacing(10);
        mainLayout.getChildren().addAll(
                headerGrid, gridSettings, spacer1,
                headerLocation, locationSettings, spacer2,
                headerOrientation, boebotOrientationNorth, boebotOrientationEast, boebotOrientationSouth, boebotOrientationWest, spacer3,
                headerPathfinder, pathWeightForwardLabel, pathWeightForward, pathWeightCornerLabel, pathWeightCorner
        );

        // Create a new Dialog with an OK and CANCEL button and add the main layout
        mainDialog = new Dialog<>();
        mainDialog.setHeaderText(null);
        mainDialog.setGraphic(null);
        mainDialog.setTitle("Application Settings");

        DialogPane dialogPane = mainDialog.getDialogPane();
        ButtonType OkayButton = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        dialogPane.getButtonTypes().addAll(OkayButton, ButtonType.CANCEL);
        dialogPane.setContent(mainLayout);
        dialogPane.getStylesheets().add("applicationStyle.css");

        // Check if all required values are (correctly) filled in and if not, display an error message
        Button okButton = (Button) mainDialog.getDialogPane().lookupButton(OkayButton);
        okButton.addEventFilter(ActionEvent.ACTION, event -> {

            // Check if illegal inputs are found
            boolean isValid = true;

            for (Object existingObject : callback.getObjectListView().getObjectList()) {
                if (boebotXSpinner.getValue() == existingObject.getLocationX() && boebotYSpinner.getValue() == existingObject.getLocationY()) {
                    isValid = false;
                }
            }

            for (Obstruction existingObstruction : callback.getObstructionListView().getObstructionList()) {
                if (boebotXSpinner.getValue() == existingObstruction.getLocationX() && boebotYSpinner.getValue() == existingObstruction.getLocationY()) {
                    isValid = false;
                }
            }

            if (!isValid) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("You cannot place the boebot on an object/obstruction!");
                errorAlert.showAndWait();
                event.consume();
            } else {
                this.gridWidth = gridWidthSpinner.getValue();
                this.gridHeight = gridHeightSpinner.getValue();
                this.boebotX = boebotXSpinner.getValue();
                this.boebotY = boebotYSpinner.getValue();

                if (boebotOrientationNorth.isSelected()) {
                    this.boebotVX = 0;
                    this.boebotVY = 1;
                }
                if (boebotOrientationEast.isSelected()) {
                    this.boebotVX = 1;
                    this.boebotVY = 0;
                }
                if (boebotOrientationSouth.isSelected()) {
                    this.boebotVX = 0;
                    this.boebotVY = -1;
                }
                if (boebotOrientationWest.isSelected()) {
                    this.boebotVX = -1;
                    this.boebotVY = 0;
                }

                this.turnWeight = (int) pathWeightCorner.getValue();
                this.forwardWeight = (int) pathWeightForward.getValue();
            }
        });
    }

    /**
     * Opens a dialog box that allows the user to change the settings of the robot
     *
     * @author Kerr
     */
    @SuppressWarnings("unchecked") // All casts are manual and do not cause issues
    public void openDialog() {

        // Set the settings based on the current settings
        ((Spinner<Integer>) gridSettings.getChildren().get(1)).getValueFactory().setValue(gridWidth);
        ((Spinner<Integer>) gridSettings.getChildren().get(3)).getValueFactory().setValue(gridHeight);

        SpinnerValueFactory.IntegerSpinnerValueFactory valueFactoryBoebotX = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, gridWidth - 1);
        valueFactoryBoebotX.setValue(boebotX);
        ((Spinner<Integer>) locationSettings.getChildren().get(1)).setValueFactory(valueFactoryBoebotX);

        SpinnerValueFactory.IntegerSpinnerValueFactory valueFactoryBoebotY = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, gridHeight - 1);
        valueFactoryBoebotY.setValue(boebotY);
        ((Spinner<Integer>) locationSettings.getChildren().get(3)).setValueFactory(valueFactoryBoebotY);

        ((RadioButton) mainLayout.getChildren().get(7)).setSelected(boebotVX == 0 && boebotVY == 1);
        ((RadioButton) mainLayout.getChildren().get(8)).setSelected(boebotVX == 1 && boebotVY == 0);
        ((RadioButton) mainLayout.getChildren().get(9)).setSelected(boebotVX == 0 && boebotVY == -1);
        ((RadioButton) mainLayout.getChildren().get(10)).setSelected(boebotVX == -1 && boebotVY == 0);

        ((Slider) mainLayout.getChildren().get(14)).setValue(forwardWeight);
        ((Slider) mainLayout.getChildren().get(16)).setValue(turnWeight);

        // Show the dialog and wait until the user has pressed cancel or okay
        mainDialog .showAndWait();
    }
}