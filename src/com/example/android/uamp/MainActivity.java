package com.example.android.uamp;

import org.json.JSONException;

import com.example.testexample.R;

import android.R.integer;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.os.Build;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.CommonDataKinds.Email;

public class MainActivity extends ActionBarActivity {

	  @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
	}

	    private float fX, fY, sX, sY,tX,tY;
	    private float sfX,sfY,ssX,ssY,stX,stY;
	    private int ptrID1, ptrID2;
	    private float mAngle;
	  int fixed_volume=0;
	  int change_volume=0;
	  LinearLayout layout;
	  Display display;
	  Point size;
	  DisplayMetrics metrics;
	  
	  private static final int INVALID_POINTER_ID = -1;

	// The ‘active pointer’ is the one currently moving our object.
	private int mActivePointerId_1 = INVALID_POINTER_ID;
	private int mActivePointerId_2 = INVALID_POINTER_ID;
	  
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        layout=(LinearLayout)findViewById(R.id.layout);
        layout.addView(new CustomView(MainActivity.this));
        
        display = getWindowManager().getDefaultDisplay(); 
        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        size = new Point();
        display.getSize(size);
       
      
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
        	
             return true;
        }
        return super.onOptionsItemSelected(item);
    }

   
    public class CustomView extends View {

        Bitmap mBitmap;
        Paint paint_outer;
        Paint paint_middle,paint_inner;
        Paint paint_volume,paint_mode;
        int mode=0;

        public CustomView(Context context) {
            super(context);
//            mBitmap = Bitmap.createBitmap(400, 800, Bitmap.Config.ARGB_8888);
            paint_outer = new Paint();
            paint_outer.setAntiAlias(true);
            paint_outer.setColor(Color.CYAN);
            paint_outer.setStyle(Style.STROKE);
            
            paint_middle = new Paint();
            paint_middle.setAntiAlias(true);
            paint_middle.setStyle(Style.FILL);
            
            paint_inner = new Paint();
            paint_inner.setAntiAlias(true);
            paint_inner.setColor(Color.BLACK);
            paint_inner.setStyle(Style.FILL);
            
            paint_volume=new Paint();
            paint_volume.setColor(Color.YELLOW);
            paint_volume.setTextSize(20);
            
            paint_mode=new Paint();
            paint_mode.setColor(Color.CYAN);
            paint_mode.setTextSize(50);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            float x_final=0;
            float y_final=0;
            int radius=0;
            
           if(sX!=0.0 || sY!=0.0 || fX!=0.0 || fY!=0.0){
        	   
           	switch(mode){
           	case 2:{
           		TouchPoint(canvas);
           		paint_middle.setColor(Color.CYAN);
           		canvas.drawText("Volume", 10, 50, paint_mode);
           		break;
           	}
           	case 3:{
           		TouchPoint(canvas);
           		paint_middle.setColor(Color.GREEN);
           		canvas.drawText("Bass", 10, 50, paint_mode);
           		break;
           	}
           	case 4:{
           		canvas.drawText("Options", 10, 50, paint_mode);
           		break;
           	}
           	}
           }
           
           if(ptrID1==-1 || ptrID2==-1){
        	   canvas.drawColor(0, Mode.CLEAR);
           }
        }

        public void TouchPoint(Canvas canvas){
        	float x_point=(sX+fX)/2;
        	float y_point=(sY+fY)/2;
        	float y_col=(ssY+sfY)/2;
        	float outer_radius=100;
        	float middle_radius=80;
        	float inner_radius=40;
        	
        	if(mode==3){
        		 x_point=(sX+fX+tX)/3;
            	 y_point=(sY+fY+tY)/3;
            	 y_col=(ssY+sfY+stY)/3;
        	}
        	 canvas.drawCircle(x_point, y_point, 100, paint_outer);
            	canvas.drawCircle(x_point, y_point, 80, paint_middle);
            	canvas.drawCircle(x_point, y_point, 40, paint_inner);
            	canvas.drawLine( 0,y_point, x_point, y_point, paint_middle);
            	
            	change_volume=-1*(int) ((metrics.heightPixels-y_col)-(metrics.heightPixels-(sY+fY)/2))/10;
            	
            	if((ssY+sfY)/2 >(sY+fY)/2){
            		
	            	canvas.drawRect(0,y_point, 35, (ssY+sfY)/2, paint_middle);
	            	
	            	canvas.drawText(String.valueOf(fixed_volume+change_volume<=100?fixed_volume+change_volume<0?fixed_volume=0:fixed_volume+change_volume:"MAX"), 0, y_point-25, paint_volume);
	            	
            	}else{
            		
            		canvas.drawRect(0,y_col ,15, y_point, paint_middle);
		          	canvas.drawText(String.valueOf(fixed_volume+change_volume>=0?fixed_volume+change_volume:"MIN"), 0, y_point+25, paint_volume);
		          	
              	}
            	
        }
        public boolean onTouchEvent(MotionEvent event) {
        	mode=event.getPointerCount();
        	switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                ptrID1 = event.getPointerId(event.getActionIndex());
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                ptrID2 = event.getPointerId(event.getActionIndex());
                sX = event.getX(event.findPointerIndex(ptrID1));
                sY = event.getY(event.findPointerIndex(ptrID1));
                fX = event.getX(event.findPointerIndex(ptrID2));
                fY = event.getY(event.findPointerIndex(ptrID2));
                if(mode==3){
                	int ponterId=-1;
                	switch((ptrID1+ptrID2)){
                	case 1:{
                		ponterId=2;
                		break;
                	}
                	
                	case 2:{
                		ponterId=1;
                		break;
                	}
                	
                	case 3:{
                		ponterId=0;
                		break;
                	}
                	}
                	tX = event.getX(ponterId);
                    tY = event.getY(ponterId);
                    stX=tX;
                    stY=tY;
                }
               ssX=sX;
                ssY=sY;
                sfX=fX;
                sfY=fY;
                
            	
               break;
            case MotionEvent.ACTION_MOVE:
                if(ptrID1 != INVALID_POINTER_ID && ptrID2 != INVALID_POINTER_ID){
                    float nfX, nfY, nsX, nsY;
                    sX = event.getX(event.findPointerIndex(ptrID1));
                    sY = event.getY(event.findPointerIndex(ptrID1));
                   fX = event.getX(event.findPointerIndex(ptrID2));
                    fY = event.getY(event.findPointerIndex(ptrID2));
                    invalidate(); 
                }
                break;
            case MotionEvent.ACTION_UP:
            	if(fixed_volume+change_volume<=100){
            		fixed_volume=fixed_volume+change_volume; 
            	}
            	ptrID1 = INVALID_POINTER_ID;
            	invalidate();
                break;
            case MotionEvent.ACTION_POINTER_UP:
            	ptrID2 = INVALID_POINTER_ID;
                break;
            case MotionEvent.ACTION_CANCEL:
            	ptrID1 = INVALID_POINTER_ID;
                ptrID2 = INVALID_POINTER_ID;
                break;
        }
        	     
            return true;
        }
        
        private int getIndex(MotionEvent event) {
        	 
        	  int idx = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
        	 
        	  return idx;
        }
    }
   
}
