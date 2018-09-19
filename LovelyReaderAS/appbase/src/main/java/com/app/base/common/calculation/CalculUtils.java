/**
 * 
 */
package com.app.base.common.calculation;

/**
 * 
 * 
 * @author tianyu912@yeah.net
 */
public final class CalculUtils {

	/**
	 * 计算行数。
	 * 
	 * @param tatalCount
	 *            总个数。
	 * @param unitCount
	 *            单位个数。
	 * @return
	 */
	public static RowCountResult calculationRows(int tatalCount, int unitCount) {
		int rows = tatalCount / unitCount;
		int mod = tatalCount % unitCount;
		if (mod != 0) {
			rows += 1;
		}
		
		RowCountResult rowCountResult = new RowCountResult();
		rowCountResult.rows = rows;
		rowCountResult.mod = mod;
		return rowCountResult;
	}
	
	/**
	 * 计算行数。当最后一行的内容小于或等于每行总个数的二分之一时,
	 * 那么整个行数就减去一行.,也就是说比如正常是4行,现在变为3行
	 * 然后把内容平均分布在这3行之中.
	 * 
	 * @param tatalCount
	 *            总个数。
	 * @param unitCount 每行总个数。
	 * @return
	 */
	public static int calculationRowsB(int tatalCount, int unitCount) {
		/*int rows = tatalCount / unitCount;
		int half = unitCount / 2;
		int mod = tatalCount % unitCount;
	
		RowColumn rowColumn = new RowColumn();
		
		if (mod <= half) {
			rowColumn.rows = rows; 
			
			totalCount / rows;
			
			
			
			return rows;
		} else {
			return rows + 1;
		}*/
		return 0;
	
	}
}
