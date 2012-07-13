package Pan.Ai;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognizerIntent;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class AiActivity extends Activity {
    /** Called when the activity is first created. */

	//语音识别资源
    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;   
    private ListView mList;   

	//初始化Handler对象  
	MainHandler handler = new MainHandler();
	//初始化媒体(耳机)广播对象.  
	final MediaButtonReceiver mediaButtonReceiver = new MediaButtonReceiver(handler);

	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mList = (ListView) findViewById(R.id.list);   //用 R 里面列表资源 list 进行定义赋值
        
		//初始化语音识别对象
		// Check to see if a recognition activity is present   
        PackageManager pm = getPackageManager();   
        List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);   
        if (activities.size() != 0)   
        {   
			//注册媒体(耳机)广播对象  
			IntentFilter intentFilter = new IntentFilter(Intent.ACTION_MEDIA_BUTTON);
			//简要说明，设置过滤优先级为最高，setPriority的参数为最大值(2^32 – 1 = 2147483647)，这样，就会在第一时间把线控事件的广播中止，使其他应用无法接收到。
			intentFilter.setPriority(Integer.MAX_VALUE);  
			registerReceiver(mediaButtonReceiver, intentFilter);  
        }  
        else   
        {   
        	Toast.makeText(this, "初始化语音识别对象失败!", Toast.LENGTH_SHORT).show();
        }

      
      
        
	}

    private void startVoiceRecognitionActivity()   
    {   
        //通过Intent传递语音识别的模式   
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);   
        //语言模式和自由形式的语音识别   
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);   
        //提示语音开始   
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speech recognition demo");   
        //开始执行我们的Intent、语音识别   
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);   
    }   
    //当语音结束时的回调函数onActivityResult   
    @Override   
    protected void onActivityResult(int requestCode, int resultCode, Intent data)   
    {   
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK)   
        {   
            // 取得语音的字符   
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if((String)"你好" == matches.get(0).toString())
            	Toast.makeText(this, "你好，很高兴认识你！", Toast.LENGTH_LONG).show();
            mList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, matches));   
        }   
  
        super.onActivityResult(requestCode, resultCode, data);   
    }   
	
    @Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
    	//取消注册
    	if(!(null == mediaButtonReceiver))
    			unregisterReceiver(mediaButtonReceiver);
    	
    	super.onDestroy();
	}

	public class MainHandler extends Handler{  
	   @Override  
	   public void handleMessage(Message msg) {  
	       int what = msg.what;  
	       switch(what){  
	       case 100://单击按键广播  
	           Bundle data = msg.getData();  
	           //按键值  
	           int keyCode = data.getInt("key_code");  
	           //按键时长  
	           long eventTime = data.getLong("event_time");  
	           //设置超过2000毫秒，就触发长按事件  
	           boolean isLongPress = (eventTime>2000);  

/*	            switch (keyCode) {
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
	            case KeyEvent.KEYCODE_MEDIA_NEXT:
	            	//音乐上一首，蓝牙有效
	            	Toast.makeText(context, "KEYCODE_MEDIA_NEXT", Toast.LENGTH_SHORT).show();
	                break;
	            case KeyEvent.KEYCODE_MEDIA_STOP:
	            	Toast.makeText(context, "KEYCODE_MEDIA_STOP", Toast.LENGTH_SHORT).show();
	                break;
	            default:
	                break;
	            }
*/	           
	           switch(keyCode){  
	           case KeyEvent.KEYCODE_HEADSETHOOK://耳机的中键被按下，播放或暂停  
	        	   Toast.makeText(AiActivity.this, "耳机的中键被按下", Toast.LENGTH_SHORT).show();
	        	   startVoiceRecognitionActivity();
	        	   break;
	           case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE://音乐播放与暂停，蓝牙有效  
	        	   startVoiceRecognitionActivity();
	        	   Toast.makeText(AiActivity.this, "KEYCODE_MEDIA_PLAY_PAUSE", Toast.LENGTH_SHORT).show();
	               break;  
	           //短按=播放下一首音乐，长按=当前音乐快进  
	           case KeyEvent.KEYCODE_MEDIA_NEXT:  
	               if(isLongPress){  
	            	   Toast.makeText(AiActivity.this, "KEYCODE_MEDIA_NEXT 短按", Toast.LENGTH_SHORT).show();//自定义 
	               }else{  
	            	   Toast.makeText(AiActivity.this, "KEYCODE_MEDIA_NEXT 长按", Toast.LENGTH_SHORT).show();//自定义  
	            	   startVoiceRecognitionActivity();
	               }  
	               break;  
	                 
	           //短按=播放上一首音乐，长按=当前音乐快退    
	           case KeyEvent.KEYCODE_MEDIA_PREVIOUS:  
	               if(isLongPress){  
	            	   Toast.makeText(AiActivity.this, "KEYCODE_MEDIA_PREVIOUS 短按", Toast.LENGTH_SHORT).show();//自定义  
	               }else{  
	            	   Toast.makeText(AiActivity.this, "KEYCODE_MEDIA_PREVIOUS 长按", Toast.LENGTH_SHORT).show();//自定义//自定义  
	               }  
	               break;  
	           }  
	             
	           break;  
	       default://其他消息-则扔回上层处理  
	           super.handleMessage(msg);  
	       }  
	   }  
	}    
}