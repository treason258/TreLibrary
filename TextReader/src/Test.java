import bean.PosBean;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Test {

    private static String TAG = Test.class.getSimpleName();

    public static void main(String[] args) {
        System.out.println("--------------------------------");
        System.out.println(TAG);
        System.out.println("读取：");
        List<String> textList = getText();
        List<PosBean> posBeanList = new ArrayList<>();
        PosBean posBean;
        for (String textLine : textList) {
            String[] textLineArray = textLine.split("\\s+");
            posBean = new PosBean();
            posBean.setDate(textLineArray[0]);
            posBean.setTime(textLineArray[1]);
            posBean.setCard(textLineArray[2]);
            posBean.setType(textLineArray[3]);
            posBean.setMoneyPay(textLineArray[4]);
            posBean.setMoneyEarn(textLineArray[5]);
            posBean.setMoneyTax(textLineArray[6]);
            posBean.setMoneyRate(textLineArray[7]);
            posBean.setMoneySum(textLineArray[8]);
            if (textLineArray.length >= 10) {
                posBean.setRemark(textLineArray[9]);
            }
            posBeanList.add(posBean);
        }
        System.out.println("打印：");
        double moneyPaySum = 0;
        double moneyEarnSum = 0;
        double moneyTaxSum = 0;
        for (PosBean bean : posBeanList) {
            System.out.println(bean.toString());
            moneyPaySum += Double.valueOf(bean.getMoneyPay());
            moneyEarnSum += Double.valueOf(bean.getMoneyEarn());
            moneyTaxSum += Double.valueOf(bean.getMoneyTax());
        }

        System.out.println("统计：");
        System.out.println("moneyPaySum -> " + moneyPaySum);
        System.out.println("moneyEarnSum -> " + moneyEarnSum);
        System.out.println("moneyTaxSum -> " + moneyTaxSum);
        System.out.println("--------------------------------");
    }

    private static List<String> getText() {
        List<String> textList = new ArrayList<>();
        try {
            File file = new File("txt/momomomo.txt");
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