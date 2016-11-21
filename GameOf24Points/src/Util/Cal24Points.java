package Util;

import Game.GameConstants;

import java.util.ArrayList;
import java.util.Scanner;

public class Cal24Points implements GameConstants {


    public static String isCanWorkOut24(int num1, int num2, int num3, int num4) {
        int[] a = new int[]{num1, num2, num3, num4};

        Data[] datas = new Data[]{new Data(num1, num1 + ""), new Data(num2, num2 + ""),
                new Data(num3, num3 + ""), new Data(num4, num4 + "")};
        return calculate(datas);
    }

    private static String calculate(Data[] datas) {
        int length = datas.length;
        if (length == 1) {
            if (datas[0].elem == 24) return datas[0].exp;
            return FALSE;
        }
        for (int i = 0; i < length; i++) {
            for (int j = i + 1; j < length; j++) {
                int x = datas[i].elem;
                int y = datas[j].elem;
                Data[] newDatas = new Data[length - 1];
                //添加旧的data给新data
                if (length > 2) add(newDatas, datas, i, j);

                String result;

                //加
                newDatas[0] = new Data(x + y, datas[i].getExp() + add + datas[j].getExp());
                result = calculate(newDatas);
                if (!result.equals(FALSE)) return result;

                //乘，加括号
                newDatas[0] = new Data(x * y, left + datas[i].getExp() + right + mul + left + datas[j].getExp() + right);
                result = calculate(newDatas);
                if (!result.equals(FALSE)) return result;

                //减，减数加括号
                if (x > y) {
                    newDatas[0] = new Data(x - y, datas[i].getExp() + '-' + left + datas[j].getExp() + right);
                } else {
                    newDatas[0] = new Data(y - x, datas[j].getExp() + '-' + left + datas[i].getExp() + right);
                }
                result = calculate(newDatas);
                if (!result.equals(FALSE)) return result;

                //除，加括号
                if (y != 0 && x % y == 0) {//x/y
                    newDatas[0] = new Data(x / y, left + datas[i].getExp() + right + '/' + left + datas[j].getExp() + right);
                    result = calculate(newDatas);
                    if (!result.equals(FALSE)) return result;
                } else if (x != 0 && y % x == 0) {//y/x
                    newDatas[0] = new Data(y / x, left + datas[j].getExp() + right + '/' + left + datas[i].getExp() + right);
                    result = calculate(newDatas);
                    if (!result.equals(FALSE)) return result;
                }

            }
        }
        return FALSE;

    }

    private static void add(Data[] newDatas, Data[] datas, int i, int j) {
        int length = datas.length;
        for (int m = 0, index = 1; m < length; m++) {
            if (m == i || m == j) continue;
            newDatas[index++] = new Data(datas[m].getElem(), datas[m].getExp());
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            int a[] = new int[4];
            for (int i = 0; i < 4; i++) a[i] = scanner.nextInt();
            System.out.println(new Cal24Points().isCanWorkOut24(a[0], a[1], a[2], a[3]));
        }
    }
    private static class Data {
        int elem;
        String exp;

        private Data(int elem, String exp) {
            this.elem = elem;
            this.exp = exp;
        }

        private int getElem() {
            return elem;
        }

        private String getExp() {
            return exp;
        }

    }
}
