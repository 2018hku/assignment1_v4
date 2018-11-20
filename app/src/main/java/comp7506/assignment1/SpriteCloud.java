package comp7506.assignment1;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class SpriteCloud {

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
    private Paint paint = new Paint();

    public SpriteCloud(GameView gameView, Bitmap bmp, int startX, int startY) {
        //compute size of each bmp
        this.width = bmp.getWidth();
        this.height = bmp.getHeight();

        this.gameView = gameView;
        this.bmp = bmp;

        //set opaque to 80%
        bmp.setHasAlpha(true);
        paint.setAlpha(80);

        //set start location
        x = startX;
        y = startY;

        //set cloud speed
        xSpeed = 5;
    }

    //update cloud next location
    private void update() {
        x = x + xSpeed;
        if (y >= gameView.getHeight() - height - ySpeed || y + ySpeed <= 0) {
            ySpeed = -ySpeed;
        }
        currentFrame = ++currentFrame;
    }

    //draw cloud at next location
    public void onDraw(Canvas canvas) {
        update();
        canvas.drawBitmap(bmp, x, y, paint);
    }

    //report the cloud is outside game view for main class to remove it.
    public boolean isoutOfScope() {
        if (y > gameView.getHeight()*2)
        {return true;}
        return false;
    }
}
