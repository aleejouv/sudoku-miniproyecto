package com.bold.sudoku.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

// : Imports necesarios para Alertas y Botones
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import java.util.Optional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SudokuController {

    // --- Campos FXML Inyectados ---
    @FXML private GridPane sudokuGrid;

    // : Inyectar los nuevos botones
    @FXML private Button startButton;
    @FXML private Button helpButton;

    // (Referencias a todas las c36 celdas FXML...)
    @FXML private TextField cell00; @FXML private TextField cell01; @FXML private TextField cell02;
    @FXML private TextField cell03; @FXML private TextField cell04; @FXML private TextField cell05;
    @FXML private TextField cell10; @FXML private TextField cell11; @FXML private TextField cell12;
    @FXML private TextField cell13; @FXML private TextField cell14; @FXML private TextField cell15;
    @FXML private TextField cell20; @FXML private TextField cell21; @FXML private TextField cell22;
    @FXML private TextField cell23; @FXML private TextField cell24; @FXML private TextField cell25;
    @FXML private TextField cell30; @FXML private TextField cell31; @FXML private TextField cell32;
    @FXML private TextField cell33; @FXML private TextField cell34; @FXML private TextField cell35;
    @FXML private TextField cell40; @FXML private TextField cell41; @FXML private TextField cell42;
    @FXML private TextField cell43; @FXML private TextField cell44; @FXML private TextField cell45;
    @FXML private TextField cell50; @FXML private TextField cell51; @FXML private TextField cell52;
    @FXML private TextField cell53; @FXML private TextField cell54; @FXML private TextField cell55;

    // --- Campos de Lógica ---

    private ArrayList<ArrayList<TextField>> cells = new ArrayList<>();

    private final int[][] initialBoard = {
            {5, 1, 0, 0, 3, 6},
            {0, 0, 0, 0, 0, 0},
            {6, 3, 0, 0, 1, 4},
            {0, 0, 0, 0, 0, 0},
            {4, 5, 0, 0, 2, 3},
            {0, 0, 0, 0, 0, 0}
    };

    // : Lógica para HU-5 (Límite de ayudas)
    private int hintCounter = 0;
    private final int MAX_HINTS = 5; // Límite de 5 ayudas

    /**
     * Método de inicialización
     */
    @FXML
    public void initialize() {
        populateCellsArrayList();
        setupInputListeners();
        // El juego ya NO inicia aquí, espera al botón.
    }

    /**
     * : Implementa HU-2 (Inicio con confirmación)
     * Se llama cuando se presiona el botón "Iniciar Juego".
     */
    @FXML
    private void handleStartButtonAction() {
        // Criterio de Aceptación HU-2: Alerta de confirmación
        Optional<ButtonType> result = showAlert("Iniciar Nuevo Juego",
                "¿Estás seguro de que quieres empezar un nuevo juego? Se perderá el progreso actual.",
                Alert.AlertType.CONFIRMATION);

        // Si el usuario presiona OK
        if (result.isPresent() && result.get() == ButtonType.OK) {
            hintCounter = 0; // Reinicia el contador de ayudas
            startNewGame();
        }
    }

    /**
     * : Implementa HU-5 (Opción de Ayuda) [cite: 97]
     * Se llama cuando se presiona el botón "Ayuda".
     */
    @FXML
    private void handleHelpButtonAction() {
        // Criterio HU-5: No se puede terminar el tablero con la ayuda
        if (hintCounter >= MAX_HINTS) {
            showAlert("Límite de Ayudas", "Has usado todas tus " + MAX_HINTS + " ayudas.", Alert.AlertType.INFORMATION);
            return;
        }

        ArrayList<ArrayList<Integer>> boardModel = getBoardModel();

        // 1. Buscar una celda vacía [cite: 98]
        for (int r = 0; r < 6; r++) {
            for (int c = 0; c < 6; c++) {
                if (boardModel.get(r).get(c) == 0) {

                    // 2. Encontrar un número válido (1-6) para esa celda
                    for (int num = 1; num <= 6; num++) {
                        if (isSafe(r, c, num, boardModel)) {

                            // 3. Mostrar la sugerencia [cite: 99]
                            TextField cell = cells.get(r).get(c);
                            cell.setText(String.valueOf(num));

                            // Criterio HU-5: Destacar visualmente [cite: 99]
                            cell.setStyle("-fx-alignment: center; -fx-background-color: #a0e0a0;"); // Verde claro

                            hintCounter++; // Incrementar el contador de ayudas
                            showAlert("Ayuda", "Sugerencia (" + hintCounter + "/" + MAX_HINTS + ") colocada.", Alert.AlertType.INFORMATION);

                            // Re-validar el tablero por si esta ayuda soluciona un error
                            validateBoard();
                            return; // Salir después de dar UNA sola ayuda
                        }
                    }

                    // Si llega aquí, es una celda vacía sin solución (tablero irresoluble)
                    showAlert("Sin Ayuda", "No se encontró un número válido para la celda vacía. Revisa tus números.", Alert.AlertType.WARNING);
                    return;
                }
            }
        }

        // Si llega aquí, el tablero está lleno
        showAlert("Tablero Completo", "No hay celdas vacías para ayudar.", Alert.AlertType.INFORMATION);
    }

    /**
     * Lógica para rellenar el tablero (llamada por handleStartButtonAction)
     */
    private void startNewGame() {
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 6; col++) {
                int value = initialBoard[row][col];
                TextField cell = cells.get(row).get(col);

                if (value != 0) {
                    cell.setText(String.valueOf(value));
                    cell.setEditable(false);
                    cell.setStyle("-fx-alignment: center; -fx-background-color: #f0f0f0; -fx-font-weight: bold;");
                } else {
                    cell.setText("");
                    cell.setEditable(true);
                    cell.setStyle("-fx-alignment: center;");
                }
            }
        }
        validateBoard();
    }

    /**
     * Configura los listeners para HU-3 y HU-4
     */
    private void setupInputListeners() {
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 6; col++) {
                TextField cell = cells.get(row).get(col);

                cell.textProperty().addListener((observable, oldValue, newValue) -> {
                    // HU-3: Permitir solo 1-6 [cite: 77]
                    if (!newValue.matches("[1-6]?")) {
                        cell.setText(oldValue);
                    }

                    // HU-4: Validación en tiempo real [cite: 89]
                    validateBoard();
                });
            }
        }
    }

    /**
     * Valida todo el tablero (HU-4) [cite: 87, 88]
     */
    private void validateBoard() {
        ArrayList<ArrayList<Integer>> boardModel = getBoardModel();
        ArrayList<ArrayList<Boolean>> errorCells = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            ArrayList<Boolean> row = new ArrayList<>();
            for (int j = 0; j < 6; j++) { row.add(false); }
            errorCells.add(row);
        }

        // Comprobar Filas, Columnas y Bloques
        for (int r = 0; r < 6; r++) findDuplicates(boardModel, errorCells, r, 0, r, 5, true);
        for (int c = 0; c < 6; c++) findDuplicates(boardModel, errorCells, 0, c, 5, c, false);
        for (int br = 0; br < 6; br += 2) {
            for (int bc = 0; bc < 6; bc += 3) {
                findDuplicatesInBlock(boardModel, errorCells, br, bc);
            }
        }

        applyErrorStyles(errorCells);
    }

    // (Métodos findDuplicates y findDuplicatesInBlock... son idénticos a la versión anterior)

    private void findDuplicates(ArrayList<ArrayList<Integer>> board, ArrayList<ArrayList<Boolean>> errors, int startRow, int startCol, int endRow, int endCol, boolean byRow) {
        Set<Integer> seen = new HashSet<>();
        Set<Integer> duplicates = new HashSet<>();
        for (int r = startRow; r <= endRow; r++) {
            for (int c = startCol; c <= endCol; c++) {
                int val = board.get(r).get(c);
                if (val != 0) {
                    if (!seen.add(val)) {
                        duplicates.add(val);
                    }
                }
            }
        }
        if (!duplicates.isEmpty()) {
            for (int r = startRow; r <= endRow; r++) {
                for (int c = startCol; c <= endCol; c++) {
                    if (duplicates.contains(board.get(r).get(c))) {
                        errors.get(r).set(c, true);
                    }
                }
            }
        }
    }

    private void findDuplicatesInBlock(ArrayList<ArrayList<Integer>> board, ArrayList<ArrayList<Boolean>> errors, int startRow, int startCol) {
        Set<Integer> seen = new HashSet<>();
        Set<Integer> duplicates = new HashSet<>();
        for (int r = startRow; r < startRow + 2; r++) {
            for (int c = startCol; c < startCol + 3; c++) {
                int val = board.get(r).get(c);
                if (val != 0) {
                    if (!seen.add(val)) {
                        duplicates.add(val);
                    }
                }
            }
        }
        if (!duplicates.isEmpty()) {
            for (int r = startRow; r < startRow + 2; r++) {
                for (int c = startCol; c < startCol + 3; c++) {
                    if (duplicates.contains(board.get(r).get(c))) {
                        errors.get(r).set(c, true);
                    }
                }
            }
        }
    }


    /**
     * Aplica el borde rojo de error (HU-4) [cite: 88, 91]
     */
    private void applyErrorStyles(ArrayList<ArrayList<Boolean>> errorCells) {
        for (int r = 0; r < 6; r++) {
            for (int c = 0; c < 6; c++) {
                TextField cell = cells.get(r).get(c);
                String currentStyle = cell.getStyle();

                // Preservar el estilo de "pista" o "ayuda" si no hay error
                String baseStyle;
                if (currentStyle.contains("-fx-background-color: #f0f0f0")) { // Pista inicial
                    baseStyle = "-fx-alignment: center; -fx-background-color: #f0f0f0; -fx-font-weight: bold;";
                } else if (currentStyle.contains("-fx-background-color: #a0e0a0")) { // Ayuda
                    baseStyle = "-fx-alignment: center; -fx-background-color: #a0e0a0;";
                } else { // Celda normal editable
                    baseStyle = "-fx-alignment: center;";
                }

                if (errorCells.get(r).get(c)) {
                    cell.setStyle(baseStyle + " -fx-border-color: red; -fx-border-width: 2;");
                } else {
                    cell.setStyle(baseStyle + " -fx-border-color: null;");
                }
            }
        }
    }

    // (Método getBoardModel... idéntico)
    private ArrayList<ArrayList<Integer>> getBoardModel() {
        ArrayList<ArrayList<Integer>> board = new ArrayList<>();
        for (int r = 0; r < 6; r++) {
            ArrayList<Integer> rowList = new ArrayList<>();
            for (int c = 0; c < 6; c++) {
                int val = parseIntSafe(cells.get(r).get(c).getText());
                rowList.add(val);
            }
            board.add(rowList);
        }
        return board;
    }

    // (Método parseIntSafe... idéntico)
    private int parseIntSafe(String text) {
        if (text == null || text.isEmpty()) return 0;
        try { return Integer.parseInt(text); }
        catch (NumberFormatException e) { return 0; }
    }

    // (Método populateCellsArrayList... idéntico)
    private void populateCellsArrayList() {
        ArrayList<TextField> allCells = new ArrayList<>(Arrays.asList(
                cell00, cell01, cell02, cell03, cell04, cell05,
                cell10, cell11, cell12, cell13, cell14, cell15,
                cell20, cell21, cell22, cell23, cell24, cell25,
                cell30, cell31, cell32, cell33, cell34, cell35,
                cell40, cell41, cell42, cell43, cell44, cell45,
                cell50, cell51, cell52, cell53, cell54, cell55
        ));
        cells.clear();
        for (int r = 0; r < 6; r++) {
            ArrayList<TextField> rowList = new ArrayList<>();
            for (int c = 0; c < 6; c++) {
                rowList.add(allCells.get(r * 6 + c));
            }
            cells.add(rowList);
        }
    }

    // --- MÉTODOS AYUDANTES (Helpers) ---

    /**
     * : Helper para mostrar alertas (Usado por HU-2 y HU-5)
     */
    private Optional<ButtonType> showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null); // No usamos header
        alert.setContentText(content);
        return alert.showAndWait();
    }

    /**
     * Helper para HU-5. Verifica si un número es seguro
     * para colocar en una celda específica.
     */
    private boolean isSafe(int row, int col, int num, ArrayList<ArrayList<Integer>> board) {
        // 1. Comprobar Fila
        for (int c = 0; c < 6; c++) {
            if (board.get(row).get(c) == num) {
                return false;
            }
        }
        // 2. Comprobar Columna
        for (int r = 0; r < 6; r++) {
            if (board.get(r).get(col) == num) {
                return false;
            }
        }
        // 3. Comprobar Bloque 2x3
        int blockStartRow = (row / 2) * 2;
        int blockStartCol = (col / 3) * 3;
        for (int r = blockStartRow; r < blockStartRow + 2; r++) {
            for (int c = blockStartCol; c < blockStartCol + 3; c++) {
                if (board.get(r).get(c) == num) {
                    return false;
                }
            }
        }

        // Si pasa las 3 pruebas, es un número seguro
        return true;
    }
}