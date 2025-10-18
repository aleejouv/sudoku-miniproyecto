module com.bold.sudoku {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;


    opens com.bold.sudoku to javafx.fxml;
    opens com.bold.sudoku.controller to javafx.fxml;
    exports com.bold.sudoku;
}