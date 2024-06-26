package frontend.mainviewelements;

import backend.Obstruction;
import frontend.ApplicationMain;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Class that is responsible for creating a tableView with a list of Obstruction locations together with
 * adding add/edit/delete buttons for manipulating this data.
 */
public class ObstructionListView {

    private VBox mainView = new VBox();
    private ObservableList<Obstruction> obstructionList = FXCollections.observableArrayList();
    private TableView<Obstruction> ObstructionTable;

    private Button addButton;
    private Button editButton;
    private Button deleteButton;

    /**
     * Constructor that generates a TableView with with a list of Obstruction locations together with
     * adding add/edit/delete buttons for manipulating this data.
     * The TableView also manipulates the gridView of the GUI
     * @param callback class to which the method should callback
     *
     * @author Kerr
     */
    public ObstructionListView(ApplicationMain callback) {

        // Create three buttons for adding, editing, deleting Obstructions
        this.addButton = new Button("Add");
        this.editButton = new Button("Edit");
        this.deleteButton = new Button("Delete");

        // Add the three manipulation buttons to an HBox
        HBox buttonHBox = new HBox();
        buttonHBox.getChildren().addAll(addButton, editButton, deleteButton);
        buttonHBox.setAlignment(Pos.CENTER);

        // Create a new TableView
        this.ObstructionTable = new TableView<>();
        ObstructionTable.setItems(FXCollections.observableList(obstructionList));
        ObstructionTable.setPrefWidth(330);

        // Add a new TableColumn for the label of an Obstruction
        TableColumn<Obstruction, String> label = new TableColumn<>("Label");
        label.setPrefWidth(130);
        label.setCellValueFactory(new PropertyValueFactory<>("label"));
        label.setSortType(TableColumn.SortType.ASCENDING);


        // Add a new TableColumn used by the location X and Y data
        TableColumn<Obstruction, Integer> location = new TableColumn<>("Location");

        // Add a new TableColumn for the location X and Y data
        TableColumn<Obstruction, Integer> locationX = new TableColumn<>("X");
        locationX.setPrefWidth(100);
        locationX.setCellValueFactory(new PropertyValueFactory<>("locationX"));

        TableColumn<Obstruction, Integer> locationY = new TableColumn<>("Y");
        locationY.setPrefWidth(100);
        locationY.setCellValueFactory(new PropertyValueFactory<>("locationY"));


        // Construct the TableView from the previously created columns
        location.getColumns().addAll(locationX, locationY);
        ObstructionTable.getColumns().addAll(label, location);
        ObstructionTable.getSortOrder().add(label);

        // Add all the previously created elements to the mainView
        mainView.setSpacing(10);
        mainView.getChildren().addAll(ObstructionTable, buttonHBox);
        mainView.setAlignment(Pos.CENTER);

        // Buttons functionality
        addButton.setOnAction(e -> callback.onAddObstructionEvent());

        editButton.setOnAction(e -> callback.onEditObstructionEvent());

        deleteButton.setOnAction(e -> callback.onDeleteObstructionEvent());
    }

    /**
     * Method that updates the ObstructionListView when settings have changed (this will delete the entire list)
     *
     * @author Kerr
     */
    public void updateObstructionListView() {ObstructionTable.getItems().clear();}

    /**
     * Disable the add, edit, delete and start route buttons
     *
     * @author Kerr
     */
    public void disableButtons() {
        addButton.setDisable(true);
        editButton.setDisable(true);
        deleteButton.setDisable(true);
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
     * Getter method that returns the obstructionList, the ObservableList responsible for the TableView
     * @return the ObservableList responsible for the TableView
     */
    public ObservableList<Obstruction> getObstructionList() {return obstructionList;}

    /**
     * Getter method that returns the ObstructionTable, that TableView containing all the Obstruction data.
     * @return TableView containing all the Obstruction data
     *
     * @author Kerr
     */
    public TableView<Obstruction> getObstructionTable() {return ObstructionTable;}

    /**
     * Get an obstruction given an location X and location Y
     * @param locationX the X location of the obstruction
     * @param locationY the Y location of the obstruction
     * @return the obstruction on the location (X,Y)
     *
     * @author Kerr
     */
    public Obstruction getObstructionFromLocation(int locationX, int locationY) {
        for (Obstruction obstruction : obstructionList) {
            if (obstruction.getLocationX() == locationX && obstruction.getLocationY() == locationY) {
                return obstruction;
            }
        }
        return null;
    }
}