package comp7506.assignment1;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class SpriteForeground {
    private GameView gameView;
    private Bitmap bmp;
    private int x = 0;
    private int y = 0;
    private int currentFrame = 0;
    private int width;
    private int height;

    public SpriteForeground(GameView gameView, Bitmap bmp, int startX, int startY) {
        //compute size of each bmp
        this.width = bmp.getWidth() ;
        this.height = bmp.getHeight() ;

        this.gameView = gameView;
        this.bmp = bmp;

        x = startX;
        y = startY;
  }

    private void update() {
    }

    public void onDraw(Canvas canvas) {
        canvas.drawBitmap(bmp, x, y, null);
    }
}
