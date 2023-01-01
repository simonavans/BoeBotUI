package FrontEnd.MainViewElements;

import FrontEnd.MainView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

/**
 * Class that controls layout and functionality of the menubar.
 */
public class menuBarView {

    private MenuBar mainLayout;

    /**
     * Generates a menubar with options fo manipulating objects and settings.
     * @param callback class to which the method should callback
     */
    public menuBarView(MainView callback) {

        // Create three submenu items for adding objects, editing objects and deleting objects.
        MenuItem menuAddObject = new MenuItem("Add Object");
        MenuItem menuEditObjects = new MenuItem("Edit Object");
        MenuItem menuDeleteObject = new MenuItem("Delete Object");

        // Create a menu for editing object properties and add the previously created submenus
        Menu ObjectEditor = new Menu("Object Editor");
        ObjectEditor.getItems().addAll(menuAddObject, menuEditObjects, menuDeleteObject);

        // Create one submenu item for changing the settings on the robot.
        MenuItem menuGeneralSettings = new MenuItem("General Settings");

        // Create a menu for the settings and add the previously created submenu
        Menu settingsMenu = new Menu("Settings");
        settingsMenu.getItems().addAll(menuGeneralSettings);

        // Create a new menubar and add the created menus to it.
        mainLayout = new MenuBar();
        mainLayout.getMenus().add(ObjectEditor);
        mainLayout.getMenus().add(settingsMenu);

        // menu functionality
        menuAddObject.setOnAction(e -> callback.onObjectListEvent("Add"));

        menuEditObjects.setOnAction(e -> callback.onObjectListEvent("Edit"));

        menuDeleteObject.setOnAction(e -> callback.onObjectListEvent("Delete"));

        menuGeneralSettings.setOnAction(e -> callback.onMenuBarEvent("General Settings"));
    }

    /**
     * Getter method that returns the mainLayout, the layout containing all the menus.
     * @return generated layout with menu options.
     *
     * @author Kerr
     */
    public MenuBar getMainLayout() {
        return this.mainLayout;
    }
}
