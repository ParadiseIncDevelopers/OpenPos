package com.free;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import androidx.annotation.NonNull;

public class NetworkChangeReceiver
{
    public static void NetworkCallback(Activity activity, Runnable run)
    {

        ConnectivityManager.NetworkCallback callback = new ConnectivityManager.NetworkCallback()
        {
            @Override
            public void onAvailable(@NonNull Network network)
            {
                run.run();
            }

            @Override
            public void onLost(@NonNull Network network)
            {
                Intent intent = new Intent(activity, NetworkErrorActivity.class);
                activity.startActivity(intent);
                activity.finish();
            }

            @Override
            public void onUnavailable()
            {
                super.onUnavailable();
            }
        };

        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkRequest request = new NetworkRequest.Builder().addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).build();
        connectivityManager.registerNetworkCallback(request, callback);
    }
}
