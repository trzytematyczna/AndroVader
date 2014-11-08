package mralucp.game.androvader.model;

import android.graphics.Bitmap;

public class Enemy extends GameObject {

	public Enemy(Bitmap bitmap, Wector wector) {
		super(bitmap, wector);
		speed.x = 2;
	}
}
