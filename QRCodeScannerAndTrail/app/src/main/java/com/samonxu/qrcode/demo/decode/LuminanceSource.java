package com.samonxu.qrcode.demo.decode;

import android.graphics.Bitmap;

/**
 * Created by yinlili on 2017/3/21.
 */
public abstract class LuminanceSource extends com.google.zxing.LuminanceSource {

        protected LuminanceSource(int width, int height) {
            super(width, height);
        }

        public abstract Bitmap renderCroppedGreyScaleBitmap();
}

