package com.bold.sudoku.controller;

import com.bold.sudoku.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

import java.io.IOException;


public class IntroController {

    @FXML
    void startGame(ActionEvent event) throws IOException {
        System.out.println("HOLA CLICK");
        Main.getNavigation().showSudokuGame();

    }
    @FXML
    private void mostrarAyuda() {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Cómo jugar Sudoku");
        alerta.setHeaderText("Reglas básicas del Sudoku 6x6");
        alerta.setContentText(
                "1. Rellena la cuadrícula con números del 1 al 6.\n"
                        + "2. No repitas números en la misma fila, columna o bloque 2x3.\n"
                        + "3. Usa la lógica, no el azar. Si todo falla, culpa al generador del tablero."
        );
        alerta.showAndWait();
    }

}