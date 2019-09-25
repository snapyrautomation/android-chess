package jwtc.android.chess;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

public class ChessPieceView extends View {
    protected int piece, color;

    public ChessPieceView(Context context, int piece, int color) {
        super(context);

        this.piece = piece;
        this.color = color;
    }

    public void onDraw(Canvas canvas) {
        canvas.drawBitmap(BitmapCache.arrPieceBitmaps[color][piece], BitmapCache.matrix, BitmapCache.paint);
    }

    public void startDragging() {
        setVisibility(View.INVISIBLE);
    }

    public void stopDragging() {
        setVisibility(View.VISIBLE);
    }
}
