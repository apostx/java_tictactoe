package com.topdesk.cases.tictactoe.yoursolution;

import com.topdesk.cases.tictactoe.CellState;

public enum SuggestionType {

	WIN(2, (CellState player, CellStateCounter[] lineInfoList, int dimension) -> {
		for (CellStateCounter lineInfo : lineInfoList)
			if (lineInfo.getMatchNumber(player) == dimension - 1)
				return 0;

		return -1;
	}),
	EXPAND(1, (CellState player, CellStateCounter[] lineInfoList, int dimension) -> {
		final int DIRECTION_NUM = 4;

		int expandableLineCounter = 0;
		int emptyLineCounter = 0;
		int playerCellNum;
		int emptyCellNum;

		for (CellStateCounter lineInfo : lineInfoList) {
			playerCellNum = lineInfo.getMatchNumber(player);
			emptyCellNum = lineInfo.getMatchNumber(CellState.EMPTY);

			if (emptyCellNum == dimension)
				++emptyLineCounter;

			else if (playerCellNum + emptyCellNum == dimension)
				++expandableLineCounter;
		}

		return (DIRECTION_NUM + 1) * expandableLineCounter + emptyLineCounter - 1;
	}),
	FREE(0, (CellState player, CellStateCounter[] lineInfoList, int dimension) -> {
		return 0;
	});

	public final int priorityValue;
	public final IPriorityCalculator secondaryPriorityCalculator;

	private SuggestionType(int priorityValue, IPriorityCalculator secondaryPriorityCalculator) {
		this.priorityValue = priorityValue;
		this.secondaryPriorityCalculator = secondaryPriorityCalculator;
	}
}
