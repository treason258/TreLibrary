package com.mjiayou.trecore.util;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.EmbossMaskFilter;
import android.graphics.LayerRasterizer;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.MaskFilterSpan;
import android.text.style.RasterizerSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SuggestionSpan;
import android.text.style.UnderlineSpan;
import android.view.View;

import com.mjiayou.trecore.R;
import com.mjiayou.trecorelib.util.ToastUtil;

import java.net.URL;

/**
 * Created by treason on 2016/12/20.
 */

public class SpannableStringUtil {

    public static Spanned getDemo(final Context context) {

//        setSpan(Object what, int start, int end, int flags)
//        start： 指定Span的开始位置
//        end： 指定Span的结束位置，并不包括这个位置。
//        flags：取值有如下四个
//            Spannable.SPAN_EXCLUSIVE_INCLUSIVE：在 Span 前面输入的字符不应用 Span 的效果，在后面输入的字符应用 Span 效果。
//            Spannable.SPAN_INCLUSIVE_EXCLUSIVE：在 Span 前面输入的字符应用 Span 的效果，在后面输入的字符不应用 Span 效果。
//            Spannable.SPAN_INCUJSIVE_INCLUSIVE：在 Span 前后输入的字符都应用 Span 的效果。
//            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE：前后都不包括。
//        what： 对应的各种Span，不同的Span对应不同的样式。已知的可用类有：
//            BackgroundColorSpan : 文本背景色
//            ForegroundColorSpan : 文本颜色
//            MaskFilterSpan : 修饰效果，如模糊(BlurMaskFilter)浮雕
//            RasterizerSpan : 光栅效果
//            StrikethroughSpan : 删除线
//            SuggestionSpan : 相当于占位符
//            UnderlineSpan : 下划线
//            AbsoluteSizeSpan : 文本字体（绝对大小）
//            DynamicDrawableSpan : 设置图片，基于文本基线或底部对齐。
//            ImageSpan : 图片
//            RelativeSizeSpan : 相对大小（文本字体）
//            ScaleXSpan : 基于x轴缩放
//            StyleSpan : 字体样式：粗体、斜体等
//            SubscriptSpan : 下标（数学公式会用到）
//            SuperscriptSpan : 上标（数学公式会用到）
//            TextAppearanceSpan : 文本外貌（包括字体、大小、样式和颜色）
//            TypefaceSpan : 文本字体
//            URLSpan : 文本超链接
//            ClickableSpan : 点击事件

//        BlurMaskFilter(10f, BlurMaskFilter.Blur.NORMAL);
//        第一个参数：指定模糊边缘的半径；
//        第二个参数：指定模糊的风格，可选值有：
//        BlurMaskFilter.Blur.NORMAL：内外模糊
//        BlurMaskFilter.Blur.OUTER：外部模糊
//        BlurMaskFilter.Blur.INNER：内部模糊
//        BlurMaskFilter.Blur.SOLID：内部加粗，外部模糊

        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        spannableStringBuilder.append("设置字体颜色"); // 0,6
        spannableStringBuilder.append("设置背景颜色"); // 6,12
        spannableStringBuilder.append("设置字体大小"); // 12,18
        spannableStringBuilder.append("设置粗体AA"); // 18,24
        spannableStringBuilder.append("设置斜体AA"); // 24,30

        spannableStringBuilder.append("设置粗体斜体"); // 30,36
        spannableStringBuilder.append("设置删除线A"); // 36,42
        spannableStringBuilder.append("设置下划线A"); // 42,48
        spannableStringBuilder.append("设置图片代替"); // 48,54 - 图片(50,52)
        spannableStringBuilder.append("设置点击事件"); // 54,60

        spannableStringBuilder.append("设置模糊效果"); // 60,66
        spannableStringBuilder.append("设置浮雕效果"); // 66,72
        spannableStringBuilder.append("设置光栅效果"); // 72,78
        spannableStringBuilder.append("设置占位符A"); // 78,84
        spannableStringBuilder.append("XXXXXX"); // 84,90

        // 设置字体颜色
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(context.getResources().getColor(R.color.tc_theme));
        spannableStringBuilder.setSpan(foregroundColorSpan, 0, 6, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        // 设置背景颜色
        BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(context.getResources().getColor(R.color.tc_theme));
        spannableStringBuilder.setSpan(backgroundColorSpan, 6, 12, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        // 设置字体大小
        AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(20);
        spannableStringBuilder.setSpan(absoluteSizeSpan, 12, 18, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        // 设置粗体AA
        StyleSpan styleSpanBold = new StyleSpan(Typeface.BOLD);//粗体
        spannableStringBuilder.setSpan(styleSpanBold, 18, 24, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        // 设置斜体AA
        StyleSpan styleSpanItalic = new StyleSpan(Typeface.ITALIC);//斜体
        spannableStringBuilder.setSpan(styleSpanItalic, 24, 30, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        // 设置粗体斜体
        StyleSpan styleSpanBoldAndItalic = new StyleSpan(Typeface.BOLD_ITALIC);//粗斜体
        spannableStringBuilder.setSpan(styleSpanBoldAndItalic, 30, 36, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        // 设置删除线A
        StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
        spannableStringBuilder.setSpan(strikethroughSpan, 36, 42, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        // 设置下划线A
        UnderlineSpan underlineSpan = new UnderlineSpan();
        spannableStringBuilder.setSpan(underlineSpan, 42, 48, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        // 设置图片代替
        Drawable drawable = context.getResources().getDrawable(R.drawable.tc_launcher);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        ImageSpan imageSpan = new ImageSpan(drawable);
        spannableStringBuilder.setSpan(imageSpan, 50, 52, Spannable.SPAN_EXCLUSIVE_INCLUSIVE); //将index为50,51的字符用图片替代
        // 设置点击事件
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                ToastUtil.show("触发点击事件");
            }
        };
        spannableStringBuilder.setSpan(clickableSpan, 54, 60, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        // 设置模糊效果
        MaskFilterSpan maskFilterSpanBlur = new MaskFilterSpan(new BlurMaskFilter(3, BlurMaskFilter.Blur.OUTER));
        spannableStringBuilder.setSpan(maskFilterSpanBlur, 60, 66, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        // 设置浮雕效果
        MaskFilterSpan maskFilterSpanEmboss = new MaskFilterSpan(new EmbossMaskFilter(new float[]{1, 1, 3}, 1.5f, 8, 3));
        spannableStringBuilder.setSpan(maskFilterSpanEmboss, 66, 72, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        // 设置光栅效果
        RasterizerSpan rasterizerSpan = new RasterizerSpan(new LayerRasterizer());
        spannableStringBuilder.setSpan(rasterizerSpan, 72, 78, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        // 设置占位符A
        SuggestionSpan suggestionSpan = new SuggestionSpan(context, new String[]{"提示1", "提示2"}, SuggestionSpan.FLAG_AUTO_CORRECTION);
        spannableStringBuilder.setSpan(suggestionSpan, 78, 84, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        // spannedConcat 张三回复李四：你好
        String usernameA = "张三";
        String usernameB = "李四";
        final String content = "你好";
        // usernameA
        SpannableString spannableStringA = new SpannableString(usernameA);
        ClickableSpan clickableSpanA = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                ToastUtil.show("usernameA -> onClick");
            }

            @Override
            public void updateDrawState(TextPaint textPaint) {
                textPaint.setColor(context.getResources().getColor(R.color.tc_theme));
                textPaint.setUnderlineText(true);
            }
        };
        spannableStringA.setSpan(clickableSpanA, 0, usernameA.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        // usernameB
        SpannableString spannableStringB = new SpannableString(usernameB);
        ClickableSpan clickableSpanB = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                ToastUtil.show("usernameB -> onClick");
            }

            @Override
            public void updateDrawState(TextPaint textPaint) {
                textPaint.setColor(context.getResources().getColor(R.color.tc_theme));
                textPaint.setUnderlineText(true);
            }
        };
        spannableStringB.setSpan(clickableSpanB, 0, usernameB.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        // setText
        Spanned spannedReply = Html.fromHtml("<font color=" + context.getResources().getColor(R.color.tc_text_black) + ">回复</font>");
        Spanned spannedColon = Html.fromHtml("<font color=" + context.getResources().getColor(R.color.tc_text_black) + ">：</font>");
        Spanned spannedContent = Html.fromHtml("<font color=" + context.getResources().getColor(R.color.tc_text_black) + ">" + content + "</font>");
        Spanned spannedConcat = (Spanned) TextUtils.concat(spannableStringA, spannedReply, spannableStringB, spannedColon, spannedContent);

        // spannedHtml
        String html = "<html>" +
                "<head>" +
                "<title>TextView使用HTML</title>" +
                "</head>" +
                "<body>" +
                "<p><strong>强调</strong></p>" +
                "<p><em>斜体</em></p>" +
                "<p><a href=\"http://www.baidu.com/\">超链接HTML入门</a>学习HTML!</p>" +
                "<p><font color=\"#aabb00\">颜色1" + "</p>" +
                "<p><font color=\"#00bbaa\">颜色2</p>" +
                "<h1>标题1</h1>" +
                "<h3>标题2</h3>" +
                "<h6>标题3</h6>" +
                "<p>大于>小于<</p>" +
                "<p>下面是网络图片</p>" +
                "<img src=\"http://avatar.csdn.net/0/3/8/2_zhang957411207.jpg\"/>" +
                "</body>" +
                "</html>";
        Spanned spannedHtml = Html.fromHtml(html, new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String source) {
                Drawable drawable = null;
                URL url;
                try {
                    url = new URL(source);
                    drawable = Drawable.createFromStream(url.openStream(), ""); // 获取网路图片
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                return drawable;
            }
        }, null);

        // autoLinkStr
        String strAutoLink = "Email: admin@mjiayou.com\n" +
                "PhoneA: +86-18600574121\n" +
                "PhoneB: 18600574121\n" +
                "Tel: 3719366\n" +
                "HTTP: https://www.baidu.com/";

        // baoxiangStr
        String strBaoxiang = String.format(context.getResources().getString(R.string.baoxiang), 2, 18, "银宝箱");

        return (Spanned) TextUtils.concat(spannableStringBuilder,
                "\n\n", spannedConcat,
//                "\n\n", spannedHtml,
                "\n\n", strAutoLink,
                "\n\n", strBaoxiang);
    }
}
