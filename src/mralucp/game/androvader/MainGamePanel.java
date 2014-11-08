package mralucp.game.androvader;

import java.util.LinkedList;
import java.util.Random;

import mralucp.game.androvader.model.Asteroid;
import mralucp.game.androvader.model.Bullet;
import mralucp.game.androvader.model.CollisionDetector;
import mralucp.game.androvader.model.Enemy;
import mralucp.game.androvader.model.GameObject;
import mralucp.game.androvader.model.Heart;
import mralucp.game.androvader.model.Spaceship;
import mralucp.game.androvader.model.Wector;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MainGamePanel extends SurfaceView implements SurfaceHolder.Callback{
	
	public double SHOOT_CHANCE=0.065;
	public long ENEMY_UPDATE_TIME=20;
	public int ENEMY_GAP=3;
	public int ENEMY_LINES=5;
	public Bitmap ALIEN1=BitmapFactory.decodeResource(getResources(), R.drawable.alien1);
	public Bitmap ALIEN2=BitmapFactory.decodeResource(getResources(), R.drawable.alien2);
	public Bitmap ASTEROID_BITMAP=BitmapFactory.decodeResource(getResources(), R.drawable.asteroid);
	public Bitmap SPACESHIP_BITMAP=BitmapFactory.decodeResource(getResources(), R.drawable.spaceship_rot);
	public Bitmap BULLET_BITMAP=BitmapFactory.decodeResource(getResources(), R.drawable.bullet);
	public Bitmap BULLET_BITMAP_SPACESHIP=BitmapFactory.decodeResource(getResources(), R.drawable.bulletm);
	public Bitmap BACKGROUND=BitmapFactory.decodeResource(getResources(), R.drawable.space);
	public Bitmap HEART_BITMAP=BitmapFactory.decodeResource(getResources(), R.drawable.heart);
	
	private static Random generator = new Random();
	private MainThread thread;
	private Spaceship spaceship;
	private Asteroid asteroid1;
	private Asteroid asteroid2;
	private LinkedList<Heart> hearts= new LinkedList<Heart>();;
	private LinkedList<Enemy> enemies = new LinkedList<Enemy>();
	private LinkedList<Bullet> bullets = new LinkedList<Bullet>();
	private long lastBulletTime=0;
	private long lastEnemyUpdate=0;
	private long bulletPeriod = 500 ;
	private int points = 0;
	private Paint scorePaint=new Paint();
	private Paint lifePaint=new Paint();
	Handler myHandler = new Handler();
//	private long start;
	
	public MainGamePanel(Context context) {
		super(context);
		getHolder().addCallback(this);
		setFocusable(true);
		thread = new MainThread(getHolder(), this);
	}
	public int getpoints(){
		return points;
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		// TODO Auto-generated method stub
	}

	public void surfaceCreated(SurfaceHolder holder) {
		spaceship = new Spaceship(SPACESHIP_BITMAP, new Wector((getWidth() / 2) - 20, getHeight() - 100), 10, 7);
		createEnemies();
		createAsteroids();
		createHearts();
		setScorePaint();
		setLifePaint();
		thread.setRunning(true);
		thread.start();
//		start=System.currentTimeMillis();
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean retry = true;
		thread.setRunning(false);
		while (retry) {
			try {
				thread.join();
				retry = false;
			} catch (InterruptedException e) {
			}
		}
	}
	

	private void createEnemies() {
		int y=0;
		int x=0;
		while (y<ENEMY_LINES)
		{
			if (y%2==0)	enemies.add(new Enemy(ALIEN1, new Wector(x, y*ALIEN2.getHeight()+y*20)));
			else enemies.add(new Enemy(ALIEN2, new Wector(x, y*ALIEN2.getHeight()+y*20)));
			x=x+ALIEN1.getWidth()+20;
			if (x>=getWidth()-ENEMY_GAP*ALIEN1.getWidth())
			{
				x=0;
				y++;
			}
		}
		/*}
		for (int i=0;i<5;i++)
		{
			enemies.add(new Enemy(alien2, new Wector(alien2.getWidth()*i+10*i,alien2.getHeight()+40)));
		}*/
		//enemies.add(new Gold(BitmapFactory.decodeResource(getResources(), R.drawable.food), new Wector(generator.nextInt(getWidth()), 0)));
	}
	
	public void createAsteroids()
	{
		asteroid1=new Asteroid(ASTEROID_BITMAP,new Wector(40,getHeight()-spaceship.getSpriteHeight()-ASTEROID_BITMAP.getHeight()/6-30),20,5,6,true);
		asteroid2=new Asteroid(ASTEROID_BITMAP,new Wector(getWidth()-40-ASTEROID_BITMAP.getWidth()/5,getHeight()-spaceship.getSpriteHeight()-ASTEROID_BITMAP.getHeight()/6-30),17,5,6,false);
	}
	public void createHearts(){
		synchronized(hearts){
			int j=2;
			for(int i=0; i<spaceship.getLife(); i++){
				hearts.add(new Heart (HEART_BITMAP, new Wector(getWidth()-30-j, 5)));
				j+=30;
			}
		}
	}

	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
			if(event.getX() < spaceship.getBoundingRect().centerX() + spaceship.getBoundingRect().width() + 30 && event.getX() > spaceship.getBoundingRect().centerX() - spaceship.getBoundingRect().width() - 30 && event.getY()>getHeight()-spaceship.getBitmapHeight()-20){
				if(System.currentTimeMillis() - lastBulletTime > bulletPeriod){
				synchronized(bullets)
				{
					bullets.add(new Bullet(BULLET_BITMAP_SPACESHIP,new Wector(spaceship.getBoundingRect().centerX(),spaceship.getPosition().y),false));
				}
				lastBulletTime=System.currentTimeMillis();
				}
			}
			else spaceship.handleActionMove((int) event.getX(), getWidth());
		}
		if (event.getAction() == MotionEvent.ACTION_UP) {
			spaceship.handleActionUP();
		}
		return true; 
	}
	
	protected void render(Canvas canvas) {
		canvas.drawBitmap(BACKGROUND,0,0,null);
		spaceship.draw(canvas);
		if (asteroid1!=null) asteroid1.draw(canvas);
		if (asteroid2!=null) asteroid2.draw(canvas);
		//if (asteroid1!=null) asteroid1.draw(canvas);
		for (GameObject enemy : enemies) {
			enemy.draw(canvas);
		}
		synchronized(bullets)
		{
			for (GameObject bullet : bullets) {
				bullet.draw(canvas);
			}
		}
		canvas.drawText("Score: "+String.valueOf(points), 20, 20, scorePaint);
//		canvas.drawText("Life: "+String.valueOf(life), getWidth()-80, 20, lifePaint);
		for(Heart heart: hearts){
			heart.draw(canvas);
		}
	}

	public void update() {
		spaceship.update(System.currentTimeMillis(), getWidth());
		if (asteroid1!=null) asteroid1.update(System.currentTimeMillis());
		if (asteroid2!=null) asteroid2.update(System.currentTimeMillis());
		updateEnemies();
		updateBullets();
		updateHearts();
		checkCollisions();
		handleColisions();
		clearEnemies();
		clearBullets();
	}

	public void checkCollisions() {
		for (GameObject enemy : enemies) {
			if (CollisionDetector.isCollision(spaceship.getBoundingRect(), enemy.getBoundingRect())) {
				enemy.setIsTouched(true);
			}
		}
		synchronized(bullets)
		{
			for (Bullet bullet: bullets) {
				for (GameObject enemy : enemies){
					if (!bullet.enemy && CollisionDetector.isCollision(bullet.getBoundingRect(), enemy.getBoundingRect())){
						if (!bullet.enemy)
						{
							bullet.setIsTouched(true);
							enemy.setIsTouched(true);
						}
					}
				}
				if (bullet.enemy && CollisionDetector.isCollision(bullet.getBoundingRect(), spaceship.getBoundingRect()))
				{
					bullet.setIsTouched(true);
					spaceship.setIsTouched(true);
				}
				if (asteroid1!=null && CollisionDetector.isCollision(bullet.getBoundingRect(), asteroid1.getBoundingRect()))
				{
					bullet.setIsTouched(true);
					asteroid1.takeLife(1);
				}
				if (asteroid2!=null && CollisionDetector.isCollision(bullet.getBoundingRect(), asteroid2.getBoundingRect()))
				{
					bullet.setIsTouched(true);
					asteroid2.takeLife(1);
				}
			}
		}
	}

	public void handleColisions() {
//		int czas;
		for (int i = 0; i < enemies.size();) {
			GameObject obstacle = enemies.get(i);
			if (obstacle.getIsTouched()) {
				enemies.remove(i);
//				czas=Math.round(System.currentTimeMillis()-start/1000);
				points=points+10;//+300/czas;
			} else {
				i++;
			}
		}
		synchronized(bullets)
		{
			for (int i = 0; i < bullets.size();) {
				GameObject obstacle = bullets.get(i);
				if (obstacle.getIsTouched()) {
					bullets.remove(i);
				} else {
					i++;
				}
			}
		}
		if(spaceship.getIsTouched()){
			spaceship.takeLife(1);
			if (!hearts.isEmpty()) hearts.remove(hearts.size()-1);
			if(spaceship.getLife()<=0){
				//truuru przerwij :P
			}
			spaceship.setIsTouched(false);
		}
		if (asteroid1!=null && asteroid1.getLife()==0)
		{
			asteroid1=null;
		}
		if (asteroid2!=null && asteroid2.getLife()==0)
		{
			asteroid2=null;
		}
	}

	public void clearEnemies() {
		for (int i = 0; i < enemies.size();) {
			if (enemies.get(i).getPosition().y >= getHeight() - 30) {
				enemies.remove(i);
			} else {
				i++;
			}
		}
	}
	
	public void clearBullets() {
		synchronized(bullets)
		{
			for (int i = 0; i < bullets.size();) {
				if (bullets.get(i).getPosition().y < 0 || bullets.get(i).getPosition().y > getHeight()) {
					bullets.remove(i);
				} else {
					i++;
				}
			}
		}
	}

	public void updateEnemies() {
		if (System.currentTimeMillis()-lastEnemyUpdate>ENEMY_UPDATE_TIME)
		{
			lastEnemyUpdate=System.currentTimeMillis();
			if (this.enemies.size()!=0)
			{
				Enemy first=enemies.get(0);
				Enemy last=enemies.get(enemies.size()-1);
				for (Enemy primal: enemies)
				{
					if (first.getPosition().x>primal.getPosition().x) first=primal;
					if (last.getPosition().x<primal.getPosition().x) last=primal;
				}
				if (first.getPosition().x<0 || last.getPosition().x+last.getBitmapWidth()>getWidth())
				{
					for (GameObject allenemy : enemies)
					{
						allenemy.setSpeedReverse();
						allenemy.setDescent(5);
					}
				}
				else
				{
					for (GameObject allenemy : enemies)
					{
						allenemy.setDescent(0);
					}
				}
				for (GameObject enemy : enemies) {
					enemy.update();
				}
				if (enemies.size()>0)
				{
					if (generator.nextDouble()<SHOOT_CHANCE)
					{
						int shooter=generator.nextInt(enemies.size());
						Bullet bullet=new Bullet(BULLET_BITMAP,enemies.get(shooter).getPosition(),true);
						bullets.add(bullet);
					}
				}
			}
		}
	}
	
	public void updateBullets() {
		synchronized(bullets)
		{
			for (GameObject bullet : bullets) {
				bullet.update();
			}
		}
	}
	public void updateHearts(){
		for (Heart heart: hearts){
			heart.update();
		}
	}
	
	
	public void setScorePaint()
	{
		scorePaint.setColor(Color.GREEN);
		scorePaint.setTextSize(20);
		//scorePaint.setTextScaleX(2.0f);
	}
	public void setLifePaint()
	{
		lifePaint.setColor(Color.GREEN);
		lifePaint.setTextSize(20);
		//scorePaint.setTextScaleX(2.0f);
	}
	
}
