import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class Calculator {

    private ExpressionNode rootNode;
    private ExpressionNode currentNode;

    private Integer scale;
    private RoundingMode roundingMode;

    public Calculator(double num, Integer scale, RoundingMode roundingMode){
        if (scale == null) {
            throw new RuntimeException("系统异常：scale 参数异常");
        }
        if (roundingMode == null) {
            throw new RuntimeException("系统异常：roundingMode 参数异常");
        }
        this.scale = scale;
        this.roundingMode = roundingMode;
        rootNode = new ExpressionNode("", String.valueOf(num));
        currentNode = rootNode;
    }

    public double add(double num){
        append("+", num);
        return calc();
    }

    public double subtract(double num){
        append("-", num);
        return calc();
    }

    public double multi(double num){
        append("*", num);
        return calc();
    }

    public double divide(double num){
        if (num == 0) {
            throw new RuntimeException("系统异常：被除数不能为0");
        }
        append("/", num);
        return calc();
    }

    public double undo() {
        if (currentNode.getPreNode() == null) {
            System.out.println("warn: 已经回退到顶了");
            return calc();
        }

        currentNode = currentNode.getPreNode();
        return calc();
    }

    public double redo() {
        if (currentNode.getNextNode() == null) {
            System.out.println("warn: 已经到最后了");
            return calc();
        }
        currentNode = currentNode.getNextNode();
        return calc();
    }

    private void append(String operator, double num){
        ExpressionNode newNode = new ExpressionNode(operator, String.valueOf(num));
        ExpressionNode nextNode = currentNode.getNextNode();

        currentNode.setNextNode(newNode);
        newNode.setPreNode(currentNode);

        if (nextNode != null) {
            newNode.setNextNode(nextNode);
            nextNode.setPreNode(newNode);
        }
        currentNode = newNode;
    }

    private double calc() {
        BigDecimal total = new BigDecimal(rootNode.getNum());

        if (rootNode.equals(currentNode)) {
            return Double.valueOf(rootNode.getNum());
        }
        ExpressionNode t = rootNode.getNextNode();
        for (;;) {
            total = doCalc(total, t.getOperator(), new BigDecimal(t.getNum()));
            if (t.equals(currentNode) || t.getNextNode() == null) {
                break;
            }
            t = t.getNextNode();
        }
        return total.setScale(scale, roundingMode).doubleValue();

    }

    private BigDecimal doCalc(BigDecimal a, String operator, BigDecimal b) {
        BigDecimal t;
        switch (operator) {
            case "+":
                t = a.add(b);
                break;
            case "-":
                t = a.subtract(b);
                break;
            case "*":
                t = a.multiply(b);
                break;
            case "/":
                t = a.divide(b, scale, roundingMode);
                break;
            default:
                throw new RuntimeException("系统异常：不支持操作符");
        }
        return t;
    }

    public String getExpression() {
        StringBuffer sb = new StringBuffer(rootNode.getNum());
        ExpressionNode t = rootNode.getNextNode();
        for (;;) {
            if ("*".equals(t.getOperator())) {
                sb.insert(0, "(").append(")").append(t.getOperator()).append(t.getNum());
            } else if("/".equals(t.getOperator())) {
                sb.insert(0, "(").append(")").append(t.getOperator()).append(t.getNum());
            } else {
                sb.append(t.getOperator()).append(t.getNum());
            }
            if (t.equals(currentNode) || t.getNextNode() == null) {
                break;
            }
            t = t.getNextNode();
        }
        return sb.toString();
    }

}

class ExpressionNode {
    private ExpressionNode preNode;
    private ExpressionNode nextNode;
    private String operator;
    private String num;

    public ExpressionNode(String operator, String num){
        this.operator = operator;
        this.num = num;
    }

    public ExpressionNode getPreNode() {
        return preNode;
    }

    public void setPreNode(ExpressionNode preNode) {
        this.preNode = preNode;
    }

    public ExpressionNode getNextNode() {
        return nextNode;
    }

    public void setNextNode(ExpressionNode nextNode) {
        this.nextNode = nextNode;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
}
