package com.spaceshooter.user.shooter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

import com.example.user.newshooter.R;

/**
 * Created by user on 6/8/2016.
 */
public class EnemyShip {
    RectF rect;

    // The player ship will be represented by a Bitmap
    private Bitmap bitmap;

    Context context;

    // How long and high our paddle will be
    private float width;
    private float height;

    // X is the far left of the rectangle which forms our paddle
    private float x;

    // Y is the top coordinate
    private float y;

    // This will hold the pixels per second speedthat the paddle will move
    public float shipSpeed;

    private boolean isVisible;

    private boolean isActive;

    public EnemyShip(Context context, int screenX, int screenY){
        // Initialize a blank RectF
        rect = new RectF();
        width = screenX/8;
        height = screenY/6;
        // Start ship in roughly the screen centre
        x = screenX / 2;
        y = -200;
        // Initialize the bitmap
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy);
        // stretch the bitmap to a size appropriate for the screen resolution
        bitmap = Bitmap.createScaledBitmap(bitmap, (int) (width), (int) (height), false);

        // How fast is the spaceship in pixels per second
        shipSpeed = 750;

        isVisible = false;

        isActive = false;
        
        this.context = context;
    }


    public void update(long fps){
        if(isActive){
            y = y + shipSpeed / fps;
        }

        // Update rect which is used to detect hits
        rect.top = y;
        rect.bottom = y + height;
        rect.left = x;
        rect.right = x + width;

    }

    public void startShip(float startX){
        isActive = true;
        isVisible = true;
        x = startX;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public boolean getStatus(){
        return isActive;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public RectF getRect() {
        return rect;
    }

    public void setStatus(boolean active) { isActive = active; }

    public void setY(float y) { this.y = y; }

    public void setShipSpeed(float shipSpeed) {
        this.shipSpeed = shipSpeed;
    }
}
