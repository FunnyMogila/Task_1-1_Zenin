package vsu.cs.soshich;

import java.util.ArrayList;
import java.util.List;

public class MyBigInteger {
    private List<Integer> value;
    private boolean sign;

    private MyBigInteger(List<Integer> value, boolean sign) {
        setValue(value, sign);
    }

    public MyBigInteger(String number) {
        setValue(number);
    }

    private void setValue(List<Integer> value, boolean sign) {
        this.value = value;
        this.sign = sign;
    }

    public void setValue(String number) {
        List<Integer> val = new ArrayList<>();
        boolean s = number.charAt(0) != '-';
        if (!s) {
            number = number.substring(1);
        }
        String[] arr = number.split("");
        for (int i = 0; i < arr.length; i++) {
            val.add(Integer.parseInt(arr[i]));
        }
        redactToCorrect(val);
        setValue(val, s);
    }

    public String getValue() {
        String value = "";
        for (int i = 0; i < this.value.size(); i++) {
            value += this.value.get(i);
        }
        if (!sign) {
            value = "-" + value;
        }
        return value;
    }

    private void redactToCorrect(List<Integer> list) {
        while (list.get(0) == 0 && list.size() != 1) {
            list.remove(0);
        }
    }

    private void redactToCorrect(List<Integer> list1, List<Integer> list2) {
        List<Integer> helpList;
        int lengthDifference = Math.abs(list1.size() - list2.size());
        if (list1.size() > list2.size()) {
            helpList = list2;
        } else {
            helpList = list1;
        }
        for (int i = 0; i < lengthDifference; i++) {
            helpList.add(i, 0);
        }
    }

    private boolean getMinAndMaxByModule(List<Integer> root, List<Integer> arg) {
        redactToCorrect(root, arg);
        for (int i = 0; i < root.size(); i++) {
            if (root.get(i) > arg.get(i)) {
                return true;
            }
            if (root.get(i) < arg.get(i)) {
                return false;
            }
        }
        return true;
    }

    private List<Integer> additionForSameSigns(List<Integer> root, List<Integer> addition) {
        redactToCorrect(root, addition);
        List<Integer> sum = new ArrayList<>();
        int count = 0;
        int repeats = root.size() - 1;
        for (int i = repeats; i > -1; i--) {
            int element = (count + root.get(i) + addition.get(i)) % 10;
            sum.add(0, element);
            count = (count + root.get(i) + addition.get(i)) / 10;
        }
        if (count != 0) {
            sum.add(0, count);
        }
        return sum;
    }

    private List<Integer> additionForDifferentSigns(List<Integer> max, List<Integer> min) {
        redactToCorrect(max, min);
        List<Integer> sum = new ArrayList<>();
        int count = 0;
        for (int i = max.size() - 1; i > -1; i--) {
            int element;
            int indicator = max.get(i) - min.get(i) + count;
            if (indicator < 0) {
                element = 10 - min.get(i) + max.get(i) + count;
                count = -1;
            } else {
                element = max.get(i) - min.get(i) + count;
                count = 0;
            }
            sum.add(0, element);
        }
        redactToCorrect(sum);
        return sum;
    }

    public MyBigInteger add(MyBigInteger arg) {
        List<Integer> sum;
        boolean sign = this.sign;
        if (this.sign == arg.sign) {
            sum = additionForSameSigns(this.value, arg.value);
        } else {
            List<Integer> min = arg.value;
            List<Integer> max = this.value;
            if (!getMinAndMaxByModule(max, min)) {
                max = arg.value;
                min = this.value;
                sign = arg.sign;
            }
            sum = additionForDifferentSigns(max, min);
        }
        return new MyBigInteger(sum, sign);
    }

    public MyBigInteger subtract(MyBigInteger arg) {
        arg.sign = !arg.sign;
        MyBigInteger res = this.add(arg);
        arg.sign = !arg.sign;
        return res;
    }

    private List<Integer> calcMultiply(List<Integer> list1, List<Integer> list2) {
        redactToCorrect(list1, list2);
        List<Integer> multiplier = list2;
        List<Integer> root = list1;
        if (!getMinAndMaxByModule(root, multiplier)) {
            root = list2;
            multiplier = list1;
        }
        List<Integer> product = new ArrayList<>();
        List<Integer> helpList = new ArrayList<>();
        for (int i = multiplier.size() - 1; i > -1; i--) {
            int count = 0;
            helpList.clear();
            for (int j = root.size() - 1; j > -1; j--) {
                int res = (count + multiplier.get(i) * root.get(j)) % 10;
                helpList.add(0, res);
                count = (count + multiplier.get(i) * root.get(j)) / 10;
            }
            for (int k = 0; k < multiplier.size() - 1 - i; k++) {
                helpList.add(0);
            }
            if (count != 0) {
                helpList.add(0, count);
            }
            redactToCorrect(product, helpList);
            product = additionForSameSigns(product, helpList);
        }
        redactToCorrect(product);
        return product;
    }

    public MyBigInteger multiply(MyBigInteger arg) {
        List<Integer> product = calcMultiply(this.value, arg.value);
        boolean sign = (arg.sign == this.sign) | (product.get(0) == 0);
        return new MyBigInteger(product, sign);
    }

    private List<Integer> calcIntDiv(List<Integer> root, List<Integer> divider) {
        String quotient = "";
        if (root.size() < divider.size()) {
            quotient = "0";
        } else {
            int count = 0;
            while (getMinAndMaxByModule(root, divider)) {
                root = additionForDifferentSigns(root, divider);
                count += 1;
            }
            quotient += count;
        }
        String[] arr = quotient.split("");
        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < arr.length; i++) {
            res.add(Integer.parseInt(arr[i]));
        }
        redactToCorrect(res);
        return res;
    }

    public MyBigInteger integerDivide(MyBigInteger arg) {
        List<Integer> quotient = calcIntDiv(this.value, arg.value);
        boolean sign = (arg.sign == this.sign) | (quotient.get(0) == 0);
        return new MyBigInteger(quotient, sign);
    }

    public MyBigInteger getDivisionRemainder(MyBigInteger arg) {
        List<Integer> remainder = new ArrayList<>();
        boolean sign;
        if (!getMinAndMaxByModule(this.value, arg.value)) {
            remainder.add(0);
            sign = true;
        } else {
            remainder = calcIntDiv(this.value, arg.value);
            remainder = calcMultiply(arg.value, remainder);
            remainder = additionForDifferentSigns(this.value, remainder);
            redactToCorrect(remainder);
            sign = (arg.sign == this.sign) | (remainder.get(0) == 0);
        }
        return new MyBigInteger(remainder, sign);
    }
}
