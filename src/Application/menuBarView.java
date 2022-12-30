package Application;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;

// TODO make the menu options functional
// TODO See if functionality can work with callbacks

/**
 * Class that controls layout and functionality of the menubar.
 */
class menuBarView {

    private MenuBar mainLayout;

    menuBarView(ObjectListView objectListView, GridView gridView) {

        // Create three submenu items for adding objects, getting a list of objects and inserting a manual route.
        MenuItem menuAddObject = new MenuItem("Add Object");
        MenuItem menuEditObjects = new MenuItem("Edit Object");
        MenuItem menuDeleteObject = new MenuItem("Delete Object");

        // Create a menu for editing route properties and add the previously created submenus
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

        TableView<Object> objectTable = objectListView.getObjectTable();

        menuAddObject.setOnAction(e -> {
            AddObjectView.addNodeDialog(gridView, objectListView);
        });

        menuEditObjects.setOnAction(e -> {
            if (objectTable.getSelectionModel().getSelectedIndices().size() != 0) {
                int index = objectTable.getSelectionModel().getSelectedIndices().get(0);
                AddObjectView.addNodeDialog(gridView, objectListView, index);
            }
        });

        menuDeleteObject.setOnAction(e -> {
            if (objectTable.getSelectionModel().getSelectedIndices().size() != 0) {
                int index = objectTable.getSelectionModel().getSelectedIndices().get(0);


                gridView.deletePointOfInterest(objectTable.getItems().get(index).getLocationX(), objectTable.getItems().get(index).getLocationY());
                gridView.deletePointOfInterest(objectTable.getItems().get(index).getDestinationX(), objectTable.getItems().get(index).getDestinationY());

                objectTable.getItems().remove(index);
            }
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
