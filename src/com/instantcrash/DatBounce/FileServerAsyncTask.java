package com.instantcrash.DatBounce;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class FileServerAsyncTask extends AsyncTask<Void, Void, String> {

    private static final String TAG = "FileServerAsyncTask";

    private Context context;

    public FileServerAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(Void... params) {
        try {

            /**
             * Create a server socket and wait for client connections. This
             * call blocks until a connection is accepted from a client
             */
            ServerSocket serverSocket = new ServerSocket(8888);
            Socket client = serverSocket.accept();

            /**
             * If this code is reached, a client has connected and transferred data
             * Save the input stream from the client as a JPEG file
             */
//            final File f = new File(Environment.getExternalStorageDirectory() + "/"
//                    + context.getPackageName() + "/wifip2pshared-" + System.currentTimeMillis()
//                    + ".jpg");
//
//            File dirs = new File(f.getParent());
//            if (!dirs.exists())
//                dirs.mkdirs();
//            f.createNewFile();
            InputStream inputstream = client.getInputStream();

            OutputStream myOs = new ByteArrayOutputStream();
            myOs.write("hello world".getBytes());

            copyFile(inputstream, myOs);

            serverSocket.close();
            return "done";
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

    public static boolean copyFile(InputStream inputStream, OutputStream out) {
        byte buf[] = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
            out.close();
            inputStream.close();
        } catch (IOException e) {
            Log.d(TAG, "copyFile ERR: "+e.toString());
            return false;
        }
        return true;
    }

    /**
     * Start activity that can handle the text
     */
    @Override
    protected void onPostExecute(String result) {
        if (result != null) {
            Toast.makeText(context, "DATA SENT!", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "send data");
        }
    }

}
