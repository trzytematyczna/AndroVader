package mralucp.game.androvader.model;

import java.util.Random;

import android.graphics.Bitmap;

public class Gold extends GameObject {

	private static Random generator = new Random();

	public Gold(Bitmap bitmap, Wector wector) {
		super(bitmap, wector);
		speed.y = generator.nextInt(5) + 3;
	}
}
