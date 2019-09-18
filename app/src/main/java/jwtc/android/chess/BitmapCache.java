package jwtc.android.chess;

import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;

import jwtc.chess.board.BoardConstants;
import jwtc.chess.board.ChessBoard;

public class BitmapCache {
    private static final String TAG = "BitmapCache";
    public static Bitmap[][] arrPieceBitmaps = new Bitmap[2][6];
    public static Bitmap bitmapBorder, bitmapSelect, bitmapSelectLight;
    public static Bitmap bitmapTile;
    public static Paint paint = new Paint();
    public static Matrix matrix = new Matrix();

    // @TODO skip if already initialized
    public static void init(AssetManager am, SharedPreferences prefs) {
        String sFolder = "highres/";
        String sPat = prefs.getString("tileSet", "");

        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setFilterBitmap(true);

        try {

            if (prefs.getBoolean("extrahighlight", false)) {
                BitmapCache.bitmapBorder = BitmapFactory.decodeStream(am.open(sFolder + "border.png"));
            } else {
                BitmapCache.bitmapBorder = null;
            }

            BitmapCache.bitmapSelect = BitmapFactory.decodeStream(am.open(sFolder + "select.png"));
            BitmapCache.bitmapSelectLight = BitmapFactory.decodeStream(am.open(sFolder + "select_light.png"));

            if (sPat.length() > 0) {
                BitmapCache.bitmapTile = BitmapFactory.decodeStream(am.open("tiles/" + sPat + ".png"));
            } else {
                BitmapCache.bitmapTile = null;
            }
            // pawn
            BitmapCache.arrPieceBitmaps[ChessBoard.BLACK][BoardConstants.PAWN] = BitmapFactory.decodeStream(am.open(sFolder + "pb.png"));
            BitmapCache.arrPieceBitmaps[ChessBoard.WHITE][BoardConstants.PAWN] = BitmapFactory.decodeStream(am.open(sFolder + "pw.png"));

            // kNight
            BitmapCache.arrPieceBitmaps[ChessBoard.BLACK][BoardConstants.KNIGHT] = BitmapFactory.decodeStream(am.open(sFolder + "nb.png"));
            BitmapCache.arrPieceBitmaps[ChessBoard.WHITE][BoardConstants.KNIGHT] = BitmapFactory.decodeStream(am.open(sFolder + "nw.png"));

            // bishop
            BitmapCache.arrPieceBitmaps[ChessBoard.BLACK][BoardConstants.BISHOP] = BitmapFactory.decodeStream(am.open(sFolder + "bb.png"));
            BitmapCache.arrPieceBitmaps[ChessBoard.WHITE][BoardConstants.BISHOP] = BitmapFactory.decodeStream(am.open(sFolder + "bw.png"));

            // rook
            BitmapCache.arrPieceBitmaps[ChessBoard.BLACK][BoardConstants.ROOK] = BitmapFactory.decodeStream(am.open(sFolder + "rb.png"));
            BitmapCache.arrPieceBitmaps[ChessBoard.WHITE][BoardConstants.ROOK] = BitmapFactory.decodeStream(am.open(sFolder + "rw.png"));

            // queen
            BitmapCache.arrPieceBitmaps[ChessBoard.BLACK][BoardConstants.QUEEN] = BitmapFactory.decodeStream(am.open(sFolder + "qb.png"));
            BitmapCache.arrPieceBitmaps[ChessBoard.WHITE][BoardConstants.QUEEN] = BitmapFactory.decodeStream(am.open(sFolder + "qw.png"));

            // king
            BitmapCache.arrPieceBitmaps[ChessBoard.BLACK][BoardConstants.KING] = BitmapFactory.decodeStream(am.open(sFolder + "kb.png"));
            BitmapCache.arrPieceBitmaps[ChessBoard.WHITE][BoardConstants.KING] = BitmapFactory.decodeStream(am.open(sFolder + "kw.png"));

        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
        }
    }

    public static void updateWidth(int width) {
        // any piece
        Log.i(TAG, "updateWidth: " + width);
        float scale = (float) width / BitmapCache.arrPieceBitmaps[ChessBoard.BLACK][BoardConstants.PAWN].getWidth();
        BitmapCache.matrix.setScale(scale, scale);
    }
}
