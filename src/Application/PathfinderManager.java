package Application;

import PathFinding.Grid;
import PathFinding.Node;
import PathFinding.PathFinder;
import javafx.collections.ObservableList;


import java.util.ArrayList;
import java.util.Arrays;

public class PathfinderManager {

    private ArrayList<ArrayList<int[]>> routeToInt;
    private GridView gridView;

    private int currentRouteNumber = -1;
    private int currentStepNumber = 0;


    PathfinderManager(GridView gridView) {
        this.gridView = gridView;
    }

    void configurePathfinder(ObservableList<Object> objectList) {
        // Configure pathfinder
        Grid grid = new Grid(10, 10);
        PathFinder pathfinder = new PathFinder(grid, 0, 0, 1, 0);

        ArrayList<int[][]> list = new ArrayList<>();

        // Add objects based on the TableView from the objectListView and define the location to where they need to go
        for (Object object : objectList) {

            int objectLocationX = object.getLocationX();
            int objectLocationY = object.getLocationY();
            int objectDestinationX = object.getDestinationX();
            int objectDestinationY = object.getDestinationY();

            grid.addObstruction(objectLocationX, objectLocationY);

            int[] start = {objectLocationX, objectLocationY};
            int[] end = {objectDestinationX, objectDestinationY};
            int[][] rearrangements = {start, end};
            list.add(rearrangements);
        }

        // Determine a route
        routeToInt = new ArrayList<>();
        ArrayList<ArrayList<String>> routeToString = new ArrayList<>();

        for (int[][] destination : list) {
            int startOrientationVX = pathfinder.getStartOrientationVX();
            int startOrientationVY = pathfinder.getStartOrientationVY();

            ArrayList<Node> result1 = pathfinder.calculateShortestPathFromSource(destination[0][0], destination[0][1], false);
            grid.removeObstruction(destination[0][0], destination[0][1]);

            routeToInt.add(pathfinder.convertRouteToInt(result1));
            routeToString.add(pathfinder.convertRouteToString(result1, startOrientationVX, startOrientationVY, false));

            startOrientationVX = pathfinder.getStartOrientationVX();
            startOrientationVY = pathfinder.getStartOrientationVY();

            ArrayList<Node> result2 = pathfinder.calculateShortestPathFromSource(destination[1][0], destination[1][1], true);
            grid.addObstruction(destination[1][0], destination[1][1]);

            routeToInt.add(pathfinder.convertRouteToInt(result2));
            routeToString.add(pathfinder.convertRouteToString(result2, startOrientationVX, startOrientationVY, true));
        }

        // Debug
        for (ArrayList<int[]> arrayList : routeToInt) {
//            gridView.markRoute(arrayList);
            for (int[] array : arrayList) {
                System.out.print(Arrays.toString(array) + ", ");
            }
            System.out.println();
        }

        System.out.println();

        for (ArrayList<String> arrayList : routeToString) {
            for (String string : arrayList) {
                System.out.print(string + ", ");
            }
            System.out.println();
        }
    }

    void displayNextRoute() {
        currentRouteNumber++;
        currentStepNumber = 0;
        gridView.resetLineSegments();
        gridView.markRoute(routeToInt.get(currentRouteNumber));

    }

    void displayNextStep() {
        int[] currentStep = routeToInt.get(currentRouteNumber).get(currentStepNumber);
        int[] nextStep = routeToInt.get(currentRouteNumber).get(currentStepNumber + 1);
        int x1 = currentStep[0];
        int x2 = nextStep[0];
        int y1 = currentStep[1];
        int y2 = nextStep[1];
        gridView.markTraversed(Math.min(x1, x2), Math.min(y1, y2), Math.max(x1, x2), Math.max(y1, y2));
        currentStepNumber++;
    }
}
