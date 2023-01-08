package FrontEnd.MainViewElements;

import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class LegendView {

    private GridPane mainLayout;

    /**
     * Generates a legend to explain the symbols in the GridView
     */
    public LegendView() {

        // Create main font
        javafx.scene.text.Font font = Font.font("Verdana", FontWeight.BOLD, 15);

        // Create a new circle marking an object location
        Circle locationCircle = new Circle(0, 0, 18, Color.rgb(198,0,48));

        // Create centered label for the circle symbol
        Text objectLocationLabel = new Text("0A");
        objectLocationLabel.setFont(font);
        objectLocationLabel.setX(-objectLocationLabel.prefWidth(-1) / 2);
        objectLocationLabel.setY(0);
        objectLocationLabel.setTextOrigin(VPos.CENTER);
        objectLocationLabel.setFill(Color.WHITE);

        // Add the circle and label to a group
        Group groupLocationCircle = new Group(locationCircle, objectLocationLabel);

        // Create a new circle marking an object destination
        Circle destinationCircle = new Circle(0, 0, 18, Color.WHITE);
        destinationCircle.setStroke(Color.rgb(198,0,48));
        destinationCircle.setStrokeWidth(2);

        // Create centered label for the circle symbol
        Text objectDestinationLabel = new Text("0B");
        objectDestinationLabel.setFont(font);
        objectDestinationLabel.setX(-objectDestinationLabel.prefWidth(-1) / 2);
        objectDestinationLabel.setY(0);
        objectDestinationLabel.setTextOrigin(VPos.CENTER);

        // Add the circle and label to a group
        Group groupDestinationCircle = new Group(destinationCircle, objectDestinationLabel);

        // Create a new circle marking an obstruction location
        Circle obstructionCircle = new Circle(0, 0, 18, Color.GRAY);

        // Create centered label for the circle symbol
        Text obstructionLocationLabel = new Text("X0");
        obstructionLocationLabel.setFont(font);
        obstructionLocationLabel.setX(-obstructionLocationLabel.prefWidth(-1) / 2);
        obstructionLocationLabel.setY(0);
        obstructionLocationLabel.setTextOrigin(VPos.CENTER);
        obstructionLocationLabel.setFill(Color.WHITE);

        // Add the circle and label to a group
        Group groupObstructionCircle = new Group(obstructionCircle, obstructionLocationLabel);

        // Create a new line marking an traversed path
       Line lineTraversed = new Line(0, 0, 25, 0);
       lineTraversed.setStroke(Color.GREEN);
       lineTraversed.setStrokeWidth(5);

        // Create a new line marking an traversed path
        Line lineUntraversed = new Line(0, 0, 25, 0);
        lineUntraversed.setStroke(Color.rgb(198, 0, 48));
        lineUntraversed.setStrokeWidth(5);

        // Create labels for each legend element
        Label objectLocationText = new Label("Object\nLocation:");
        objectLocationText.setFont(font);

        Label objectDestinationText = new Label("Object\nDestination:");
        objectDestinationText.setFont(font);

        Label obstructionLocationText = new Label("Obstruction\nLocation:");
        obstructionLocationText.setFont(font);

        Label lineTraversedText = new Label("Traversed:");
        lineTraversedText.setFont(font);

        Label lineUntraversedText = new Label("Untraversed: ");
        lineUntraversedText.setFont(font);

        // Create the mainLayout and add all the previously created elements to it.
        this.mainLayout = new GridPane();
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setVgap(15);
        mainLayout.setHgap(10);

        mainLayout.add(objectLocationText, 0,0);
        mainLayout.add(groupLocationCircle, 1,0);

        mainLayout.add(objectDestinationText, 0, 1);
        mainLayout.add(groupDestinationCircle, 1, 1);

        mainLayout.add(obstructionLocationText, 0, 2);
        mainLayout.add(groupObstructionCircle, 1, 2);

        mainLayout.add(lineTraversedText, 0, 3);
        mainLayout.add(lineTraversed, 1, 3);

        mainLayout.add(lineUntraversedText, 0, 4);
        mainLayout.add(lineUntraversed, 1, 4);
    }

    /**
     * Getter method that returns the mainLayout, a legend to explain the symbols in the GridView.
     * @return a legend to explain the symbols in the GridView.
     *
     * @author Kerr
     */
    public GridPane getMainLayout() {return this.mainLayout;}
}