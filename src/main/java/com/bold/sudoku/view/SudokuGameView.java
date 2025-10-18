package com.bold.sudoku.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class SudokuGameView {
    private Parent view;

    public SudokuGameView() throws IOException {
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/com/bold/sudoku/sudoku.fxml"));
        Parent root = mainLoader.load();

        this.view = root;
    }

    public Parent getView(){
        return this.view;
    }
}
