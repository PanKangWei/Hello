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
      
    // ������. @param handler  
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
	            	//���ֲ�������ͣ��������Ч
	            	Toast.makeText(context, "KEYCODE_MEDIA_PLAY_PAUSE", Toast.LENGTH_SHORT).show();
	                break;
	            case KeyEvent.KEYCODE_HEADSETHOOK: // �������м�������
	            	Toast.makeText(context, "�������м�������", Toast.LENGTH_SHORT).show();
	                break;
	            case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
	            	///������һ�ף�������Ч
	            	Toast.makeText(context, "KEYCODE_MEDIA_PREVIOUS", Toast.LENGTH_SHORT).show();
	                break;
	            case KeyEvent.KEYCODE_MEDIA_STOP:
	            	Toast.makeText(context, "KEYCODE_MEDIA_STOP", Toast.LENGTH_SHORT).show();
	                break;
	            case KeyEvent.KEYCODE_MEDIA_NEXT:
	            	//������һ�ף�������Ч
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
		        data.putLong("event_time", event.getEventTime()-event.getDownTime());  //�������µ��ɿ���ʱ��
		        msg.setData(data);  
		        handler.sendMessage(msg);  
		    } 
        }
        //��ֹ�㲥(���ñ�ĳ����յ��˹㲥�����ܸ���)  
        abortBroadcast();  
    }
}
