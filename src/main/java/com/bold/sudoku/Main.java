package com.bold.sudoku;

import com.bold.sudoku.navigation.INavigationManager;
import com.bold.sudoku.navigation.NavigationManager;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private static INavigationManager navigation;
    private static Stage mainStage;

    public void start(Stage stage) throws IOException {
        mainStage = stage;
        navigation = new NavigationManager(stage);
        navigation.showIntro();
    }

    public static Stage getMainStage(){
        return mainStage;
    }

    public static INavigationManager getNavigation(){
        return navigation;
    }
}
