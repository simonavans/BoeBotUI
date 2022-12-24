package Application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class NodeListView {

    private VBox vBox = new VBox();
    private ObservableList<NodeList> nodeList = FXCollections.observableArrayList();

    public NodeListView() {
        Button addButton = new Button("Add");
        Button editButton = new Button("Edit");
        Button deleteButton = new Button("Delete");
        Button startRouteButton = new Button("Start Route");

        HBox buttonHBox = new HBox();
        buttonHBox.getChildren().addAll(addButton, editButton, deleteButton);
        buttonHBox.setAlignment(Pos.CENTER);

        TableView<NodeList> table = new TableView<>();
        table.setItems(FXCollections.observableList(nodeList));
        table.setPrefWidth(180);

        TableColumn label = new TableColumn<NodeList, String>("Label");
        label.setPrefWidth(40);
        label.setCellValueFactory(new PropertyValueFactory<NodeList, String>("label"));

        TableColumn location = new TableColumn("Location");
        TableColumn destination = new TableColumn("Destination");

        TableColumn locationX = new TableColumn<NodeList, Integer>("X");
        locationX.setPrefWidth(35);
        locationX.setCellValueFactory(new PropertyValueFactory<NodeList, Integer>("locationX"));

        TableColumn locationY = new TableColumn<NodeList, Integer>("Y");
        locationY.setPrefWidth(35);
        locationY.setCellValueFactory(new PropertyValueFactory<NodeList, Integer>("locationY"));

        TableColumn destinationX = new TableColumn<NodeList, Integer>("X");
        destinationX.setPrefWidth(35);
        destinationX.setCellValueFactory(new PropertyValueFactory<NodeList, Integer>("destinationX"));

        TableColumn destinationY = new TableColumn<NodeList, Integer>("Y");
        destinationY.setPrefWidth(35);
        destinationY.setCellValueFactory(new PropertyValueFactory<NodeList, Integer>("destinationY"));

        location.getColumns().addAll(locationX, locationY);
        destination.getColumns().addAll(destinationX, destinationY);
        table.getColumns().addAll(label, location, destination);

        addButton.setOnAction(e -> {

            AddNodeView addNodeView = new AddNodeView();

            NodeList nodeList = new NodeList("A1", addNodeView.getLocationX(), addNodeView.getLocationY(), addNodeView.getDestinationX(), addNodeView.getDestinationY());
            table.getItems().add(nodeList);
        });

        editButton.setOnAction(e -> {

        });

        deleteButton.setOnAction(e -> {
            int index = table.getSelectionModel().getSelectedIndices().get(0);
            table.getItems().remove(index);
        });

        startRouteButton.setOnAction(e -> {

        });


        vBox.getChildren().addAll(table, buttonHBox, startRouteButton);
        vBox.setAlignment(Pos.CENTER);
    }

    public VBox getvBox1() {
        return vBox;
    }
}
