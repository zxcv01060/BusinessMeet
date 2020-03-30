package tw.com.bussinessmeet.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import org.w3c.dom.Node;

import java.io.ByteArrayOutputStream;

public class AvatarHelper {
    public static Bitmap toCircle(Bitmap avatar){

        int width = avatar.getWidth();
        int height = avatar.getHeight();
        if (width >= height) {
            avatar = Bitmap.createBitmap(
                    avatar,
                    width / 2 - height / 2,
                    0,
                    height,
                    height
            );
        } else {
            avatar = Bitmap.createBitmap(
                    avatar,
                    0,
                    height / 2 - width / 2,
                    width,
                    width
            );
        }
        int r = 0;

        if(width > height) {
            r = height;
        } else {
            r = width;
        }

        Bitmap avatarCircle = Bitmap.createBitmap(r,r, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(avatarCircle);
        Paint paint = new Paint();
        paint.setAntiAlias(true); //去掉鋸齒
        Rect rect = new Rect(0,0,r,r);
        RectF rectF = new RectF(rect);
        canvas.drawRoundRect(rectF,r/2,r/2,paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(avatar, null, rect, paint);
        return avatarCircle;
    }
    public String setImageResource(ImageView avatar){
        Drawable avatarDraw = avatar.getDrawable();
        Log.d("resultavatar",String.valueOf(avatarDraw));
        if(avatarDraw == null){
            return "";
        }
        Bitmap avatarBitmap = ((BitmapDrawable)avatar.getDrawable()).getBitmap();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        avatarBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] avatarByteArray = outputStream.toByteArray();
        return Base64.encodeToString(avatarByteArray,Base64.NO_WRAP);
    }

    public Bitmap getImageResource(String avatar){
        Log.d("avatar","avatar");
        byte[] avatarByteArray = Base64.decode(avatar, Base64.NO_WRAP);
        Bitmap avatarBitmap = BitmapFactory.decodeByteArray(avatarByteArray, 0, avatarByteArray.length);

        return avatarBitmap;
    }
}
