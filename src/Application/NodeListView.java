package Application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

class NodeListView {

    private VBox vBox = new VBox();
    private ObservableList<NodeList> nodeList = FXCollections.observableArrayList();

    NodeListView() {
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

        TableColumn<NodeList, String> label = new TableColumn<>("Label");
        label.setPrefWidth(40);
        label.setCellValueFactory(new PropertyValueFactory<>("label"));

        TableColumn location = new TableColumn<NodeList, Object>("Location");
        TableColumn destination = new TableColumn<NodeList, Object>("Destination");

        TableColumn<NodeList, Integer> locationX = new TableColumn<>("X");
        locationX.setPrefWidth(35);
        locationX.setCellValueFactory(new PropertyValueFactory<>("locationX"));

        TableColumn<NodeList, Integer> locationY = new TableColumn<>("Y");
        locationY.setPrefWidth(35);
        locationY.setCellValueFactory(new PropertyValueFactory<>("locationY"));

        TableColumn<NodeList, Integer> destinationX = new TableColumn<>("X");
        destinationX.setPrefWidth(35);
        destinationX.setCellValueFactory(new PropertyValueFactory<>("destinationX"));

        TableColumn<NodeList, Integer> destinationY = new TableColumn<>("Y");
        destinationY.setPrefWidth(35);
        destinationY.setCellValueFactory(new PropertyValueFactory<>("destinationY"));

        location.getColumns().addAll(locationX, locationY);
        destination.getColumns().addAll(destinationX, destinationY);
        table.getColumns().addAll(label, location, destination);

        addButton.setOnAction(e -> {

            AddNodeView addNodeView = new AddNodeView();
            int[] result = addNodeView.generateAddNodeView();

            if (result != null) {
                NodeList nodeList = new NodeList("A1", result[0], result[1], result[2], result[3]);
                table.getItems().add(nodeList);
            }
        });

        editButton.setOnAction(e -> {

        });

        deleteButton.setOnAction(e -> {
            if (table.getSelectionModel().getSelectedIndices().size() != 0) {
                int index = table.getSelectionModel().getSelectedIndices().get(0);
                table.getItems().remove(index);
            }
        });

        startRouteButton.setOnAction(e -> {

        });


        vBox.getChildren().addAll(table, buttonHBox, startRouteButton);
        vBox.setAlignment(Pos.CENTER);
    }

    VBox getVBox1() {
        return vBox;
    }
}
