package FrontEnd;

import BackEnd.BluetoothConnection;
import BackEnd.Object;
import BackEnd.Obstruction;
import BackEnd.PathFinding.Grid;
import BackEnd.PathFinding.PathFinder;
import Callbacks.*;
import FrontEnd.MainViewElements.*;
import FrontEnd.DialogWindows.ObjectDialog;
import FrontEnd.DialogWindows.ObstructionDialog;
import FrontEnd.DialogWindows.SetComPortDialog;
import FrontEnd.DialogWindows.SettingsDialog;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Optional;

//================================================================================
// Notes
//================================================================================

// The boebot as three use cases: full automatic, semi automatic, manual -> check all three


//================================================================================
// MUST FIX
//================================================================================

//TODO must haves:
// add textual updates (new class should be created)
// what to do when user spams manual control buttons?
// fix bluetooth
// don't allow boebot to crash into objects when giving instructions
// unify bluetooth commands;

//TODO bugs:
// fix current sizing of tables and elements
// Fix close request on splash screen
// Sorting the TableView with more than 10 objects results in the order Object 1, object 10, object 11, object 2 etc.

//================================================================================
// COULD FIX
//================================================================================

//TODO cleanup:
// Cleanup dialog windows
// make "Pickup" a valid step

//TODO 'fun'ctionality:
// Make it possible to have the user change the order of objects so the boebot picks them up in a different order
// add path preview
// add boebot settings
// Make application somewhat resolution independent
// connect legend to GridView

public class MainView extends Application implements SettingsCallback, ObjectListCallback, ObstructionListCallback, bluetoothCallback {

    // Create the settings instance
    private SettingsDialog settingsDialog = new SettingsDialog(this,8, 8, 0, 0, 1, 0, 40, 20);

    // Create the dialog boxes instances
    private ObjectDialog objectDialog = new ObjectDialog(this);
    private ObstructionDialog obstructionDialog = new ObstructionDialog(this);
    private SetComPortDialog setComPortDialog = new SetComPortDialog(this);

    // Create the main Layout elements instances
    private menuBarView menuBarView = new menuBarView(this);
    private ObjectListView objectListView = new ObjectListView(this);
    private ObstructionListView obstructionListView = new ObstructionListView(this);
    private GridView gridView = new GridView(this);
    private ControlsView controlsView = new ControlsView(this);
    private LegendView legendView = new LegendView();

    // Create the back end instances
    private Grid grid = new Grid(this);
    private PathFinder pathfinder = new PathFinder(this);
    private BluetoothConnection bluetoothConnection = new BluetoothConnection(this);

    // Automatic controls
    private ArrayList<int[]> routeToInt = new ArrayList<>();
    private ArrayList<String> routeToString = new ArrayList<>();
    private int stepNumberString = 0;
    private int stepNumberInt = 0;
    private int routeNumber = 1;

    // Automatic/manual controls
    private String currentStep = "";
    private String nextStep = "";
    private boolean inAutomaticMode = false;
    private Object holding = null;


    //================================================================================
    // Setting up the main interface
    //================================================================================


    public static void main(String[] args) {
        Application.launch(MainView.class);
    }


    /**
     * Sets up all the GUI elements and opens the application.
     *
     * @author Kerr
     */
    public void start(Stage primaryStage) {

        // Create an initial dialog to ask the user to select a COMPORT
        setComPortDialog.getMainDialog().showAndWait();

        // Set window settings
        primaryStage.setTitle("BoeBot GUI");
        primaryStage.setWidth(1200);
        primaryStage.setHeight(1000);

        // Create all Major UI elements
        VBox controls = controlsView.getMainLayout();
        Pane grid = gridView.getMainLayout();
        VBox objectList = objectListView.getMainView();
        VBox obstructionList = obstructionListView.getMainView();
        MenuBar menuBar = menuBarView.getMainLayout();
        GridPane legend = legendView.getMainLayout();

        // Create main layout
        BorderPane mainView = new BorderPane();

        // Set top layout
        Image image = new Image("file:Header.jpg");
        ImageView imageView = new ImageView(image);


        Line lineMiddle = new Line(0, 248, 1200, 248);
        lineMiddle.setStroke(Color.rgb(198, 0, 48));
        lineMiddle.setStrokeWidth(4);

        VBox topLayout = new VBox();
        topLayout.getChildren().addAll(menuBar, imageView, lineMiddle);
        mainView.setTop(topLayout);

        // Set left layout
        VBox leftLayout = new VBox();
        leftLayout.setAlignment(Pos.CENTER);
        leftLayout.setSpacing(10);

        leftLayout.getChildren().addAll(obstructionList, objectList);
        mainView.setLeft(leftLayout);
        BorderPane.setMargin(leftLayout, new Insets(40));

        // Set center layout
        mainView.setCenter(grid);

        // Set right layout
        Button emergencyBreak = new Button("Emergency break");
        emergencyBreak.setMaxWidth(Double.MAX_VALUE);
        emergencyBreak.setOnAction(e -> onBluetoothReceiveEvent("Application: Brake"));
        emergencyBreak.setDefaultButton(true);

        VBox rightLayout = new VBox();
        rightLayout.setAlignment(Pos.CENTER);
        rightLayout.setSpacing(40);
        rightLayout.getChildren().addAll(controls, legend, emergencyBreak);
        mainView.setRight(rightLayout);
        BorderPane.setMargin(rightLayout, new Insets(50));

        // Set bottom layout
        // mainView.setBottom();

        //TODO remove (used for debugging)
        Button nextStep = new Button("Debug");
        rightLayout.getChildren().addAll(nextStep);

        nextStep.setOnAction(e ->
                {
//                    displayNextStep();
                    bluetoothConnection.sendAutomaticControl("bla");
                });

        // Set the stage
        Scene scene = new Scene(mainView);
        scene.getStylesheets().add("applicationStyle.css");

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * Stop the application and close the bluetooth connection
     */
    @Override
    public void stop() {
        if (bluetoothConnection.isConnected()) bluetoothConnection.closePort();
    }


    //================================================================================
    // Getters
    //================================================================================


    /**
     * Getter method that gets the pathfinder grid used by the mainView
     * @return The pathfinder grid used by the mainView
     *
     * @author Kerr
     */
    public Grid getGrid() {
        return grid;
    }

    /**
     * Getter method that gets the settings used by the mainView
     * @return The settings used by the mainView
     *
     * @author Kerr
     */
    public SettingsDialog getSettingsDialog() {
        return settingsDialog;
    }

    /**
     * Getter method that gets the objectListView used by the mainView
     * @return the objectListView used by the mainView
     *
     * @author Kerr
     */
    public ObjectListView getObjectListView() {
        return objectListView;
    }

    /**
     * Getter method that gets the obstructionListView used by the mainView
     * @return the obstructionListView used by the mainView
     *
     * @author Kerr
     */
    public ObstructionListView getObstructionListView() {
        return obstructionListView;
    }

    /**
     * Getter method that gets the bluetooth connection class used by the mainView
     * @return bluetooth connection class used by the mainView
     *
     * @author Kerr
     */
    public BluetoothConnection getBluetoothConnection() {
        return bluetoothConnection;
    }


    //================================================================================
    // Misc/general (helper)methods
    //================================================================================

    /**
     * General method that opens an Alert window displaying an error message.
     * @param errorMessage the error message to be displayed
     *
     * @author Kerr
     */
    public void displayError(String errorMessage) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.getDialogPane().getStylesheets().add("applicationStyle.css");
        errorAlert.setHeaderText(null);
        errorAlert.setContentText(errorMessage);
        errorAlert.showAndWait();
    }

    /**
     * Method that sets the application in automatic mode
     *
     * @author Kerr
     */
    private void enableAutomaticMode() {
        inAutomaticMode = true;
        objectListView.disableButtons();
        menuBarView.disableMenus();
        obstructionListView.disableButtons();
    }

    /**
     * Method that sets the application in manual mode
     *
     * @author Kerr
     */
    private void disableAutomaticMode() {
        inAutomaticMode = false;
        gridView.resetLineSegments();
        objectListView.enableButtons();
        menuBarView.enableMenus();
        obstructionListView.enableButtons();
    }

    /**
     * Helper method that checks if location 1 and 3 or 1 and 4 or 2 and 3 or 2 and 4 are the same.
     * contested object location = 1
     * contested object destination = 2
     * existing object location = 3
     * existing object destination = 4
     * @return true = they are the same, false = they are not the same.
     *
     * @author Kerr
     */
    private boolean isDuplicateLocation(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4) {
        if (x3 == x1 && y3 == y1) {
            return true;
        } else if (x4 == x2 && y4 == y2) {
            return true;
        } else if (x4 == x1 && y4 == y1) {
            return true;
        } else return x3 == x2 && y3 == y2;
    }

    /**
     * Helper method that checks if location 1 and 3 or 1 and 4 or 2 and 3 or 2 and 4 are the same.
     * contested object location = 1
     * contested object destination = 2
     * existing object location = 3
     * @return true = they are the same, false = they are not the same.
     *
     * @author Kerr
     */
    private boolean isDuplicateLocation(int x1, int y1, int x2, int y2, int x3, int y3) {
        if (x3 == x1 && y3 == y1) {
            return true;
        } else return x3 == x2 && y3 == y2;
    }

    /**
     * Helper method that based on a list of labels in the format <name x> generates a label with the lowest possible
     * unique (positive) number x
     * @param labelList an Arraylist of labels in the format <name x>
     * @return a label with the lowest possible unique (positive) number x
     */
    private String generateObjectNumber(ArrayList<String> labelList) {
        String objectNumber = "";
        int i = 1;
        while (objectNumber.equals("")) {
            if (!labelList.contains(i + "")) {
                objectNumber = i + "";
            }
            i++;
        }
        return objectNumber;
    }


    //================================================================================
    // Callbacks - Settings
    //================================================================================


    /**
     * Method that updates the settings
     *
     * @author Kerr
     */
    @Override
    public void onSettingsEvent() {
        settingsDialog.openDialog();

        // If the grid size has changed
        if (settingsDialog.gridHeight != gridView.getGridHeight() || settingsDialog.gridWidth != gridView.getGridWidth()) {
            gridView.updateGrid();
            gridView.generateGridView();
            gridView.updateBoebotLocation();
            gridView.updateBoebotOrientation();

            grid.updateGrid();
            pathfinder.updateGrid();

            objectListView.updateObjectListView();
            obstructionListView.updateObstructionListView();
        }

        // If the boebot location has changed
        if (settingsDialog.boebotX != pathfinder.getStartX() || settingsDialog.boebotY != pathfinder.getStartY()) {
            gridView.updateBoebotLocation();
            gridView.updateBoebotOrientation();

            pathfinder.updateStartLocation();
        }

        // If the boebot orientation has changed
        if (settingsDialog.boebotVY != pathfinder.getStartOrientationVY() || settingsDialog.boebotVX != pathfinder.getStartOrientationVX()) {
            gridView.updateBoebotOrientation();

            pathfinder.updateStartOrientation();
        }

        // If the pathfinder Weights have changed
        if (settingsDialog.forwardWeight != pathfinder.getForwardWeight() || settingsDialog.turnWeight != pathfinder.getTurnWeight()) {
            pathfinder.updateWeights();
        }
    }


    //================================================================================
    // Callbacks - Adding, editing and deleting objects
    //================================================================================


    /**
     * Method that implements the logic for adding an object to the ObjectList
     *
     * @author Kerr
     */
    @Override
    public void onAddObjectEvent() {
        Optional result = objectDialog.openAddDialog();

        if (result.isPresent()) {
            int[] userInput = (int[]) result.get();
            addObject(userInput[0], userInput[1], userInput[2], userInput[3]);
        }
    }

    /**
     * add an object to the objectList
     * @param locationX X location of the object
     * @param locationY Y location of the object
     * @param destinationX X destination of the object
     * @param destinationY Y destination of the object
     *
     * @author Kerr
     */
    private void addObject(int locationX, int locationY, int destinationX, int destinationY) {

        // Determine the lowest unique value for the label of the object
        ArrayList<String> labelList = new ArrayList<>();
        for (Object item : objectListView.getObjectList()) {
            labelList.add(item.getLabel().split(" ")[1]);
        }

        String objectNumber = generateObjectNumber(labelList);

        // Update the gridView
        gridView.markObjectLocation(locationX, locationY, objectNumber + "A");
        gridView.markObjectDestination(destinationX, destinationY, objectNumber + "B");

        // Update the tableView
        Object newObject = new Object("Object " + objectNumber, locationX, locationY, destinationX, destinationY);
        objectListView.getObjectTable().getItems().add(newObject);
        objectListView.getObjectTable().sort();
    }

    /**
     * Method that implements the logic for editing an object in the ObjectList
     *
     * @author Kerr
     */
    @Override
    public void onEditObjectEvent() {
        TableView<Object> objectTable = objectListView.getObjectTable();

        if (objectTable.getSelectionModel().getSelectedIndices().size() != 0) {
            Object object = objectTable.getItems().get(objectTable.getSelectionModel().getSelectedIndices().get(0));
            Optional result = objectDialog.openEditDialog(object);

            if (result.isPresent()) {
                int[] userInput = (int[]) result.get();
                editObject(userInput[0], userInput[1], userInput[2], userInput[3], object);
            }
        }
    }

    /**
     * add an object to the objectList
     * @param locationX X location of the object
     * @param locationY Y location of the object
     * @param destinationX X destination of the object
     * @param destinationY Y destination of the object
     * @param object object that is being edited
     *
     * @author Kerr
     */
    private void editObject(int locationX, int locationY, int destinationX, int destinationY, Object object) {

        //TODO see if it is possible to update the object, instead of deleting it.
        gridView.deletePointOfInterest(object.getLocationX(), object.getLocationY());
        gridView.deletePointOfInterest(object.getDestinationX(), object.getDestinationY());

        gridView.markObjectLocation(locationX, locationY, object.getLabel().split(" ")[1] + "A");
        gridView.markObjectDestination(destinationX, destinationY, object.getLabel().split(" ")[1] + "B");

        // Update the tableView
        object.setLocationX(locationX);
        object.setLocationY(locationY);
        object.setDestinationX(destinationX);
        object.setDestinationY(destinationY);
        objectListView.getObjectTable().refresh();
    }

    /**
     * Method that implements the logic for deleting an object from the ObjectList
     *
     * @author Kerr
     */
    @Override
    public void onDeleteObjectEvent() {
        TableView<Object> objectTable = objectListView.getObjectTable();
        if (objectTable.getSelectionModel().getSelectedIndices().size() != 0) {
            int index = objectTable.getSelectionModel().getSelectedIndices().get(0);
            deleteObject(index);
        }
    }

    /**
     * Delete an object from the object list
     * @param index the index of the object
     *
     * @author Kerr
     */
    private void deleteObject(int index) {
        TableView<Object> objectTable = objectListView.getObjectTable();
        gridView.deletePointOfInterest(objectTable.getItems().get(index).getLocationX(), objectTable.getItems().get(index).getLocationY());
        gridView.deletePointOfInterest(objectTable.getItems().get(index).getDestinationX(), objectTable.getItems().get(index).getDestinationY());
        objectTable.getItems().remove(index);
    }

    /**
     * Helper method that verifies if an object is valid. An object is valid when no other object/obstructions has the
     * same location or destination, The object destination and location are not the same and the object location is
     * not on the location of the robot.
     * @param locationX the x location of the object
     * @param locationY the y location of the object
     * @param destinationX the x destination of the object
     * @param destinationY the y destination of the object
     * @param object the object that will be edited (null if an object is to be added)
     * @return true = the object is valid, false = the object is not valid
     *
     * @author Kerr
     */
    @Override
    public boolean isValidObject(int locationX, int locationY, int destinationX, int destinationY, Object object) {
        // Test if destination and location are the same
        if (locationX == destinationX && locationY == destinationY) {
            return false;
        }

        // Test if location is the same as boebot location
        if (locationX == settingsDialog.boebotX && locationY == settingsDialog.boebotY) {
            return false;
        }

        // Test if destination or location are the same as another object
        for (Object existingObject : objectListView.getObjectList()) {

            // If the existing object it the same as the object that is being edited, ignore the existing object
            if (existingObject.equals(object)) {
                continue;
            }

            if (isDuplicateLocation(locationX, locationY, destinationX, destinationY, existingObject.getLocationX(),
                    existingObject.getLocationY(), existingObject.getDestinationX(), existingObject.getDestinationY())) {
                return false;
            }
        }

        // Test if destination or location are the same as an obstruction
        for (Obstruction existingObstruction : obstructionListView.getObstructionList()) {
            if (isDuplicateLocation(locationX, locationY, destinationX, destinationY, existingObstruction.getLocationX(),
                    existingObstruction.getLocationY())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Method that implements the logic for starting a route
     *
     * @author Kerr
     */
    @Override
    public void onStartRouteEvent() {
        TableView<Object> objectTable = objectListView.getObjectTable();

        if (objectTable.getItems().isEmpty()) {
            displayError("Please add objects to the object list!");
        } else {
            boolean succeeded = pathfinder.calculateRoute(objectListView.getObjectList(), obstructionListView.getObstructionList());
            if (!succeeded) {
                displayError("No route was found, please verify the obstruction and object list!");
            } else {

                // Disable (most) UI buttons
                enableAutomaticMode();

                 // Reset the stepNumberString
                stepNumberString = 0;
                stepNumberInt = 0;
                routeNumber = 1;

                // Set the route to String and Integer
                routeToString = pathfinder.getRouteToString();
                routeToInt = pathfinder.getRouteToInt();

                // Show the first route
                gridView.displayUntraversedRoute(routeToInt.subList(stepNumberInt, getNthIndex(routeNumber)));
                transmitNextStep();
            }
        }
    }

    //================================================================================
    // Callbacks - Adding, editing, deleting and converting obstructions
    //================================================================================

    /**
     * Method that implements the logic for adding an obstruction to the obstruction list
     *
     * @author Kerr
     */
    @Override
    public void onAddObstructionEvent() {
        Optional result = obstructionDialog.openAddDialog();

        if (result.isPresent()) {
            int[] userInput = (int[]) result.get();
            addObstruction(userInput[0], userInput[1]);
        }
    }

    /**
     * add an obstruction to the obstruction list
     * @param locationX X location of the obstruction
     * @param locationY Y location of the obstruction
     *
     * @author Kerr
     */
    private void addObstruction(int locationX, int locationY) {

        // Determine the lowest unique value for the label of the object
        ArrayList<String> labelList = new ArrayList<>();
        for (Obstruction item : obstructionListView.getObstructionList()) {
            labelList.add(item.getLabel().split(" ")[1]);
        }

        String obstructionNumber = generateObjectNumber(labelList);

        // Update the gridView
        gridView.markObstructionLocation(locationX, locationY, "X" + obstructionNumber);

        // Update the tableView
        Obstruction newObstruction = new Obstruction("Obstruction " + obstructionNumber, locationX, locationY);
        obstructionListView.getObstructionTable().getItems().add(newObstruction);
        obstructionListView.getObstructionTable().sort();

    }

    /**
     * Method that implements the logic for editing an obstruction in the obstruction list
     *
     * @author Kerr
     */
    @Override
    public void onEditObstructionEvent() {
        TableView<Obstruction> obstructionTable = obstructionListView.getObstructionTable();

        if (obstructionTable.getSelectionModel().getSelectedIndices().size() != 0) {
            Obstruction obstruction = obstructionTable.getItems().get(obstructionTable.getSelectionModel().getSelectedIndices().get(0));
            Optional result = obstructionDialog.openEditDialog(obstruction);

            if (result.isPresent()) {
                int[] userInput = (int[]) result.get();

                if (userInput.length == 2) {
                    editObstruction(userInput[0], userInput[1], obstruction);
                } else {
                    convertObstruction(userInput[0], userInput[1], userInput[2], userInput[3], obstruction);
                }
            }
        }
    }

    /**
     * edit an obstruction in the obstruction list
     * @param locationX X location of the obstruction
     * @param locationY Y location of the obstruction
     * @param obstruction obstruction that is edited
     *
     * @author Kerr
     */
    private void editObstruction(int locationX, int locationY, Obstruction obstruction) {

        //TODO see if it is possible to update the object, instead of deleting it.
        gridView.deletePointOfInterest(obstruction.getLocationX(), obstruction.getLocationY());

        gridView.markObstructionLocation(locationX, locationY, "X" + obstruction.getLabel().split(" ")[1]);

        // Update the tableView
        obstruction.setLocationX(locationX);
        obstruction.setLocationY(locationY);
        obstructionListView.getObstructionTable().refresh();
    }

    /**
     * Method that implements the logic for converting an obstruction into an object
     *
     * @author Kerr
     */
    @Override
    public void convertObstruction(int locationX, int locationY, int destinationX, int destinationY, Obstruction obstruction) {
        TableView<Obstruction> obstructionTable = obstructionListView.getObstructionTable();
        gridView.deletePointOfInterest(obstruction.getLocationX(), obstruction.getLocationY());
        obstructionTable.getItems().remove(obstruction);

        addObject(locationX, locationY, destinationX, destinationY);
    }

    /**
     * Method that implements the logic for deleting an obstruction from the obstruction list
     *
     * @author Kerr
     */
    @Override
    public void onDeleteObstructionEvent() {
        TableView<Obstruction> obstructionTable = obstructionListView.getObstructionTable();

        if (obstructionTable.getSelectionModel().getSelectedIndices().size() != 0) {
            int index = obstructionTable.getSelectionModel().getSelectedIndices().get(0);

            gridView.deletePointOfInterest(obstructionTable.getItems().get(index).getLocationX(), obstructionTable.getItems().get(index).getLocationY());
            obstructionTable.getItems().remove(index);
        }
    }

    /**
     * Helper method that verifies if an obstruction is valid. An obstruction is valid when no other object/obstructions
     * have the same location or destination, The obstruction destination and location are not the same and the obstruction
     * location is not on the location of the robot.
     * @param locationX the x location of the obstruction
     * @param locationY the y location of the obstruction
     * @param obstruction the obstruction that will be edited (null if an obstruction is to be added)
     * @return true = the obstruction is valid, false = the obstruction is not valid
     *
     * @author Kerr
     */
    @Override
    public boolean isValidObstruction(int locationX, int locationY, Obstruction obstruction) {
        // Test if location is the same as boebot location
        if (locationX == settingsDialog.boebotX && locationY == settingsDialog.boebotY) {return false;}

        // Test if the location location is the same as another obstruction
        for (Obstruction existingObstruction : obstructionListView.getObstructionList()) {

            // If the existing obstruction it the same as the obstruction that is being edited, ignore the existing obstruction
            if (existingObstruction.equals(obstruction)) {
                continue;
            }
            if (existingObstruction.getLocationX() == locationX && existingObstruction.getLocationY() == locationY) {
                return false;
            }
        }

        // Test if location is the same as an object
        for (Object existingObject : objectListView.getObjectList()) {

           if (isDuplicateLocation(existingObject.getLocationX(), existingObject.getLocationY(),
                    existingObject.getDestinationX(), existingObject.getDestinationY(), locationX, locationY)) {
               return false;
           }
        }
        return true;
    }

    /**
     * Helper method that verifies if an obstruction is valid for conversion. An obstruction is valid when no other
     * object/obstructions has the same location or destination, The obstruction destination and location are not the
     * same and the object location is not on the location of the robot.
     * @param locationX the x location of the obstruction
     * @param locationY the y location of the obstruction
     * @param destinationX the x destination of the obstruction (once converted)
     * @param destinationY the y destination of the obstruction (once converted)
     * @param obstruction the obstruction that will be converted
     * @return true = the obstruction is valid, false = the obstruction is not valid
     *
     * @author Kerr
     */
    @Override
    public boolean isValidConversion(int locationX, int locationY, int destinationX, int destinationY, Obstruction obstruction) {
        // Test if destination and location are the same
        if (locationX == destinationX && locationY == destinationY) {
            return false;
        }

        // Test if location is the same as boebot location
        if (locationX == settingsDialog.boebotX && locationY == settingsDialog.boebotY) {
            return false;
        }

        // Test if destination or location are the same as another object
        for (Object existingObject : objectListView.getObjectList()) {

            if (isDuplicateLocation(locationX, locationY, destinationX, destinationY, existingObject.getLocationX(),
                    existingObject.getLocationY(), existingObject.getDestinationX(), existingObject.getDestinationY())) {
                return false;
            }
        }

        // Test if destination or location are the same as an obstruction
        for (Obstruction existingObstruction : obstructionListView.getObstructionList()) {

            // If the existing obstruction it the same as the obstruction that is being converted, ignore the existing obstruction
            if (existingObstruction.equals(obstruction)) {
                continue;
            }

            if (existingObstruction.getLocationX() == locationX && existingObstruction.getLocationY() == locationY) {
                return false;
            } else if (existingObstruction.getLocationX() == destinationX && existingObstruction.getLocationY() == destinationY) {
                return false;
            }
        }
        return true;
    }


    //================================================================================
    // Callbacks - Receiving and transmitting bluetooth signals
    //================================================================================

    /**
     * Send an command to the boebot over bluetooth. This will NOT disable sending other commands over bluetooth and will
     * always be active
     * @param command command that should be send over bluetooth
     *
     * @author Kerr
     */
    @Override
    public void onBluetoothTransmitEvent(String command) {

        //TODO consider deleting switch statement

        switch (command) {
            case "Brake":
                bluetoothConnection.sendAutomaticControl("Brake");
                break;
            case "Uncharted":
                bluetoothConnection.sendAutomaticControl("Uncharted");
                break;
            case "Disallowed":
                bluetoothConnection.sendAutomaticControl("Disallowed");
                break;
            case "Forward":
                bluetoothConnection.sendAutomaticControl("Forward");
                break;
            case "Left":
                bluetoothConnection.sendAutomaticControl("Left");
                break;
            case "Right":
                bluetoothConnection.sendAutomaticControl("Right");
                break;
            case "Place":
                bluetoothConnection.sendAutomaticControl("Place");
                break;
        }
    }


















    private void validateCommand(String command) {
        // Steps
        // - validate if step is allowed:
        // if current step == "" -> validate is command is allowed and set command as current step + perform step
        // if current step != "" -> check if next step == "" -> validate command and set command as next step
        // if current step != "" -> check if next step != "" -> send "Disallowed" command to boebot and ignore

        // Do not forget enable/disable automatic mode!


        disableAutomaticMode(); //TODO only disable if a manual control is allowed
        currentStep = command; //TODO this will cause problems

        // A switch statement is used instead of directly passing the command through to make it easier to change the
        // command send over bluetooth without having to do this in the other classes of the application
        switch (command) {
            case "Forward" :
                bluetoothConnection.sendManualControl("Forward");
                break;
            case "Left" :
                bluetoothConnection.sendManualControl("Left");
                break;
            case "Right" :
                bluetoothConnection.sendManualControl("Right");
                break;
            case "Place" :
                bluetoothConnection.sendManualControl("Place"); //TODO do not allow the boebot to place an object if there is an object behind it
                break;
            case "Resume" :
                break;
        }
    }


    //TODO add a method (probably to the resume part) that remembers if the boebot was
    // holding an object when stopping and where it should stand so when resuming the pathfinder can first place this
    // object before continuing

    /**
     * Method that processes received commands from the boebot.
     * @param receivedCommand received commands from the boebot.
     */
    @Override
    public void onBluetoothReceiveEvent(String receivedCommand) {

        String command = receivedCommand.split(" ")[1];

        switch (command) {
            case "Succeeded":

                if (inAutomaticMode) {

                    displayNextStep();
                    transmitNextStep();

                } else {
                    bluetoothConnection.enableBluetooth();
                    switch (currentStep) {
                        case "Forward":
                            moveForward();
                            if (objectListView.getObjectFromLocation(settingsDialog.boebotX, settingsDialog.boebotY) != null) {
                                pickUpObject();
                            }
                            break;
                        case "Left":
                            rotateLeft();
                            break;
                        case "Right":
                            rotateRight();
                            break;
                        case "Place":
                            placeObject();
                            break;
                    }

                    // If a next step exists perform the next step
                    if (!nextStep.equals("")) {
                        onBluetoothTransmitEvent(nextStep);
                    }

                    // move the steps up one in the queue
                    currentStep = nextStep;
                    nextStep = "";
                }

                break;
            case "Object":

                // If the object is known by the object list, ignore this step
                if (objectListView.getObjectFromLocation(settingsDialog.boebotX + settingsDialog.boebotVX, settingsDialog.boebotY + settingsDialog.boebotVY) != null) {
                    break;
                }

                // If the obstruction is known by the obstruction list, ignore this step
                if (obstructionListView.getObstructionFromLocation(settingsDialog.boebotX + settingsDialog.boebotVX, settingsDialog.boebotY + settingsDialog.boebotVY) != null) {
                    break;
                }

                // Tell the boebot the obstruction is unknown
                onBluetoothTransmitEvent("Uncharted");
                disableAutomaticMode();

                // Mark the location of the obstruction and add it to the list of obstructions
                int obstructionX = settingsDialog.boebotX + settingsDialog.boebotVX;
                int obstructionY = settingsDialog.boebotY + settingsDialog.boebotVY;
                addObstruction(obstructionX, obstructionY);

                // Display an error
                displayError("An unknown obstruction was found at (" + obstructionX + ", " + obstructionY + "), the boebot has stopped.\nPlease press RESUME to continue.");

            case "Brake":
                onBluetoothTransmitEvent(command);
                disableAutomaticMode();
                displayError("Emergency break engaged. The boebot can no longer continue. Please restart the application and place the boebot in its starting position.");
                Platform.exit();

                break;
            case "Resume":
                //TODO how to resume/break when in the middle of a turn?
                validateCommand(command);
                break;











            case "Forward":
                validateCommand(command);
                break;
            case "Left":
                validateCommand(command);
                break;
            case "Right":
                validateCommand(command);
                break;
            case "Place":
                validateCommand(command);
                break;
        }
    }


    //================================================================================
    // Boebot location - processing the boebot location and orientation
    //================================================================================


    /**
     * Method that removes the object that will be picked up from the grid
     *
     * @author Kerr
     */
    //TODO keep in mind here that this commands assumes the boebot is already on the location of the object
    private void pickUpObject() {
        Object object = objectListView.getObjectFromLocation(settingsDialog.boebotX, settingsDialog.boebotY);
        holding = object;

        // Remove the object location from the gridView
        gridView.deletePointOfInterest(object.getLocationX(), object.getLocationY());
    }

    /**
     * Method that rotates the boebot 90 degrees anti-clockwise and updates the robots orientation
     *
     * @author Kerr
     */
    private void rotateRight() {
        gridView.rotateBoebot(-90);
        int boebotVX = settingsDialog.boebotVX;
        settingsDialog.boebotVX = settingsDialog.boebotVY;
        settingsDialog.boebotVY = -boebotVX;
    }

    /**
     * Method that rotates the boebot 90 degrees clockwise and updates the robots orientation
     *
     * @author Kerr
     */
    private void rotateLeft() {
        gridView.rotateBoebot(90);
        int boebotVX = settingsDialog.boebotVX;
        settingsDialog.boebotVX = -settingsDialog.boebotVY;
        settingsDialog.boebotVY = boebotVX;
    }

    /**
     * Method that moves the boebot forward and updates the robots location
     *
     * @author Kerr
     */
    private void moveForward() {
        int locationX = settingsDialog.boebotX;
        int locationY = settingsDialog.boebotY;

        if (inAutomaticMode) {
            // Mark the line between the old and new location as traversed
            gridView.markTraversed(
                    Math.min(locationX + settingsDialog.boebotVX, locationX),
                    Math.min(locationY + settingsDialog.boebotVY, locationY),
                    Math.max(locationX + settingsDialog.boebotVX, locationX),
                    Math.max(locationY + settingsDialog.boebotVY, locationY));
        }

        // Put the robot on the new location
        gridView.markBoeBotLocation(locationX + settingsDialog.boebotVX, locationY + settingsDialog.boebotVY);

        // Change the location of the boebot in the settings
        getSettingsDialog().boebotX = locationX + settingsDialog.boebotVX;
        getSettingsDialog().boebotY = locationY + settingsDialog.boebotVY;
    }

    /**
     * Method that removes the object that will be placed from the grid and listView and adds it changes it into an
     * obstruction. The method also updates the location of the boebot accordingly.
     *
     * @author Kerr
     */
    private void placeObject() {
        int locationX = settingsDialog.boebotX;
        int locationY = settingsDialog.boebotY;

        // Remove the object from the object list
        objectListView.getObjectTable().getItems().remove(holding); //TODO this might cause problems if this list is used for tracking the location of objects (the object location is then not relevant)
        gridView.deletePointOfInterest(holding.getDestinationX(), holding.getDestinationY());
        holding = null;

        /// Add the object to the obstruction list
        addObstruction(locationX, locationY);

        // Put the robot on the new location
        gridView.markBoeBotLocation(locationX - settingsDialog.boebotVX, locationY - settingsDialog.boebotVY);

        // Change the location of the boebot in the settings
        getSettingsDialog().boebotX = locationX - settingsDialog.boebotVX;
        getSettingsDialog().boebotY = locationY - settingsDialog.boebotVY;
    }


    //================================================================================
    // Manual control - processing the manual controls
    //================================================================================



























    //================================================================================
    // Automatic control - processing the automatic controls
    //================================================================================


    /**
     * Helper method that gets the index of the nth occurrence of the value null in the routeToInt.
     *
     * @param n nth occurrence of the value null to search for
     * @return index of the nth occurrence of the value null
     */
    private int getNthIndex(int n) {
        for (int i = 0; i < routeToInt.size(); i++) {
            if (routeToInt.get(i) == null) {
                n--;
                if (n == 0) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * Method that draws the next step of the current route, moving or rotating the image of the boebot accordingly,
     * marking routes as traversed/untraversed and removing/replacing objects.
     *
     * @author Kerr
     */
    private void displayNextStep() {
        // If the not the entire route has been shown, show the next step
        if (stepNumberString != routeToString.size()) {
            String command = routeToString.get(stepNumberString);


            if (command.equals("Pickup")) {
                stepNumberString++;
                stepNumberInt += 2;
                routeNumber++;

                pickUpObject();

                // Display the next route
                gridView.displayUntraversedRoute(routeToInt.subList(stepNumberInt, getNthIndex(routeNumber)));

                // Update the command to the next step (2 steps further)
                command = routeToString.get(stepNumberString);
            }

            switch (command) {
                case "Right" :
                    rotateRight();
                    break;

                case "Left" :
                    rotateLeft();
                    break;

                case "Forward" :
                    moveForward();
                    stepNumberInt++;
                    break;

                case "Place" :

                    placeObject();
                    stepNumberInt += 2;
                    routeNumber++;

                    if (stepNumberString != routeToString.size() - 1) {
                        // If this is not the last route, display the next route
                        gridView.displayUntraversedRoute(routeToInt.subList(stepNumberInt, getNthIndex(routeNumber)));
                    } else {
                        // Else clear the GridView and enable menu items again
                        disableAutomaticMode();
                    }
            }
            stepNumberString++;
        }
    }

    /**
     * Helper method that transmits the next step in the list of instructions to the robot
     *
     * @author Kerr
     */
    private void transmitNextStep() {
        if (stepNumberString != routeToString.size()) {
            currentStep = routeToString.get(stepNumberString);
            nextStep = routeToString.get(stepNumberString + 1);

            // If the current step is to pick an object up, skip this step (this step is only used internally and not
            // by the boebot itself)
            if (currentStep.equals("Pickup")) {
                onBluetoothTransmitEvent(routeToString.get(stepNumberString + 1));
                currentStep = routeToString.get(stepNumberString + 1);
                nextStep = routeToString.get(stepNumberString + 2);
            } else {
                onBluetoothTransmitEvent(currentStep);
            }
        }
    }
}