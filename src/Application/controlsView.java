package Application;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class controlsView {

    public VBox getManualControls() {

        VBox manualControls = new VBox(new Label("Manual Controls"));
        manualControls.setAlignment(Pos.TOP_CENTER);
        manualControls.setSpacing(10);

        HBox turnControls = new HBox();

        turnControls.setSpacing(10);
        Button buttonTurnLeft = new Button("Turn Left");
        Button buttonTurnRight = new Button("Turn right");
        turnControls.getChildren().addAll(buttonTurnLeft, buttonTurnRight);

        Button buttonForward = new Button("Forward");
        Button buttonToggleGrabber = new Button("Toggle Grabber");
        Button buttonBreak = new Button("Break");

        manualControls.getChildren().addAll(buttonForward, turnControls, buttonToggleGrabber, buttonBreak);

        return manualControls;
    }
}
