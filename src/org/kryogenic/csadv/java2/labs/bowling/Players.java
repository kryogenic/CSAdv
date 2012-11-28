package org.kryogenic.csadv.java2.labs.bowling;

public final class Players {
	private int idx = 0;
	private final int max;
	
	public Players(int amount) {
		if(amount > 4)
			throw new IllegalArgumentException("The maximum number of players is 4. Given value: " + amount);
		max = amount - 1;
	}
	public Player get() {
		return get(idx);
	}
	public Player get(int player) {
		return values[player];
	}
	public int size() {
		return max + 1;
	}
	public void step() {
		idx = idx == max ? 0 : idx + 1;
	}
	
	private static Player[] values = { new Player("Player 1"), new Player("Player 2"), new Player("Player 3"), new Player("Player 4") };
	
	public static class Player {
		private int ball = 1;
		private int frame = 0;
		private final String name;
		private final int[] scores = new int[10];

		private Player(String name) {
			this.name = name;
		}
		public void addScore(int score) {
			scores[frame] = score;
			if(ball == 2 || score > 10) {
				ball = 0;
				frame++;
			}
			ball++;
		}
		public int getScore(int frame) {
			return scores[frame];
		}
		@Override
		public String toString() {
			return name;
		}
		public int total() {
			int sum = 0;
			for(int i : scores) {
				sum += i;
			}
			return sum;
		}
	}
}