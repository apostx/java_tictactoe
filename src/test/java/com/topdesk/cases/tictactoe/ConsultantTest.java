package com.topdesk.cases.tictactoe;

import static java.util.Objects.requireNonNull;
import static java.util.Spliterators.spliteratorUnknownSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.junit.Before;
import org.junit.Test;

import com.topdesk.cases.tictactoe.yoursolution.YourConsultant;

public class ConsultantTest {

	private Consultant consultant;

	@Before
	public void setConsultant() {
		consultant = new YourConsultant();
	}

	@Test
	public void mightSuggestAnyCell_OnEmptyBoard() {
		assertNotNull(consultant.suggest(board("empty")));
	}

	@Test
	public void suggestsLastRemainingCell_OnAlmostFullBoard() {
		assertEquals(CellLocation.BOTTOM_LEFT, consultant.suggest(board("only_move")));
	}

	@Test
	public void winsWhenPossible() {
		assertEquals(CellLocation.TOP_LEFT, consultant.suggest(board("one_winning_move01")));
	}

	@Test
	public void avoidsLosingWhenPossible() {
		assertEquals(CellLocation.CENTRE_LEFT, consultant.suggest(board("one_move_not_losing01")));
	}

	@Test
	public void favorsWinningOverNotLosing() {
		assertEquals(CellLocation.BOTTOM_RIGHT, consultant.suggest(board("winning_vs_not_losing")));
	}

	@Test
	public void forksWhenSafelyPossible() {
		assertInList(consultant.suggest(board("fork00")), CellLocation.TOP_RIGHT, CellLocation.BOTTOM_RIGHT);
	}

	@Test
	public void preventsOpponentFromForking() {
		assertEquals(CellLocation.CENTRE_CENTRE, consultant.suggest(board("forced_middle")));
	}

	@Test
	public void avoidsBadEarlyMove() {
		assertFalse(consultant.suggest(board("only_one_not_winning_move01")).equals(CellLocation.CENTRE_LEFT));
	}

	@Test
	public void suggestsAdvantageousEarlyPosition() {
		assertInList(consultant.suggest(board("first_move_o01")), CellLocation.TOP_LEFT, CellLocation.TOP_RIGHT, CellLocation.BOTTOM_LEFT, CellLocation.BOTTOM_RIGHT);
	}

	@Test(expected = IllegalStateException.class)
	public void throwsException_OnDraw() {
		consultant.suggest(board("drawn01"));
	}

	@Test(expected = IllegalStateException.class)
	public void throwsException_WhenO_AlreadyWon() {
		consultant.suggest(board("o_won01"));
	}

	@Test(expected = IllegalStateException.class)
	public void throwsException_WhenX_AlreadyWon() {
		consultant.suggest(board("x_won01"));
	}

	private static void assertInList(CellLocation actual, CellLocation... validSuggestion) {
		if (!Arrays.stream(validSuggestion).anyMatch(item -> item.equals(actual))) {
			fail("Expected one of: " + Arrays.toString(validSuggestion) + " but is: " + actual);
		}
	}

	private static GameBoard board(String file) {
		List<String> lines = linesOf(file);
		return field -> {
			Coordinate coord = FIELD_COORDINATE.get(requireNonNull(field));
			switch (lines.get(coord.y).charAt(coord.x)) {
			case 'x':
				return CellState.OCCUPIED_BY_X;
			case 'o':
				return CellState.OCCUPIED_BY_O;
			default:
				return CellState.EMPTY;
			}
		};
	}

	private static List<String> linesOf(String file) {
		Scanner boardScanner = new Scanner(ConsultantTest.class.getResourceAsStream(file + ".txt"));
		return StreamSupport.stream(spliteratorUnknownSize(boardScanner, 0), false)
				.collect(Collectors.toList());
	}

	private static final Map<CellLocation, Coordinate> FIELD_COORDINATE = new EnumMap<>(CellLocation.class);
	static {
		FIELD_COORDINATE.put(CellLocation.TOP_LEFT, Coordinate.of(0, 0));
		FIELD_COORDINATE.put(CellLocation.TOP_CENTRE, Coordinate.of(1, 0));
		FIELD_COORDINATE.put(CellLocation.TOP_RIGHT, Coordinate.of(2, 0));

		FIELD_COORDINATE.put(CellLocation.CENTRE_LEFT, Coordinate.of(0, 1));
		FIELD_COORDINATE.put(CellLocation.CENTRE_CENTRE, Coordinate.of(1, 1));
		FIELD_COORDINATE.put(CellLocation.CENTRE_RIGHT, Coordinate.of(2, 1));

		FIELD_COORDINATE.put(CellLocation.BOTTOM_LEFT, Coordinate.of(0, 2));
		FIELD_COORDINATE.put(CellLocation.BOTTOM_CENTRE, Coordinate.of(1, 2));
		FIELD_COORDINATE.put(CellLocation.BOTTOM_RIGHT, Coordinate.of(2, 2));
	}

	private static final class Coordinate {
		private static final Coordinate[] VALUES = {
				new Coordinate(0, 0),
				new Coordinate(0, 1),
				new Coordinate(0, 2),
				new Coordinate(1, 0),
				new Coordinate(1, 1),
				new Coordinate(1, 2),
				new Coordinate(2, 0),
				new Coordinate(2, 1),
				new Coordinate(2, 2) };

		final int x;
		final int y;

		private Coordinate(int x, int y) {
			this.x = x;
			this.y = y;
		}

		static Coordinate of(int x, int y) {
			return VALUES[x * 3 + y];
		}
	}
}
