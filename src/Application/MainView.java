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
        ObjectListView objectListView = new ObjectListView(gridView);

        VBox controls = controlsView.getMainLayout();
        Pane grid = gridView.getMainLayout();
        VBox nodeList = objectListView.getMainView();


        BorderPane mainView = new BorderPane();
        mainView.setRight(controls);
        mainView.setCenter(grid);
        mainView.setLeft(nodeList);
        mainView.setTop(menuBarView.getMainLayout());

        BorderPane.setMargin(controls, new Insets(40));
        BorderPane.setMargin(nodeList, new Insets(40));

        primaryStage.setScene(new Scene(mainView));
        primaryStage.show();
    }
}
