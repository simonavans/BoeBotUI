package frontend.mainviewelements;

import frontend.ApplicationMain;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Class that controls layout and functionality of buttons to manually control the robot.
 */
public class ControlsView {

    private VBox mainLayout; // Layout generated by this class

    /**
     * Generates a total of five buttons: forward, left and right (next to each other), toggle grabber and break.
     * @param callback class to which the method should callback
     *
     * @author Kerr
     */
    public ControlsView(ApplicationMain callback) {
        // Create two buttons for turning left and right
        Button buttonTurnLeft = new Button("Turn Left");
        Button buttonTurnRight = new Button("Turn right");

        // Add left and right button to their own hBox
        HBox turnControls = new HBox();
        turnControls.setSpacing(10);
        turnControls.getChildren().addAll(buttonTurnLeft, buttonTurnRight);

        // Create three buttons for moving forward, grabber control and break. Set their width at MAX_value to ensure
        // alignment with other buttons.
        Button buttonForward = new Button("Forward");
        buttonForward.setMaxWidth(Double.MAX_VALUE);
        Button buttonPlaceObject = new Button("Place object");
        buttonPlaceObject.setMaxWidth(Double.MAX_VALUE);
        Button emergencyBreak = new Button("Emergency break");
        emergencyBreak.setMaxWidth(Double.MAX_VALUE);
        emergencyBreak.setDefaultButton(true);

        // Set basic format of the main Layout
        mainLayout = new VBox();
        mainLayout.setSpacing(10);

        // Add all buttons to the mainLayout
        mainLayout.getChildren().addAll(buttonForward, turnControls, buttonPlaceObject, emergencyBreak);

        // Buttons functionality
        buttonForward.setOnAction(e -> callback.onBluetoothReceiveEvent("Application: Forward"));
        buttonTurnLeft.setOnAction(e -> callback.onBluetoothReceiveEvent("Application: Left"));
        buttonTurnRight.setOnAction(e -> callback.onBluetoothReceiveEvent("Application: Right"));
        buttonPlaceObject.setOnAction(e -> callback.onBluetoothReceiveEvent("Application: Place"));
        emergencyBreak.setOnAction(e -> callback.onBluetoothReceiveEvent("Application: Brake"));
    }

    /**
     * Getter method that returns the mainLayout, the layout containing all the buttons.
     * @return generated layout with five buttons for manual controls.
     *
     * @author Kerr
     */
    public VBox getMainLayout() {return this.mainLayout; }
}