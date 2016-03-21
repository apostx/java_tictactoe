package com.topdesk.cases.tictactoe.yoursolution;

import com.topdesk.cases.tictactoe.CellLocation;
import com.topdesk.cases.tictactoe.CellState;
import com.topdesk.cases.tictactoe.GameBoard;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class AnalyzedGameBoard {

	private static final CellLocation[] CELL_LOCATION_LIST = CellLocation.values();
	private static final List<CellState> PLAYER_LIST = getPlayerList();

	private final GameBoard _gameBoard;
	private final int _dimension;
	private final CellState _player;

	private final CellStateCounter[] _horizontalLineInfoList;
	private final CellStateCounter[] _verticalLineInfoList;
	private final CellStateCounter _primaryDiagonalLineInfo;
	private final CellStateCounter _secondaryDiagonalLineInfo;

	private final CellStateCounter _gameBoardCellStateNumber;

	public AnalyzedGameBoard(GameBoard gameBoard, int dimension) {
		_gameBoard = gameBoard;
		_dimension = dimension;

		_gameBoardCellStateNumber = new CellStateCounter();

		_player = getNextPlayer();

		_horizontalLineInfoList = new CellStateCounter[dimension];
		_verticalLineInfoList = new CellStateCounter[dimension];

		// count number of different cellstates for every possible lines
		int x;
		for (int y = 0; y < dimension; ++y) {
			for (x = 0; x < dimension; ++x)
				_gameBoardCellStateNumber.addMatch(getCellState(x, y));

			_horizontalLineInfoList[y] = countCellStatesInDirection(y * dimension, Direction.RIGHT);
			_verticalLineInfoList[y] = countCellStatesInDirection(y, Direction.DOWN);
		}

		_primaryDiagonalLineInfo = countCellStatesInDirection(0, Direction.RIGHT_DOWN);
		_secondaryDiagonalLineInfo = countCellStatesInDirection((dimension - 1) * dimension, Direction.RIGHT_UP);
	}

	private CellStateCounter countCellStatesInDirection(int cellIndex, Direction direction) {
		CellStateCounter result = new CellStateCounter();
		CellState currCellState;

		for (int i = 0; i < _dimension; ++i) {
			currCellState = getCellState(cellIndex + i * (_dimension * direction.y + direction.x));
			result.addMatch(currCellState);
		}

		return result;
	}

	public CellStateCounter[] getLineInfoList() {
		int length = 2 * _dimension + 2;

		CellStateCounter[] lineInfo = new CellStateCounter[length];

		System.arraycopy(_horizontalLineInfoList, 0, lineInfo, 0, _dimension);
		System.arraycopy(_verticalLineInfoList, 0, lineInfo, _dimension, _dimension);

		lineInfo[length - 2] = _primaryDiagonalLineInfo;
		lineInfo[length - 1] = _secondaryDiagonalLineInfo;

		return lineInfo;
	}

	public int getGameBoardCellStateNumber(CellState cellState) {
		return _gameBoardCellStateNumber.getMatchNumber(cellState);
	}

	public CellState getCellState(int index) {
		return _gameBoard.getCellState(CELL_LOCATION_LIST[index]);
	}

	public CellState getCellState(int x, int y) {
		return getCellState(getCellIndex(_dimension, x, y));
	}

	private static int getCellIndex(int dimension, int x, int y) {
		return y * dimension + x;
	}

	private CellLocation getCellLocation(int x, int y) {
		return CELL_LOCATION_LIST[getCellIndex(_dimension, x, y)];
	}

	public Suggestion getBestSuggestion() {
		CellLocation cellLocation;
		CellStateCounter[] lineInfoList;

		Suggestion bestSuggestion = new Suggestion();
		Suggestion currSuggestion;

		int x, y;
		for (y = 0; y < _dimension; ++y)
			for (x = 0; x < _dimension; ++x)
				if (getCellState(x, y) == CellState.EMPTY)
					for (CellState currPlayer : PLAYER_LIST) {
						cellLocation = getCellLocation(x, y);
						lineInfoList = getLineInfoList(x, y);

						currSuggestion = bestSuggestionForCellAndPlayer(currPlayer, cellLocation, lineInfoList, _dimension);

						if (currSuggestion.betterThan(bestSuggestion, _player))
							bestSuggestion = currSuggestion;
					}

		return bestSuggestion;
	}

	private Suggestion bestSuggestionForCellAndPlayer(CellState player, CellLocation cellLocation, CellStateCounter[] lineInfoList, int dimension) {
		SuggestionType[] suggestionTypeList = SuggestionType.values();

		Suggestion bestSuggestion = new Suggestion(player, cellLocation);
		Suggestion currSuggestion = new Suggestion(bestSuggestion);

		for (SuggestionType suggestionType : suggestionTypeList) {
			currSuggestion.type = suggestionType;
			currSuggestion.secondaryPriorityValue = suggestionType.secondaryPriorityCalculator.calculatePriority(player, lineInfoList, dimension);

			if (0 <= currSuggestion.secondaryPriorityValue && currSuggestion.betterThan(bestSuggestion))
				bestSuggestion.copyDataFrom(currSuggestion);
		}

		return bestSuggestion;
	}

	/**
	 * Collects data of possible lines of a cell
	 */
	private CellStateCounter[] getLineInfoList(int x, int y) {
		int lineInfoSize = 2;

		CellStateCounter primaryDiagonalLineInfo = null;
		CellStateCounter secondaryDiagonalLineInfo = null;

		if (y == x) {
			++lineInfoSize;
			primaryDiagonalLineInfo = _primaryDiagonalLineInfo;
		}

		if (_dimension - 1 == y + x) {
			++lineInfoSize;
			secondaryDiagonalLineInfo = _secondaryDiagonalLineInfo;
		}

		CellStateCounter[] lineInfoList = new CellStateCounter[lineInfoSize];
		lineInfoList[0] = _horizontalLineInfoList[y];
		lineInfoList[1] = _verticalLineInfoList[x];

		if (secondaryDiagonalLineInfo != null)
			lineInfoList[--lineInfoSize] = secondaryDiagonalLineInfo;

		if (primaryDiagonalLineInfo != null)
			lineInfoList[--lineInfoSize] = primaryDiagonalLineInfo;

		return lineInfoList;
	}

	private CellState getNextPlayer() {
		int xNumber = getGameBoardCellStateNumber(CellState.OCCUPIED_BY_X);
		int oNumber = getGameBoardCellStateNumber(CellState.OCCUPIED_BY_O);

		return oNumber < xNumber ? CellState.OCCUPIED_BY_O : CellState.OCCUPIED_BY_X;
	}

	private static List<CellState> getPlayerList() {
		List<CellState> playerList = new ArrayList<>(Arrays.asList(CellState.values()));
		playerList.remove(CellState.EMPTY);
		return playerList;
	}
}
