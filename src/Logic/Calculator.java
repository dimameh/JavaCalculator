package Logic;

import java.util.Stack;

public class Calculator {

    public static double CalculateExpression(String expression) {
        //Стек для хранения чисел
        Stack<Double> numbers = new Stack<>();
        for (String character : expression.split(" ")) {
            //Числа всегда добавляем в стек
            if (IsDigit(character)) {
                numbers.push(Double.parseDouble(character));
                continue;
            }
            double a;
            double b;
            //Если попадается оператор, выполняем его с двумя последними числами в стеке
            switch (character) {
                case "-":
                    a = numbers.pop();
                    b = numbers.pop();
                    numbers.push(b - a);
                    break;
                case "+":
                    numbers.push(numbers.pop() + numbers.pop());
                    break;
                case "/":
                    a = numbers.pop();
                    b = numbers.pop();
                    numbers.push(b / a);
                    break;
                case "*":
                    numbers.push(numbers.pop() * numbers.pop());
                    break;
                default:
                    break;
            }
        }

        //Оставшееся число в стеке является ответом
        return numbers.pop();
    }

    /**
     * Проверка является ли строка числом
     */
    private static boolean IsDigit(String str) {
        try {
            Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    /**
     * Выполняет конвертацию строки с выражением
     * из инфиксной нотации в постфиксную
     */
    public static String Convert(String expression) {
        //builder добавление происходит быстрее чем string
        StringBuilder resultString = new StringBuilder();

        // В стек скидываются операции
        Stack<Character> operations = new Stack<>();

        // Переводим строку в массив символов
        char[] chars = expression.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            char ch = chars[i];
            if (chars[i] == '-' && ((i > 0 && !Character.isDigit(chars[i - 1])) || i == 0)) {

                resultString.append("-"); //в переменную для чисел добавляется знак "-"
            } else if (Character.isDigit(ch)) {
                // считываем поциферно число
                while (Character.isDigit(chars[i])) {
                    resultString.append(chars[i++]);

                    if (i == chars.length) {
                        break;
                    }
                }
                // Отделяем все элементы пробелами
                i--;
                resultString.append(' ');
            } else if (ch == '(') {
                // Левую скобку закидываем в стек
                operations.push(ch);
            } else if (ch == ')') {
                // При встрече правой скобки, делаем pop стэка операций
                // и прибавляем к результирующей строке все что напушило в стэк
                // пока не встретим в нем левую скобку
                while (operations.peek() != '(') {
                    resultString.append(operations.pop()).append(' ');
                }
                //Выкидываем более ненужную левую скобку из стека
                operations.pop();
            } else if (IsOperator(ch)) {
                // Попаем из стека все операторы большего приоритета после чего
                // заносим рассматриваемый оператор в стек
                while (!operations.isEmpty() && Priority(operations.peek()) >= Priority(ch)) {
                    resultString.append(operations.pop()).append(' ');
                }
                operations.push(ch);
            }
        }

        // Когда символы закончились, выпушиваем из стека все оставшиеся операторы в конец строки
        while (!operations.isEmpty()) {
            resultString.append(operations.pop()).append(' ');
        }

        return resultString.toString();
    }

    /**
     * Является ли символ одним из доступных операторов
     */
    private static boolean IsOperator(char ch) {
        return ch == '*' || ch == '/' || ch == '+' || ch == '-';
    }

    /**
     * Возвращает приоритет переданного оператора
     */
    private static int Priority(char operator) {
        switch (operator) {
            case '*':
            case '/':
                return 2;
            case '+':
            case '-':
                return 1;
        }
        return 0;
    }
}
