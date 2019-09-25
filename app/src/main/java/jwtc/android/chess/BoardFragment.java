package jwtc.android.chess;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import jwtc.chess.Pos;
import jwtc.chess.board.ChessBoard;

import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.ArrayList;

import static android.view.DragEvent.ACTION_DRAG_EXITED;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BoardFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class BoardFragment extends Fragment implements View.OnDragListener {
    private final static String TAG = "BoardFragment";
    private ChessFieldView[] chessFieldViews = new ChessFieldView[64];
    private ArrayList<ChessPieceView> chessPieceViews = new ArrayList<ChessPieceView>();
    private ChessFieldSelectionView chessFieldSelectionView;
    private OrderedFrameLayout mainLayout;
    private OnFragmentInteractionListener mListener;
    protected int fieldLength = 0;

    public BoardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Activity activity = getActivity();
        AssetManager assetManager = activity.getAssets();
        SharedPreferences sharedPreferences = activity.getSharedPreferences("ChessPlayer", Activity.MODE_PRIVATE);
        BitmapCache.init(assetManager, sharedPreferences);
        ColorScheme.init(sharedPreferences);

        mainLayout = activity.findViewById(R.id.MainBoardLayout);

        Log.i(TAG, mainLayout == null ? "main null" : "main not null");

        for (int i = 0; i < 64; i++) {
            ChessFieldView chessFieldView = new ChessFieldView(activity);
            chessFieldView.fieldColor = (i & 1) == 0 ? (((i >> 3) & 1) == 0 ? ChessBoard.WHITE : ChessBoard.BLACK) : (((i >> 3) & 1) == 0 ? ChessBoard.BLACK : ChessBoard.WHITE);
            chessFieldView.fieldIndex = i;
            chessFieldView.setOnDragListener(this);
            chessFieldViews[i] = chessFieldView;
        }

        chessFieldSelectionView = new ChessFieldSelectionView(activity);

        final Window window = activity.getWindow();
        final View contentView = window.getDecorView().findViewById(android.R.id.content);
//        adjustWidth(contentView, window);
        ViewTreeObserver vto = contentView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                contentView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                adjustWidth(contentView, window);
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_board, container, false);
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {

        ChessPieceView chessPieceView = (ChessPieceView) event.getLocalState();
        ChessFieldView chessFieldView = v instanceof ChessFieldView ? (ChessFieldView) v : null;

        if(event.getAction() == DragEvent.ACTION_DRAG_STARTED){
            if (chessFieldView != null) {
                Log.i(TAG, "chessFieldView started " + chessFieldView.fieldIndex);
//                chessFieldView.setUnselected();
            }
            return true;
        }
        if(event.getAction() == DragEvent.ACTION_DRAG_ENTERED) {
            if (chessFieldView != null) {
                Log.i(TAG, "chessFieldView entered " + chessFieldView.fieldIndex);

                setSelectedField(chessFieldView, true, chessFieldView.fieldIndex % 2 == 0);
            }
            return true;
        }
        if (event.getAction() == ACTION_DRAG_EXITED) {
            if (chessFieldView != null) {
                Log.i(TAG, "chessFieldView left " + chessFieldView.fieldIndex);
                setSelectedField(chessFieldView, false, false);
            }
            return true;
        }
        if(event.getAction() == DragEvent.ACTION_DRAG_LOCATION) {
            return true;
        }
        if(event.getAction() == DragEvent.ACTION_DROP) {
            Log.i(TAG, "ACTION_DROP");
            setSelectedField(chessFieldView, false, false);

            if  (chessFieldView.fieldIndex % 2 == 0) {
                chessPieceView.setX(v.getX());
                chessPieceView.setY(v.getY());
                chessPieceView.stopDragging();
                return true;
            }
            chessPieceView.stopDragging();
            return false;
        }
        return true;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void adjustWidth(final View contentView, final Window window) {

        contentView.post(new Runnable() {
            @Override
            public void run() {
                Rect rectangle = new Rect();
                contentView.getWindowVisibleDisplayFrame(rectangle);
                int contentViewTop = window.findViewById(Window.ID_ANDROID_CONTENT).getTop();
                //int titleBarHeight= contentViewTop - statusBarHeight;
                int availableHeight = (rectangle.bottom - rectangle.top) - contentViewTop;
                int availableWidth = rectangle.right - rectangle.left;
                int boardLength, margin = 0;

                // portrait
                if (availableHeight > availableWidth) {
                    boardLength = availableWidth;
                    fieldLength = availableWidth / 8;
                    margin = (availableWidth - 8 * fieldLength) / 2;
                } else {
                    boardLength = availableHeight;
                    fieldLength = availableHeight / 8;
                }
                // RESET!!
                mainLayout.removeAllViews();
                //mainLayout.setChildrenDrawingOrderEnabled(true);
                ViewGroup.LayoutParams mainLayoutParams = mainLayout.getLayoutParams();
                mainLayoutParams.width = boardLength;
                mainLayoutParams.height = boardLength;
                mainLayout.setLayoutParams(mainLayoutParams);
                Log.i(TAG, "adjust " + availableWidth + ", " + availableHeight + " - " + fieldLength + "  " + margin);

                for (int i = 0; i < 64; i++) {
                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(fieldLength, fieldLength);
                    ChessFieldView chessFieldView = chessFieldViews[i];
                    chessFieldView.setLayoutParams(layoutParams);
                    mainLayout.addView(chessFieldView);
                    chessFieldView.setX(Pos.col(i) * fieldLength);
                    chessFieldView.setY(Pos.row(i) * fieldLength);
                }

                FrameLayout.LayoutParams layoutParamsSelection = new FrameLayout.LayoutParams(fieldLength * 2, fieldLength * 2);
                chessFieldSelectionView.setLayoutParams(layoutParamsSelection);
                mainLayout.addView(chessFieldSelectionView);

                for (int i = 0; i < chessPieceViews.size(); i++) {
                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(fieldLength, fieldLength);
                    ChessPieceView pieceView = chessPieceViews.get(i);
                    pieceView.setLayoutParams(layoutParams);
                    mainLayout.addView(pieceView);
                }

                BitmapCache.updateWidth(fieldLength);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        Activity activity = getActivity();
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(90, 90);
        ChessPieceView pieceView = new ChessPieceView(activity, ChessBoard.KING, ChessBoard.WHITE);

        chessPieceViews.add(pieceView);

//        layoutParams.topMargin = 20;
//        layoutParams.leftMargin = 20;


        pieceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.i(TAG, "onTouch");
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    MyDragShadowBuilder bu = new MyDragShadowBuilder(v);
                    ClipData clp = ClipData.newPlainText("", "");
                    v.startDrag(clp, bu, v, 0);
                    ChessPieceView draggedPiece = (ChessPieceView)v;
                    draggedPiece.startDragging();
                    return true;
                } else {
                    return false;
                }
            }
        });

        pieceView.setOnDragListener(this);

//        chessFieldViews[1].setOnDragListener(new View.OnDragListener() {
//            @Override
//            public boolean onDrag(View v, DragEvent event) {
//                Log.i(TAG, "drag target");
//                if(event.getAction() == DragEvent.ACTION_DRAG_ENTERED) {
//                    Log.i(TAG, "drag entered");
//                    return true;
//                }
//                return false;
//            }
//        });
    }

    protected void setSelectedField(ChessFieldView chessFieldView, boolean isSelected, boolean isValid) {
        if (isSelected) {
            chessFieldView.setSelected();
            float offset = -chessFieldSelectionView.getLayoutParams().width / 4;
            chessFieldSelectionView.setX(Pos.col(chessFieldView.fieldIndex) * fieldLength + offset);
            chessFieldSelectionView.setY(Pos.row(chessFieldView.fieldIndex) * fieldLength + offset);
            chessFieldSelectionView.appearAt(isValid);
        } else {
            chessFieldView.setUnselected();
            chessFieldSelectionView.dissapear();
        }
    }


    private static class MyDragShadowBuilder extends View.DragShadowBuilder {

        // The drag shadow image, defined as a drawable thing
        private Point mScaleFactor;

        // Defines the constructor for myDragShadowBuilder
        public MyDragShadowBuilder(View v) {

            // Stores the View parameter passed to myDragShadowBuilder.
            super(v);

        }

        // Defines a callback that sends the drag shadow dimensions and touch point back to the
        // system.
        @Override
        public void onProvideShadowMetrics (Point size, Point touch) {
            // Defines local variables
            int width, height;

            // Sets the width of the shadow to half the width of the original View
            width = getView().getWidth() * 2;

            // Sets the height of the shadow to half the height of the original View
            height = getView().getHeight() * 2;

            // The drag shadow is a ColorDrawable. This sets its dimensions to be the same as the

            // Sets the size parameter's width and height values. These get back to the system
            // through the size parameter.
            size.set(width, height);

            mScaleFactor = size;

            // Sets the touch point's position
            touch.set(width / 2, height / 2);
        }

        // Defines a callback that draws the drag shadow in a Canvas that the system constructs
        // from the dimensions passed in onProvideShadowMetrics().
        @Override
        public void onDrawShadow(Canvas canvas) {
            canvas.scale(mScaleFactor.x/(float)getView().getWidth(), mScaleFactor.y/(float)getView().getHeight());
//            BitmapCache.paint.setColor(ColorScheme.colorSelected);
//            canvas.drawCircle(getView().getWidth()/2, getView().getHeight()/2, getView().getWidth()/2, BitmapCache.paint);
            getView().draw(canvas);
        }
    }
}
