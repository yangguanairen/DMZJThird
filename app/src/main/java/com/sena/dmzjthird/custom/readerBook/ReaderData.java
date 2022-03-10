package com.sena.dmzjthird.custom.readerBook;

import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * FileName: ReaderData
 * Author: JiaoCan
 * Date: 2022/3/8 17:25
 */

public class ReaderData {
    public static final float BASE_OFFSET = 24f;  // 基础偏移量

    public List<String> pageContents;  // 每一页的文本内容
    public float mTextSize;   // 字体大小
    public float mLineSpace;  // 行间距
    public float mFinalLineSpace;

    // 为了文本居中显示
    public float xOffset; // x轴偏移量
    public float yOffset; // y轴偏移量

    public int totalPageNum;

    public ReaderData(String content, float viewWidth, float viewHeight, float textSize, float lineSpace) {

        mTextSize = textSize;
        mLineSpace = lineSpace;

        String fullWidthText = toSBC(content);

        computeContent(fullWidthText, viewWidth, viewHeight);
    }

    private void computeContent(String content, float viewWidth, float viewHeight) {
        // 一行可容纳的最大字符数
        int maxNum = (int) Math.floor((viewWidth - (BASE_OFFSET * 2)) / mTextSize);
        // x轴偏移量
        xOffset = (viewWidth - maxNum * mTextSize) / 2;
        StringBuilder sb = new StringBuilder();
        for (String item: content.split("\n")) {
            for (int i = 0; i < item.length(); i++) {
                if ((i + 1) % maxNum == 0 && i != item.length() - 1) {
                    sb.append(item.charAt(i)).append("\n");
                } else {
                    sb.append(item.charAt(i));
                }
            }
            sb.append("\n");
        }

        Paint paint = new Paint();
        paint.setTextSize(mTextSize);
        FontMetrics fontMetrics = paint.getFontMetrics();
        mFinalLineSpace = fontMetrics.bottom - fontMetrics.top + mLineSpace;
        // 一页的最大行数
        int maxLine = (int) Math.floor((viewHeight - (BASE_OFFSET * 4)) / mFinalLineSpace);
        // y轴偏移量
        yOffset = (viewHeight - maxLine * mFinalLineSpace) / 2;
        // 总行数
        String[] lines = sb.toString().split("\n");
        // 总页数
        totalPageNum = (int) Math.ceil((float) lines.length / maxLine);
        // 每页的文本内容
        pageContents = new ArrayList<>();
        for (int i = 0; i < lines.length; i+=maxLine) {
            StringBuilder singleStr = new StringBuilder();
            for (int j = i; j < Math.min(i + maxLine, lines.length); j++) {
                singleStr.append(lines[j]).append("\n");
            }
            pageContents.add(singleStr.toString());
        }
    }

    // 半角转全角，保证所有文字相同宽度
    private String toSBC(String input) {

        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 32) {
                c[i] = (char) 12288;
            } else if (c[i] > 32 && c[i] < 127) {
                c[i] = (char) (c[i] + 65248);
            }
        }
        return new String(c);

    }
}
