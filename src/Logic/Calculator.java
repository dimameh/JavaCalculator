package Logic;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Stack;

/**
 * Класс производящий расчеты выражений записанных строкой типа String
 */
public class Calculator {

    /**
     * Расчет выражения в инфиксной нотации
     * @param expression Выражение в инфиксной нотации
     * @return Результат вычислений
     */
    public static BigDecimal CalculateExpression(String expression){
        return Calculate(Convert(expression));
    }

    //region приватные методы

    /**
     * Расчет выражения в обратной польской нотации с точностью в 20 значащих цифр
     * @param expression Выражение в обратной польской нотации
     * @return Результат вычислений
     */
    private static BigDecimal Calculate(String expression) {
        //Точность вычислений
        MathContext mc = new MathContext(20);

        //Стек для хранения чисел
        Stack<BigDecimal> numbers = new Stack<>();

        for (String character : expression.split(" ")) {
            //Числа всегда добавляем в стек
            if (IsDigit(character)) {
                numbers.push(new BigDecimal(character));
                continue;
            }
            BigDecimal a = numbers.pop();
            BigDecimal b = numbers.pop();
            //Если попадается оператор, выполняем его с двумя последними числами в стеке
            switch (character) {
                case "-":
                    numbers.push(b.subtract(a, mc)) ;
                    break;
                case "+":
                    numbers.push(b.add(a, mc));
                    break;
                case "/":
                    numbers.push(b.divide(a, mc));
                    break;
                case "*":
                    numbers.push(b.multiply(a, mc));
                    break;
                default:
                    break;
            }
        }

        //Последнее число, оставшееся в стеке является ответом
        return numbers.pop();
    }

    /**
     * Выполняет конвертацию строки с выражением
     * из инфиксной нотации в постфиксную (Обратную польскую)
     */
    private static String Convert(String expression) {
        //Результирующая строка
        StringBuilder resultString = new StringBuilder();

        // В стек скидываются операции
        Stack<Character> operations = new Stack<>();

        // Переводим строку в массив символов
        char[] chars = PrepareExpression(expression);

        //region Проверки на правильность ввода

        //Проверка на неправильно расстваленные скобки
        if(!IsBracketsCorrect(chars)){
            throw new IllegalArgumentException("Ошибка. Неправильно расставлены скобки.");
        }
        //Остальные проверки введенного выражения
        if(!IsExpressionCorrect(chars)){
            throw new IllegalArgumentException("Ошибка. при вводе выражения.");
        }
        //endregion

        //Преобразование выражения в постфиксную нотацию
        for (int i = 0; i < chars.length; i++) {
            char ch = chars[i];

            //Добавить знак "-" к числу если он является унарным оператором
            if (chars[i] == '-' && (i == 0 || IsOperator(chars[i-1]) || chars[i-1] == '(')) {
                resultString.append(ch);
            }
            //Если символ - цифра считать поциферно число
            else if (Character.isDigit(ch) ) {
                try{
                    // считываем поциферно число
                    while (Character.isDigit(chars[i]) || (chars[i] == '.' && Character.isDigit(chars[i+1]))) {
                        resultString.append(chars[i++]);
                        if (i == chars.length) {
                            break;
                        }
                    }
                }
                catch(Exception e){
                    throw new IllegalArgumentException("Ошибка. После точки всегда должна идти цифра.");
                }
                i--;
                // Отделяем все элементы пробелами
                resultString.append(' ');
            }
            // Левую скобку просто закидываем в стек
            else if (ch == '(') {
                operations.push(ch);
            }
            // При встрече правой скобки, делаем pop стэка операций
            // и прибавляем к результирующей строке все что напушило в стэк
            // пока не встретим в нем левую скобку
            else if (ch == ')') {
                while (operations.peek() != '(') {
                    resultString.append(operations.pop()).append(' ');
                }
                //Выкидываем более ненужную левую скобку из стека
                operations.pop();
            }
            // Достаем из стека все операторы бОльшего приоритета
            // после чего заносим рассматриваемый оператор в стек
            else if (IsOperator(ch)) {
                while (!operations.isEmpty() && Priority(operations.peek()) >= Priority(ch)) {
                    resultString.append(operations.pop()).append(' ');
                }
                operations.push(ch);
            }
        }

        // Когда символы закончились, достаем из стека все оставшиеся операторы в конец строки
        while (!operations.isEmpty()) {
            resultString.append(operations.pop()).append(' ');
        }

        return resultString.toString();
    }

    /**
     * Подготовка записи выражения к обработке
     */
    private static char[] PrepareExpression(String expression){
        expression = expression.replace(",",".");
        return expression.toCharArray();
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

    //region методы проверок

    /**
     * Проверка правильности расстановки скобок
     */
    private static boolean IsBracketsCorrect(char[] chars){
        int index = 0;
        for (char ch: chars) {
            if(ch == '('){
                index++;
            }
            if(ch == ')'){
                index--;
            }
        }
        return index == 0;
    }

    /**
     * Проверка выражения на несоответствие общпринятым законам записи
     */
    private static boolean IsExpressionCorrect(char[] chars){
        for (int i = 0; i < chars.length; i++){
            //Буквы
            if(Character.isLetter(chars[i])){
                return false;
            }
            //Пробелы
            if(Character.isWhitespace(chars[i])){
                return false;
            }
            //Проверки для символов, не являющимися последними в строке
            if(i!=chars.length-1){
                //Два оператора подряд
                if(IsOperator(chars[i]) &&  IsOperator(chars[i+1]) && chars[i+1] != '-') {
                    return false;
                }
                //Точка после оператора
                if(IsOperator(chars[i]) && chars[i+1]=='.'){
                    return false;
                }
                //Две точки или точка после одной из скобок
                if(IsBracketOrDot(chars[i]) && chars[i+1] == '.'){
                    return false;
                }
                //Одна из скобок сразу после точки
                if(chars[i] == '.' && (chars[i+1] == '(' || chars[i+1] == ')')){
                    return false;
                }
                //Оператор после точки
                if(chars[i] == '.' && IsOperator(chars[i+1])){
                    return false;
                }
            }
        }
        for (int i = 0; i < chars.length; i++){
            if(chars[i] == '.'){
                i++;
                while(IsDigit(Character.toString(chars[i]))){
                    i++;
                    if(i == chars.length-1){
                        break;
                    }
                }
                if(IsOperator(chars[i]) || chars[i] == '(' || chars[i] == ')'){
                    continue;
                }
                if(chars[i] == '.'){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Проверка является ли строка числом
     */
    private static boolean IsDigit(String str) {
        try {
            Double.parseDouble(str);
        }
        catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    /**
     * Является ли символ одним из доступных операторов
     */
    private static boolean IsOperator(char ch) {
        return ch == '*' || ch == '/' || ch == '+' || ch == '-';
    }

    /**
     * Является ли символ скобкой или плавающей точкой
     */
    private static boolean IsBracketOrDot(char ch) {
        return ch == '(' || ch == ')' || ch == '.' || ch == ',';
    }
    //endregion
    //endregion
}
