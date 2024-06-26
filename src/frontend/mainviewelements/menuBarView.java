package frontend.mainviewelements;

import frontend.ApplicationMain;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

/**
 * Class that controls layout and functionality of the menubar.
 */
public class menuBarView {

    private MenuBar mainLayout;

    private Menu objectMenu;
    private Menu settingsMenu;
    private Menu obstructionMenu;

    /**
     * Generates a menubar with options fo manipulating objects and settings.
     * @param callback class to which the method should callback
     */
    public menuBarView(ApplicationMain callback) {

        // Create three submenu items for adding objects, editing objects and deleting objects.
        MenuItem menuAddObject = new MenuItem("Add Object");
        MenuItem menuEditObjects = new MenuItem("Edit Object");
        MenuItem menuDeleteObject = new MenuItem("Delete Object");

        // Create a menu for editing object properties and add the previously created submenus
        this.objectMenu = new Menu("Object Editor");
        objectMenu.getItems().addAll(menuAddObject, menuEditObjects, menuDeleteObject);

        // Create three submenu items for adding obstructions, editing obstructions and deleting obstructions.
        MenuItem menuAddObstruction = new MenuItem("Add Obstruction");
        MenuItem menuEditObstruction = new MenuItem("Edit Obstruction");
        MenuItem menuDeleteObstruction = new MenuItem("Delete Obstruction");

        // Create a menu for editing obstruction properties and add the previously created submenus
        this.obstructionMenu = new Menu("Obstruction Editor");
        obstructionMenu.getItems().addAll(menuAddObstruction, menuEditObstruction, menuDeleteObstruction);

        // Create one submenu item for changing the settings on the robot.
        MenuItem menuApplicationSettings = new MenuItem("Application Settings");

        // Create a menu for the settings and add the previously created submenu
        this.settingsMenu = new Menu("Settings");
        settingsMenu.getItems().addAll(menuApplicationSettings);

        // Create a new menubar and add the created menus to it.
        mainLayout = new MenuBar();
        mainLayout.getMenus().addAll(objectMenu, obstructionMenu, settingsMenu);

        // menu functionality
        menuAddObject.setOnAction(e -> callback.onAddObjectEvent());
        menuEditObjects.setOnAction(e -> callback.onEditObjectEvent());
        menuDeleteObject.setOnAction(e -> callback.onDeleteObjectEvent());

        menuAddObstruction.setOnAction(e -> callback.onAddObstructionEvent());
        menuEditObstruction.setOnAction(e -> callback.onEditObjectEvent());
        menuDeleteObstruction.setOnAction(e -> callback.onDeleteObstructionEvent());

        menuApplicationSettings.setOnAction(e ->  callback.onSettingsEvent());
    }

    /**
     * Disable the object and settings menu
     *
     * @author Kerr
     */
    public void disableMenus() {
        objectMenu.setDisable(true);
        obstructionMenu.setDisable(true);
        settingsMenu.setDisable(true);
    }

    /**
     * Enable the object and settings menu
     *
     * @author Kerr
     */
    public void enableMenus() {
        objectMenu.setDisable(false);
        obstructionMenu.setDisable(false);
        settingsMenu.setDisable(false);
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