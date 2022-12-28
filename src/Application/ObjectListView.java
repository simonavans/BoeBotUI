package Application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

class ObjectListView {

    private VBox mainView = new VBox();
    private ObservableList<Object> objectList = FXCollections.observableArrayList();
    private TableView<Object> objectTable;

    ObjectListView(GridView gridView) {
        Button addButton = new Button("Add");
        Button editButton = new Button("Edit");
        Button deleteButton = new Button("Delete");
        Button startRouteButton = new Button("Start Route");

        HBox buttonHBox = new HBox();
        buttonHBox.getChildren().addAll(addButton, editButton, deleteButton);
        buttonHBox.setAlignment(Pos.CENTER);

        this.objectTable = new TableView<>();
        objectTable.setItems(FXCollections.observableList(objectList));
        objectTable.setPrefWidth(180);

        TableColumn<Object, String> label = new TableColumn<>("Label");
        label.setPrefWidth(40);
        label.setCellValueFactory(new PropertyValueFactory<>("label"));

        TableColumn location = new TableColumn<Object, java.lang.Object>("Location");
        TableColumn destination = new TableColumn<Object, java.lang.Object>("Destination");

        TableColumn<Object, Integer> locationX = new TableColumn<>("X");
        locationX.setPrefWidth(35);
        locationX.setCellValueFactory(new PropertyValueFactory<>("locationX"));

        TableColumn<Object, Integer> locationY = new TableColumn<>("Y");
        locationY.setPrefWidth(35);
        locationY.setCellValueFactory(new PropertyValueFactory<>("locationY"));

        TableColumn<Object, Integer> destinationX = new TableColumn<>("X");
        destinationX.setPrefWidth(35);
        destinationX.setCellValueFactory(new PropertyValueFactory<>("destinationX"));

        TableColumn<Object, Integer> destinationY = new TableColumn<>("Y");
        destinationY.setPrefWidth(35);
        destinationY.setCellValueFactory(new PropertyValueFactory<>("destinationY"));

        location.getColumns().addAll(locationX, locationY);
        destination.getColumns().addAll(destinationX, destinationY);
        objectTable.getColumns().addAll(label, location, destination);

        addButton.setOnAction(e -> {
            AddObjectView.addNodeDialog(gridView, this);

        });

        editButton.setOnAction(e -> {
            if (objectTable.getSelectionModel().getSelectedIndices().size() != 0) {
                int index = objectTable.getSelectionModel().getSelectedIndices().get(0);
                AddObjectView.addNodeDialog(gridView, this, index);
            }
        });

        deleteButton.setOnAction(e -> {

            if (objectTable.getSelectionModel().getSelectedIndices().size() != 0) {
                int index = objectTable.getSelectionModel().getSelectedIndices().get(0);


                gridView.deletePointOfInterest(objectTable.getItems().get(index).getLocationX(), objectTable.getItems().get(index).getLocationY());
                gridView.deletePointOfInterest(objectTable.getItems().get(index).getDestinationX(), objectTable.getItems().get(index).getDestinationY());

                objectTable.getItems().remove(index);
            }
        });

        startRouteButton.setOnAction(e -> {

        });

        mainView.getChildren().addAll(objectTable, buttonHBox, startRouteButton);
        mainView.setAlignment(Pos.CENTER);
    }

    public VBox getMainView() {
        return mainView;
    }

    public ObservableList<Object> getObjectList() {
        return objectList;
    }

    public TableView<Object> getObjectTable() {
        return objectTable;
    }
}
