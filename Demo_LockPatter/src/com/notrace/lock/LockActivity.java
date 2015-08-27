package com.notrace.lock;

import java.util.List;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.notrace.BaseActivity;
import com.notrace.R;
import com.notrace.lock.LockPatternView.Cell;
import com.notrace.lock.LockPatternView.DisplayMode;

public class LockActivity extends BaseActivity implements
		LockPatternView.OnPatternListener {

	private List<Cell> lockPattern;
	private LockPatternView lockPatternView;
	private TextView unPass;
	private TextView lock_error;
	private TextView lock_reset;
	private TextView reUser;
	private ImageView imageViewUserLogo;
	// private TextView textName;

	private static int num = 0;
	private String patternString;

	private boolean isReSet = false;
	private boolean isFinish = false;

	private static final int CALL_LOGOUT_SUCCESS = CommonConstants.FIRST_VAL++;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		isOpenLock = false;

		isReSet = this.getIntent().getBooleanExtra("isreset", false);
		isFinish = this.getIntent().getBooleanExtra("isfinish", false);
		Log.d("aa", isReSet + "");
		Log.d("aa", isFinish + "");
		patternString = LockDataKeeper.getLockPass(this);
		if (patternString == null) {
			finish();
			return;
		}
		lockPattern = LockPatternView.stringToPattern(patternString);
		setContentView(R.layout.activity_lock);
		lockPatternView = (LockPatternView) findViewById(R.id.lock_pattern);
		lockPatternView.setOnPatternListener(this);

		unPass = (TextView) findViewById(R.id.unPass);
		lock_error = (TextView) findViewById(R.id.lock_error);
		reUser = (TextView) findViewById(R.id.reUser);
		lock_reset = (TextView) findViewById(R.id.lock_reset);
		imageViewUserLogo = (ImageView) findViewById(R.id.imageViewUserLogo);

		// if (Global.TOKEN_ID != null && Global.indexUserInfoResp != null
		// && Global.indexUserInfoResp.getUserPicUrl() != null) {
		// String imgUrl = Global.indexUserInfoResp.getUserPicUrl();
		// ImageDesc imageDesc = new ImageDesc("1", ImageDesc.TYPE_USER_ICON,
		// imgUrl);
		// Bitmap bitmap = ImageManager.getInstance().getBitmap(imageDesc);
		// if (bitmap != null) {
		// // 头像切成圆形
		// imageViewUserLogo.setImageBitmap(toRoundBitmap(bitmap));
		// }
		// }
		// else {
		// Resources r = this.getResources();
		// Bitmap bitmap = BitmapFactory.decodeResource(r,
		// R.drawable.jyq_icon_login_logo);
		// imageViewUserLogo.setImageBitmap(toRoundBitmap(bitmap));
		// }

		if (isReSet) {
			lock_error.setVisibility(View.GONE);
			reUser.setVisibility(View.GONE);
			lock_reset.setVisibility(View.VISIBLE);
		} else {
			lock_error.setVisibility(View.VISIBLE);
			reUser.setVisibility(View.VISIBLE);
			lock_reset.setVisibility(View.GONE);
		}

		unPass.setOnClickListener(new OnClickListener() {// 忘记密码
			@Override
			public void onClick(View v) {
				showConfirmDialog("忘记密码", "您是否重置密码", "取消", null, "确定",
						new OnClickListener() {
							@Override
							public void onClick(View v) {

								reqLogOut();
							}
						});

			}
		});
		reUser.setOnClickListener(new OnClickListener() {// 更换账号
			@Override
			public void onClick(View v) {
				showConfirmDialog(getString(R.string.lockpattern_changepwd),
						"您是否更换账号", "取消", null, "确定", new OnClickListener() {
							@Override
							public void onClick(View v) {
								logOut();
								reqLogOut();
							}
						});

			}
		});
	}

	public void reqLogOut() {
//		showProgressDialog(false, 0);
//		BaseReqData reqData = new BaseReqData();
//		BaseReq req = new BaseReq(Global.Key_logout, reqData);
//		ProcessManager.getInstance().addProcess(this, req,
//				new ProcessListener() {
//					@Override
//					public boolean onDone(BaseResp responseBean) {
//						// if (responseBean.isOk()) {
//						// runCallFunctionInHandler(CALL_LOGOUT_SUCCESS);
//						// } else {
//						// showToastText(responseBean.getRspInf());
//						// }
//						logOut();
//						return false;
//					}
//				});
	}

	/**
	 * 退出，必须要在退出接口返回的时候才能就行 因为退出接口是异步的， 有可能先清理了数据再调用退出接口，则退出接口会失败
	 */
	public void logOut() {
		CookieSyncManager.createInstance(getApplicationContext());
		CookieManager.getInstance().removeAllCookie();

		// 这个地方修改，当要注销的时候， LockDataKeeper对外部不要提供这么多方法
		LockDataKeeper.setLockPass(LockActivity.this, null);
		LockGlobal.isLogined = false;
		LockDataKeeper.setIsLogin(LockActivity.this, false);

		/** 清除登录状态 */
//		Global.clear();

//		DataKeeper.clear(this);

		/*
		 * tao BaseActivity.exit(LockActivity.this); Intent intent = new
		 * Intent(LockActivity.this, VerificationMobileActivity.class);
		 * intent.putExtra("isfromlock", true); startActivity(intent); finish();
		 */
	}

	private long firstOnClick = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		// disable back key
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (isFinish) {
				finish();
			} else {
				long time = System.currentTimeMillis();
				if (time - firstOnClick > 3000L) {
					Toast.makeText(
							this,
							"再按一次退出"
									+ getResources().getString(
											R.string.app_name),
							Toast.LENGTH_SHORT).show();
					firstOnClick = time;
				} else {
					finish();
					BaseActivity.exit(this);
				}
			}
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onPatternStart() {
	}

	@Override
	public void onPatternCleared() {
	}

	@Override
	public void onPatternCellAdded(List<Cell> pattern) {
	}

	@Override
	public void onPatternDetected(List<Cell> pattern) {

		if (pattern.equals(lockPattern)) {
			num = 0;
			setResult(RESULT_OK);
			finish();
		} else {
			lockPatternView.setDisplayMode(DisplayMode.Wrong);

			lockPatternView.enableInput();
			if (pattern.size() >= 4) {
				num += 1;
			}

			lock_error.setVisibility(View.VISIBLE);
			reUser.setVisibility(View.VISIBLE);
			lock_reset.setVisibility(View.GONE);

			if (num >= 5) {
				logOut();
				reqLogOut();
			} else {
				lock_error.setText("密码输入错误，还可输入" + (5 - num) + "次");
				runCallFunctionInHandler(LOCK_ERROR_RESET_PASS);
			}
		}

	}

	private static int LOCK_ERROR_RESET_PASS = CommonConstants.FIRST_VAL++;

//	public void call(int callID, Object... args) {
//
//	}

	@Override
	public void onClick(View v) {
	}

	@Override
	public void callFuction(int callID, Object... args) {
		if (LOCK_ERROR_RESET_PASS == callID) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			lockPatternView.clearPattern();
		} else if (callID == CALL_LOGOUT_SUCCESS) {
			logOut();
		}
	}



}
