package com.topdesk.cases.tictactoe.yoursolution;

import com.topdesk.cases.tictactoe.CellState;

/**
 * Secondary comparison for suggestions with same type
 */
public interface IPriorityCalculator {

	/**
	 * @return -1 when type is wrong
	 */
	int calculatePriority(CellState player, CellStateCounter[] lineInfoList, int dimension);
}
