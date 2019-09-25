package jwtc.android.chess;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

public class ChessFieldSelectionView extends View {
    private Animator currentAnimator;
    private static Paint _paint = new Paint();
    private boolean isValid = false;

    public ChessFieldSelectionView(Context context) {
        super(context);
        setVisibility(View.GONE);
    }

    public void onDraw(Canvas canvas) {
        int color = isValid ? ColorScheme.colorValid : ColorScheme.colorInvalid;
        _paint.setColor(color);
        canvas.drawCircle(this.getWidth()/2, this.getHeight()/2, this.getWidth()/2, _paint);
    }

    public void appearAt(boolean valid) {
        isValid = valid;
        if (currentAnimator != null) {
            currentAnimator.cancel();
        }
        setVisibility(View.VISIBLE);

        AnimatorSet set = new AnimatorSet();
        set.play(ObjectAnimator.ofFloat(this, View.SCALE_X, 0f, 1f))
           .with(ObjectAnimator.ofFloat(this, View.SCALE_Y, 0f, 1f));
        set.setDuration(100);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                currentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                currentAnimator = null;
            }
        });
        set.start();
        currentAnimator = set;
    }

    public void dissapear() {
        setVisibility(View.GONE);
    }
}
