package Application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainView extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        int resolutionPX = 1000;
        int resolutionPY = 720;

        primaryStage.setTitle("BoeBot GUI");
        primaryStage.setWidth(resolutionPX);
        primaryStage.setHeight(resolutionPY);

        menuBarView menuBarView = new menuBarView();
        ControlsView controlsView = new ControlsView();
        GridView gridView = new GridView(resolutionPX, resolutionPY, 10, 10, 20, 45);
        NodeListView nodeListView = new NodeListView();

        VBox controls = controlsView.getMainLayout();
        Pane grid = gridView.getMainLayout();
        VBox nodeList = nodeListView.getvBox1();


        BorderPane mainView = new BorderPane();
        mainView.setRight(controls);
        mainView.setCenter(grid);
        mainView.setLeft(nodeList);
        mainView.setTop(menuBarView.getMainLayout());

        BorderPane.setMargin(controls, new Insets(40));
        BorderPane.setMargin(nodeList, new Insets(40));


        gridView.markTraversed(0,0, 1, 0);
        gridView.markUntraversed(3,3, 4, 3);
        gridView.markObjectLocation(1,1, "A1");
        gridView.markObjectDestination(2,2, "A2");
        gridView.markObstructionLocation(3,3, "A3");
        gridView.markBoeBotLocation(4,4);



        primaryStage.setScene(new Scene(mainView));
        primaryStage.show();
    }
}
