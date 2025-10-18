package com.bold.sudoku.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;

public class IntroView extends Stage {

    private Parent view;

    public IntroView() throws IOException {
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/com/bold/sudoku/intro.fxml"));
        Parent root = mainLoader.load();

        this.view = root;
    }

    public Parent getView(){
        return this.view;
    }
}
