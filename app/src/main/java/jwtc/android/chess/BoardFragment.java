package jwtc.android.chess;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Rect;
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
    private FrameLayout mainLayout;

    private OnFragmentInteractionListener mListener;

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

        mainLayout = activity.findViewById(R.id.MainBoardLayout);

        Log.i(TAG, mainLayout == null ? "main null" : "main not null");

        for (int i = 0; i < 64; i++) {
            ChessFieldView chessFieldView = new ChessFieldView(activity);
            chessFieldView.fieldColor = (i & 1) == 0 ? (((i >> 3) & 1) == 0 ? ChessBoard.WHITE : ChessBoard.BLACK) : (((i >> 3) & 1) == 0 ? ChessBoard.BLACK : ChessBoard.WHITE);
            chessFieldView.fieldIndex = i;
            chessFieldView.setOnDragListener(this);
            chessFieldViews[i] = chessFieldView;
        }


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
        if(event.getAction() == DragEvent.ACTION_DRAG_STARTED){
            Log.i(TAG, "drag started.");
            return true;
        }
        if(event.getAction() == DragEvent.ACTION_DRAG_ENTERED) {
            Log.i(TAG, "drag entered source");
            return true;
        }
        if(event.getAction() == DragEvent.ACTION_DRAG_LOCATION) {
//                    v.setX(event.getX());
//                    v.setY(event.getY());
            return true;
        }
        if(event.getAction() == DragEvent.ACTION_DROP) {
            ChessPieceView chessPieceView = (ChessPieceView)event.getLocalState();
            chessPieceView.setX(v.getX());
            chessPieceView.setY(v.getY());
            Log.i(TAG, "DROP ENTERED");
            return true;
        }
        else {
            return false;
        }
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
                int boardLength, fieldLength, margin = 0;

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
                ViewGroup.LayoutParams mainLayoutParams = mainLayout.getLayoutParams();
                mainLayoutParams.width = boardLength;
                mainLayoutParams.height = boardLength;
                mainLayout.setLayoutParams(mainLayoutParams);
                Log.i(TAG, "adjust " + availableWidth + ", " + availableHeight + " - " + fieldLength + "  " + margin);
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(fieldLength, fieldLength);

                for (int i = 0; i < 64; i++) {
                    ChessFieldView chessFieldView = chessFieldViews[i];
                    chessFieldView.setLayoutParams(layoutParams);
                    mainLayout.addView(chessFieldView);
                    chessFieldView.setX(Pos.col(i) * fieldLength);
                    chessFieldView.setY(Pos.row(i) * fieldLength);
                }

                for (int i = 0; i < chessPieceViews.size(); i++) {
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
        ChessPieceView pieceView = new ChessPieceView(activity);
        pieceView.piece = ChessBoard.KING;
        pieceView.color = ChessBoard.WHITE;

        chessPieceViews.add(pieceView);

//        layoutParams.topMargin = 20;
//        layoutParams.leftMargin = 20;


        pieceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.i(TAG, "onTouch");
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    View.DragShadowBuilder bu = new View.DragShadowBuilder(v);
                    ClipData clp = ClipData.newPlainText("", "");
                    v.startDrag(clp, bu, v, 0);
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
}
