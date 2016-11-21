//package Util;
//
//import java.util.Collections;
//import java.util.Scanner;
//import java.util.Stack;
//
///**
// *  �������ʽ��ֵ
// *  ֱ�ӵ���Calculator���෽��conversion()
// *  �����������ʽ��������һ������ֵ���
// *  ���������̴��󣬽�����һ��NaN
// */
//public class Calculator {
//    private Stack<String> postfixStack = new Stack<String>();// ��׺ʽջ
//    private Stack<Character> opStack = new Stack<Character>();// �����ջ
//    private int[] operatPriority = new int[] { 0, 3, 2, 1, -1, 1, 0, 2 };// ���������ASCII��-40����������������ȼ�
//
//    public static double conversion(String expression) {
//        double result = 0;
//        Calculator cal = new Calculator();
//        try {
//            expression = transform(expression);
//            result = cal.calculate(expression);
//        } catch (Exception e) {
//            // e.printStackTrace();
//            // ������󷵻�NaN
//            return 0.0 / 0.0;
//        }
//        // return new String().valueOf(result);
//        return result;
//    }
//
//    /**
//     * �����ʽ�и����ķ��Ÿ���
//     *
//     * @param expression
//     *            ����-2+-1*(-3E-2)-(-1) ��תΪ ~2+~1*(~3E~2)-(~1)
//     * @return
//     */
//    private static String transform(String expression) {
//        char[] arr = expression.toCharArray();
//        for (int i = 0; i < arr.length; i++) {
//            if (arr[i] == '-') {
//                if (i == 0) {
//                    arr[i] = '~';
//                } else {
//                    char c = arr[i - 1];
//                    if (c == '+' || c == '-' || c == '*' || c == '/' || c == '(' || c == 'E' || c == 'e') {
//                        arr[i] = '~';
//                    }
//                }
//            }
//        }
//        if(arr[0]=='~'||arr[1]=='('){
//            arr[0]='-';
//            return "0"+new String(arr);
//        }else{
//            return new String(arr);
//        }
//    }
//
//    /**
//     * ���ո����ı��ʽ����
//     *
//     * @param expression
//     *            Ҫ����ı��ʽ����:5+12*(3+5)/7
//     * @return
//     */
//    public double calculate(String expression) {
//        Stack<String> resultStack = new Stack<String>();
//        prepare(expression);
//        Collections.reverse(postfixStack);// ����׺ʽջ��ת
//        String firstValue, secondValue, currentValue;// �������ĵ�һ��ֵ���ڶ���ֵ�����������
//        while (!postfixStack.isEmpty()) {
//            currentValue = postfixStack.pop();
//            if (!isOperator(currentValue.charAt(0))) {// ����������������������ջ��
//                currentValue = currentValue.replace("~", "-");
//                resultStack.push(currentValue);
//            } else {// ������������Ӳ�����ջ��ȡ����ֵ�͸���ֵһ���������
//                secondValue = resultStack.pop();
//                firstValue = resultStack.pop();
//
//                // ��������Ƿ���Ϊ����
//                firstValue = firstValue.replace("~", "-");
//                secondValue = secondValue.replace("~", "-");
//
//                String tempResult = calculate(firstValue, secondValue, currentValue.charAt(0));
//                resultStack.push(tempResult);
//            }
//        }
//        return Double.valueOf(resultStack.pop());
//    }
//
//    /**
//     * ����׼���׶ν����ʽת����Ϊ��׺ʽջ
//     *
//     * @param expression
//     */
//    private void prepare(String expression) {
//        opStack.push(',');// ���������ջ��Ԫ�ض��ţ��˷������ȼ����
//        char[] arr = expression.toCharArray();
//        int currentIndex = 0;// ��ǰ�ַ���λ��
//        int count = 0;// �ϴ����������������������������ַ��ĳ��ȱ��ڻ���֮�����ֵ
//        char currentOp, peekOp;// ��ǰ��������ջ��������
//        for (int i = 0; i < arr.length; i++) {
//            currentOp = arr[i];
//            if (isOperator(currentOp)) {// �����ǰ�ַ��������
//                if (count > 0) {
//                    postfixStack.push(new String(arr, currentIndex, count));// ȡ���������֮�������
//                }
//                peekOp = opStack.peek();
//                if (currentOp == ')') {// �����������������ջ�е�Ԫ���Ƴ�����׺ʽջ��ֱ������������
//                    while (opStack.peek() != '(') {
//                        postfixStack.push(String.valueOf(opStack.pop()));
//                    }
//                    opStack.pop();
//                } else {
//                    while (currentOp != '(' && peekOp != ',' && compare(currentOp, peekOp)) {
//                        postfixStack.push(String.valueOf(opStack.pop()));
//                        peekOp = opStack.peek();
//                    }
//                    opStack.push(currentOp);
//                }
//                count = 0;
//                currentIndex = i + 1;
//            } else {
//                count++;
//            }
//        }
//        if (count > 1 || (count == 1 && !isOperator(arr[currentIndex]))) {// ���һ���ַ��������Ż��������������������׺ʽջ��
//            postfixStack.push(new String(arr, currentIndex, count));
//        }
//
//        while (opStack.peek() != ',') {
//            postfixStack.push(String.valueOf(opStack.pop()));// ��������ջ�е�ʣ���Ԫ����ӵ���׺ʽջ��
//        }
//    }
//
//    /**
//     * �ж��Ƿ�Ϊ��������
//     *
//     * @param c
//     * @return
//     */
//    private boolean isOperator(char c) {
//        return c == '+' || c == '-' || c == '*' || c == '/' || c == '(' || c == ')';
//    }
//
//    /**
//     * ����ASCII��-40���±�ȥ�����������ȼ�
//     *
//     * @param cur
//     * @param peek
//     * @return
//     */
//    public boolean compare(char cur, char peek) {// �����peek���ȼ�����cur������true��Ĭ�϶���peek���ȼ�Ҫ��
//        boolean result = false;
//        if (operatPriority[(peek) - 40] >= operatPriority[(cur) - 40]) {
//            result = true;
//        }
//        return result;
//    }
//
//    /**
//     * ���ո��������������������
//     *
//     * @param firstValue
//     * @param secondValue
//     * @param currentOp
//     * @return
//     */
//    private String calculate(String firstValue, String secondValue, char currentOp) {
//        String result = "";
//        switch (currentOp) {
//            case '+':
//                result = String.valueOf(ArithHelper.add(firstValue, secondValue));
//                break;
//            case '-':
//                result = String.valueOf(ArithHelper.sub(firstValue, secondValue));
//                break;
//            case '*':
//                result = String.valueOf(ArithHelper.mul(firstValue, secondValue));
//                break;
//            case '/':
//                result = String.valueOf(ArithHelper.div(firstValue, secondValue));
//                break;
//        }
//        return result;
//    }
//
//    public static void main(String[] args) {
//        String exp;
//        Scanner scanner=new Scanner(System.in);
//        while (!(exp=scanner.nextLine()).equals("end")){
//
//            double result = Calculator.conversion(exp);
//            System.out.println(exp + " = " + result);
//        }
//
//        System.out.println();
//    }
//}
// class ArithHelper {
//
//    // Ĭ�ϳ������㾫��
//    private static final int DEF_DIV_SCALE = 16;
//
//    // ����಻��ʵ����
//    private ArithHelper() {
//    }
//
//    /**
//     * �ṩ��ȷ�ļӷ����㡣
//     *
//     * @param v1 ������
//     * @param v2 ����
//     * @return ���������ĺ�
//     */
//
//    public static double add(double v1, double v2) {
//        java.math.BigDecimal b1 = new java.math.BigDecimal(Double.toString(v1));
//        java.math.BigDecimal b2 = new java.math.BigDecimal(Double.toString(v2));
//        return b1.add(b2).doubleValue();
//    }
//
//    public static double add(String v1, String v2) {
//        java.math.BigDecimal b1 = new java.math.BigDecimal(v1);
//        java.math.BigDecimal b2 = new java.math.BigDecimal(v2);
//        return b1.add(b2).doubleValue();
//    }
//
//    /**
//     * �ṩ��ȷ�ļ������㡣
//     *
//     * @param v1 ������
//     * @param v2 ����
//     * @return ���������Ĳ�
//     */
//
//    public static double sub(double v1, double v2) {
//        java.math.BigDecimal b1 = new java.math.BigDecimal(Double.toString(v1));
//        java.math.BigDecimal b2 = new java.math.BigDecimal(Double.toString(v2));
//        return b1.subtract(b2).doubleValue();
//    }
//
//    public static double sub(String v1, String v2) {
//        java.math.BigDecimal b1 = new java.math.BigDecimal(v1);
//        java.math.BigDecimal b2 = new java.math.BigDecimal(v2);
//        return b1.subtract(b2).doubleValue();
//    }
//
//    /**
//     * �ṩ��ȷ�ĳ˷����㡣
//     *
//     * @param v1
//     *            ������
//     * @param v2
//     *            ����
//     * @return ���������Ļ�
//     */
//
//    public static double mul(double v1, double v2) {
//        java.math.BigDecimal b1 = new java.math.BigDecimal(Double.toString(v1));
//        java.math.BigDecimal b2 = new java.math.BigDecimal(Double.toString(v2));
//        return b1.multiply(b2).doubleValue();
//    }
//
//    public static double mul(String v1, String v2) {
//        java.math.BigDecimal b1 = new java.math.BigDecimal(v1);
//        java.math.BigDecimal b2 = new java.math.BigDecimal(v2);
//        return b1.multiply(b2).doubleValue();
//    }
//
//    /**
//     * �ṩ����ԣ���ȷ�ĳ������㣬�����������������ʱ����ȷ�� С�����Ժ�10λ���Ժ�������������롣
//     *
//     * @param v1
//     *            ������
//     * @param v2
//     *            ����
//     * @return ������������
//     */
//
//    public static double div(double v1, double v2) {
//        return div(v1, v2, DEF_DIV_SCALE);
//    }
//
//    public static double div(String v1, String v2) {
//        java.math.BigDecimal b1 = new java.math.BigDecimal(v1);
//        java.math.BigDecimal b2 = new java.math.BigDecimal(v2);
//        return b1.divide(b2, DEF_DIV_SCALE, java.math.BigDecimal.ROUND_HALF_UP).doubleValue();
//    }
//
//    /**
//     * �ṩ����ԣ���ȷ�ĳ������㡣�����������������ʱ����scale����ָ �����ȣ��Ժ�������������롣
//     *
//     * @param v1 ������
//     * @param v2 ����
//     * @param scale ��ʾ��ʾ��Ҫ��ȷ��С�����Ժ�λ��
//     * @return ������������
//     */
//
//    public static double div(double v1, double v2, int scale) {
//        if (scale < 0) {
//            throw new IllegalArgumentException("The   scale   must   be   a   positive   integer   or   zero");
//        }
//        java.math.BigDecimal b1 = new java.math.BigDecimal(Double.toString(v1));
//        java.math.BigDecimal b2 = new java.math.BigDecimal(Double.toString(v2));
//        return b1.divide(b2, scale, java.math.BigDecimal.ROUND_HALF_UP).doubleValue();
//    }
//
//    /**
//     * �ṩ��ȷ��С��λ�������봦��
//     *
//     * @param v ��Ҫ�������������
//     * @param scale С���������λ
//     * @return ���������Ľ��
//     */
//
//    public static double round(double v, int scale) {
//        if (scale < 0) {
//            throw new IllegalArgumentException("The   scale   must   be   a   positive   integer   or   zero");
//        }
//        java.math.BigDecimal b = new java.math.BigDecimal(Double.toString(v));
//        java.math.BigDecimal one = new java.math.BigDecimal("1");
//        return b.divide(one, scale, java.math.BigDecimal.ROUND_HALF_UP).doubleValue();
//    }
//
//    public static double round(String v, int scale) {
//        if (scale < 0) {
//            throw new IllegalArgumentException("The   scale   must   be   a   positive   integer   or   zero");
//        }
//        java.math.BigDecimal b = new java.math.BigDecimal(v);
//        java.math.BigDecimal one = new java.math.BigDecimal("1");
//        return b.divide(one, scale, java.math.BigDecimal.ROUND_HALF_UP).doubleValue();
//    }
//}
