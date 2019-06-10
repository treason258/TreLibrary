/**
 * 
 */
package com.haoyang.reader.sdk;

/**
 * 行间距的枚举.
 * 
 * @author tianyu912@yeah.net
 *
 */
public enum LineSpace {

	lineSpaceNone(15),
	lineSpaceSmall(30),
	lineSpaceMiddle(40),
	lineSpaceBig(50),
	lineSpaceTooBig(60);

	private int value;

	LineSpace(int value) {

		this.value = value;
	}

	public int getValue() {

		return this.value;
	}

	public static LineSpace valueOf(int value) {

		switch (value) {
		case 15:

			return lineSpaceNone;
		case 30:

			return lineSpaceSmall;
		case 40:

			return lineSpaceMiddle;
		case 50:

			return lineSpaceBig;
		case 60:

			return lineSpaceTooBig;
		default:

			return lineSpaceMiddle;
		} // end switch
	}
}
