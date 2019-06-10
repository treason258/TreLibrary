package com.haoyang.reader.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TextShowView extends TextView {

	private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

	private List<List<Word>> linesList = new ArrayList<List<Word>>();
	private boolean singeLine = false;

	private int windowWidth, windowHeight;

	public TextShowView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public TextShowView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TextShowView(Context context) {
		super(context);
	}

	/**
	 * 算出每个文字所在的位置和总的高度并设置。
	 */
	public void make() {

		this.linesList.clear();

		int oriWidth = this.getWidth();
		int lineSpace = (int) this.getLineSpacingExtra();

		int bottomSpace = this.getPaddingBottom();
		int topSpace = this.getPaddingTop();
		int leftSpace = this.getPaddingLeft();
		int rightSpace = this.getPaddingRight();

		int textMaxWidth = oriWidth - leftSpace - rightSpace;

		int textSize = (int) this.getTextSize();
		paint.setTextSize(textSize);

		int textColor = this.getCurrentTextColor();
		paint.setColor(textColor);

		int textWidth = (int) this.paint.measureText(this.getText().toString());
		if (textWidth <= textMaxWidth) { // 只有一行的情况.

			singeLine = true;
			int rowHeight = topSpace + textSize + bottomSpace;

			ViewGroup.LayoutParams params = new RelativeLayout.LayoutParams(
					oriWidth, rowHeight);

			this.setLayoutParams(params);

			this.windowWidth = oriWidth;
			this.windowHeight = rowHeight;
			return;
		}
		singeLine = false;
		paint.setTextAlign(Paint.Align.LEFT);

		parser(lineSpace, textSize, textMaxWidth, topSpace, paint);
		int size = linesList.size();
		int height = topSpace + (size) * (textSize + lineSpace) + bottomSpace;

		ViewGroup.LayoutParams params = new RelativeLayout.LayoutParams(
				oriWidth, height);
		this.setLayoutParams(params);

		this.windowWidth = oriWidth;
		this.windowHeight = height;
	}

	public void onDraw(Canvas canvas) {

		if (this.getText() == null || this.getText().toString().length() == 0) {
			return;
		}

		int bottomSpace = this.getPaddingBottom();
		int topSpace = this.getPaddingTop();
		int leftSpace = this.getPaddingLeft();
		// int rightSpace = this.getPaddingRight();

		int oriWidth = this.getWidth();
		int textSize = (int) this.getTextSize();

		if (singeLine) {
			
			FontMetricsInt fontMetrics = paint.getFontMetricsInt();
			int rowHeight = topSpace + textSize + bottomSpace;

			int baseline = (rowHeight + 0 - fontMetrics.bottom - fontMetrics.top) / 2; // 计算文字基线.

			paint.setTextAlign(Paint.Align.CENTER);
			canvas.drawText(this.getText().toString(), oriWidth / 2, baseline,
					paint);
		}

		paint.setTextAlign(Paint.Align.LEFT);
		int size = this.linesList.size();
		for (int i = 0; i < size; i++) {
			List<Word> wordList = linesList.get(i);
			int wordSize = wordList.size();
			for (int j = 0; j < wordSize; j++) {

				Word word = wordList.get(j);
				canvas.drawText(word.word, leftSpace + word.x, word.y, paint);
			}
		} // end for i
	}

	private void parser(int textSize, int lineSpace, int width, int topSpace,
			Paint paint) {

		FontMetricsInt fontMetrics = paint.getFontMetricsInt();

		int height = textSize + lineSpace;

		int baseline = (height + 0 - fontMetrics.bottom - fontMetrics.top) / 2; // 计算文字基线.

		List<Word> list = new ArrayList<Word>();

		int x = 0, y = baseline + topSpace;

		final String text = this.getText().toString();
		int size = text.length();
		for (int i = 0; i < size; i++) {

			char c = text.charAt(i);
			String abc = String.valueOf(c);
			int w = (int) paint.measureText(abc);

			// Log.d("aaa", abc + " x = " + x + " aaa w = " + w);
			int ww = x + w;
			if (ww > width) {

				handlerWordSpace(width - x, list);
				linesList.add(list);

				list = new ArrayList<Word>();
				x = 0;
				y = y + height;
			}

			Word word = new Word();
			word.x = x;
			word.y = y;
			word.word = abc;
			list.add(word);
			// if (i % 2 == 0) {
			// paint.setColor(Color.RED);
			// } else {
			// paint.setColor(Color.BLUE);
			// }
			//
			// canvas.drawLine(word.x, word.y, x + w, y, paint);
			x = x + w;
			if (i == size - 1) {
				linesList.add(list);
			}
		}
	}

	/**
	 * 计算每个文字的位置。
	 * 
	 * @param space
	 * @param list
	 */
	private void handlerWordSpace(int remainderSpace, List<Word> list) {

		int size = list.size();

		if (size == 1) {

			return;
		}

		int s = remainderSpace / (size - 1);
		int yun = remainderSpace % (size - 1);

		int bak = yun;

		for (int i = 1; i < size; i++) {

			Word word = list.get(i);

			if (yun > 0) {
				word.x += s * i + 1;
			} else {
				word.x += s * i + bak;
			}

			if (yun >= 0) {
				word.x += 1;
				yun--;
			}
		} // end for i
	}

	public int getWindowWidth() {
		return windowWidth;
	}

	public int getWindowHeight() {
		return windowHeight;
	}
}

class Word {

	public int x;
	public int y;
	public String word;
}
