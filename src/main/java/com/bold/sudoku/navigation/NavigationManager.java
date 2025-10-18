package com.bold.sudoku.navigation;

import com.bold.sudoku.view.IntroView;
import com.bold.sudoku.view.SudokuGameView;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class NavigationManager implements INavigationManager{
    private static NavigationManager instance;
    private final Stage mainStage;

    public NavigationManager(Stage mainStage){
        this.mainStage = mainStage;
    }

    public static NavigationManager getInstance(Stage mainStage){
        if(instance == null){
            return instance = new NavigationManager(mainStage);
        }
        return instance;
    }

    @Override
    public void showIntro() throws IOException {
        IntroView view = new IntroView();
        Scene scene = new Scene(view.getView());
        this.mainStage.setScene(scene);
        this.mainStage.setHeight(600);
        this.mainStage.setWidth(800);
        this.mainStage.show();
    }

    @Override
    public void showSudokuGame() throws IOException{
        SudokuGameView view = new SudokuGameView();
        this.mainStage.getScene().setRoot(view.getView());
        this.mainStage.show();
    }


}
