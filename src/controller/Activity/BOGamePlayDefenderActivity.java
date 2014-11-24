package controller.Activity;

import tools.Wifi.WifiApplication;
import view.play.GameDefenderView;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

public class BOGamePlayDefenderActivity extends BOGamePlayActivity {

	public GameDefenderView gameView;
	public WifiApplication app;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		app = (WifiApplication) this.getApplication();
		gameView = new GameDefenderView(this,app);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 设置窗口没有标题
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏显示

		// 获取图片宽高
		/*
		 * WindowManager windowManager = getWindowManager(); Log.i("宽：",
		 * windowManager.getDefaultDisplay().getWidth()+"");
		 * Log.i("高",windowManager.getDefaultDisplay().getHeight()+"");
		 */
		DisplayMetrics aaa = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(aaa);
		int x = aaa.widthPixels;
		int y = aaa.heightPixels;
		// Log.i(x+"", y+"");
		setContentView(gameView);

	}

}
