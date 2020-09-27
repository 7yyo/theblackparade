package demo;

public class chineseNumber {

    private final static String one = "一";
    private final static String two = "二";
    private final static String three = "三";
    private final static String four = "四";
    private final static String five = "五";
    private final static String six = "六";
    private final static String seven = "七";
    private final static String eight = "八";
    private final static String nine = "九";
    private final static String ten = "十";
    private final static String zero = "";
    private static String[] unit = {"十", "百", "千", "万", "十", "百", "千", "亿", "十", "百", "千", "万"};

    public static String getChineseNumber(String number) {
        String chineseNumber = "";
        int unitNum = 0;
        for (int i = number.length(); i > 0; i--) {
            String numberStr = String.valueOf(number.charAt(i - 1));
            switch (String.valueOf(number.charAt(i - 1))) {
                case "1":
                    chineseNumber += one;
                    break;
                case "2":
                    chineseNumber += two;
                    break;
                case "3":
                    chineseNumber += three;
                    break;
                case "4":
                    chineseNumber += four;
                    break;
                case "5":
                    chineseNumber += five;
                    break;
                case "6":
                    chineseNumber += six;
                    break;
                case "7":
                    chineseNumber += seven;
                    break;
                case "8":
                    chineseNumber += eight;
                    break;
                case "9":
                    chineseNumber += nine;
                    break;
                case "10":
                    chineseNumber += ten;
                    break;
                case "0":
                    chineseNumber += zero;
                    break;
            }
            if (i - 0 > 1) {
                chineseNumber += unit[unitNum];
            }
            unitNum++;
        }
        return getRealMoney(chineseNumber);
    }

    public static String getRealMoney(String money) {
        String realMoney = "";
        for (int i = money.length(); i > 0; i--) {
            realMoney += money.charAt(i - 1);
        }
        return realMoney;
    }

    public static void main(String[] args) {
        System.out.println(getChineseNumber("123345"));
    }

}
