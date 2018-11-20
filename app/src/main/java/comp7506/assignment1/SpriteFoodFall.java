package comp7506.assignment1;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class SpriteFoodFall {
    private static final int MAX_SPEED = 5;
    private GameView gameView;
    private Bitmap bmp;
    private int x = 0;
    private int y = 0;
    private int xSpeed;
    private int ySpeed;
    private int currentFrame = 0;
    private int width;
    private int height;
    private int health;
    private boolean is_healthy;
    private int gravity = 8;
    private boolean toremove;

    public SpriteFoodFall(GameView gameView, Bitmap bmp, int startX, int startY, boolean is_healthy) {

        //scaling the food size to width 80
        float aspectRatio = bmp.getWidth() / (float) bmp.getHeight();
        width = 80;
        height = Math.round(width / aspectRatio);
        bmp = Bitmap.createScaledBitmap(bmp, width, height, false);

        //compute size of each bmp
        this.width = bmp.getWidth();
        this.height = bmp.getHeight();

        this.gameView = gameView;
        this.bmp = bmp;

        //set start location
        x = startX;
        y = startY;

        //initialise falling speed
        ySpeed = gravity;

        //define the type of food created
        this.is_healthy = is_healthy;

        //define the health for each food
        health = 1;
    }

    //update x,y location of the bitmap
    private void update() {
        if (x >= gameView.getWidth() - width - xSpeed || x + xSpeed <= 0) {
            xSpeed = -xSpeed;
        }
        x = x + xSpeed;
        if (y >= gameView.getHeight() - height - ySpeed || y + ySpeed <= 0) {
            ySpeed = -ySpeed;
        }
        y = y + ySpeed;
        currentFrame = ++currentFrame;
    }

    //draw bitmap / food
    public void onDraw(Canvas canvas) {
        update();
        canvas.drawBitmap(bmp, x, y, null);
    }

    //get helper function
    public boolean is_healthy(){
        return is_healthy;
    }

    //collision detection
    public boolean isCollision(float x2, float y2) {
        return x2 > x && x2 < x + width && y2 > y && y2 < y + height;
    }

    //check if food has fell to the floor
    public boolean isoutOfScope() {
        if (toremove){return true;}
        return ySpeed <= 0;
    }

    //function to reduce health of food
    //on last health = 1, return false
    public boolean reduceHealth(){
        if (health == 1){
            toremove = true;
            return false;
        }
        health--;
        return true;
    }

    //helper function to get x coordinate
    public int getX(){
        return x;
    }

    //helper function to get y coordinate
    public int getY(){
        return y;
    }

}
