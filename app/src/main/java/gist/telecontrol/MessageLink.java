package gist.telecontrol;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

public class MessageLink extends Handler{

    private Activity mActivity;
    public static final int LAN = 0;
    public static final int BLUETOOTH = 1;
    public static final String LAN_TEXT = "Searching";
    public static final String BLUETOOTH_TEXT = "Scanning";
    public boolean mLANMessaging = true;
    public boolean mBluetoothMessaging = false;

    public MessageLink(Activity activity){
        mActivity = activity;
    }

    public void handleMessage(Message msg){
        switch(msg.what){
            case LAN:
                if(mLANMessaging) ((TextView)mActivity.findViewById(R.id.lan_devices_text)).setText("" + LAN_TEXT + msg.obj);
                break;
            case BLUETOOTH:
                if(mBluetoothMessaging) ((TextView)mActivity.findViewById(R.id.bt_devices_text)).setText("" + BLUETOOTH_TEXT + msg.obj);
                break;
            default:
                break;
        }
    }

    public void setLANMessaging(boolean lanMessaging){
        mLANMessaging = lanMessaging;
    }

    public void setBluetoothMessaging(boolean bluetoothMessaging){
        mBluetoothMessaging = bluetoothMessaging;
    }
}