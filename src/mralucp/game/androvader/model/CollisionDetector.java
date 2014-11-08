package mralucp.game.androvader.model;

import android.graphics.Rect;

public final class CollisionDetector {

	public static boolean isCollision(Rect one, Rect two) {
		return one.intersect(two);
	}
}