package com.topdesk.cases.tictactoe.yoursolution;

import com.topdesk.cases.tictactoe.CellLocation;
import com.topdesk.cases.tictactoe.CellState;

public class Suggestion {

	public CellLocation cellLoacation;
	public CellState preferedCellState = CellState.EMPTY;
	public SuggestionType type = SuggestionType.FREE;
	public int secondaryPriorityValue = -1;

	public Suggestion() {
	}

	public Suggestion(CellState preferedCellState, CellLocation cellLoacation) {
		this.cellLoacation = cellLoacation;
		this.preferedCellState = preferedCellState;
	}

	public Suggestion(Suggestion source) {
		copyDataFrom(source);
	}

	public boolean betterThan(Suggestion otherSuggestion, CellState player) {
		int priorityValueDifference = type.priorityValue - otherSuggestion.type.priorityValue;
		int secondaryPriorityValueDifference = secondaryPriorityValue - otherSuggestion.secondaryPriorityValue;
		boolean isBetterPreferedCellState = preferedCellState != otherSuggestion.preferedCellState && preferedCellState == player;

		/**
		 * better priorityValue OR equal priorityValue AND better
		 * secondaryPriorityValue OR equal priorityValue AND equal
		 * secondaryPriorityValue AND better preferedCellState
		 */
		return betterThan(otherSuggestion) || (0 == priorityValueDifference && 0 == secondaryPriorityValueDifference && isBetterPreferedCellState);
	}

	public boolean betterThan(Suggestion otherSuggestion) {
		int priorityValueDifference = type.priorityValue - otherSuggestion.type.priorityValue;
		int secondaryPriorityValueDifference = secondaryPriorityValue - otherSuggestion.secondaryPriorityValue;

		/**
		 * better priorityValue OR equal priorityValue AND better
		 * secondaryPriorityValue
		 */
		return 0 < priorityValueDifference || (0 == priorityValueDifference && 0 < secondaryPriorityValueDifference);
	}

	public void copyDataFrom(Suggestion otherSuggestion) {
		cellLoacation = otherSuggestion.cellLoacation;
		preferedCellState = otherSuggestion.preferedCellState;
		type = otherSuggestion.type;
		secondaryPriorityValue = otherSuggestion.secondaryPriorityValue;
	}
}
