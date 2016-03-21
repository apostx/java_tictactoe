package com.topdesk.cases.tictactoe.yoursolution;

import com.topdesk.cases.tictactoe.CellLocation;
import com.topdesk.cases.tictactoe.CellState;
import com.topdesk.cases.tictactoe.Consultant;
import com.topdesk.cases.tictactoe.GameBoard;

public class YourConsultant implements Consultant {

	private static final int DIMENSION = 3;

	@Override
	public CellLocation suggest(GameBoard gameBoard) {
		AnalyzedGameBoard analyzedGameBoard = new AnalyzedGameBoard(gameBoard, DIMENSION);

		checkIllegalStates(analyzedGameBoard);

		Suggestion suggestion = analyzedGameBoard.getBestSuggestion();

		return suggestion.cellLoacation;
	}

	private static void checkIllegalStates(AnalyzedGameBoard analyzedGameBoard) {
		checkFull(analyzedGameBoard);
		checkWin(analyzedGameBoard);
	}

	private static void checkFull(AnalyzedGameBoard analyzedGameBoard) {
		if (analyzedGameBoard.getGameBoardCellStateNumber(CellState.EMPTY) == 0)
			throw new IllegalStateException();
	}

	private static void checkWin(AnalyzedGameBoard analyzedGameBoard) {
		for (CellStateCounter lineInfo : analyzedGameBoard.getLineInfoList())
			for (CellState cellState : CellState.values())
				if (cellState != CellState.EMPTY && lineInfo.getMatchNumber(cellState) == DIMENSION)
					throw new IllegalStateException();
	}
}
