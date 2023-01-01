package FrontEnd.MainViewElements;

import BackEnd.Object;
import FrontEnd.MainView;
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

    /**
     * Constructor that generates a TableView with with a list of object locations and destinations together with
     * adding  add/edit/delete buttons for manipulating this data. Finally it adds a start route button,
     * that sends data about the route to the Boebot.
     * The TableView also manipulates the gridView of the GUI
     * @param callback class to which the method should callback
     *
     * @author Kerr
     */
    public ObjectListView(MainView callback) {

        // Create four buttons for adding, editing, deleting objects and starting a route
        Button addButton = new Button("Add");
        Button editButton = new Button("Edit");
        Button deleteButton = new Button("Delete");
        Button startRouteButton = new Button("Start Route");

        // Add the three manipulation buttons to an HBox
        HBox buttonHBox = new HBox();
        buttonHBox.getChildren().addAll(addButton, editButton, deleteButton);
        buttonHBox.setAlignment(Pos.CENTER);

        // Create a new TableView
        this.objectTable = new TableView<>();
        objectTable.setItems(FXCollections.observableList(objectList));
        objectTable.setPrefWidth(205);

        // Add a new TableColumn for the label of an object
        TableColumn<Object, String> label = new TableColumn<>("Label");
        label.setPrefWidth(45);
        label.setCellValueFactory(new PropertyValueFactory<>("label"));

        // Add a new TableColumn used by the location X and Y data and destination X and Y data
        TableColumn<Object, Integer> location = new TableColumn<>("Location");
        TableColumn<Object, Integer> destination = new TableColumn<>("Destination");

        // Add a new TableColumn for the location X and Y data and destination X and Y data
        TableColumn<Object, Integer> locationX = new TableColumn<>("X");
        locationX.setPrefWidth(40);
        locationX.setCellValueFactory(new PropertyValueFactory<>("locationX"));

        TableColumn<Object, Integer> locationY = new TableColumn<>("Y");
        locationY.setPrefWidth(40);
        locationY.setCellValueFactory(new PropertyValueFactory<>("locationY"));

        TableColumn<Object, Integer> destinationX = new TableColumn<>("X");
        destinationX.setPrefWidth(40);
        destinationX.setCellValueFactory(new PropertyValueFactory<>("destinationX"));

        TableColumn<Object, Integer> destinationY = new TableColumn<>("Y");
        destinationY.setPrefWidth(40);
        destinationY.setCellValueFactory(new PropertyValueFactory<>("destinationY"));

        // Construct the TableView from the previously created columns
        location.getColumns().addAll(locationX, locationY);
        destination.getColumns().addAll(destinationX, destinationY);
        objectTable.getColumns().addAll(label, location, destination);

        // Add all the previously created elements to the mainView
        mainView.getChildren().addAll(objectTable, buttonHBox, startRouteButton);
        mainView.setAlignment(Pos.CENTER);

        // Buttons functionality
        addButton.setOnAction(e -> callback.onObjectListEvent("Add"));

        editButton.setOnAction(e -> callback.onObjectListEvent("Edit"));

        deleteButton.setOnAction(e -> callback.onObjectListEvent("Delete"));

        startRouteButton.setOnAction(e -> callback.onObjectListEvent("Start Route"));
    }

    public void updateObjectListView() {
        objectTable.getItems().clear();
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
}
