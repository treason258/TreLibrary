/**
 * 
 */
package com.app.base.service.android;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;


/**
 * 
 * Handler的服务类,对Handler的一些功能进行了简化.
 * 
 * @author tianyu912@yeah.net
 */
public abstract class AbstractHandlerService extends AbstractAndroidService {

	private MyHandler handler;

	public AbstractHandlerService(Activity activity) {
		super(activity);
		this.handler = new MyHandler(activity, this);
	}

	public abstract void handleMessage(Message msg);
	
	/**
	 * 向handler发消息
	 * 
	 * 
	 * @param what
	 */
	public void sendMessage2Handler() {
		Message message = handler.obtainMessage();
		handler.sendMessage(message);
	}
	
	/**
	 * 向handler发消息
	 * 
	 * 
	 * @param what
	 */
	public void sendMessage2Handler(int what) {
		Message message = handler.obtainMessage(what);
		handler.sendMessage(message);
	}

	/**
	 * 向handler发消息
	 * 
	 * 
	 * @param value
	 */
	public void sendMessage2Handler(int what, int arg1) {

		Message message = handler.obtainMessage(what, arg1, arg1);
		handler.sendMessage(message);
	}

	/**
	 * 向handler发消息
	 * 
	 * 
	 * @param value
	 */
	public void sendMessage2Handler(int what, int arg1, long delay) {

		Message message = handler.obtainMessage(what, arg1, arg1);
		handler.sendMessageDelayed(message, delay);
	}

	
	/**
	 * 向handler发消息
	 * 
	 * 
	 * @param value
	 */
	public void sendMessage2Handler(int what, int arg1, Object object) {

		Message message = handler.obtainMessage(what, arg1, arg1, object);
		handler.sendMessage(message);
	}

	/**
	 * 向handler发消息
	 * 
	 * 
	 * @param value
	 */
	public void sendMessage2Handler(int what, Object object) {

		Message message = handler.obtainMessage(what, object);
		handler.sendMessage(message);
	}
	
	private static class MyHandler extends Handler {  
        private final WeakReference<Activity> mActivity;  
  
        private final AbstractHandlerService abstractHandlerService;
        
        
        public MyHandler(Activity activity, AbstractHandlerService abstractHandlerService) {  
            this.mActivity = new WeakReference<Activity>(activity);  
            this.abstractHandlerService = abstractHandlerService;
        }  
  
        @Override  
        public void handleMessage(Message msg) {  
            //System.out.println(msg);  
            if (mActivity.get() == null) {  
                return;  
            }  
            if (abstractHandlerService != null) {
            	abstractHandlerService.handleMessage(msg);
            }
        }  
    } // end MyHandler   

}
