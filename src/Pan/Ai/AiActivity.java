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

	//����ʶ����Դ
    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;   
    private ListView mList;   

	//��ʼ��Handler����  
	MainHandler handler = new MainHandler();
	//��ʼ��ý��(����)�㲥����.  
	final MediaButtonReceiver mediaButtonReceiver = new MediaButtonReceiver(handler);

	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mList = (ListView) findViewById(R.id.list);   //�� R �����б���Դ list ���ж��帳ֵ
        
		//��ʼ������ʶ�����
		// Check to see if a recognition activity is present   
        PackageManager pm = getPackageManager();   
        List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);   
        if (activities.size() != 0)   
        {   
			//ע��ý��(����)�㲥����  
			IntentFilter intentFilter = new IntentFilter(Intent.ACTION_MEDIA_BUTTON);
			//��Ҫ˵�������ù������ȼ�Ϊ��ߣ�setPriority�Ĳ���Ϊ���ֵ(2^32 �C 1 = 2147483647)���������ͻ��ڵ�һʱ����߿��¼��Ĺ㲥��ֹ��ʹ����Ӧ���޷����յ���
			intentFilter.setPriority(Integer.MAX_VALUE);  
			registerReceiver(mediaButtonReceiver, intentFilter);  
        }  
        else   
        {   
        	Toast.makeText(this, "��ʼ������ʶ�����ʧ��!", Toast.LENGTH_SHORT).show();
        }

      
      
        
	}

    private void startVoiceRecognitionActivity()   
    {   
        //ͨ��Intent��������ʶ���ģʽ   
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);   
        //����ģʽ��������ʽ������ʶ��   
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);   
        //��ʾ������ʼ   
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speech recognition demo");   
        //��ʼִ�����ǵ�Intent������ʶ��   
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);   
    }   
    //����������ʱ�Ļص�����onActivityResult   
    @Override   
    protected void onActivityResult(int requestCode, int resultCode, Intent data)   
    {   
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK)   
        {   
            // ȡ���������ַ�   
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if((String)"���" == matches.get(0).toString())
            	Toast.makeText(this, "��ã��ܸ�����ʶ�㣡", Toast.LENGTH_LONG).show();
            mList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, matches));   
        }   
  
        super.onActivityResult(requestCode, resultCode, data);   
    }   
	
    @Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
    	//ȡ��ע��
    	if(!(null == mediaButtonReceiver))
    			unregisterReceiver(mediaButtonReceiver);
    	
    	super.onDestroy();
	}

	public class MainHandler extends Handler{  
	   @Override  
	   public void handleMessage(Message msg) {  
	       int what = msg.what;  
	       switch(what){  
	       case 100://���������㲥  
	           Bundle data = msg.getData();  
	           //����ֵ  
	           int keyCode = data.getInt("key_code");  
	           //����ʱ��  
	           long eventTime = data.getLong("event_time");  
	           //���ó���2000���룬�ʹ��������¼�  
	           boolean isLongPress = (eventTime>2000);  

/*	            switch (keyCode) {
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
	            case KeyEvent.KEYCODE_MEDIA_NEXT:
	            	//������һ�ף�������Ч
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
	           case KeyEvent.KEYCODE_HEADSETHOOK://�������м������£����Ż���ͣ  
	        	   Toast.makeText(AiActivity.this, "�������м�������", Toast.LENGTH_SHORT).show();
	        	   startVoiceRecognitionActivity();
	        	   break;
	           case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE://���ֲ�������ͣ��������Ч  
	        	   startVoiceRecognitionActivity();
	        	   Toast.makeText(AiActivity.this, "KEYCODE_MEDIA_PLAY_PAUSE", Toast.LENGTH_SHORT).show();
	               break;  
	           //�̰�=������һ�����֣�����=��ǰ���ֿ��  
	           case KeyEvent.KEYCODE_MEDIA_NEXT:  
	               if(isLongPress){  
	            	   Toast.makeText(AiActivity.this, "KEYCODE_MEDIA_NEXT �̰�", Toast.LENGTH_SHORT).show();//�Զ��� 
	               }else{  
	            	   Toast.makeText(AiActivity.this, "KEYCODE_MEDIA_NEXT ����", Toast.LENGTH_SHORT).show();//�Զ���  
	            	   startVoiceRecognitionActivity();
	               }  
	               break;  
	                 
	           //�̰�=������һ�����֣�����=��ǰ���ֿ���    
	           case KeyEvent.KEYCODE_MEDIA_PREVIOUS:  
	               if(isLongPress){  
	            	   Toast.makeText(AiActivity.this, "KEYCODE_MEDIA_PREVIOUS �̰�", Toast.LENGTH_SHORT).show();//�Զ���  
	               }else{  
	            	   Toast.makeText(AiActivity.this, "KEYCODE_MEDIA_PREVIOUS ����", Toast.LENGTH_SHORT).show();//�Զ���//�Զ���  
	               }  
	               break;  
	           }  
	             
	           break;  
	       default://������Ϣ-���ӻ��ϲ㴦��  
	           super.handleMessage(msg);  
	       }  
	   }  
	}    
}