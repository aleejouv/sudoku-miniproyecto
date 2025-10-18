package com.bold.sudoku.controller;

import com.bold.sudoku.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;


public class IntroController {

    @FXML
    void startGame(ActionEvent event) throws IOException {
        System.out.println("HOLA CLICK");
        Main.getNavigation().showSudokuGame();
    }
}