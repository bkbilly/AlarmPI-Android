package bkbilly.alarmpi;

import android.app.Application;
import android.util.Log;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import java.net.URISyntaxException;

public class ChatApplication {

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("https://spinet.asuscomm.com:5003");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public Socket getSocket() {
        return mSocket;
    }
}
