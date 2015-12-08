package com.osofflinemap;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AssetsUtils {

    private static final int BUFFER_SIZE = 2048;

    public static void getFileFromAssets(Context context, String sourceName, File file) {
        try {
            InputStream in = context.getAssets().open(sourceName);
            writeBytesToFile(in, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeBytesToFile(InputStream is, File file) throws IOException {
        byte[] data = new byte[BUFFER_SIZE];
        int byteCount;
        FileOutputStream fos = new FileOutputStream(file);
        while ((byteCount = is.read(data)) > -1) {
            fos.write(data, 0, byteCount);
        }
        fos.close();
    }
}
