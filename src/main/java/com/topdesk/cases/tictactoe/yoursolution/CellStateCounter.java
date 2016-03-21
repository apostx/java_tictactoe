package com.topdesk.cases.tictactoe.yoursolution;

import com.topdesk.cases.tictactoe.CellState;
import java.util.EnumMap;
import java.util.Map;

public class CellStateCounter {

	private final Map<CellState, Integer> _matchNumberContainer = new EnumMap<>(CellState.class);

	public void addMatch(CellState cellState) {
		_matchNumberContainer.put(cellState, getMatchNumber(cellState) + 1);
	}

	public int getMatchNumber(CellState cellState) {
		Integer matchNumber = _matchNumberContainer.get(cellState);
		matchNumber = matchNumber == null ? 0 : matchNumber;

		return matchNumber;
	}
}
