package com.example.user.newshooter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;
/**
 * Created by user on 6/8/2016.
 */
public class SpaceShooterView extends SurfaceView implements Runnable{

    Context context;
    final Random random = new Random();
    // This is our thread
    private Thread gameThread = null;

    // Our SurfaceHolder to lock the surface before we draw our graphics
    private SurfaceHolder ourHolder;

    // A boolean which we will set and unset
    // when the game is running- or not.
    private volatile boolean playing;

    // A Canvas and a Paint object
    private Canvas canvas;
    private Paint paint;

    // This variable tracks the game frame rate
    private long fps;

    // This is used to help calculate the fps
    private long timeThisFrame;

    // The size of the screen in pixels
    private int screenX;
    private int screenY;

    // The players ship
    //private EnemyShip enemyShip;
    private ArrayList<EnemyShip> enemyShip;
    private PlayerShip playerShip;
    private PauseButton pauseButton;
    // The player's bullets
    private ArrayList<Bullet> bullets;

    // For sound FX
    private SoundPool soundPool;

    // The score
    static int score = 0;
    // Lives
    static int lives = 3;


    private boolean wasTouched;


    int bTime = 0;
    int eTime = 0;

    int eSpeed = 45;
    int bSpeed = 10;
    boolean loaded=false;
    private int soundID1;
    private int soundID2;
    private int soundID3;

    MediaPlayer mPlayer;

    private int a = 0;

    // When the we initialize (call new()) on gameView
    // This special constructor method runs

    public SpaceShooterView(Context context, int x, int y) {
        // The next line of code asks the
        // SurfaceView class to set up our object.
        // How kind.
        super(context);

        // Make a globally available copy of the context so we can use it in another method
        this.context = context;

        // Initialize ourHolder and paint objects
        ourHolder = getHolder();
        paint = new Paint();

        screenX = x;
        screenY = y;

        // This SoundPool is deprecated but don't worry
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC,0);

        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener(){
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status){
                loaded=true;
                Log.e("Test","sampleId="+sampleId+" status="+status);
            }
        });

        soundID1= soundPool.load(context, R.raw.shoot,1);
        soundID2 = soundPool.load(context,R.raw.vzr,1);
        soundID3 = soundPool.load(context,R.raw.uh,1);
        prepareLevel();
    }

    private void prepareLevel(){

        // Here we will initialize all the game objects

        // Make a new player space ship
        playerShip = new PlayerShip(context, screenX-100, screenY-200);
        // Prepare the players bullet
        bullets = new ArrayList<Bullet>();
        // Initialize the invadersBullets array

        // Build an army of invaders
        enemyShip = new ArrayList<EnemyShip>();
        for (int i = 0; i <= 15; i++) {
            EnemyShip enemy = new EnemyShip(context, screenX, screenY);
            enemyShip.add(enemy);
        }
        // Build the shelters
        pauseButton = new PauseButton(context, screenX-100, screenY-200);

    }
    @Override
    public void run() {
        mPlayer = MediaPlayer.create(context, R.raw.bit);
        mPlayer.start();
        while (playing) {

            // Capture the current time in milliseconds in startFrameTime
            long startFrameTime = System.currentTimeMillis();
            // Update the frame
            update();
            // Draw the frame
            draw();
            // Calculate the fps this frame
            // We can then use the result to
            // time animations and more.
            timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame >= 1) {
                fps = 1000 / timeThisFrame;
            }
        }

        mPlayer.stop();
    }

    private void update(){
        if (score >= 100){
            if (score == 100) {
                mPlayer.stop();
                mPlayer = MediaPlayer.create(context, R.raw.cool);
            }
            mPlayer.start();
        }
        // Has the player lost
        boolean lost = false;

        if(bTime == bSpeed){
            playerShip.shoot(bullets);
            bTime-=bSpeed;
        }
        else bTime++;
        if(eTime == eSpeed){
            EnemyShip enemy = enemyShip.get(a);
            enemy.startShip((random.nextInt((screenX-(int)enemy.getWidth()*2))+(int)enemy.getWidth())+random.nextFloat());
            enemy.setY(-200);
            a++;
            if (a == 15) {
                a = 0;
            }
            eTime-=eSpeed;
        }
        else eTime++;
        // Move the player's ship
        playerShip.update(fps);
        // Update the invaders if visible
        for(int i = 0; i < enemyShip.size(); i++){
            EnemyShip e = enemyShip.get(i);
            if(e.getStatus()) {
                e.update(fps);
            }
            if((e.getY() > screenY && e.isVisible() == true) || (RectF.intersects(playerShip.getRect(), e.getRect()) && e.isVisible() == true)) {
                soundPool.play(soundID3, 1, 1, 0, 0, 1);
                e.setVisible(false);
                e.setStatus(false);
                lives--;
                if (lives == 0){
                    playing = false;
                    Intent intent = new Intent(getContext(), FinalActivity.class);
                    getContext().startActivity(intent);
                }
            }
        }
        // Update all the invaders bullets if active
//
        // Did an invader bump into the edge of the screen

        if(lost){
            prepareLevel();
        }

        // Update the players bullet

        for(int i = 0; i < bullets.size(); i++){
            Bullet b = bullets.get(i);
            if(b.getStatus()) {
                b.update(fps);
            }
            if(b.getY() < 0)
                bullets.remove(b);
        }
        // Has the player's bullet hit the top of the screen

        // Has an invaders bullet hit the bottom of the screen

        // Has the player's bullet hit an invader
        for(Bullet b : bullets) {
            if (b.getStatus()) {
                if (b.isVisible()) {
                    for (EnemyShip e : enemyShip) {
                        if (e.getStatus()) {
                            if (e.isVisible()) {
                                if (RectF.intersects(b.getRect(), e.getRect())) {
                                    soundPool.play(soundID2, 1, 1, 0, 0, 1);
                                    e.setVisible(false);
                                    e.setStatus(false);
                                    b.setVisible(false);
                                    score = score + 1;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void draw(){
        // Make sure our drawing surface is valid or we crash

        if (ourHolder.getSurface().isValid()) {
            // Lock the canvas ready to draw
            canvas = ourHolder.lockCanvas();

            // Draw the background color
            canvas.drawColor(Color.BLACK);
            canvas.drawBitmap(pauseButton.getBitmap(), pauseButton.getX(), pauseButton.getY(), paint);
            // Choose the brush color for drawing
            if(score <= 100) {
                paint.setColor(Color.WHITE);
                for (int i = 0; i < 50; i++)
                    canvas.drawCircle(random.nextInt(screenX), random.nextInt(screenY), 1, paint);
            }
            else if (score > 100){
                paint.setColor(Color.argb(random.nextInt(255), random.nextInt(255), random.nextInt(255), 255));
                for (int i = 0; i < 50; i++)
                    canvas.drawCircle(random.nextInt(screenX), random.nextInt(screenY), 7, paint);
            }
            paint.setColor(Color.argb(255, 255, 255, 255));

            // Draw the player spaceship
            canvas.drawBitmap(playerShip.getBitmap(), playerShip.getX(), playerShip.getY(), paint);
            // Draw the invaders
            for (EnemyShip e : enemyShip) {
                if (e.getStatus() && e.isVisible()) {
                    canvas.save();
                    canvas.rotate(180);
                    canvas.drawBitmap(e.getBitmap(), -e.getWidth() - e.getX(), -e.getHeight() - e.getY(), paint);

                    canvas.restore();
                }
            }
            // Draw the bricks if visible

            // Draw the players bullet if active
            paint.setColor(Color.CYAN);
            for (int i = 0; i < bullets.size(); i++) {
                Bullet bull = bullets.get(i);
                if (bull.getStatus() && bull.isVisible()) {
                    canvas.drawBitmap(bull.getBitmap(), null, bull.getRect(), paint);
                    //soundPool.play(soundID1, 0.1f,0.1f,0,0,1);
                }
            }
            // Draw the invaders bullets if active

            // Draw the score and remaining lives
            // Change the brush color
            paint.setColor(Color.argb(255, 249, 129, 0));
            paint.setTextSize(40);
            canvas.drawText("Score: " + score + "   Lives: " + lives, 10, 50, paint);

            // Draw everything to the screen
            ourHolder.unlockCanvasAndPost(canvas);
        }
    }


    // If SpaceInvadersActivity is paused/stopped
    // shutdown our thread.
    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }

    }

    // If SpaceInvadersActivity is started then
    // start our thread.
    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    // The SurfaceView class implements onTouchListener
    // So we can override this method and detect screen touches.
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: // нажатие
                if ((pauseButton.getX() < x && (pauseButton.getX() + pauseButton.getWidth() > x))
                        && (pauseButton.getY() < y && (pauseButton.getY() + pauseButton.getHeight() > y))) {
                    if (playing)
                        pause();
                    else
                        resume();
                }
                if ((playerShip.getX() < x && (playerShip.getX() + playerShip.getWidth() > x))
                        && (playerShip.getY() < y && (playerShip.getY() + playerShip.getHeight() > y))) {

                    playerShip.setX(x-playerShip.getWidth()/2);
                    playerShip.setY(y-playerShip.getHeight()/2);
                    wasTouched = true;
                }
                break;
            case MotionEvent.ACTION_MOVE: // движение
                if (wasTouched) {
                    playerShip.setX(x-playerShip.getWidth()/2);
                    playerShip.setY(y-playerShip.getHeight()/2);
                }
                break;
            case MotionEvent.ACTION_UP: // отпускание
                if (wasTouched) {
                    playerShip.setX(x-playerShip.getWidth()/2);
                    playerShip.setY(y-playerShip.getHeight()/2);
                    wasTouched = false;
                }
                break;
        }
        return true;
    }
}
