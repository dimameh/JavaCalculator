package UI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import static Logic.Calculator.*;

public class CalculatorMainForm extends JFrame {
    private JPanel mainPanel;
    private JButton button7;
    private JButton button8;
    private JButton button9;
    private JButton divisionButton;
    private JButton button4;
    private JButton button5;
    private JButton button6;
    private JButton multiplyButton;
    private JButton button1;
    private JButton dotButton;
    private JTextField expressionTextField;
    private JButton calculateButton;
    private JLabel answerLabel;
    private JButton CButton;
    private JButton backspaceButton;
    private JButton leftBraceButton;
    private JButton rightBraceButton;
    private JButton minusButton;
    private JButton plusButton;
    private JButton button0;
    private JButton button2;
    private JButton button3;

    //region Инициализация и запуск

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

    private void AddStringToExpression(String str){
        expressionTextField.setText(expressionTextField.getText() + str);
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


}


