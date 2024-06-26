package frontend.mainviewelements;

import backend.Object;
import frontend.ApplicationMain;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Class that is responsible for creating a tableView with a list of object locations and destinations together with
 * adding add/edit/delete buttons for manipulating this data. Finally it adds a start route button,
 * that sends data about the route to the Boebot.
 */
public class ObjectListView {

    private VBox mainView = new VBox();
    private ObservableList<Object> objectList = FXCollections.observableArrayList();
    private TableView<Object> objectTable;

    private Button addButton;
    private Button editButton;
    private Button deleteButton;
    private Button startRouteButton;

    /**
     * Constructor that generates a TableView with with a list of object locations and destinations together with
     * adding  add/edit/delete buttons for manipulating this data. Finally it adds a start route button,
     * that sends data about the route to the Boebot.
     * The TableView also manipulates the gridView of the GUI
     * @param callback class to which the method should callback
     *
     * @author Kerr
     */
    public ObjectListView(ApplicationMain callback) {

        // Create four buttons for adding, editing, deleting objects and starting a route
        this.addButton = new Button("Add");
        this.editButton = new Button("Edit");
        this.deleteButton = new Button("Delete");
        this.startRouteButton = new Button("Start Route");
        startRouteButton.setDefaultButton(true);

        // Add the three manipulation buttons to an HBox
        HBox buttonHBox = new HBox();
        buttonHBox.getChildren().addAll(addButton, editButton, deleteButton);
        buttonHBox.setAlignment(Pos.CENTER);

        // Create a new TableView
        this.objectTable = new TableView<>();
        objectTable.setItems(FXCollections.observableList(objectList));
        objectTable.setPrefWidth(330);

        // Add a new TableColumn for the label of an object
        TableColumn<Object, String> label = new TableColumn<>("Label");
        label.setPrefWidth(90);
        label.setCellValueFactory(new PropertyValueFactory<>("label"));
        label.setSortType(TableColumn.SortType.ASCENDING);

        // Add a new TableColumn used by the location X and Y data and destination X and Y data
        TableColumn<Object, Integer> location = new TableColumn<>("Location");
        TableColumn<Object, Integer> destination = new TableColumn<>("Destination");

        // Add a new TableColumn for the location X and Y data and destination X and Y data
        TableColumn<Object, Integer> locationX = new TableColumn<>("X");
        locationX.setPrefWidth(60);
        locationX.setCellValueFactory(new PropertyValueFactory<>("locationX"));

        TableColumn<Object, Integer> locationY = new TableColumn<>("Y");
        locationY.setPrefWidth(60);
        locationY.setCellValueFactory(new PropertyValueFactory<>("locationY"));

        TableColumn<Object, Integer> destinationX = new TableColumn<>("X");
        destinationX.setPrefWidth(60);
        destinationX.setCellValueFactory(new PropertyValueFactory<>("destinationX"));

        TableColumn<Object, Integer> destinationY = new TableColumn<>("Y");
        destinationY.setPrefWidth(60);
        destinationY.setCellValueFactory(new PropertyValueFactory<>("destinationY"));

        // Construct the TableView from the previously created columns
        location.getColumns().addAll(locationX, locationY);
        destination.getColumns().addAll(destinationX, destinationY);
        objectTable.getColumns().addAll(label, location, destination);

        objectTable.getSortOrder().add(label);

        // Add all the previously created elements to the mainView
        mainView.setSpacing(10);
        mainView.getChildren().addAll(objectTable, buttonHBox, startRouteButton);
        mainView.setAlignment(Pos.CENTER);

        // Buttons functionality
        addButton.setOnAction(e -> callback.onAddObjectEvent());

        editButton.setOnAction(e -> callback.onEditObjectEvent());

        deleteButton.setOnAction(e -> callback.onDeleteObjectEvent());

        startRouteButton.setOnAction(e -> callback.onStartRouteEvent());
    }

    /**
     * Method that updates the ObjectListView when settings have changed (this will delete the entire list)
     *
     * @author Kerr
     */
    public void updateObjectListView() {
        objectTable.getItems().clear();
    }

    /**
     * Disable the add, edit, delete and start route buttons
     *
     * @author Kerr
     */
    public void disableButtons() {
        addButton.setDisable(true);
        editButton.setDisable(true);
        deleteButton.setDisable(true);
        startRouteButton.setDisable(true);
    }

    /**
     * Enable the add, edit, delete and start route buttons
     *
     * @author Kerr
     */
    public void enableButtons() {
        addButton.setDisable(false);
        editButton.setDisable(false);
        deleteButton.setDisable(false);
        startRouteButton.setDisable(false);
    }

    // Getters

    /**
     * Getter method that returns the mainLayout, the layout containing the TableView and buttons.
     * @return generated layout with ableView and buttons.
     *
     * @author Kerr
     */
    public VBox getMainView() {return mainView;}

    /**
     * Getter method that returns the objectList, the ObservableList responsible for the TableView
     * @return the ObservableList responsible for the TableView
     */
    public ObservableList<Object> getObjectList() {return objectList;}

    /**
     * Getter method that returns the objectTable, that TableView containing all the object data.
     * @return TableView containing all the object data
     *
     * @author Kerr
     */
    public TableView<Object> getObjectTable() {return objectTable;}

    /**
     * Get an object given an location X and location Y
     * @param locationX the X location of the object
     * @param locationY the Y location of the object
     * @return the object on the location (X,Y)
     *
     * @author Kerr
     */
    public Object getObjectFromLocation(int locationX, int locationY) {
        for (Object object : objectList) {
            if (object.getLocationX() == locationX && object.getLocationY() == locationY) {
                return object;
            }
        }
        return null;
    }

    /**
     * Get an object given an destination X and destination Y
     * @param destinationX the X destination of the object
     * @param destinationY the Y destination of the object
     * @return the object with the destination (X,Y)
     */
    public Object getObjectFromDestination(int destinationX, int destinationY) {
        for (Object object : objectList) {
            if (object.getDestinationX() == destinationX && object.getDestinationY() == destinationY) {
                return object;
            }
        }
        return null;
    }
}