package com.notrace.lock;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.notrace.BaseActivity;
import com.notrace.R;
import com.notrace.lock.LockPatternView.Cell;
import com.notrace.lock.LockPatternView.DisplayMode;

public class LockSetupActivity extends BaseActivity implements LockPatternView.OnPatternListener,
		OnClickListener
{
	private static int LOCK_SETUP_RESET_PASS = CommonConstants.FIRST_VAL++;
	private LockPatternView lockPatternView;
	private TextView lock_title;

	private static final int STEP_1 = 1;
	private static final int STEP_2 = 2;
	private static final int STEP_3 = 3;
	private static final int STEP_4 = 4;
	// private static final int SETP_5 = 4;

	private int step;

	private List<Cell> choosePattern;

	private boolean confirm = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lock_setup);

		lock_title = (TextView) findViewById(R.id.lock_title);
		lockPatternView = (LockPatternView) findViewById(R.id.lock_pattern);
		lockPatternView.setOnPatternListener(this);
		isOpenLock = false;

		step = STEP_1;
		updateView();
	}

	private void updateView() {
		switch (step) {
		case STEP_1:
			lock_title.setText("设置解锁图案");
			choosePattern = null;
			confirm = false;
			lockPatternView.clearPattern();
			lockPatternView.enableInput();
			break;
		case STEP_2:
			lockPatternView.disableInput();
			step = STEP_3;
			updateView();
			break;
		case STEP_3:
			lock_title.setText("再次设置解锁图案");
			lockPatternView.clearPattern();
			lockPatternView.enableInput();
			break;
		case STEP_4:
			if (confirm) {
				lockPatternView.disableInput();
				LockDataKeeper.setLockPass(this, LockPatternView.patternToString(choosePattern));
				setResult(RESULT_OK);
				finish();
			}
			else {
				lockPatternView.setDisplayMode(DisplayMode.Wrong);
				lockPatternView.enableInput();
				showToastText(getString(R.string.lockpattern_confirm_error));
				new Thread(runnable).start();
			}

			break;

		default:
			break;
		}
	}

	private Runnable runnable = new Runnable() {
		public void run() {
			try {
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			runCallFunctionInHandler(LOCK_SETUP_RESET_PASS);
		}
	};
//
//	public void call(int callID, Object... args) {
//		if (LOCK_SETUP_RESET_PASS == callID) {
//			step = STEP_1;
//			updateView();
//		}
//	}

	@Override
	public void onPatternStart() {}

	@Override
	public void onPatternCleared() {}

	@Override
	public void onPatternCellAdded(List<Cell> pattern) {}

	@Override
	public void onPatternDetected(List<Cell> pattern) {

		if (pattern.size() < LockPatternView.MIN_LOCK_PATTERN_SIZE) {
			Toast.makeText(this, R.string.lockpattern_recording_incorrect_too_short,
					Toast.LENGTH_LONG).show();
			lockPatternView.setDisplayMode(DisplayMode.Wrong);
			step = STEP_1;
			updateView();
			return;
		}

		if (choosePattern == null) {
			choosePattern = new ArrayList<Cell>(pattern);
			step = STEP_2;
			updateView();
			return;
		}

		if (choosePattern.equals(pattern)) {

			confirm = true;
		}
		else {
			confirm = false;
		}

		step = STEP_4;
		updateView();

	}

	@Override
	public void onClick(View v) {

	}

	@Override
	public void callFuction(int callID, Object... args) {
		if (LOCK_SETUP_RESET_PASS == callID) {
			step = STEP_1;
			updateView();
		}
	}


}
