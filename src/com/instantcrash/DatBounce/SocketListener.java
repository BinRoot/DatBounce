package com.instantcrash.DatBounce;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SocketListener implements Runnable {

    private Socket socket = null;
    private InputStream iStream;
    private OutputStream oStream;
    private static final String TAG = "SocketListener";

    String mHostAddr;
    int mPort;

//    public SocketListener(Socket socket) {
//        this.socket = socket;
//    }

    public SocketListener(String hostAddr, int port) {
        this.socket = new Socket();
        this.mHostAddr = hostAddr;
        this.mPort = port;
    }

    @Override
    public void run() {

        try {
            socket.bind(null);
            socket.connect(new InetSocketAddress(mHostAddr, mPort), 5000);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            iStream = socket.getInputStream();
            oStream = socket.getOutputStream();
            byte[] buffer = new byte[1024];
            int bytes;

            while (true) {
                try {
                    // Read from the InputStream
                    bytes = iStream.read(buffer);
                    if (bytes == -1) {
                        break;
                    }

                    // Send the obtained bytes to the UI Activity
                    Log.d(TAG, "Rec:" + String.valueOf(buffer));

                    // update UI (do later)

                } catch (IOException e) {
                    Log.e(TAG, "disconnected", e);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
