package com.instantcrash.DatBounce;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;

public class MyActivity extends Activity {

    private static final String TAG = "MyActivity";

    WifiP2pManager mManager;
    WifiP2pManager.Channel mChannel;
    BroadcastReceiver mReceiver;

    IntentFilter mIntentFilter;

    ListView peerList;
    ArrayAdapter<String> peerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        peerList = (ListView) findViewById(R.id.peer_list);
        peerAdapter = new ArrayAdapter<String>(this.getApplicationContext(), android.R.layout.simple_list_item_1);
        peerList.setAdapter(peerAdapter);

        peerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String addr = (String)peerList.getItemAtPosition(i);
                Toast.makeText(getApplicationContext(), "clicked "+addr, Toast.LENGTH_SHORT).show();
                connectTo(addr, "noname");
            }
        });

        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        mReceiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, this);

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        registerReceiver(mReceiver, mIntentFilter);


        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "peer(s) found");
            }

            @Override
            public void onFailure(int i) {
                Log.d(TAG, "no peer found, "+i);
            }
        });
    }


    public void onlineButtonPressed(View v) {


    }

    public void connectTo(final String addr, String name) {
        //obtain a peer from the WifiP2pDeviceList
        WifiP2pDevice device;
        final WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = addr;
        mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {
                //success logic
                Log.d(TAG, "connected to "+addr);
            }

            @Override
            public void onFailure(int reason) {
                //failure logic
                Log.d(TAG, "could not connected to "+addr);
            }
        });
    }

    public void updatePeer(String addr, String name) {
        if (peerAdapter.getPosition(addr) <= 0) {
            peerAdapter.add(addr);
            peerAdapter.notifyDataSetChanged();
        }
    }



    /* register the broadcast receiver with the intent values to be matched */
    @Override
    protected void onResume() {
        super.onResume();
    }

    /* unregister the broadcast receiver */
    @Override
    protected void onDestroy() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }
}
