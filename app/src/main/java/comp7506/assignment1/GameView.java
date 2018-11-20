package comp7506.assignment1;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import static android.content.ContentValues.TAG;

public class GameView extends SurfaceView {

    private SurfaceHolder holder;
    private GameLoopThread gameLoopThread;

    //variable to store the list of sprites classes
    private List<SpriteFoodFall> foodSprites = new ArrayList<SpriteFoodFall>();
    private List<SpriteCloud> cloudSprites = new ArrayList<SpriteCloud>();
    private List<SpriteBullet> bulletSprites = new ArrayList<SpriteBullet>();


    //since there is only 1 character, we do not need a list to store it
    private SpriteChar mainChar;

    //variable to store time related information
    private long lastClick,lastspawnhealthy,lastspawnbad,lastspawncloud ;
    private long lastbullettime;

    //variable used to randomly select from the library of resources available for sprite creation
    private int[] healthyfoodlist = {R.drawable.apple, R.drawable.avocado, R.drawable.melonwater};
    private int[] unhealthyfoodlist = {R.drawable.piepumpkin, R.drawable.pretzel, R.drawable.beer};
    private int[] cloudlist = {R.drawable.cloud1, R.drawable.cloud2, R.drawable.cloud3, R.drawable.cloud4};
    private int fruit,cloud;

    //cloud sequence instead of random generation to control the look and feel of the clouds
    private int[] cloudheight = {0 , 600 , 200 ,100, 300, 0, 200};
    private int cloudcounter = 0;

    //timer variable
    private int timer = 60;
    private int timercounter = 0;

    //score variables
    private int score = 0;
    private int foodcounter = 0;

    //required to set cloud to opaque
    private Paint paint = new Paint();

    // 5 second spawn time per food
    private float foodspawntime = 5000;
    private float clouddspawntime = 5000;
    private float bulletcooldown = 0;
    private float nextspawntime;

    // with reference to SpriteFoodFall.java variable
    private int foodwidth = 80;
    private int difficulty = 1;

    //variable to store the gender class
    private String gender;

    //object for random generation
    Random rnd = new Random();

    public GameView(Context context, String gender) {
        super(context);

        this.gender = gender;
        gameLoopThread = new GameLoopThread(this);

        paint.setColor(Color.BLACK);
        paint.setTextSize(100);

        holder = getHolder();
        holder.addCallback(new Callback() {
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                createCharSprites();
                createForeground();
                gameLoopThread.setRunning(true);
                gameLoopThread.start();
            }
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,int width, int height) {
            }
        });
    }

    //function to spawncloud based on a fixed sequence of height to improve appearance
    //simulation of parallax background
    //1 cloud will spawn every 5 seconds
    private void spawnCloud() {
        //check time since last spawn
        if (System.currentTimeMillis() - lastspawncloud > clouddspawntime) {
            lastspawncloud = System.currentTimeMillis();
            cloud = cloudlist[rnd.nextInt(cloudlist.length)];
            cloudSprites.add(new SpriteCloud(this, BitmapFactory.decodeResource(getResources(), cloud),-500,cloudheight[cloudcounter]));
            cloudcounter++;
            if (cloudcounter >= cloudheight.length){
                cloudcounter = 0 ;
            }
        }
    }

    //function to spawn healthyfood every 5 seconds
    private void spawnHealthyFood() {
        //check time since last spawn
        if (System.currentTimeMillis() - lastspawnhealthy > foodspawntime) {
            lastspawnhealthy = System.currentTimeMillis();
            fruit = healthyfoodlist[rnd.nextInt(3)];
            foodSprites.add(new SpriteFoodFall(this, BitmapFactory.decodeResource(getResources(), fruit),rnd.nextInt(this.getWidth()-foodwidth),0,true));
            foodcounter++;
        }
    }

    //function to spawn bullets
    private void spawnBullet(int x, int y) {
        //check time since last spawn
        if (System.currentTimeMillis() - lastbullettime > bulletcooldown) {
            lastbullettime = System.currentTimeMillis();
            //https://stackoverflow.com/questions/32244851/androidjava-lang-outofmemoryerror-failed-to-allocate-a-23970828-byte-allocatio
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            bulletSprites.add(new SpriteBullet(this, BitmapFactory.decodeResource(getResources(), R.drawable.arrow,options),x,y));
        }
    }

    //function to spawn unhealthyfood at random timing between 0-5 seconds
    private void spawnBadFood() {
        //check time since last spawn
        if (System.currentTimeMillis() - lastspawnbad > nextspawntime) {
            lastspawnbad = System.currentTimeMillis();
            nextspawntime = rnd.nextInt((int)foodspawntime);

            fruit = unhealthyfoodlist[rnd.nextInt(3)];
            foodSprites.add(new SpriteFoodFall(this, BitmapFactory.decodeResource(getResources(), fruit),rnd.nextInt(this.getWidth()-foodwidth),0,false));
        }
    }

    //function to create character sprites
    private void createCharSprites() {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.bad4);

        if (this.gender.equals("female")){
            bmp = BitmapFactory.decodeResource(getResources(), R.drawable.good4);
        }

        mainChar = new SpriteChar(this, bmp, this.getWidth()/2 ,this.getHeight()-100);
    }


    private void createForeground() {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.hills1);
        //foreground = new
    }

    @Override
    protected void onDraw(Canvas canvas) {

        //background sky blue colour
        canvas.drawColor(Color.parseColor("#87ceeb"));

        //draw the trees and grass
        drawForeGround(canvas);

        //draw the food and cloud
        updateFood(canvas);
        updateCloud(canvas);
        updateBullet(canvas);

        //draw the main char
        mainChar.onDraw(canvas);

        //spawn new food or cloud based on the timing
        spawnHealthyFood();
        spawnBadFood();
        spawnCloud();

        //after 30 frames update as 1 second. must sync with GameLoopThread.java
        timercounter++;
        if (timercounter >= 30){
            timercounter = 0;
            timer--;
        }

        //draw the score and timer
        canvas.drawText("Score: " + String.valueOf(score), this.getWidth()-450, 100, paint);
        canvas.drawText("Timer: " + String.valueOf(timer), 0, 100, paint);

        //time over, calculate results and go to results activity
        if(timer == 0){
            //game over stop game
            //timer = 60;
            gameLoopThread.setRunning(false);
            int bmi = calculateBMI(score, foodcounter);
            Intent gameoverintent = new Intent(this.getContext(),ResultActivity.class);
            gameoverintent.putExtra("final_score", String.valueOf(bmi));
            gameoverintent.putExtra("bmi_class", calculateBMIClassification(bmi));
            this.getContext().startActivity(gameoverintent);
        }

    }

    //About BMI
    //< 18.5 is under weight
    //18.5-23 is normal
    //23-27.5 pre-obese
    //> 27.5 obese
    //Based on current game setting of 60seconds and 30fps we expect 20 healthy food per game
    // Take range of BMI from 19 onwards, every healthy food i missed i will increase final BMI by 1
    private int calculateBMI(int score, int total){
        int maxBMI = 39;

        int bmi = maxBMI - score;
        return bmi;
    }

    //https://en.wikipedia.org/wiki/Body_mass_index
    private String calculateBMIClassification(int bmi){
        if (bmi<=23){
            return "Normal";
        }
        else if (bmi<=27){
            return "Pre-Obese";
        }
        else if (bmi <=39){
            return "Obese";
        }
        return "Super Obese";
    }

    //draw the trees and grass
    private void drawForeGround(Canvas canvas){
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.hills1);
        float aspectRatio = bmp.getWidth() / (float) bmp.getHeight();
        int width = this.getWidth();
        int height = Math.round(width / aspectRatio);
        bmp = Bitmap.createScaledBitmap(bmp, width, height, false);
        canvas.drawBitmap(bmp, 0, this.getHeight() - bmp.getHeight(), null);

        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.hills2);
        aspectRatio = bmp.getWidth() / (float) bmp.getHeight();
        width = this.getWidth();
        height = Math.round(width / aspectRatio);
        bmp = Bitmap.createScaledBitmap(bmp, width, height, false);
        canvas.drawBitmap(bmp, 0, this.getHeight() - bmp.getHeight(), null);
    }

    //update food location
    private void updateFood(Canvas canvas){
        for (SpriteFoodFall spriteFood : foodSprites) {
                spriteFood.onDraw(canvas);
        }
        for (SpriteFoodFall spriteFood : foodSprites) {
            if(spriteFood.isoutOfScope()) {
                foodSprites.remove(spriteFood);
                break;
            }

            //check collision with bullet
            for (int i = bulletSprites.size() - 1; i >= 0; i--) {
                SpriteBullet sprite = bulletSprites.get(i);
                if (spriteFood.isCollision(sprite.getX(), sprite.getY())) {
                    //reduce health of food
                    //if health == 1 then remove food and update score
                    if (!spriteFood.reduceHealth()){
                        if (spriteFood.is_healthy()){
                            score++;
                        }
                        else{
                            score--;
                        }
                        //foodSprites.remove(spriteFood);
                        bulletSprites.remove(sprite);
                        Log.e(TAG, "Hit ");
                        MediaPlayer mp = MediaPlayer.create(this.getContext(), R.raw.hit);
                        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            public void onCompletion(MediaPlayer mp) {
                                mp.stop(); // finish current activity
                                if (mp != null) {
                                    mp.release();
                                }
                            }
                        });
                        mp.start();

                        break;}
                }
            }
        }
    }


    //update bullet location
    private void updateBullet(Canvas canvas){
        for (SpriteBullet spriteBullet : bulletSprites) {
            spriteBullet.onDraw(canvas);
        }
        for (SpriteBullet spriteBullet : bulletSprites) {
            if(spriteBullet.isoutOfScope()) {
                bulletSprites.remove(spriteBullet);
                break;
            }
        }
    }

    //update cloud location
    private void updateCloud(Canvas canvas){
        for (SpriteCloud sprite : cloudSprites) {
            sprite.onDraw(canvas);
        }
        for (SpriteCloud sprite : cloudSprites) {
            if(sprite.isoutOfScope()) {
                cloudSprites.remove(sprite);
                break;
            }
        }
    }


    //ontouch destroy food elements
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (System.currentTimeMillis() - lastClick > 200) {
            lastClick = System.currentTimeMillis();
            synchronized (getHolder()) {
            spawnBullet(mainChar.getX(), mainChar.getY()-50);
            }
            /*
            synchronized (getHolder()) {
                for (int i = foodSprites.size() - 1; i >= 0; i--) {
                    SpriteFoodFall sprite = foodSprites.get(i);
                    if (sprite.isCollision(event.getX(), event.getY())) {
                        //reduce health of food
                        //if health == 1 then remove food and update score
                        if (!sprite.reduceHealth()){
                        if (sprite.is_healthy()){
                            score++;
                        }
                        else{
                            score--;
                        }
                        foodSprites.remove(sprite);
                        break;}
                    }
                }
            }*/
        }
        return true;
    }
}
