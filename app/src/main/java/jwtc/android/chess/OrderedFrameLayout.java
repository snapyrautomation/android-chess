package jwtc.android.chess;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class OrderedFrameLayout extends FrameLayout {
    private static final String TAG = "OrderedFrameLayout";
    protected int selectedField = -1;

    public OrderedFrameLayout(@NonNull Context context) {
        super(context);
        setChildrenDrawingOrderEnabled(true);
    }

    public OrderedFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setChildrenDrawingOrderEnabled(true);
    }

    public OrderedFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setChildrenDrawingOrderEnabled(true);
    }

//    @Override
//    protected int getChildDrawingOrder(int childCount, int drawingPosition) {
//        if (selectedField >= 0) {
//            //Log.i(TAG, " getChildDrawingOrder " + childCount + "," + drawingPosition + ", " + selectedField);
//            if (selectedField == drawingPosition) {
//                Log.i(TAG, "set 63 for " + selectedField);
//                return 63;
//            }
//            if (drawingPosition > selectedField && drawingPosition <= 63) {
//                return drawingPosition - 1;
//            }
//        }
//        return drawingPosition;
//    }
//
//    public void setSelectedField(int index) {
//        selectedField = index;
//    }
}
