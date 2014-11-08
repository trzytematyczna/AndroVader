package mralucp.game.androvader.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Spaceship extends GameObject {

	private Rect sourceRect; // the rectangle to be drawn from the animation
								// bitmap
	private int life=10;
	
	private int frameNr; // number of frames in animation
	private int currentFrame; // the current frame
	private long frameTicker; // the time of the last frame update
	private int framePeriod; // milliseconds between each frame (1000/fps)
	private int spriteWidth; // the width of the sprite to calculate the cut out
								// rectangle
	private int spriteHeight; // the height of the sprite

	public Spaceship(Bitmap bitmap, Wector position, int fps, int frameCount) {
		super(bitmap, position);
//		life=100;
		frameNr = frameCount;
		currentFrame = 4;
		spriteWidth = bitmap.getWidth() / frameCount;
		spriteHeight = bitmap.getHeight();
		sourceRect = new Rect(0, 0, spriteWidth, spriteHeight);
		framePeriod = 1000 / fps;
		frameTicker = 0l;
	}

	public void handleActionMove(int eventX, int canvasWidth) {
		if (eventX < 50) {
			speed = new Wector(-10, 0);
		} else if (eventX > canvasWidth - 50) {
			speed = new Wector(10, 0);
		}
	}

	public void handleActionUP() {
		speed = new Wector(0, 0);
	}

	public void borders(int canvasWidth) {
		if (position.x > canvasWidth - spriteWidth) {
			position.x = canvasWidth - spriteWidth;
		}
		if (position.x < 0) {
			position.x = 0;
		}
	}

	// the draw method which draws the corresponding frame
	public void draw(Canvas canvas) {
		Rect destRect = new Rect(position.x, position.y, position.x + spriteWidth, position.y + spriteHeight);
		canvas.drawBitmap(bitmap, sourceRect, destRect, null);
		boundingRect = destRect;
	}

	// the update method for ship
	public void update(long gameTime, int canvasWidth) {
		super.update();
		calculateSourceRect(gameTime);
		borders(canvasWidth);
	}

	private void calculateSourceRect(long gameTime) {
		if (this.speed.x==0)
		{
			if ((currentFrame==0 || currentFrame==1 || currentFrame==2))
			{
				if (gameTime > frameTicker + framePeriod) {
					frameTicker = gameTime;
					// increment the frame
					currentFrame++;
				}
			}
			if ((currentFrame==4 || currentFrame==5 || currentFrame==6))
			{
				if (gameTime > frameTicker + framePeriod) {
					frameTicker = gameTime;
					// increment the frame
					currentFrame--;
				}
			}
		}
		if (this.speed.x>0)
		{
			if ((currentFrame!=6)){
				if (gameTime > frameTicker + framePeriod) {
					frameTicker = gameTime;
					// increment the frame
					currentFrame++;
				}
			}
		}
		if (this.speed.x<0)
		{
			if ((currentFrame!=0)){
				if (gameTime > frameTicker + framePeriod) {
					frameTicker = gameTime;
					// increment the frame
					currentFrame--;
				}
			}
		}
		sourceRect.left = currentFrame * spriteWidth;
		sourceRect.right = sourceRect.left + spriteWidth;
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
	
	public int getSpriteWidth()
	{
		return this.spriteWidth;
	}
	public int getSpriteHeight()
	{
		return this.spriteHeight;
	}
}
