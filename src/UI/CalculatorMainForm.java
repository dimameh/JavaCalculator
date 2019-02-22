package UI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import static Logic.Calculator.*;

/**
 * Класс главной формы
 */
public class CalculatorMainForm extends JFrame {

    private JPanel mainPanel;

    //region кнопки цифр
    private JButton button7;
    private JButton button8;
    private JButton button9;
    private JButton button4;
    private JButton button5;
    private JButton button6;
    private JButton button0;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    //endregion

    //region кнопки операций
    private JButton divisionButton;
    private JButton multiplyButton;
    private JButton minusButton;
    private JButton plusButton;
    private JButton calculateButton;
    //endregion

    //region кнопки редактирования
    private JButton CButton;
    private JButton backspaceButton;
    //endregion

    //region остальные кнопки ввода
    private JButton dotButton;
    private JButton leftBraceButton;
    private JButton rightBraceButton;
    //endregion

    //region элементы расчета
    private JTextField expressionTextField;
    private JLabel answerLabel;
    //endregion

    //region инициализация

    /**
     * Инициализация окна
     */
    private CalculatorMainForm() {
        setContentPane(mainPanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        InitFormObjects();
        setVisible(true);
    }

    /**
     * Проверка вводимых символов в текстовое поле выражения
     */
    private boolean IsCorrectChar(char ch){
        if (ch >= '0' && ch <= '9') {
            return true;
        }

        if (ch == '-' || ch == '+' || ch == '/' || ch == '*') {
            return true;
        }

        if (ch == '(' || ch == ')' || ch == '.' || ch == ',') {
            return true;
        }

        return ch == KeyEvent.VK_BACK_SPACE;
    }

    /**
     * Подписывание кнопок, предназначеных для ввода выражения
     */
    private void addActionsToWriterButtons(){
        var writeButtonTextAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton button = (JButton)e.getSource();
                AddStringToExpression(button.getText());
            }
        };

        leftBraceButton.addActionListener(writeButtonTextAction);
        rightBraceButton.addActionListener(writeButtonTextAction);

        button0.addActionListener(writeButtonTextAction);
        button1.addActionListener(writeButtonTextAction);
        button2.addActionListener(writeButtonTextAction);
        button3.addActionListener(writeButtonTextAction);
        button4.addActionListener(writeButtonTextAction);
        button5.addActionListener(writeButtonTextAction);
        button6.addActionListener(writeButtonTextAction);
        button7.addActionListener(writeButtonTextAction);
        button8.addActionListener(writeButtonTextAction);
        button9.addActionListener(writeButtonTextAction);

        dotButton.addActionListener(writeButtonTextAction);
        plusButton.addActionListener(writeButtonTextAction);
        minusButton.addActionListener(writeButtonTextAction);
        multiplyButton.addActionListener(writeButtonTextAction);
        divisionButton.addActionListener(writeButtonTextAction);
    }

    /**
     * Дописать что либо в конец выражения
     * @param str то что дописывается
     */
    private void AddStringToExpression(String str){
        expressionTextField.setText(expressionTextField.getText() + str);
    }

    /**
     * Подписка объектов формы на события
     */
    private void InitFormObjects() {

        calculateButton.addActionListener(e -> Calculate());

        CButton.addActionListener(e -> {
            answerLabel.setText("=");
            expressionTextField.setText("");
        });
        backspaceButton.addActionListener(e -> {
            if(expressionTextField.getText().equals("")){
                expressionTextField.setText("");
                return;
            }
            expressionTextField.setText(expressionTextField.getText().substring(0, expressionTextField.getText().length() - 1));
        });
        addActionsToWriterButtons();

        expressionTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!IsCorrectChar(c)) {
                    e.consume();  // ignore event
                }
            }
        });
    }

    //endregion

    /**
     * Запуск приложения
     */
    public static void main(String[] args) {
        new CalculatorMainForm();
    }

    /**
     * Вычислить значение выражения
     */
    private void Calculate(){
        try{
            answerLabel.setText( "= " + CalculateExpression(expressionTextField.getText()));
        }
        catch (Exception e){
            if(e.getMessage().length()<1) {
                JOptionPane.showMessageDialog(mainPanel, "Неизвестная ошибка");
                answerLabel.setText("Ошибка");
                return;
            }
            JOptionPane.showMessageDialog(mainPanel, e.getMessage());
            answerLabel.setText("Ошибка ввода");
        }
    }
}


