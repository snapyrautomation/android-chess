package jwtc.android.chess;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;

public class ChessPieceView extends View {
    public int piece, color;

    public ChessPieceView(Context context) {
        super(context);
    }

    public void onDraw(Canvas canvas) {
        BitmapCache.paint.setColor(0xffff9900);
        canvas.drawRect(new Rect(0, 0, getWidth(), getHeight()), BitmapCache.paint);
        canvas.drawBitmap(BitmapCache.arrPieceBitmaps[color][piece], BitmapCache.matrix, BitmapCache.paint);
    }
}
