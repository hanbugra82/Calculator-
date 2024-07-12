    package com.bugra.calculator;

    import javafx.animation.TranslateTransition;
    import javafx.application.Platform;
    import javafx.event.ActionEvent;
    import javafx.fxml.FXML;
    import javafx.geometry.Insets;
    import javafx.scene.Node;
    import javafx.scene.Scene;
    import javafx.scene.control.*;
    import javafx.scene.input.Clipboard;
    import javafx.scene.input.ClipboardContent;
    import javafx.scene.layout.GridPane;
    import javafx.scene.layout.Pane;
    import javafx.scene.layout.VBox;
    import javafx.scene.text.Font;
    import javafx.scene.text.FontWeight;
    import javafx.scene.text.Text;
    import javafx.util.Duration;
    import java.math.BigDecimal;
    import java.util.Stack;

    public class CalculatorController {
        @FXML
        private ToggleButton rpnModeToggle;
        private boolean isRpnMode = false;
        private BigDecimal lastResult = BigDecimal.ZERO;
        private boolean startNewOperation = true;
        @FXML
        private TextField resultTextField;
        @FXML
        private VBox historyContent;
        @FXML
        private Button historyButton1;
        @FXML
        private ScrollPane historyScrollPane;
        @FXML
        private TextField buttonTextDisplayField;
        private StringBuilder operation = new StringBuilder();
        @FXML
        private GridPane gridPane;
        @FXML
        private void handleCeButtonAction() {
            resultTextField.clear();
            buttonTextDisplayField.clear();
            resultTextField.setText("0");
        }
        @FXML
        private void handleEnterAction(ActionEvent event) {
            if (isRpnMode) {
                appendToOperationRpn(" ");
                if (operation.toString().trim().split("\\s+").length > 1) {
                    calculateRpnResult();
                }
            }
        }
        public void initialize() {

            gridPane.setHgap(2); // Hücreler arası yatay boşluk
            gridPane.setVgap(2); // Hücreler arası dikey boşluk

            for (Node node : gridPane.getChildren()) {
                if (node instanceof Button) {
                    Button button = (Button) node;
                    button.setPrefWidth(80);  // Başlangıç genişliği
                    button.setPrefHeight(30);  // Başlangıç yüksekliği
                }
            }


            historyScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            historyScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);


            Platform.runLater(() -> {
                Scene scene = gridPane.getScene();
                if (scene != null) {
                    double screenHeight = scene.getHeight();
                    historyScrollPane.setPrefHeight(screenHeight / 2);
                    historyScrollPane.setTranslateY(screenHeight / 2);
                    scene.heightProperty().addListener((obs, oldVal, newVal) -> {
                        double newHeight = newVal.doubleValue() / 2;
                        historyScrollPane.setPrefHeight(newHeight);
                        historyScrollPane.setTranslateY(newHeight);
                    });
                }
            });


                Scene scene = gridPane.getScene();
                if (scene != null) {
                    scene.widthProperty().addListener((obs, oldVal, newVal) -> updateButtonHeights());
                    scene.heightProperty().addListener((obs, oldVal, newVal) -> updateButtonHeights());
                }


            for (Node node : gridPane.getChildren()) {
                if (node instanceof Button) {
                    Button button = (Button) node;
                    button.setMaxWidth(Double.MAX_VALUE);
                    button.setMaxHeight(Double.MAX_VALUE);
                    GridPane.setMargin(button, new Insets(1, 1, 1, 1));

                    if ("binButton".equals(button.getId()) || "octButton".equals(button.getId()) ||
                            "hexButton".equals(button.getId()) || "historyButton".equals(button.getId())) {
                        button.setPrefHeight(30);
                        button.setMaxHeight(30);
                        button.maxHeightProperty().bind(gridPane.heightProperty().divide(gridPane.getRowCount()).subtract(10));
                    }
                }
            }

            ContextMenu contextMenu = new ContextMenu();
            contextMenu.setStyle("-fx-font-size: 20px;");

            MenuItem cut = new MenuItem("Cut");
            MenuItem copy = new MenuItem("Copy");
            MenuItem paste = new MenuItem("Paste");
            MenuItem delete = new MenuItem("Delete");
            MenuItem selectAll = new MenuItem("Select All");

            copy.setOnAction(e -> {
                final Clipboard clipboard = Clipboard.getSystemClipboard();
                final ClipboardContent content = new ClipboardContent();
                content.putString(resultTextField.getText());
                clipboard.setContent(content);
            });
            delete.setOnAction(e -> {
                resultTextField.clear();
            });
            selectAll.setOnAction(e -> {
                resultTextField.selectAll();
            });
            paste.setOnAction(e -> {
                final Clipboard clipboard = Clipboard.getSystemClipboard();
                resultTextField.setText(clipboard.getString());
            });
            cut.setOnAction(e -> {
                final Clipboard clipboard = Clipboard.getSystemClipboard();
                final ClipboardContent content = new ClipboardContent();
                content.putString(resultTextField.getText());
                clipboard.setContent(content);
                resultTextField.clear();
            });

            contextMenu.getItems().addAll(cut, copy, paste, delete, selectAll);
            resultTextField.setContextMenu(contextMenu);
            dimPane.setOnMouseClicked(event -> {
                historyScrollPane.setVisible(false);
                dimPane.setVisible(false);
            });

            Text text = new Text();
            text.textProperty().bind(resultTextField.textProperty());
            text.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 44));
            text.layoutBoundsProperty().addListener((observable, oldValue, newValue) -> {
                double maxWidth = 338.0;
                if (text.getBoundsInLocal().getWidth() > maxWidth) {
                    double fontSize = resultTextField.getFont().getSize() * maxWidth / text.getBoundsInLocal().getWidth();
                    resultTextField.setFont(Font.font(resultTextField.getFont().getFamily(), fontSize));
                }
            });
        }

        private void updateButtonHeights() {
            Platform.runLater(() -> {
                double windowHeight = gridPane.getScene().getWindow().getHeight();
                double newHeight = Math.max(20, windowHeight / gridPane.getRowCount() - 10);

                for (Node node : gridPane.getChildren()) {
                    if (node instanceof Button) {
                        Button button = (Button) node;
                        if ("binButton".equals(button.getId()) || "octButton".equals(button.getId()) ||
                                "hexButton".equals(button.getId()) || "historyButton".equals(button.getId()) ||
                                "enterButton".equals(button.getId())) {
                            button.setPrefHeight(30);
                            button.setMaxHeight(30);
                            button.maxHeightProperty().bind(gridPane.heightProperty().divide(gridPane.getRowCount()).subtract(10));
                        }
                        else {
                            button.setPrefHeight(newHeight);
                        }
                    }
                }
            });
        }

        @FXML
        private void handleRpnModeToggle(ActionEvent event) {
            isRpnMode = rpnModeToggle.isSelected();
            if (isRpnMode) {
                System.out.println("RPN Mode Enabled");
            } else {
                System.out.println("RPN Mode Disabled");
            }
        }
        @FXML
        private Pane dimPane;
        @FXML
        private void handleHistoryButtonAction() {
            double screenHeight = gridPane.getScene().getHeight();
            historyScrollPane.setPrefHeight(screenHeight / 2);
            historyScrollPane.setTranslateY(screenHeight);

            TranslateTransition transition = new TranslateTransition(Duration.seconds(0.5), historyScrollPane);

            if (!historyScrollPane.isVisible()) {
                historyScrollPane.setVisible(true);
                dimPane.setVisible(true);
                dimPane.toFront();
                historyScrollPane.toFront();
                transition.setToY(screenHeight / 2);
            } else {
                transition.setToY(screenHeight);
                transition.setOnFinished(event -> {
                    historyScrollPane.setVisible(false);
                    dimPane.setVisible(false);
                });
            }
            transition.play();
        }

        @FXML
        private void handleTrashButtonAction() {
            historyContent.getChildren().removeIf(node -> node != historyButton1);
        }

        private ContextMenu createHistoryContextMenu(TextField historyItemField) {
            ContextMenu contextMenu = new ContextMenu();
            contextMenu.setStyle("-fx-font-size: 15px;");

            MenuItem copy = new MenuItem("Copy");
            MenuItem selectAll = new MenuItem("Select All");
            copy.setOnAction(e -> {
                Clipboard clipboard = Clipboard.getSystemClipboard();
                ClipboardContent content = new ClipboardContent();
                content.putString(historyItemField.getSelectedText());
                clipboard.setContent(content);
            });

            selectAll.setOnAction(e -> historyItemField.selectAll());
            contextMenu.getItems().addAll(copy, selectAll);
            return contextMenu;
        }

        private void addHistoryItem(String operation, String result) {

            TextField historyItemField = new TextField(operation + " = " + result);
            historyItemField.setEditable(false);
            historyItemField.getStyleClass().add("history-item-field");
            historyItemField.setContextMenu(createHistoryContextMenu(historyItemField));
            historyContent.getChildren().add(historyItemField);
        }

        @FXML
        private void handleCButtonAction() {
            operation.setLength(0);
            resultTextField.setText("0");
            buttonTextDisplayField.setText("");
            startNewOperation = true;
        }

        @FXML
        private void handleBackspaceButtonAction() {
            if (operation.length() > 0) {
                operation.deleteCharAt(operation.length() - 1);
                buttonTextDisplayField.setText(operation.toString());
                resultTextField.setText(operation.toString());
            }
        }

        @FXML
        private void handleReciprocalButtonAction() {
            try {
                double value = Double.parseDouble(resultTextField.getText());
                if (value != 0) {
                    resultTextField.setText(String.valueOf(1 / value));
                    operation.setLength(0);
                    operation.append(resultTextField.getText());
                    buttonTextDisplayField.setText(operation.toString());
                } else {
                    resultTextField.setText("Error");
                }
            } catch (NumberFormatException e) {
                resultTextField.setText("Error");
            }
        }

        @FXML
        private void handleSquareButtonAction() {
            try {
                double value = Double.parseDouble(resultTextField.getText());
                resultTextField.setText(String.valueOf(value * value));
                operation.setLength(0);
                operation.append(resultTextField.getText());
                buttonTextDisplayField.setText(operation.toString());
            } catch (NumberFormatException e) {
                resultTextField.setText("Error");
            }
        }

        @FXML
        private void handleRootButtonAction() {
            try {
                double value = Double.parseDouble(resultTextField.getText());
                if (value >= 0) {
                    resultTextField.setText(String.valueOf(Math.sqrt(value)));
                    operation.setLength(0);
                    operation.append(resultTextField.getText());
                    buttonTextDisplayField.setText(operation.toString());
                } else {
                    resultTextField.setText("Error");
                }
            } catch (NumberFormatException e) {
                resultTextField.setText("Error");
            }
        }

        @FXML
        private void handleOperatorButtonAction(ActionEvent event) {
            Button clickedButton = (Button) event.getSource();
            String buttonText = clickedButton.getText();
            String operator = handleOperatorButton(buttonText);
            if (!operator.isEmpty()) {
                if (isRpnMode) {
                    appendToOperationRpn(operator.trim());
                    calculateRpnResult();
                } else {
                    appendToOperation(operator);
                }
            }
        }

        @FXML
        private void handleNumberButtonAction(ActionEvent event) {
            Button clickedButton = (Button) event.getSource();
            String buttonText = clickedButton.getText();

            if (isRpnMode) {
                appendToOperationRpn(buttonText);
                resultTextField.setText(operation.toString());
            } else {
                // Normal hesaplama modu
                if (startNewOperation || (!operation.isEmpty() && !Character.isDigit(operation.charAt(operation.length() - 1)) && operation.charAt(operation.length() - 1) != '.')) {
                    resultTextField.clear();
                    startNewOperation = false;
                }

                resultTextField.setText(resultTextField.getText() + buttonText);
                appendToOperation(buttonText);
            }
        }
        private void calculateRpnResult() {
            if (operation.length() == 0) return;

            String[] tokens = operation.toString().trim().split("\\s+");
            Stack<BigDecimal> values = new Stack<>();

            try {
                for (String token : tokens) {
                    if (token.matches("-?\\d+(\\.\\d+)?")) {  // Sayı kontrolü
                        values.push(new BigDecimal(token));
                    } else {  // Operatör kontrolü
                        BigDecimal b = values.pop();
                        BigDecimal a = values.pop();
                        switch (token) {
                            case "+":
                                values.push(a.add(b));
                                break;
                            case "-":
                                values.push(a.subtract(b));
                                break;
                            case "*":
                                values.push(a.multiply(b));
                                break;
                            case "/":
                                if (b.compareTo(BigDecimal.ZERO) == 0)
                                    throw new ArithmeticException("Cannot divide by zero");
                                values.push(a.divide(b, 10, BigDecimal.ROUND_HALF_UP));
                                break;
                        }
                    }
                }
                BigDecimal result = values.pop();
                Platform.runLater(() -> {
                    resultTextField.setText(result.stripTrailingZeros().toPlainString());
                    buttonTextDisplayField.setText(operation.toString() + " = " + result.stripTrailingZeros().toPlainString());
                    addHistoryItem(operation.toString(), result.stripTrailingZeros().toPlainString());
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    resultTextField.setText("Error: Invalid Expression");
                    buttonTextDisplayField.setText("Error: Invalid Expression");
                });
            }
        }

        @FXML
        private void handleEqualsButtonAction() {
            if (isRpnMode) {
                calculateRpnResult();
            } else {
                calculateResult();
            }
        }

        private void appendToOperationRpn(String text) {
            operation.append(text + " ");
            buttonTextDisplayField.setText(operation.toString());
        }


        private void appendToOperation(String text) {
            operation.append(text);
            buttonTextDisplayField.setText(operation.toString());
        }

        @FXML
        private void handleToggleSign() {
            try {
                double value = Double.parseDouble(resultTextField.getText());
                resultTextField.setText(String.valueOf(-value));
                operation.setLength(0);
                operation.append(resultTextField.getText());
                buttonTextDisplayField.setText(operation.toString());
            } catch (NumberFormatException e) {
                resultTextField.setText("Error");
            }
        }

        @FXML
        private void handelCalculatePercentage() {
            try {
                double value = Double.parseDouble(resultTextField.getText());
                resultTextField.setText(String.valueOf(value / 100));
                operation.setLength(0);
                operation.append(resultTextField.getText());
                buttonTextDisplayField.setText(operation.toString());
            } catch (NumberFormatException e) {
                resultTextField.setText("Error");
            }
        }

        @FXML
        private void handleDecimalButtonAction() {
            String currentText = resultTextField.getText();
            if (!currentText.contains(".")) {
                resultTextField.setText(currentText + ".");
            } else {
                resultTextField.setText(currentText);
            }
            appendToOperation(".");
        }

        private String handleOperatorButton(String text) {
            switch (text) {
                case "×":
                    return " * ";
                case "÷":
                    return " / ";
                case "+":
                    return " + ";
                case "-":
                    return " - ";
                case "±":
                    handleToggleSign();
                    return "";
                case "=":
                    calculateResult();
                    return "";
                case "CE":
                    handleCeButtonAction();
                    return "";
                case "C":
                    handleCButtonAction();
                    return "";
                case "←":
                    handleBackspaceButtonAction();
                    return "";
                case "%":
                    handelCalculatePercentage();
                    return "";
                case "1/x":
                    handleReciprocalButtonAction();
                    return "";
                case "x²":
                    handleSquareButtonAction();
                    return "";
                case "√x":
                    handleRootButtonAction();
                    return "";
                case ".":
                    return ".";
                default:
                    return text;
            }
        }

        @FXML
        private void calculateResult() {
            if (operation.length() == 0) {
                return;
            }

            try {
                String expression = operation.toString();
                BigDecimal result;
                String[] tokens = expression.split(" ");
                Stack<BigDecimal> values = new Stack<>();
                Stack<String> operators = new Stack<>();

                for (String token : tokens) {
                    if (token.matches("[0-9]+\\.?[0-9]*")) {
                        values.push(new BigDecimal(token));
                    } else if (token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/")) {
                        while (!operators.empty() && hasPrecedence(token, operators.peek())) {
                            values.push(applyOp(operators.pop(), values.pop(), values.pop()));
                        }
                        operators.push(token);
                    }
                }

                while (!operators.empty()) {
                    values.push(applyOp(operators.pop(), values.pop(), values.pop()));
                }

                result = values.pop();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        resultTextField.setText(result.stripTrailingZeros().toPlainString());
                        buttonTextDisplayField.setText(expression + " = " + result.stripTrailingZeros().toPlainString());
                        addHistoryItem(expression, result.stripTrailingZeros().toPlainString());
                    }
                });

                lastResult = result;
                startNewOperation = true;
            } catch (ArithmeticException ex) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        resultTextField.setText("Error: Divide by Zero!");
                        buttonTextDisplayField.setText("Error: Divide by Zero!");
                    }
                });
            } catch (Exception ex) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        resultTextField.setText("Error: Invalid Expression");
                        buttonTextDisplayField.setText("Error: Invalid Expression");
                    }
                });
            }
        }

        private static boolean hasPrecedence(String op1, String op2) {
            if (op2.equals("(") || op2.equals(")")) {
                return false;
            }
            if ((op1.equals("*") || op1.equals("/")) && (op2.equals("+") || op2.equals("-"))) {
                return false;
            }
            return true;
        }

        private static BigDecimal applyOp(String op, BigDecimal b, BigDecimal a) {
            switch (op) {
                case "+":
                    return a.add(b);
                case "-":
                    return a.subtract(b);
                case "*":
                    return a.multiply(b);
                case "/":
                    if (b.equals(BigDecimal.ZERO)) {
                        throw new ArithmeticException("Cannot divide by zero");
                    }
                    return a.divide(b, 10, BigDecimal.ROUND_HALF_UP);
            }
            return BigDecimal.ZERO;
        }

        @FXML
        private void handleBinButtonClick(ActionEvent event) {
            convertToBinary();
            resultTextField.setStyle("-fx-font-size: 32px;");
        }

        @FXML
        private void convertToBinary() {
            String currentText = resultTextField.getText();
            try {
                String[] parts = currentText.split("\\.");
                int integerPart = Integer.parseInt(parts[0]);
                String binaryIntegerPart = Integer.toBinaryString(integerPart);
                StringBuilder paddedBinaryInteger = new StringBuilder(binaryIntegerPart);
                while (paddedBinaryInteger.length() % 4 != 0) {
                    paddedBinaryInteger.insert(0, '0');
                }

                StringBuilder formattedBinary = new StringBuilder();
                for (int i = 0; i < paddedBinaryInteger.length(); i++) {
                    formattedBinary.append(paddedBinaryInteger.charAt(i));
                    if ((i + 1) % 4 == 0 && i != paddedBinaryInteger.length() - 1) {
                        formattedBinary.append(" ");
                    }
                }

                String binaryResult = formattedBinary.toString();
                resultTextField.setText(binaryResult);
                addHistoryItem("Bin(" + currentText + ")", binaryResult);
            } catch (NumberFormatException e) {
                resultTextField.setText("Invalid Input");
            }
        }

        @FXML
        private void convertToHex() {
            String currentText = resultTextField.getText();
            try {
                String[] parts = currentText.split("\\.");
                int integerPart = Integer.parseInt(parts[0]);
                String hexIntegerPart = Integer.toHexString(integerPart).toUpperCase();
                String hexResult = hexIntegerPart;
                resultTextField.setText(hexResult);
                addHistoryItem("Hex(" + currentText + ")", hexResult);
            } catch (NumberFormatException e) {
                resultTextField.setText("Invalid Input");
            }
        }

        @FXML
        private void convertToOct() {
            String currentText = resultTextField.getText();
            try {
                String[] parts = currentText.split("\\.");
                int integerPart = Integer.parseInt(parts[0]);
                String octIntegerPart = Integer.toOctalString(integerPart);
                String octResult = octIntegerPart;
                resultTextField.setText(octResult);
                addHistoryItem("Oct(" + currentText + ")", octResult);
            } catch (NumberFormatException e) {
                resultTextField.setText("Invalid Input");
            }
        }

    }
