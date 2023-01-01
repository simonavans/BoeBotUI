package FrontEnd;

import BackEnd.BluetoothConnection;
import BackEnd.Object;
import BackEnd.PathFinding.Grid;
import BackEnd.PathFinding.PathFinder;
import Callbacks.*;
import FrontEnd.MainViewElements.*;
import FrontEnd.dialogWindows.AddObjectView;
import FrontEnd.dialogWindows.SettingsView;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import java.util.Objects;

// TODO Consider changing the format of the buttons and other elements

public class MainView extends Application implements  ManualControlCallback, ObjectListCallback, addObjectCallback, menuBarCallback, SettingsCallback {

    private SettingsView settingsView = new SettingsView(this);

    private menuBarView menuBarView = new menuBarView(this);
    private ObjectListView objectListView = new ObjectListView(this);
    private GridView gridView = new GridView(this);
    private ControlsView controlsView = new ControlsView(this);
    private LegendView legendView = new LegendView();

    private Grid grid = new Grid(this);

    //TODO fix parameters for these
    private BluetoothConnection bluetoothConnection = new BluetoothConnection();
    private PathFinder pathfinder = new PathFinder(this);

    /**
     * Sets up all the GUI elements and opens the application.
     *
     * @author Kerr
     */
    public void start(Stage primaryStage) {

        // Set window settings
        primaryStage.setTitle("BoeBot GUI");
        primaryStage.setWidth(1000);
        primaryStage.setHeight(800);

        // Create all Major UI elements
        VBox controls = controlsView.getMainLayout();
        Pane grid = gridView.getMainLayout();
        VBox nodeList = objectListView.getMainView();
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
        mainView.setLeft(nodeList);
        BorderPane.setMargin(nodeList, new Insets(40));

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
        //TODO add textual updates
        Line lineBottom = new Line(0, 795, 1000, 795);
        lineBottom.setStroke(Color.rgb(198, 0, 48));
        lineBottom.setStrokeWidth(2.5);

        mainView.setBottom(lineBottom);

        //TODO remove (used for debugging)
        HBox hBox = new HBox();
        Button nextRoute = new Button("Next route");
        nextRoute.setOnAction(e -> gridView.displayNextStepCompleted());
        Button nextStep = new Button("Next step");
        nextStep.setOnAction(e -> gridView.displayNextStepCompleted());
        hBox.getChildren().addAll(nextRoute, nextStep);
        rightLayout.getChildren().addAll(hBox);

        // Set the stage
        primaryStage.setScene(new Scene(mainView));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public Grid getGrid() {return grid; }

    public PathFinder getPathfinder() {return pathfinder;}

    public SettingsView getSettingsView() {return settingsView;}

    //TODO ALL the methods below still need to be optimized


    // TODO Make manual control buttons functional
    @Override
    public void onManualControlEvent(String command) {
        switch (command) {
            case "Forward": bluetoothConnection.sendManualControl("F");
                break;
            case "Left": bluetoothConnection.sendManualControl("L");
                break;
            case "Right": bluetoothConnection.sendManualControl("R");
                break;
            case "Toggle Grabber": bluetoothConnection.sendManualControl("G"); //TODO fix this one
                break;
            case "Break": bluetoothConnection.sendManualControl("B");
        }
    }

    //TODO for edit, setX for circle location instead of deleting
    @Override
    public void onObjectListEvent(String command) {

        TableView<Object> objectTable = objectListView.getObjectTable();

        switch (command) {
            case "Add":
                AddObjectView.addNodeDialog(this);
                break;
            case "Edit":
                if (objectTable.getSelectionModel().getSelectedIndices().size() != 0) {
                    AddObjectView.addNodeDialog(this, objectTable.getItems().get(objectTable.getSelectionModel().getSelectedIndices().get(0)));
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
                gridView.calculateRoute(objectListView.getObjectList());
                bluetoothConnection.sendRoute(gridView.getRouteToString());
        }
    }

    //TODO check if there is already an objectList on/going to that location/destination and if so, give an error
    //TODO for edit, setX for circle location instead of deleting

    @Override
    public void onAddObjectEvent(int locationX, int locationY, int destinationX, int destinationY, Object object) {

        if (Objects.isNull(object)) {

            // If index is set to -1, a new object is to be added
            int objectNumber = objectListView.getObjectList().size() + 1;

            // Update the gridView
            gridView.markObjectLocation(locationX, locationY, objectNumber + "A");
            gridView.markObjectDestination(destinationX, destinationY, objectNumber + "B");

            // Update the tableView
            Object newObject = new Object("Object " + objectNumber, locationX, locationY, destinationX, destinationY);
            objectListView.getObjectTable().getItems().add(newObject);

        } else {

            // If index is not set to -1, an existing object is to be changed

            // Update the gridView
            gridView.deletePointOfInterest(object.getLocationX(), object.getLocationY());
            gridView.deletePointOfInterest(object.getDestinationX(), object.getDestinationY());
            gridView.markObjectLocation(locationX, locationY, object.getLabel() + "A");
            gridView.markObjectDestination(destinationX, destinationY, object.getLabel() + "B");

            // Update the tableView
            Object newObject = new Object("Object" + object.getLabel(), locationX, locationY, destinationX, destinationY);
            objectListView.getObjectTable().getItems().remove(object);
            objectListView.getObjectTable().getItems().add(newObject);
        }
    }

    @Override
    public void onMenuBarEvent(String command) {
        switch (command) {
            case "General Settings":
                settingsView.settingsDialog();
                break;
        }
    }

    //TODO the sometimes else if, sometimes if is not great (also see GridView todo)
    //TODO weights do not work

    @Override
    public void onSettingsEvent() {
        if (settingsView.gridHeight != gridView.getGridHeight() || settingsView.gridWidth != gridView.getGridWidth()) {

            gridView.updateGrid();
            grid.updateGrid();
            pathfinder.updateGrid();
            objectListView.updateObjectListView();

        } else if (settingsView.boebotX != pathfinder.getStartX() || settingsView.boebotY != pathfinder.getStartY()) {

            gridView.updateBoebotLocation();
            pathfinder.updateStartLocation();

        } else if (settingsView.boebotVY != pathfinder.getStartOrientationVY() || settingsView.boebotVX != pathfinder.getStartOrientationVX()) {

            gridView.updateBoebotOrientation();
            pathfinder.updateStartOrientation();
        }

        if (settingsView.forwardWeight != pathfinder.getForwardWeight() || settingsView.turnWeight != pathfinder.getTurnWeight()) {

            pathfinder.updateWeights();
        }
    }
}