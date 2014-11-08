package mralucp.game.androvader.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class GameObject {

	protected Wector position;
	protected Wector speed = new Wector(0,0);
	protected Bitmap bitmap;
	Rect boundingRect = new Rect();
	protected boolean isTouched = false;

	public GameObject(Bitmap bitmap, Wector position) {
		this.bitmap = bitmap;
		this.position = position;
	}

	public void draw(Canvas canvas) {
		canvas.drawBitmap(bitmap, position.x, position.y, null);
		boundingRect = new Rect(position.x, position.y, position.x + bitmap.getWidth(), position.y + bitmap.getHeight());
	}

	public void update() {
		position = position.add(speed);
	}

	public Rect getBoundingRect() {
		return boundingRect;
	}

	public void setIsTouched(boolean b) {
		isTouched = b;
	}

	public boolean getIsTouched() {
		return isTouched;
	}
	
	public Wector getPosition(){
		return position;
	}
	public void setSpeed(Wector speed)
	{
		this.speed=speed;
	}
	public void setSpeedReverse()
	{
		this.speed.x=this.speed.x*(-1);
	}
	public void setDescent(int descent)
	{
		this.speed.y=descent;
	}
	public int getBitmapWidth()
	{
		return this.bitmap.getWidth();
	}
	public int getBitmapHeight()
	{
		return this.bitmap.getHeight();
	}
	
}