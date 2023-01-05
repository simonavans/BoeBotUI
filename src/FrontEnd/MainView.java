package FrontEnd;

import BackEnd.BluetoothConnection;
import BackEnd.Object;
import BackEnd.Obstruction;
import BackEnd.PathFinding.Grid;
import BackEnd.PathFinding.PathFinder;
import Callbacks.*;
import FrontEnd.MainViewElements.*;
import FrontEnd.DialogWindows.AddObjectDialog;
import FrontEnd.DialogWindows.AddObstructionDialog;
import FrontEnd.DialogWindows.SetComPortDialog;
import FrontEnd.DialogWindows.SettingsDialog;
import javafx.application.Application;
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
import java.util.Objects;

//TODO must:
// add textual updates (new class should be created)
// -
// what to do when user spams manual control buttons? (ControlsView)
// fix bluetooth (BluetoothConnection)

//TODO should:
// Fix close request on splash screen
// Cleanup callback methods below

//TODO could:
// add boebot settings (settingsDialog, although probably a new class should be created)
// Make it possible to have the user change the order of objects so the boebot picks them up in a different order
// add path preview (gridView)

//TODO extra:
// Make application somewhat resolution independent (GridView)
// Consider changing the format of the buttons and other elements (Application wide)
// connect legend to GridView (GridView and LegendView)

public class MainView extends Application implements ObjectListCallback, addObjectCallback, ObstructionListCallback, addObstructionCallback, menuBarCallback, SettingsCallback, gridViewCallback, bluetoothCallback {

    private SettingsDialog settingsDialog = new SettingsDialog(this);

    private menuBarView menuBarView = new menuBarView(this);
    private ObjectListView objectListView = new ObjectListView(this);
    private ObstructionListView obstructionListView = new ObstructionListView(this);
    private GridView gridView = new GridView(this);
    private ControlsView controlsView = new ControlsView(this);
    private LegendView legendView = new LegendView();
    private Grid grid = new Grid(this);
    private PathFinder pathfinder = new PathFinder(this);
    private BluetoothConnection bluetoothConnection = new BluetoothConnection(this);

    /**
     * Sets up all the GUI elements and opens the application.
     *
     * @author Kerr
     */
    public void start(Stage primaryStage) {

        // Create an initial dialog to ask the user to select a COMPORT
        SetComPortDialog comPortView = new SetComPortDialog(this);

        // Set window settings
        primaryStage.setTitle("BoeBot GUI");
        primaryStage.setWidth(1000);
        primaryStage.setHeight(800);

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
        Image image = new Image("file:header.png");
        ImageView imageView = new ImageView(image);

        Line lineMiddle = new Line(0, 248, 1000, 248);
        lineMiddle.setStroke(Color.rgb(198, 0, 48));
        lineMiddle.setStrokeWidth(2.5);

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
        VBox rightLayout = new VBox();
        rightLayout.setAlignment(Pos.CENTER);
        rightLayout.setSpacing(40);

        rightLayout.getChildren().addAll(controls, legend);
        mainView.setRight(rightLayout);
        BorderPane.setMargin(rightLayout, new Insets(50));

        // Set bottom layout
        Line lineBottom = new Line(0, 795, 1000, 795);
        lineBottom.setStroke(Color.rgb(198, 0, 48));
        lineBottom.setStrokeWidth(2.5);

        mainView.setBottom(lineBottom);

        //TODO remove (used for debugging)
        Button nextStep = new Button("Debug");
        nextStep.setOnAction(e -> bluetoothConnection.sendAutomaticControl("bla"));
        rightLayout.getChildren().addAll(nextStep);

        // Set the stage
        primaryStage.setScene(new Scene(mainView));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public void stop() {
        bluetoothConnection.closePort();
    }

    // Getters

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
     * Getter method that gets the pathfinder used by the mainView
     * @return The pathfinder used by the mainView
     *
     * @author Kerr
     */
    public PathFinder getPathfinder() {
        return pathfinder;
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
     * Getter method that gets the bluetooth connection class used by the mainView
     * @return bluetooth connection class used by the mainView
     *
     * @author Kerr
     */
    public BluetoothConnection getBluetoothConnection() {
        return bluetoothConnection;
    }


    // CALLBACKS





























































































































    // TODO Make manual control buttons functional
    // TODO don't allow boebot to crash into objects when giving instructions
    @Override
    public void onManualControlEvent(String command) {

        // A switch statement is used instead of directly passing the command through to make it easier to change the
        // command send over bluetooth without having to do this in the other classes of the application

        switch (command) {
            case "Forward":
                bluetoothConnection.sendManualControl("Forward");
                break;
            case "Left":
                bluetoothConnection.sendManualControl("Left");
                break;
            case "Right":
                bluetoothConnection.sendManualControl("Right");
                break;
            case "Place":
                bluetoothConnection.sendManualControl("Place");
                break;
            case "Brake":
                bluetoothConnection.sendAutomaticControl("Brake");
        }
    }


    //TODO remove print statements
    @Override
    // A switch statement is used instead of directly passing the command through to make it easier to change the
    // command send over bluetooth without having to do this in the other classes of the application

    public void onAutomaticControlEvent(String command) {
        switch (command) {
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

    @Override
    public void onBluetoothReceiveEvent(String command) {
        System.out.println(command);
        String shortenedCommand = command.split(" ")[0] + " " + command.split(" ")[1];
        System.out.println(shortenedCommand);

        switch (shortenedCommand) {
            case "Boebot: Succeeded":
                gridView.displayNextStep();
                gridView.transmitNextStep();
                break;
            case "Boebot: Failed": // TODO see if this one is required
                break;
            case "Boebot: Opened": // TODO see if this one is required
                break;
            case "Boebot: Closed": // TODO see if this one is required
                break;
            case "Boebot: Object":
                //TODO add a method (probably to the resume part) that remembers if the boebot was
                // holding an object when stopping and where it should stand so when resuming the pathfinder can first place this
                // object before continuing
                int x = Integer.parseInt(command.split(" ")[2].charAt(1) + "");
                int y = Integer.parseInt(command.split(" ")[2].charAt(3) + "");

                boolean isUnknownObject = true;

                for (Object object : objectListView.getObjectList()) {
                    if (x == object.getLocationX() && y == object.getDestinationY()) {
                        isUnknownObject = false;
                    }
                }

                for (Obstruction obstruction : obstructionListView.getObstructionList()) {
                    if (x == obstruction.getLocationX() && y == obstruction.getLocationY())
                        isUnknownObject = false;
                }

                if (isUnknownObject) {
                    onManualControlEvent("Brake");
                    onAddObstructionEvent(x, y, null);
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("An unknown object was found at (" + x + ", " + y + "), the boebot has stopped.\nPlease press RESUME to continue.");
                    errorAlert.showAndWait();
                }

                break;
            case "Boebot: Brake":
                break;
            case "Remote: Forward":
                break;
            case "Remote: Left":
                break;
            case "Remote: Right":
                break;
            case "Remote: Place":
                break;
            case "Remote: Brake":
                break;
            case "Remote: Resume":
                break;

        }
    }


    @Override
    public void onSettingsEvent() {
        // If the grid size has changed
        if (settingsDialog.gridHeight != gridView.getGridHeight() || settingsDialog.gridWidth != gridView.getGridWidth()) {
            gridView.updateGrid();
            grid.updateGrid();
            pathfinder.updateGrid();
            objectListView.updateObjectListView();
            obstructionListView.updateObstructionListView();
        }

        // If the boebot location has changed
        if (settingsDialog.boebotX != pathfinder.getStartX() || settingsDialog.boebotY != pathfinder.getStartY()) {
            gridView.updateBoebotLocation();
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


    @Override
    public void onMenuBarEvent(String command) {
        // TODO I picked an switch statement since this allows for easy expandability, but currently this is not necessary
        switch (command) {
            case "Application Settings":
                settingsDialog.settingsDialog();
                break;
        }
    }







    @Override
    public void onObjectListEvent(String command) {

        TableView<Object> objectTable = objectListView.getObjectTable();

        switch (command) {
            case "Add":
                AddObjectDialog.addNodeDialog(this);
                break;
            case "Edit":
                if (objectTable.getSelectionModel().getSelectedIndices().size() != 0) {
                    AddObjectDialog.addNodeDialog(this, objectTable.getItems().get(objectTable.getSelectionModel().getSelectedIndices().get(0)));
                }
                break;
            case "Delete":
                if (objectTable.getSelectionModel().getSelectedIndices().size() != 0) {
                    int index = objectTable.getSelectionModel().getSelectedIndices().get(0);
                    gridView.deletePointOfInterest(objectTable.getItems().get(index).getLocationX(), objectTable.getItems().get(index).getLocationY());
                    gridView.deletePointOfInterest(objectTable.getItems().get(index).getDestinationX(), objectTable.getItems().get(index).getDestinationY());
                    objectTable.getItems().remove(index);
                }
                break;
            case "Start Route":

                if (objectTable.getItems().isEmpty()) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("Please add objects to the object list!");
                    errorAlert.showAndWait();
                } else {

                    boolean succeeded = gridView.calculateRoute(objectListView.getObjectList(), obstructionListView.getObstructionList());
                    if (!succeeded) {
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                        errorAlert.setHeaderText(null);
                        errorAlert.setContentText("No route was found, please verify the obstruction and object list!");
                        errorAlert.showAndWait();
                    } else {
//                        bluetoothConnection.sendRoute(gridView.getRouteToString()); todo fix
                        objectListView.disableButtons();
                        menuBarView.disableMenus();
                        obstructionListView.disableButtons();
                    }
                }
        }
    }

    @Override
    public void onObstructionListEvent(String command) {
        TableView<Obstruction> obstructionTable = obstructionListView.getObstructionTable();

        switch (command) {
            case "Add":
                AddObstructionDialog.addNodeDialog(this);
                break;
            case "Edit":
                if (obstructionTable.getSelectionModel().getSelectedIndices().size() != 0) {
                    AddObstructionDialog.addNodeDialog(this, obstructionTable.getItems().get(obstructionTable.getSelectionModel().getSelectedIndices().get(0)));
                }
                break;
            case "Delete":
                if (obstructionTable.getSelectionModel().getSelectedIndices().size() != 0) {
                    int index = obstructionTable.getSelectionModel().getSelectedIndices().get(0);
                    gridView.deletePointOfInterest(obstructionTable.getItems().get(index).getLocationX(), obstructionTable.getItems().get(index).getLocationY());
                    obstructionTable.getItems().remove(index);
                }
                break;
        }
    }
































































    @Override
    @SuppressWarnings("Duplicates") //See to do below for duplicate code
    public boolean onAddObjectEvent(int locationX, int locationY, int destinationX, int destinationY, Object object) {
        // Test if inputs are valid

        // Test if destination and location are the same
        if (locationX == destinationX && locationY == destinationY) {return false;}

        // Test if location is the same as boebot location
        if (locationX == settingsDialog.boebotX && locationY == settingsDialog.boebotY) {return false;}

        // Test if destination or location are the same as another object
        for (Object existingObject : objectListView.getObjectList()) {

            if (existingObject.equals(object)) {continue;}

            if (existingObject.getLocationX() == locationX && existingObject.getLocationY() == locationY) {
                return false;
            } else if (existingObject.getDestinationX() == destinationX && existingObject.getDestinationY() == destinationY) {
                return false;
            } else if (existingObject.getDestinationX() == locationX && existingObject.getDestinationY() == locationY) {
                return false;
            } else if (existingObject.getLocationX() == destinationX && existingObject.getLocationY() == destinationY) {
                return false;
            }
        }

        // Test if destination or location are the same as an obstruction
        for (Obstruction existingObstruction : obstructionListView.getObstructionList()) {
            if (existingObstruction.getLocationX() == locationX && existingObstruction.getLocationY() == locationY) {
                return false;
            } else if (existingObstruction.getLocationX() == destinationX && existingObstruction.getLocationY() == destinationY) {
                return false;
            }
        }

        if (Objects.isNull(object)) {

            // If the object is null, a new object is to be added

            // Determine the lowest value unique label for the object
            ArrayList<String> labelList = new ArrayList<>();
            for (Object item : objectListView.getObjectList()) {
                labelList.add(item.getLabel().split(" ")[1]);
            }

            //TODO this code is duplicate
            String objectNumber = "";
            int i = 1;
            while (objectNumber.equals("")) {
                if (!labelList.contains(i + "")) {
                    objectNumber = i + "";
                }
                i++;
            }

            // Update the gridView
            gridView.markObjectLocation(locationX, locationY, objectNumber + "A");
            gridView.markObjectDestination(destinationX, destinationY, objectNumber + "B");

            // Update the tableView
            Object newObject = new Object("Object " + objectNumber, locationX, locationY, destinationX, destinationY);
            objectListView.getObjectTable().getItems().add(newObject);

        } else {

            // If the object is not null, an existing object is to be changed
            // Update the gridView
            //TODO see if it is possible to update the object, instead of deleting it.
            gridView.deletePointOfInterest(object.getLocationX(), object.getLocationY());
            gridView.deletePointOfInterest(object.getDestinationX(), object.getDestinationY());

            gridView.markObjectLocation(locationX, locationY, object.getLabel().split(" ")[1] + "A"); //TODO this is a bit awkward
            gridView.markObjectDestination(destinationX, destinationY, object.getLabel().split(" ")[1] + "B"); //TODO this is a bit awkward

            // Update the tableView
            object.setLocationX(locationX);
            object.setLocationY(locationY);
            object.setDestinationX(destinationX);
            object.setDestinationY(destinationY);
            objectListView.getObjectTable().refresh();
        }

        objectListView.getObjectTable().sort();

        return true;
    }

    // TODO not super happy about converting an obstruction to an object and the list of if statements is inconsistent
    //  with the settingsDialog (for the settings it is handled in the class, for the objectsViews in the callback)

    @Override
    public boolean onAddObjectEvent(int locationX, int locationY, int destinationX, int destinationY, Obstruction obstruction) {
        onObstructionListEvent("Delete");
        if(!onAddObjectEvent(locationX, locationY, destinationX, destinationY, (Object) null)) {
            onAddObstructionEvent(locationX, locationY, null);
            return false;
        } else {
            return true;
        }
    }


    @Override
    @SuppressWarnings("Duplicates") //See to do below for duplicate code
    public boolean onAddObstructionEvent(int locationX, int locationY, Obstruction obstruction) {
        // Test if inputs are valid

        // Test if location is the same as boebot location
        if (locationX == settingsDialog.boebotX && locationY == settingsDialog.boebotY) {return false;}

        // Test if the location location is the same as another obstruction
        for (Obstruction existingObstruction : obstructionListView.getObstructionList()) {
            if (existingObstruction.equals(obstruction)) {
                continue;
            }
            if (existingObstruction.getLocationX() == locationX && existingObstruction.getLocationY() == locationY) {
                return false;
            }
        }

        // Test if location is the same as an object
        for (Object existingObject : objectListView.getObjectList()) {
            if (existingObject.getLocationX() == locationX && existingObject.getLocationY() == locationY) {
                return false;
            } else if (existingObject.getDestinationX() == locationX && existingObject.getDestinationY() == locationY) {
                return false;
            }
        }

        if (Objects.isNull(obstruction)) {

            // If the obstruction is null, a new obstruction is to be added

            // Determine the lowest value unique label for the object
            ArrayList<String> labelList = new ArrayList<>();
            for (Obstruction item : obstructionListView.getObstructionList()) {
                labelList.add(item.getLabel().split(" ")[1]);
            }

            //TODO this code is duplicate
            String obstructionNumber = "";
            int i = 1;
            while (obstructionNumber.equals("")) {
                if (!labelList.contains(i + "")) {
                    obstructionNumber = i + "";
                }
                i++;
            }

            // Update the gridView
            gridView.markObstructionLocation(locationX, locationY, "X" + obstructionNumber);

            // Update the tableView
            Obstruction newObstruction = new Obstruction("Obstruction " + obstructionNumber, locationX, locationY);
            obstructionListView.getObstructionTable().getItems().add(newObstruction);

        } else {

            // If the obstruction is not null, an existing obstruction is to be changed
            // Update the gridView
            //TODO see if it is possible to update the obstruction, instead of deleting it.
            gridView.deletePointOfInterest(obstruction.getLocationX(), obstruction.getLocationY());
            gridView.markObstructionLocation(locationX, locationY, "X" + obstruction.getLabel().split(" ")[1]); //TODO this is a bit awkward

            // Update the tableView
            obstruction.setLocationX(locationX);
            obstruction.setLocationY(locationY);
            obstructionListView.getObstructionTable().refresh();
        }
        obstructionListView.getObstructionTable().sort();
        return true;
    }

    //TODO this could be improved
    @Override
    public void onGridViewEvent(String command) {
        switch (command) {
            case "Place" :
                Object object1 = objectListView.getObjectList().get(0);
                objectListView.getObjectTable().getItems().remove(0);
                obstructionListView.getObstructionTable().getItems().add(new Obstruction("Obstruction " + (obstructionListView.getObstructionList().size() + 1), object1.getDestinationX(), object1.getDestinationY()));
                gridView.deletePointOfInterest(object1.getDestinationX(), object1.getDestinationY());
                gridView.markObstructionLocation(object1.getDestinationX(), object1.getDestinationY(), "X" + (obstructionListView.getObstructionList().size()));
                break;
            case "Pick Up" :
                Object object2 = objectListView.getObjectList().get(0);
                gridView.deletePointOfInterest(object2.getLocationX(), object2.getLocationY());
                break;
            case "Finished Route" :
                objectListView.enableButtons();
                menuBarView.enableMenus();
                obstructionListView.enableButtons();
                break;
        }
    }
}

















    // TODO remove (used for debugging at onObjectListEvent() in the "Start Route" case)
//    String routeString = "[";
//
//                for (ArrayList<String> arrayList : gridView.getRouteToString()) {
//        routeString += "[";
//        for (String string : arrayList) {
//        routeString += string + ", ";
//        }
//        routeString = routeString.substring(0, routeString.length() - 2);
//        routeString += "], ";
//        }
//        routeString = routeString.substring(0, routeString.length() - 2);
//        System.out.println(routeString + "]");
//
//        System.out.println();
//
//        String routeInt = "[";
//
//        for (ArrayList<int[]> arrayList : gridView.getRouteToInt()) {
//        routeInt += "[";
//        for (int[] array : arrayList) {
//        routeInt += "(" + array[0] + ", " + array[1] + "), ";
//        }
//        routeInt = routeInt.substring(0, routeInt.length() - 2);
//        routeInt += "], ";
//        }
//        routeInt = routeInt.substring(0, routeInt.length() - 2);
//        System.out.println(routeInt + "]");