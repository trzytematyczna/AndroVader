package mralucp.game.androvader.model;

import android.graphics.Bitmap;

public class Bullet extends GameObject {

	public boolean enemy;
	
	public Bullet(Bitmap bitmap, Wector wector,boolean enemy) {
		super(bitmap, wector);
		this.enemy=enemy;
		if (!this.enemy) speed.y = -30;
		else speed.y=7;
	}
}
