package UI;

import javax.swing.*;
import static Logic.Calculator.CalculateExpression;
import static Logic.Calculator.Convert;

public class CalculatorMainForm extends JFrame {
    private JPanel mainPanel;
    private JTextField expressionTextField;
    private JButton calculateButton;
    private JLabel tipLabel;
    private JLabel answerLabel;

    //region Инициализация и запуск

    /**
     * Инициализация окна
     */
    private CalculatorMainForm() {
        setContentPane(mainPanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        InitFormObjects();
        setVisible(true);
    }

    /**
     * Запуск приложения
     */
    public static void main(String[] args) {
        new CalculatorMainForm();
        //System.out.println(Convert("5*(-3+8)"));
        //System.out.println(CalculateExpression(Convert("9+(3*(121*3)+5*(300/25))+36"))); //1194.0
        //System.out.println(CalculateExpression(Convert("-9+(-3.1*(121.12*-3.001)+5*-(300/-25))+-36"))); //1141.7914719999999
    }

    /**
     * Подписка объектов формы на события
     */
    private void InitFormObjects() {

        calculateButton.addActionListener(e -> answerLabel.setText(
                Double.toString(
                        CalculateExpression(
                            Convert(
                                expressionTextField.getText()
                            )
                        )
                )
        ));
    }
}


