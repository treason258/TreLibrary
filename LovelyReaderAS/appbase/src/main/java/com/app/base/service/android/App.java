/**
 * 
 */
package com.app.base.service.android;

import android.app.Application;
import android.content.res.Resources;


/**
 * 
 * 应用初始化的基类.如果系统不需要任何初始化那么就直接用这个即可.
 * @author root
 * 
 */
public class App extends Application {

	private static App instance;  
    private static Resources resources;
    
    public static App getContext(){  
        return instance;  
    }  
	
    public static Resources getMyResources(){  
        return resources;  
    }  
    
	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		resources = this.getResources();
	}
}