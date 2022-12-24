package Application;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

// TODO make the menu options functional

/**
 * Class that controls layout and functionality of the menubar.
 */
class menuBarView {

    private MenuBar mainLayout;

    menuBarView() {

        // Create three submenu items for adding objects, getting a list of objects and inserting a manual route.
        MenuItem menuAddObject = new MenuItem("Add Object");
        MenuItem menuListObjects = new MenuItem("List Objects");
        MenuItem menuManualRoute = new MenuItem("Manual Route");

        // Create a menu for editing route properties and add the previously created submenus
        Menu routeEditor = new Menu("Route Editor");
        routeEditor.getItems().addAll(menuAddObject, menuListObjects, menuManualRoute);

        // Create one submenu item for changing the settings on the robot.
        MenuItem menuGeneralSettings = new MenuItem("General Settings");

        // Create a menu for the settings and add the previously created submenu
        Menu settingsMenu = new Menu("Settings");
        settingsMenu.getItems().addAll(menuGeneralSettings);

        // Create a new menubar and add the created menus to it.
        mainLayout = new MenuBar();
        mainLayout.getMenus().add(routeEditor);
        mainLayout.getMenus().add(settingsMenu);

        // menu functionality
        menuAddObject.setOnAction(e -> {

        });

        menuListObjects.setOnAction(e -> {

        });

        menuManualRoute.setOnAction(e -> {

        });

        menuGeneralSettings.setOnAction(e -> {

        });
    }

    /**
     * Getter method that returns the mainLayout, the layout containing all the menus.
     * @return generated layout with menu options.
     *
     * @author Kerr
     */
    MenuBar getMainLayout() {
        return this.mainLayout;
    }
}
