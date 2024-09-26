import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.Arrays;

public class SudokuGame extends Application {

    private static final int SIZE = 9; // 스도쿠 크기
    private TextField[][] cells = new TextField[SIZE][SIZE];
    private GridPane mainGrid; // 그리드 레이아웃을 클래스 변수로 이동
    int[][] sudokuPuzzle;
    int[][] sudokuAnswer;
    int[][] sudokuBack;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Sudoku");
        mainGrid = new GridPane(); // 그리드 레이아웃 초기화
        mainGrid.setAlignment(Pos.CENTER);
        mainGrid.setPadding(new Insets(10));
        mainGrid.setVgap(5); // 블록 간의 수직 간격
        mainGrid.setHgap(5); // 블록 간의 수평 간격
        
        // CSS 스타일 추가: 테두리를 검은색으로 설정
        mainGrid.setStyle("-fx-background-color: white;-fx-padding: 10;");

        generateNewPuzzle(); // 새로운 퍼즐 생성

        // 버튼 생성
        Button submitButton = new Button("정답 제출");
        submitButton.setPrefSize(130, 50);
        Label resultLabel = new Label(); // 결과를 표시할 레이블
        resultLabel.setStyle("-fx-font-size: 25px; -fx-alignment: CENTER;");
        
        // 다시하기 버튼 생성
        Button restartButton = new Button("새 게임");
        restartButton.setPrefSize(130, 50);
        
        // 다시하기 버튼 클릭 시 새로운 퍼즐 생성
        restartButton.setOnAction(e -> {
            clearGrid(); // 기존 그리드 지우기
            generateNewPuzzle(); // 새로운 퍼즐 생성
            resultLabel.setText(""); // 결과 레이블 초기화
        });

        submitButton.setOnAction(e -> {
            boolean check = Arrays.deepEquals(sudokuAnswer, sudokuPuzzle);
            if (check == true) {
                resultLabel.setText("축하드립니다. 전부 맞히셨습니다.");
            }
            else {
                resultLabel.setText("틀린 부분이 있습니다. 확인해주세요.");
            }
            
        });

        // 버튼을 HBox에 추가하여 중앙 정렬
        HBox buttonBox = new HBox(submitButton, restartButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10));
        buttonBox.setSpacing(50); // 버튼 사이 간격을 50으로 설정

        // 버튼과 레이블을 VBox에 추가
        VBox vbox = new VBox(10, mainGrid, buttonBox, resultLabel);
        vbox.setStyle("-fx-background-color: white;-fx-padding: 10; -fx-alignment: CENTER;");

        // 씬 생성 및 설정
        Scene scene = new Scene(vbox, 700, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void generateNewPuzzle() {
        Sudoku Puzzle = new Sudoku();
        Sudoku.SudokuResult result = Puzzle.generateSudoku(1);
        sudokuPuzzle = result.getBoard();
        sudokuAnswer = result.getSolution();
        sudokuBack = result.getBoard();
        
        
        int i = 0;
        int j = 0;

        // 스도쿠 셀 생성 및 초기화
        for (int row = 0; row < SIZE; row++) {
            if (row % 3 == 0) {
                mainGrid.add(new Label(""), j, i);
                i = i + 1;
            }
            for (int col = 0; col < SIZE; col++) {
                if (col % 3 == 0) {
                    mainGrid.add(new Label("  "), j, i);
                    j = j + 1;
                }
                if (sudokuPuzzle[row][col] != 0) {
                    Label fixedNumber = new Label(String.valueOf(sudokuPuzzle[row][col]));
                    fixedNumber.setPrefWidth(40);
                    fixedNumber.setPrefHeight(40);
                    fixedNumber.setStyle("-fx-font-size: 20px; -fx-border-color: black; -fx-border-width: 1; -fx-alignment: CENTER;");
                    mainGrid.add(fixedNumber, j, i);
                } else {
                    // 빈 칸은 사용자가 입력할 수 있는 TextField
                    cells[row][col] = new TextField();
                    cells[row][col].setPrefWidth(40);
                    cells[row][col].setPrefHeight(40);
                    cells[row][col].setStyle("-fx-font-size: 20px; -fx-alignment: CENTER;");
                    mainGrid.add(cells[row][col], j, i);
                    
                    final int finalRow = row;
                    final int finalCol = col;

                    cells[finalRow][finalCol].setOnAction(e -> {
                    	try {
                    	    // 입력받은 값 출력
                    	    String inputValue = cells[finalRow][finalCol].getText();
                    	    System.out.println(inputValue + "를(을) 입력하셨습니다.");

                    	    // 문자열을 정수로 변환
                    	    sudokuBack[finalRow][finalCol] = Integer.parseInt(inputValue);
                    	} catch (NumberFormatException ex) {
                    	    System.out.println("유효하지 않은 입력입니다. 정수를 입력해주세요.");
                    	    
                    	    // 텍스트 지우기
                    	    cells[finalRow][finalCol].setText(""); // 텍스트를 지우는 코드
                    	}
                    });
                }
                j = j + 1;
            }
            j = 0;
            i = i + 1;
        }
    }

    private void clearGrid() {
        mainGrid.getChildren().clear(); // 기존 그리드의 모든 요소 제거
    }
}


class Sudoku {
    private static final int SIZE = 9;
    private int[][] board; // 퍼즐 보드
    private int[][] solution; // 정답 보드

    // 결과를 담을 클래스
    public class SudokuResult {
        private int[][] board;
        private int[][] solution;

        public SudokuResult(int[][] board, int[][] solution) {
            this.board = board;
            this.solution = solution;
        }

        public int[][] getBoard() {
            return board;
        }

        public int[][] getSolution() {
            return solution;
        }
    }

    // 스도쿠 생성 메서드
    public SudokuResult generateSudoku(int difficulty) {
        int[][] solutionBoard = new int[SIZE][SIZE];
        fillBoard(solutionBoard); // 정답 보드 채우기

        this.board = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            System.arraycopy(solutionBoard[i], 0, this.board[i], 0, SIZE); // 정답을 복사
        }

        removeCells(this.board, difficulty); // 난이도에 맞게 셀 제거
        this.solution = solutionBoard; // 솔루션 보드 저장
        return new SudokuResult(this.board, this.solution); // 보드와 솔루션 반환
    }

    public int[][] getBoard() {
        return this.board;
    }

    public int[][] getSolution() {
        return this.solution;
    }

    private boolean fillBoard(int[][] board) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] == 0) { // 빈 칸 찾기
                    ArrayList<Integer> randomNums = new ArrayList<>();
                    for (int i = 1; i <= 9; i++) {
                        randomNums.add(i);
                    }
                    Collections.shuffle(randomNums); // 숫자 섞기
                    for (int num : randomNums) {
                        if (checkValid(board, row, col, num)) {
                            board[row][col] = num; // 숫자 채우기
                            if (fillBoard(board)) { // 재귀적으로 채우기
                                return true;
                            }
                            board[row][col] = 0; // 백트래킹
                        }
                    }
                    return false; // 해결할 수 없는 경우
                }
            }
        }
        return true; // 모든 칸이 채워짐
    }

    private boolean checkValid(int[][] board, int row, int col, int num) {
        // 행과 열에 숫자가 있는지 확인
        for (int i = 0; i < SIZE; i++) {
            if (board[row][i] == num || board[i][col] == num) {
                return false;
            }
        }
        // 3x3 서브그리드 확인
        int startRow = 3 * (row / 3);
        int startCol = 3 * (col / 3);
        for (int i = startRow; i < startRow + 3; i++) {
            for (int j = startCol; j < startCol + 3; j++) {
                if (board[i][j] == num) {
                    return false;
                }
            }
        }
        return true;
    }

    private void removeCells(int[][] board, int difficulty) {
        Random random = new Random();
        int count = difficulty; // 지울 셀의 개수
        while (count > 0) {
            int row = random.nextInt(SIZE);
            int col = random.nextInt(SIZE);
            if (board[row][col] != 0) {
                board[row][col] = 0; // 셀 비우기
                count--;
            }
        }
    }
}
