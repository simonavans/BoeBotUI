package Application;

import PathFinding.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;

public class MainView extends Application {

    //TODO add option to cycle through routes
    //TODO catch error for not finding route.

    private ObjectListView objectListView;
    private GridView gridView;

    @Override
    /**
     * Sets up all the GUI elements and opens the application.
     *
     * @author Kerr
     */
    public void start(Stage primaryStage) throws Exception {

        // Set window settings
        int resolutionPX = 1000;
        int resolutionPY = 800;

        primaryStage.setTitle("BoeBot GUI");
        primaryStage.setWidth(resolutionPX);
        primaryStage.setHeight(resolutionPY);

        // Create all Major UI elements
        ControlsView controlsView = new ControlsView();
        this.gridView = new GridView(resolutionPX, resolutionPY, 10, 10, 10, 45);
        PathfinderManager pathfinderManager = new PathfinderManager(gridView);
        this.objectListView = new ObjectListView(gridView, pathfinderManager);

        menuBarView menuBarView = new menuBarView(objectListView, gridView);
        LegendView legendView = new LegendView();

        VBox controls = controlsView.getMainLayout();
        Pane grid = gridView.getMainLayout();
        VBox nodeList = objectListView.getMainView();
        MenuBar menuBar = menuBarView.getMainLayout();
        GridPane legend = legendView.getMainLayout();

        // Create main layout
        BorderPane mainView = new BorderPane();

        // Set top layout
        Image image = new Image("file:header.png");
        ImageView imageView = new ImageView(image);

        Line lineMiddle = new Line(0, 248 , 1000, 248);
        lineMiddle.setStroke(Color.rgb(198,0,48));
        lineMiddle.setStrokeWidth(2.5);

        VBox topLayout = new VBox();
        topLayout.getChildren().addAll(menuBar, imageView, lineMiddle);
        mainView.setTop(topLayout);

        // Set left layout
        mainView.setLeft(nodeList);
        BorderPane.setMargin(nodeList, new Insets(40));

        // Set center layout
        mainView.setCenter(grid);

        // Set right layout
        VBox rightLayout = new VBox();
        rightLayout.setAlignment(Pos.CENTER);
        rightLayout.setSpacing(60);

        rightLayout.getChildren().addAll(controls, legend);
        mainView.setRight(rightLayout);
        BorderPane.setMargin(rightLayout, new Insets(60));

        //TODO temp
        HBox hBox = new HBox();
        Button nextRoute = new Button("Next route");
        nextRoute.setOnAction(e -> pathfinderManager.displayNextRoute());
        Button nextStep = new Button("Next step");
        nextStep.setOnAction(e -> pathfinderManager.displayNextStep());
        hBox.getChildren().addAll(nextRoute, nextStep);
        rightLayout.getChildren().addAll(hBox);



        // Set bottom layout
        Line lineBottom = new Line(0, 795 , 1000, 795);
        lineBottom.setStroke(Color.rgb(198,0,48));
        lineBottom.setStrokeWidth(2.5);

        mainView.setBottom(lineBottom);

        // Set the stage
        primaryStage.setScene(new Scene(mainView));
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
