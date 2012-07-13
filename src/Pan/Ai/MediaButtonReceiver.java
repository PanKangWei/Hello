package Pan.Ai;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

public class MediaButtonReceiver extends BroadcastReceiver {
	 private static final String TAG = "MediaButtonDisabler";

    // Handler   
    private Handler handler;  
      
    // 构造器. @param handler  
    public MediaButtonReceiver(Handler handler) {  
        this.handler = handler;  
    }  
 
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Intercepted media button.");
        Toast.makeText(context, "onReceive", Toast.LENGTH_SHORT).show();
        
/*	        String intentAction = intent.getAction();
	        KeyEvent keyEvent = (KeyEvent) intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
	        int keyCode = keyEvent.getKeyCode();
	        if (Intent.ACTION_MEDIA_BUTTON.equals(intentAction)) {
	            switch (keyCode) {
	            case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
	            	//音乐播放与暂停，蓝牙额效
	            	Toast.makeText(context, "KEYCODE_MEDIA_PLAY_PAUSE", Toast.LENGTH_SHORT).show();
	                break;
	            case KeyEvent.KEYCODE_HEADSETHOOK: // 耳机的中键被按下
	            	Toast.makeText(context, "耳机的中键被按下", Toast.LENGTH_SHORT).show();
	                break;
	            case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
	            	///音乐上一首，蓝牙有效
	            	Toast.makeText(context, "KEYCODE_MEDIA_PREVIOUS", Toast.LENGTH_SHORT).show();
	                break;
	            case KeyEvent.KEYCODE_MEDIA_STOP:
	            	Toast.makeText(context, "KEYCODE_MEDIA_STOP", Toast.LENGTH_SHORT).show();
	                break;
	            case KeyEvent.KEYCODE_MEDIA_NEXT:
	            	//音乐上一首，蓝牙有效
	            	Toast.makeText(context, "KEYCODE_MEDIA_NEXT", Toast.LENGTH_SHORT).show();
	                break;
	            default:
	                break;
	            }
	        }

	        abortBroadcast();
*/
        if( Intent.ACTION_MEDIA_BUTTON.equals(intent.getAction()) )  
        {        
		    KeyEvent event = (KeyEvent)intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);  
		    if(event == null)
		    	return;  
		      
		    if( event.getAction() == KeyEvent.ACTION_UP )
		    {
		        Message msg = Message.obtain();  
		        msg.what = 100;  
		        Bundle data = new Bundle();  
		        data.putInt("key_code", event.getKeyCode() );  
		        data.putLong("event_time", event.getEventTime()-event.getDownTime());  //按键按下到松开的时长
		        msg.setData(data);  
		        handler.sendMessage(msg);  
		    } 
        }
        //终止广播(不让别的程序收到此广播，免受干扰)  
        abortBroadcast();  
    }
}
