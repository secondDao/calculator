import java.math.RoundingMode;

public class Main {
    public static void main(String[] args) {
        Calculator calculator = new Calculator(1.0001, 6, RoundingMode.HALF_UP);
        double result = calculator.add(6);
        System.out.println(calculator.getExpression() + "=" + result);

        result = calculator.subtract(2);
        System.out.println(calculator.getExpression() + "=" + result);

        result = calculator.multi(3);
        System.out.println(calculator.getExpression() + "=" + result);

        result = calculator.divide(2);
        System.out.println(calculator.getExpression() + "=" + result);

        result = calculator.undo();
        System.out.println(calculator.getExpression() + "=" + result);

        result = calculator.undo();
        System.out.println(calculator.getExpression() + "=" + result);

        result = calculator.multi(9.333);
        System.out.println(calculator.getExpression() + "=" + result);

        result = calculator.divide(6.222);
        System.out.println(calculator.getExpression() + "=" + result);

        result = calculator.redo();
        System.out.println(calculator.getExpression() + "=" + result);

        result = calculator.redo();
        System.out.println(calculator.getExpression() + "=" + result);
    }
}