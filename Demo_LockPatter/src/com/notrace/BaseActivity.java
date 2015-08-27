package com.notrace;

import java.util.LinkedList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.notrace.lock.CommonConstants;
import com.notrace.lock.LockActivity;
import com.notrace.lock.LockDataKeeper;
import com.notrace.lock.LockGlobal;



/**
 * 基本Activity类
 * 
 * 
 */
public abstract class BaseActivity extends Activity implements OnClickListener
{
	
	public static int BASEVALUE =10000;
	
	/** 所有打开的Activity */
	public static LinkedList<Activity> activityList;
	public static LinkedList<Activity> getActivityList() {
		if (activityList == null) {
			activityList = new LinkedList<Activity>();
		}
		return activityList;
	}

	public static final String LOCK = "lock";
	public static final String LOCK_KEY = "lock_key";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActivityList().add(this);
	}
	@Override
	public void onClick(View v) {
		
	}

	protected boolean isOpenLock = true;

	@Override
	protected void onResume() {
		super.onResume();
		LockGlobal.endTime = System.currentTimeMillis();
		if (isOpenLock && LockGlobal.endTime - LockGlobal.startTime > 60000L) {
			
			if (LockDataKeeper.getLockPass(this) != null) {
				Intent intent = new Intent(this, LockActivity.class);
				startActivity(intent);
			}
		}

	}
	
	 	// 在UI线程中运行call函数，一般call函数由子类复写，callID将作为call函数的参
	public void runCallFunctionInHandler(int callID, Object... args) {
		sendMessageToHanler(CommonConstants.WHAT_CALL_FUNCTION, args, callID);
	}
	
	public abstract void callFuction(int callID, Object... args);
	
	// 消息发函数
		public void sendMessageToHanler(int what, int arg1) {
			Message message = mHandler.obtainMessage();
			message.what = what;
			message.arg1 = arg1;
			message.sendToTarget();
		}

		// 消息发函数
		public void sendMessageToHanler(int what) {
			Message message = mHandler.obtainMessage();
			message.what = what;
			message.sendToTarget();
		}

		// 消息发函数
		public void sendMessageToHanler(int what, Object obj) {
			Message message = mHandler.obtainMessage();
			message.what = what;
			message.obj = obj;
			message.sendToTarget();
		}

		// 消息发函数
		public void sendMessageToHanler(int what, Object obj, int arg1) {
			Message message = mHandler.obtainMessage();
			message.what = what;
			message.obj = obj;
			message.arg1 = arg1;
			message.sendToTarget();
		}
	
	
	protected Handler mHandler=new Handler(){
		public void handleMessage(Message msg) {
			if(msg.what==CommonConstants.WHAT_CALL_FUNCTION){
				if (msg.obj == null) {
					callFuction(msg.arg1);
				}
				else {
					callFuction(msg.arg1, (Object[]) msg.obj);
				}
			}
			
			else if (msg.what == CommonConstants.WHAT_SHOW_TOAST_TEXT) {
				String info_toast_text = (String) msg.obj;
				Toast.makeText(BaseActivity.this, info_toast_text, Toast.LENGTH_SHORT).show();
			}
			
		};
	};
	
	
	public void showToastText(String text) {
		sendMessageToHanler(CommonConstants.WHAT_SHOW_TOAST_TEXT, text);
	}

	public void showConfirmDialog(String title, String message, String positive,
			final View.OnClickListener positiveListener, String negative,
			final View.OnClickListener negativeListener, boolean cancel) {

		final AlertDialog dlg = new AlertDialog.Builder(this).create();
		dlg.setCanceledOnTouchOutside(false);
		dlg.setCancelable(cancel);
		dlg.show();
		Window window = dlg.getWindow();
		window.setContentView(R.layout.layout_messgae_confirm);
		TextView textViewTitle = (TextView) window.findViewById(R.id.title);
		TextView textViewMessage = (TextView) window.findViewById(R.id.message);
		LinearLayout positiveButton = (LinearLayout) window.findViewById(R.id.positiveButton);
		LinearLayout negativeButton = (LinearLayout) window.findViewById(R.id.negativeButton);

		textViewMessage.setText(message);
		textViewTitle.setText(title);

		TextView positiveTextView = (TextView) window.findViewById(R.id.positiveTextView);
		TextView negativeTextView = (TextView) window.findViewById(R.id.negativeTextView);

		positiveTextView.setText(positive);
		positiveButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (positiveListener != null) {
					positiveListener.onClick(v);
				}
				dlg.dismiss();

			}
		});

		negativeTextView.setText(negative);
		negativeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (negativeListener != null) {
					negativeListener.onClick(v);
				}
				dlg.dismiss();
			}
		}

		);
	}

	public void showConfirmDialog(String title, String message, String positive,
			final View.OnClickListener positiveListener, String negative,
			final View.OnClickListener negativeListener) {
		showConfirmDialog(title, message, positive, positiveListener, negative, negativeListener,
				true);
	}

	public static void logOut(Context context) {
	}
	
	
	/**
	 * 用户退出
	 * @param context
	 */
	public static void exit(Context context) {
		try {
			
			if (activityList != null) {
				for (Activity activity : activityList) {
					activity.finish();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}