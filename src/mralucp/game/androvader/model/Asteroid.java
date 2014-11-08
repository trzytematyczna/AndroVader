package mralucp.game.androvader.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Asteroid extends GameObject {

	private Rect sourceRect;
	private int life=20;
	
	private int currentFrame;
	private long frameTicker;
	private int framePeriod; 
	private int spriteWidth; 
	private int spriteHeight;
	private int frameCols;
	private int frameRows;
	private boolean dir;
	
	public Asteroid(Bitmap bitmap, Wector position,int fps,int frameC,int frameR,boolean d) {
		super(bitmap, position);
		currentFrame = 0;
		spriteWidth = bitmap.getWidth() / frameC;
		spriteHeight = bitmap.getHeight()/ frameR;
		frameCols=frameC;
		frameRows=frameR;
		dir=d;
		sourceRect = new Rect(0, 0, spriteWidth, spriteHeight);
		framePeriod = 1000 / fps;
		frameTicker = 0l;
	}

	public void draw(Canvas canvas) {
		Rect destRect = new Rect(position.x, position.y, position.x + spriteWidth, position.y + spriteHeight);
		canvas.drawBitmap(bitmap, sourceRect, destRect, null);
		boundingRect = destRect;
	}
	
	public void update(long gameTime) {
		super.update();
		calculateSourceRect(gameTime);
	}
	
	private void calculateSourceRect(long gameTime) {	
		if (gameTime > frameTicker + framePeriod) {
			frameTicker = gameTime;
			if (dir) currentFrame++;
			else currentFrame--;
			if (currentFrame<0) currentFrame=frameCols*frameRows-1;
			if (currentFrame>frameCols*frameRows-1) currentFrame=0;
		}
		sourceRect.top=(currentFrame/frameCols)*spriteHeight;
		sourceRect.bottom=sourceRect.top+spriteHeight;
		sourceRect.left = (currentFrame%frameCols)*spriteWidth;
		sourceRect.right = sourceRect.left + spriteWidth;
	}
	
	public int getSpriteWidth()
	{
		return this.spriteWidth;
	}
	public int getSpriteHeight()
	{
		return this.spriteHeight;
	}
	
	public void takeLife(int amount)
	{
		this.life=this.life-amount;
		if (this.life<0) this.life=0;
	}
	
	public int getLife()
	{
		return this.life;
	}
}
