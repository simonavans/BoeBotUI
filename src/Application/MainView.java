package Application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainView extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        MenuBar menuBar = new MenuBar();

        MenuItem menuAddObject = new MenuItem("Add Object");
        MenuItem menuListObjects = new MenuItem("List Objects");
        MenuItem menuManualRoute = new MenuItem("Manual Route");
        Menu routeEditor = new Menu("Route Editor");
        menuBar.getMenus().add(routeEditor);
        routeEditor.getItems().addAll(menuAddObject, menuListObjects, menuManualRoute);

        Menu settingsMenu = new Menu("Settings");
        menuBar.getMenus().add(settingsMenu);
        MenuItem menuGeneralSettings = new MenuItem("General Settings");
        settingsMenu.getItems().addAll(menuGeneralSettings);

        BorderPane mainView = new BorderPane();
        mainView.setTop(menuBar);

        controlsView controlsView = new controlsView();

        mainView.setRight(controlsView.getManualControls());

        primaryStage.setScene(new Scene(mainView));
        primaryStage.show();

    }
}
