package mralucp.game.androvader.model;

public class Wector {

	public int x;
	public int y;

	public Wector(int x, int y) {
		this.x = x;
		this.y = y;
	}

	Wector add(Wector another) {
		return new Wector(x + another.x, y + another.y);
	}
}
