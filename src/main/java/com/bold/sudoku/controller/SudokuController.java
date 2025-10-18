package com.bold.sudoku.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import java.util.HashSet;
import java.util.Set;

public class SudokuController {

    @FXML
    private GridPane sudokuGrid;

    // Referencias a todas las celdas de texto (TextFields)
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

    // Matriz 2D para acceder fácilmente a las celdas por fila y columna
    private TextField[][] cells;

    // Definición del tablero inicial (Cumple Criterio 1: 2 números por bloque 2x3)
    // 0 representa una celda vacía.
    private final int[][] initialBoard = {
            {5, 1, 0, 0, 3, 6},
            {0, 0, 0, 0, 0, 0},
            {6, 3, 0, 0, 1, 4},
            {0, 0, 0, 0, 0, 0},
            {4, 5, 0, 0, 2, 3},
            {0, 0, 0, 0, 0, 0}
    };

    /**
     * Método de inicialización. Se llama automáticamente después de cargar el FXML.
     * Aquí es donde configuramos los listeners y comenzamos el juego.
     */
    @FXML
    public void initialize() {
        populateCellsArray();
        setupInputListeners();
        startNewGame();
    }

    /**
     * (Historia 1) Inicia un nuevo juego.
     * Rellena el tablero con los números iniciales y los hace no editables.
     * Limpia las celdas que el usuario debe rellenar.
     */
    private void startNewGame() {
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 6; col++) {
                int value = initialBoard[row][col];
                TextField cell = cells[row][col];

                if (value != 0) {
                    // Celda pre-rellenada
                    cell.setText(String.valueOf(value));
                    cell.setEditable(false);
                    // Estilo para celdas pre-rellenadas (ligeramente gris)
                    cell.setStyle("-fx-alignment: center; -fx-background-color: #f0f0f0; -fx-font-weight: bold;");
                } else {
                    // Celda vacía para el usuario
                    cell.setText("");
                    cell.setEditable(true);
                    // Estilo normal
                    cell.setStyle("-fx-alignment: center;");
                }
            }
        }
        // Valida el tablero inicial (aunque debería ser válido por definición)
        validateBoard();
    }

    /**
     * (Historia 2 y 3) Configura los listeners en cada celda editable.
     * Esto maneja la restricción de entrada (1-6) y la validación en tiempo real.
     */
    private void setupInputListeners() {
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 6; col++) {
                TextField cell = cells[row][col];

                // Agrega un listener a la propiedad de texto de la celda
                cell.textProperty().addListener((observable, oldValue, newValue) -> {
                    // --- Criterio de Aceptación (Historia 2): Permitir solo 1-6 ---
                    if (!newValue.matches("[1-6]?")) {
                        // Si el nuevo valor no es un dígito de 1 a 6 o vacío, revierte al valor anterior.
                        cell.setText(oldValue);
                    }

                    // --- Criterio de Aceptación (Historia 3): Validación en tiempo real ---
                    // Después de cualquier cambio válido, re-valida todo el tablero.
                    validateBoard();
                });
            }
        }
    }

    /**
     * (Historia 3) Valida todo el tablero y aplica estilos de error.
     * Comprueba filas, columnas y bloques 2x3 en busca de duplicados.
     */
    private void validateBoard() {
        int[][] boardModel = getBoardModel();
        boolean[][] errorCells = new boolean[6][6];

        // 1. Comprobar Filas
        for (int r = 0; r < 6; r++) {
            findDuplicates(boardModel, errorCells, r, 0, r, 5, true); // Comprueba fila r
        }

        // 2. Comprobar Columnas
        for (int c = 0; c < 6; c++) {
            findDuplicates(boardModel, errorCells, 0, c, 5, c, false); // Comprueba columna c
        }

        // 3. Comprobar Bloques 2x3
        for (int blockRow = 0; blockRow < 6; blockRow += 2) {
            for (int blockCol = 0; blockCol < 6; blockCol += 3) {
                findDuplicatesInBlock(boardModel, errorCells, blockRow, blockCol);
            }
        }

        // 4. Aplicar Estilos de Error
        applyErrorStyles(errorCells);
    }

    /**
     * Helper para `validateBoard`. Comprueba duplicados en una fila o columna.
     * @param byRow Si es true, comprueba fila. Si es false, comprueba columna.
     */
    private void findDuplicates(int[][] board, boolean[][] errors, int startRow, int startCol, int endRow, int endCol, boolean byRow) {
        Set<Integer> seen = new HashSet<>();
        Set<Integer> duplicates = new HashSet<>();

        for (int r = startRow; r <= endRow; r++) {
            for (int c = startCol; c <= endCol; c++) {
                int val = board[r][c];
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
                    if (duplicates.contains(board[r][c])) {
                        errors[r][c] = true;
                    }
                }
            }
        }
    }

    /**
     * Helper para `validateBoard`. Comprueba duplicados en un bloque 2x3.
     */
    private void findDuplicatesInBlock(int[][] board, boolean[][] errors, int startRow, int startCol) {
        Set<Integer> seen = new HashSet<>();
        Set<Integer> duplicates = new HashSet<>();

        for (int r = startRow; r < startRow + 2; r++) {
            for (int c = startCol; c < startCol + 3; c++) {
                int val = board[r][c];
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
                    if (duplicates.contains(board[r][c])) {
                        errors[r][c] = true;
                    }
                }
            }
        }
    }

    /**
     * Helper para `validateBoard`. Aplica el borde rojo a las celdas con errores.
     */
    private void applyErrorStyles(boolean[][] errorCells) {
        for (int r = 0; r < 6; r++) {
            for (int c = 0; c < 6; c++) {
                TextField cell = cells[r][c];
                String baseStyle = cell.isEditable() ? "-fx-alignment: center;" :
                        "-fx-alignment: center; -fx-background-color: #f0f0f0; -fx-font-weight: bold;";

                if (errorCells[r][c]) {
                    // Criterio de Aceptación (Historia 3): Notificación visual (borde rojo)
                    cell.setStyle(baseStyle + " -fx-border-color: red; -fx-border-width: 2;");
                } else {
                    // Restablece el estilo si no hay error
                    cell.setStyle(baseStyle + " -fx-border-color: null;");
                }
            }
        }
    }

    /**
     * Convierte el texto de las celdas (String) en una matriz de enteros (int).
     * Las celdas vacías se convierten en 0.
     */
    private int[][] getBoardModel() {
        int[][] board = new int[6][6];
        for (int r = 0; r < 6; r++) {
            for (int c = 0; c < 6; c++) {
                board[r][c] = parseIntSafe(cells[r][c].getText());
            }
        }
        return board;
    }

    /**
     * Convierte un String a int de forma segura. Devuelve 0 si está vacío o no es válido.
     */
    private int parseIntSafe(String text) {
        if (text == null || text.isEmpty()) {
            return 0;
        }
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Llena la matriz `cells[][]` con las referencias FXML inyectadas.
     * Esto hace que el acceso a las celdas (ej. cells[2][3]) sea programático.
     */
    private void populateCellsArray() {
        cells = new TextField[6][6];
        cells[0][0] = cell00; cells[0][1] = cell01; cells[0][2] = cell02;
        cells[0][3] = cell03; cells[0][4] = cell04; cells[0][5] = cell05;
        cells[1][0] = cell10; cells[1][1] = cell11; cells[1][2] = cell12;
        cells[1][3] = cell13; cells[1][4] = cell14; cells[1][5] = cell15;
        cells[2][0] = cell20; cells[2][1] = cell21; cells[2][2] = cell22;
        cells[2][3] = cell23; cells[2][4] = cell24; cells[2][5] = cell25;
        cells[3][0] = cell30; cells[3][1] = cell31; cells[3][2] = cell32;
        cells[3][3] = cell33; cells[3][4] = cell34; cells[3][5] = cell35;
        cells[4][0] = cell40; cells[4][1] = cell41; cells[4][2] = cell42;
        cells[4][3] = cell43; cells[4][4] = cell44; cells[4][5] = cell45;
        cells[5][0] = cell50; cells[5][1] = cell51; cells[5][2] = cell52;
        cells[5][3] = cell53; cells[5][4] = cell54; cells[5][5] = cell55;
    }
}