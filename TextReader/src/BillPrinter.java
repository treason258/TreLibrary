import bean.BillBean;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BillPrinter {

    private static String TAG = Test.class.getSimpleName();

    public static void main(String[] args) {
        System.out.println("--------------------------------");
        System.out.println(TAG);
        System.out.println("读取：");
        List<String> textList = getText();
        List<BillBean> billBeanList = new ArrayList<>();
        BillBean billBean;
        for (String textLine : textList) {
            String[] textLineArray = textLine.split("\\s+");
            billBean = new BillBean();
            billBean.setDate(textLineArray[0]);
            billBean.setTime(textLineArray[1]);
            String money = textLineArray[2];
            if (money.contains("=")) {
                money = money.split("=")[0];
            }
            billBean.setMoney(money);
            billBeanList.add(billBean);
        }
        System.out.println("打印：");
        double moneySum = -8845.09;
        for (BillBean bean : billBeanList) {
            System.out.println(bean.toString());
            moneySum += Double.valueOf(bean.getMoney());
        }

        System.out.println("统计：");
        System.out.println("moneySum -> " + moneySum);
        System.out.println("--------------------------------");
    }

    private static List<String> getText() {
        List<String> textList = new ArrayList<>();
        try {
            File file = new File("txt/bill.txt");
            if (file.isFile() && file.exists()) { // 判断文件是否存在
                InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String textLine;
                while ((textLine = bufferedReader.readLine()) != null) {
                    System.out.println(textLine);
                    if (textLine.startsWith("-")) {
                        continue;
                    }
                    textList.add(textLine);
                }
                inputStreamReader.close();
            } else {
                System.out.println("找不到指定的文件");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return textList;
    }
}
