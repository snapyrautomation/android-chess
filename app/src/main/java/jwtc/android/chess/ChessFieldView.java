package jwtc.android.chess;

import jwtc.chess.board.ChessBoard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class ChessFieldView extends View {
    private static Paint _paint = new Paint();
    private boolean isSelected = false;
    private int width = 0;
    public int fieldColor;
    public int fieldIndex;

    public ChessFieldView(Context context) {
        super(context);
        fieldColor = ChessBoard.BLACK;
    }

    public ChessFieldView(Context context, AttributeSet atts) {
        super(context, atts);
        fieldColor = ChessBoard.BLACK;
    }

    public void onDraw(Canvas canvas) {
        int color = fieldColor == ChessBoard.BLACK ? ColorScheme.colorBlack : ColorScheme.colorWhite;
        _paint.setColor(isSelected ? ColorScheme.colorSelected : color);
        canvas.drawRect(new Rect(0, 0, getWidth(), getHeight()), _paint);
    }

    public boolean getIsSelected() {
        return isSelected;
    }

    public void setSelected() {
        isSelected = true;
        this.invalidate();
    }

    public void setUnselected() {
        isSelected = false;
        this.invalidate();
    }
}
