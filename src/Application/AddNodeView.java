package Application;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import java.util.Optional;

public class AddNodeView {

    private int locationX;
    private int locationY;
    private int destinationX;
    private int destinationY;

    public AddNodeView() {

        Dialog<Integer> dialog = new Dialog<>();
        dialog.setHeaderText(null);
        dialog.setGraphic(null);
        dialog.setTitle("Add object");

        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Label locationXLabel = new Label("location (X):");
        Label locationYLabel = new Label("location (Y):");
        Label destinationXLabel = new Label("destination (X):");
        Label destinationYLabel = new Label("destination (Y):");

        TextField locationXTextfield = new TextField();
        TextField locationYTextfield = new TextField();
        TextField destinationXTextfield = new TextField();
        TextField destinationYTextfield = new TextField();

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);

        gridPane.add(locationXLabel, 0,0);
        gridPane.add(locationYLabel, 0,1);
        gridPane.add(locationXTextfield, 1,0);
        gridPane.add(locationYTextfield, 1,1);
        gridPane.add(destinationXLabel, 0,2);
        gridPane.add(destinationYLabel, 0,3);
        gridPane.add(destinationXTextfield, 1,2);
        gridPane.add(destinationYTextfield, 1,3);

        dialogPane.setContent(gridPane);


        dialog.setResultConverter((ButtonType button) -> {
            if (button == ButtonType.OK) {
                this.locationX = Integer.parseInt(locationXTextfield.getText());
                this.locationY = Integer.parseInt(locationYTextfield.getText());
                this.destinationX = Integer.parseInt(destinationXTextfield.getText());
                this.destinationY = Integer.parseInt(destinationYTextfield.getText());

            }
            return null;
        });

        Optional<Integer> optionalResult = dialog.showAndWait();
        optionalResult.ifPresent((Integer results) -> {
            System.out.println(
                    results.toString() + " " + results.toString() + " " + results.toString());
        });
    }

    public int getLocationX() {
        return locationX;
    }

    public int getLocationY() {
        return locationY;
    }

    public int getDestinationX() {
        return destinationX;
    }

    public int getDestinationY() {
        return destinationY;
    }
}
