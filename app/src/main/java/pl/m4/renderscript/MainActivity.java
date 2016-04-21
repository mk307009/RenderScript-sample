package pl.m4.renderscript;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "Calc Time";
    private Bitmap mBitmapIn;
    private Bitmap mBitmapOut;
    private RenderScript mRS;
    private Allocation mInAllocation;
    private Allocation mOutAllocation;
    private ScriptC_mono mScript;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBitmapIn = loadBitmap(R.drawable.toy);
        mBitmapOut = Bitmap.createBitmap(mBitmapIn.getWidth(), mBitmapIn.getHeight(),
                mBitmapIn.getConfig());
        ImageView in = (ImageView) findViewById(R.id.imageView);
        in.setImageBitmap(mBitmapIn);
        long startTime = System.currentTimeMillis();
        changeImage();
        long endTime = System.currentTimeMillis() - startTime;
        Log.i(TAG, "on cpu: "+endTime);
        startTime = System.currentTimeMillis();
        createScript();
        endTime = System.currentTimeMillis() - startTime;
        Log.i(TAG, "on gpu: "+endTime);
        in.setImageBitmap(mBitmapOut);
    }

    private void changeImage(){
        int bWidth = mBitmapIn.getWidth();
        int bHeight = mBitmapIn.getHeight();
        int[] pixels = new int[bWidth * bHeight];
        mBitmapIn.getPixels(pixels, 0, bWidth, 0, 0, bWidth, bHeight);
        for (int i = 0; i < pixels.length; i++){
            pixels[i] = pixels[i]/2;
        }
        mBitmapOut.setPixels(pixels, 0, bWidth, 0, 0, bWidth, bHeight);
    }

    private void createScript() {
        mRS = RenderScript.create(this);
        mInAllocation = Allocation.createFromBitmap(mRS, mBitmapIn,
                Allocation.MipmapControl.MIPMAP_NONE,
                Allocation.USAGE_SCRIPT);
        mOutAllocation = Allocation.createFromBitmap(mRS, mBitmapOut,
                Allocation.MipmapControl.MIPMAP_NONE,
                Allocation.USAGE_SCRIPT);
        mScript = new ScriptC_mono(mRS, getResources(), R.raw.mono);
        mScript.forEach_root(mInAllocation, mOutAllocation);
        mOutAllocation.copyTo(mBitmapOut);
    }

    private Bitmap loadBitmap(int resource) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        return BitmapFactory.decodeResource(getResources(), resource, options);
    }
}
