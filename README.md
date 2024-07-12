# Calculator Application
#### Video Demo:  <URL HERE>
This project is a multi-functional calculator application developed as a CS50 final project by Buğra Han. The application is designed
to perform basic arithmetic operations, handle advanced mathematical functions, and display results in different number
systems. The calculator's functionality is divided into three main categories based on complexity and feature set.

## Table of Contents

- [Simple Calculator Functionality](#simple-calculator-functionality)
- [Extended Calculator Functionality](#extended-calculator-functionality)
- [Calculator with Logic](#calculator-with-logic)
- [Features](#features)
- [Installation](#installation)
- [Usage](#usage)
- [Screenshots](#screenshots)

## Simple Calculator Functionality

This version of the calculator includes the following features:

1. Numeric keypad layout reflecting a typical calculator screen.
2. Result of the current operation is displayed at the top of the screen.
3. Sequential mathematical operations are supported: after pressing the equals sign (`=`), the result of the operation
   is displayed. Pressing an operation sign uses the previous result, while pressing a number starts a new operation.
4. Operations are performed in the order they are entered (e.g., `2 + 2 * 2` results in `8`).
5. The num button handles the power (`^`) functionality.

## Extended Calculator Functionality

This version builds upon the simple calculator and includes:

1. Graphic buttons with simple color changes for visual enhancement.
2. Results can be presented in binary, octal, and hexadecimal formats using additional buttons.
3. A history button that opens a new window showing a list of all actions taken since the application started.

## Calculator with Logic

The most advanced version includes:

- Enhanced operation handling: processes values and mathematical operations and calculates the result after
  pressing `=` (e.g., `2 + 2 * 2 = 6`).
- Support for an unlimited number of operating parameters.
- Reverse Polish Notation (RPN) support, allowing systematic processing of operators and operands using a stack for
  complex expressions.

## Features

- **Basic Arithmetic Operations**: Addition, subtraction, multiplication, and division.
- **Advanced Functions**: Power (`^`), binary, octal, and hexadecimal conversions.
- **History Tracking**: View all previous operations performed during the current session.
- **Responsive Design**: The calculator layout adjusts to different screen sizes, maintaining the proportion of buttons
  on phones and tablets.

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/calculator-project.git
2. Install dependencies: These dependencies should be added to your `pom.xml` file.

- JavaFX Controls: org.openjfx:javafx-controls:21
- exp4j: net.objecthunter:exp4j:0.4.8
- apfloat: org.apfloat:apfloat:1.10.0
- Apache Commons Math: org.apache.commons:commons-math3:3.6.1
- JavaFX FXML: org.openjfx:javafx-fxml:17.0.2
- ControlsFX: org.controlsfx:controlsfx:11.1.2
- Ikonli: org.kordamp.ikonli:ikonli-javafx:12.3.1
- JUnit Jupiter API (for testing): org.junit.jupiter:junit-jupiter-api:${junit.version}
- JUnit Jupiter Engine (for testing): org.junit.jupiter:junit-jupiter-engine:${junit.version}
  
## Usage

1. Enhanced operation handling: the calculator processes values and mathematical operations and calculates the result
   after pressing `=` (e.g., `2 + 2 * 2 = 6`).
2. Support for an unlimited number of operating parameters.
3. Incorporation of Reverse Polish Notation (RPN), allowing systematic processing of operators and operands using a
   stack. This feature enables consistent and effective resolution of complex expressions.
    - Example: To calculate `(3 + 4) * 2` in RPN mode, press the keys in the following order:
        1. Press `3`
        2. Press `Enter`
        3. Press `4`
        4. Press `Enter`
        5. Press `+`
        6. Press `2`
        7. Press `Enter`
        8. Press `*`
        9. Finally, press `=`

        - The result will be `14`.

## Screenshots

![screenshot](file:///C:/Users/Bugra/Desktop/CalculatorDemo%201/CalculatorDemo/src/main/resources/screenshots/screenshot.png)