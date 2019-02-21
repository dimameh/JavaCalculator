package UI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static Logic.Calculator.CalculateExpression;
import static Logic.Calculator.Convert;

public class CalculatorMainForm extends JFrame {
    private JPanel mainPanel;
    private JTextField expressionField;
    private JButton calculateButton;
    private JTextArea resultTextArea;

    //region Инициализация и запуск

    /**
     * Инициализация окна
     */
    public CalculatorMainForm() {
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
        //new CalculatorMainForm();
        //System.out.println(Convert("5*(-3+8)"));
        //TODO: Добавить отрицательные числа
        //System.out.println(CalculateExpression(Convert("9+(3*(121*3)+5*(300/25))+36"))); //1194.0
        System.out.println(CalculateExpression(Convert("-9+(-3*(121*-3)+5*-(300/-25))+-36"))); //1104
    }

    /**
     * Подписка объектов формы на события
     */
    private void InitFormObjects() {

        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }
}


