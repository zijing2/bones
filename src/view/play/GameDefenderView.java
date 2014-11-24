package view.play;

import static global.BOGlobalConst.*;

import global.BOGlobalConst;
import global.BOGlobalUserinfo;

import java.util.Currency;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.google.gson.Gson;

import tools.Socket.SocketClient.ClientMsgListener;
import tools.Socket.SocketServer.ServerMsgListener;
import tools.Wifi.WifiApplication;

import controller.Activity.BOGamePlayAttackerActivity;
import controller.Activity.BOGameReadyActivity;
import controller.Activity.R;
import data.BOSocketMessage;
import android.R.integer;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter.LengthFilter;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

public class GameDefenderView extends SurfaceView implements SurfaceHolder.Callback {

	// 手
	public Bitmap bmpDogPot;
	// 物体
	public Bitmap bmpBone;
	// 游戏主人头像
	public Bitmap bmpAvatarHost;
	// 对手头像
	public Bitmap bmpAvatarGuest;
	// 进攻按钮
	public Bitmap bmpAttackBtn;
	//防守者的手
	public Bitmap bmpDefendHand;

	// 攻击者的手
	public AttackHand attackHand;
	// 目标物
	public Target target;
	// 计时器
	public GameDefenderViewThread gvt;
	// 新建画布
	public Paint paint;
	// 引入Active对象
	public BOGamePlayAttackerActivity boGamePlayAttackerActivity;
	public Point point;
	int dogpaw_x = -1;
	int dogpaw_y = 0;

	public int screen_x;
	public int screen_y;

	public int init_ackhand_x;
	public int init_ackhand_y;
	
	public int hand_x;
	public int hand_y;

	public int touch_x;
	public int touch_y;

	// 是否允许进攻者移动
	boolean is_ready_move = false;
	// 设置圆形透明度
	public int alpha_circle = 240;
	public Canvas canvas;
	// 设置倒计时时间
	public final int GAME_TIME = 60;
	// 当前时间
	public int current_time = GAME_TIME;
	// 点击延迟时间
	public int click_time=2;
	// 设置定时器
	public Timer timer;
	public Timer click_timer;
	// 用户控制进攻者的手进攻速度
	public static int ACK_HAND_SPEED = 15;
	// 进攻者的手返回速度
	public static int ACK_HAND_BACK_SPEED = 120;
	//放弃进攻的手返回速度
	public static int ACK_GIVEUP_BACK_SPEED = ACK_HAND_BACK_SPEED;
	// 目标物体返回速度
	public static int TARGET_BACK_SPEED = ACK_HAND_BACK_SPEED;
	//防守者看到进攻者的蒙层高度
	public static int MASK_AREA_HEIGHT;
	//180度旋转后的狗爪
	public Bitmap bmpDogPot_rot;
	//180度旋转后的骨头
	public Bitmap bmpBone_rot;
	//画布的高
	public static int CANVAS_HEIGHT;
	//画布的宽
	public static int CANVAS_WIDTH;
	public WifiApplication app;
	private Handler serverHandler;
	private Handler clientHandler;
	private Gson gson;
	public Context ct;

	public GameDefenderView(Context context,WifiApplication appk) {
		super(context);
		this.ct = context;
		app = appk;
		initServerHandler();
		initClientHandler();
		initServerListener();
		initClientListener();
		// 设置画笔
		paint = new Paint();
		// 设置回调
		getHolder().addCallback(this);
		initBitmap(context); // 初始化图片资源
		// 进攻者的手
		attackHand = new AttackHand();
		attackHand.setMove_speed(ACK_HAND_SPEED);
		// 目标物
		target = new Target();

		/*
		 * DisplayMetrics displayMetrics = new DisplayMetrics();
		 * boGamePlayAttackerActivity
		 * .getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		 * screen_x = displayMetrics.widthPixels; screen_y =
		 * displayMetrics.heightPixels;
		 * dogpaw_x=screen_x/2-bmpDogPot.getWidth()/2;
		 * dogpaw_y=screen_y-MASK_AREA_HEIGHT;
		 */
		gvt = new GameDefenderViewThread(this, getHolder());// 创建后台刷屏线程
		timer = new Timer();
		// 绑定定时器功能 每一秒执行一次
		timer.schedule(new UpdateTime(), 0, 1 * 1000);
		// gvt.start();
		
		
	}

	// 检测碰撞
	private boolean checkIsCollision() {
		// Log.i("x:",dogpaw_x+"");
		// Log.i("x__:",bmpDogPot.getWidth()/2+"");
		if (attackHand.getPos_y() <= bmpBone.getHeight()) {
			attackHand.setPos_y(bmpBone.getHeight());
			return true;
		}
		return false;
	}

	// 绑定触摸事件
	private class OnTouchListenerImpl implements OnTouchListener {

		@Override
		public boolean onTouch(View arg0, MotionEvent event) {
			if(app.client!=null){
				app.client.sendMsg("aaaa");
			}else if(app.server!=null){
				app.server.sendMsgToAllCLients("aaaa");
			}
			// TODO Auto-generated method stub
		if(DEFEND_STATUS==DEFEND_CLICK_BEFORE){
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				touch_x=(int)event.getX();
				touch_y=(int)event.getY();
				touch_x=(int)touch_x-bmpDefendHand.getWidth()/2;
				touch_y=(int)touch_y-bmpDefendHand.getHeight()/2;
				DEFEND_STATUS=DEFEND_CLICK_AFTER;
				try {
					Thread.sleep(2000);
					DEFEND_STATUS=DEFEND_CLICK_BEFORE;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
				


			} else if (event.getAction() == MotionEvent.ACTION_UP) {

			}
		}
			// Log.i("kkk", "kkk");
			return true;
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		gvt.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		gvt.flag = false;

	}

	// 图片宽高自适应
	public void scaleImageSize(Bitmap bm, Paint paint, Canvas canvas, int x,
			int y) {
		// 获得图片的宽高
		int width = bm.getWidth();
		int height = bm.getHeight();
		// 设置想要的大小
		int newWidth = 80;
		int newHeight = 80;
		// 计算缩放比例
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// 得到新的图片
		Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
				true);
		// 放在画布上
		canvas.drawBitmap(newbm, x, y, paint);
	}
	
	//图像旋转
	public Bitmap Rotation(Bitmap bm, Paint paint, Canvas canvas,int digree){
		 Matrix matrix = new Matrix();
		    //绘制图像
		    canvas.drawBitmap(bm, bm.getWidth(), bm.getHeight(), null);
		    matrix.postRotate(digree);   
		    int width = bm.getWidth();  
		    int height = bm.getHeight(); 
		    Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
		    return newbm;
	}

	// 初始化资源 获取所有图片对象
	public void initBitmap(Context context) {// 方法：初始化图片
		Resources r = context.getResources(); // 获得Resources对象
		bmpDogPot = BitmapFactory.decodeResource(r, R.drawable.dog_paw); // 狗瓜子初始化
		bmpBone = BitmapFactory.decodeResource(r, R.drawable.bone); // 骨头初始化
		bmpAttackBtn = BitmapFactory.decodeResource(r, R.drawable.attack_btn);// 进攻按钮初始化
		bmpAvatarHost = BitmapFactory.decodeResource(r, R.drawable.avater); // 头像初始化
		bmpAvatarGuest = BitmapFactory.decodeResource(r, R.drawable.guest_dog);// 头像初始化
		bmpDefendHand = BitmapFactory.decodeResource(r, R.drawable.hand);// 手初始化
	}

	// 屏幕渲染方法
	protected void doDraw(Canvas canvas) {
		//把手旋转
		bmpDogPot_rot = Rotation(bmpDogPot, null, canvas,180);
		bmpBone_rot = Rotation(bmpBone, paint, canvas, 180) ;

		// ********绘制界面
		// 绘制背景
		canvas.drawColor(Color.rgb(252, 169, 147));
		
		// 绘制剩余时间
		paint.setColor(Color.WHITE);
		paint.setTextSize(40);
		canvas.drawText(current_time + "", canvas.getWidth() / 2, 120, paint);
		
		paint.setColor(Color.BLACK);
		paint.setAlpha(alpha_circle);
		
		if(DEFEND_STATUS==DEFEND_CLICK_AFTER||DEFEND_STATUS==DEFEND_CLICKED_TARGET||DEFEND_STATUS==DEFEND_MISSED_TARGET){
			
			//Log.i(canvas.getWidth()+"",bmpBone.getWidth()/2+"");
			//Log.i(attackHand.getPos_y()+"",touch_y+"");
			if(touch_x+bmpDefendHand.getWidth()>=canvas.getWidth()/2-bmpBone.getWidth()/2&&touch_x<=canvas.getWidth()/2+bmpBone.getWidth()/2
					&&touch_y+bmpDefendHand.getHeight()>=attackHand.getPos_y()&&touch_y<=attackHand.getPos_y()+100    
					){
				
				Log.i("collision","asdasdasd");
			}
			canvas.drawBitmap(bmpDefendHand, touch_x,touch_y, paint);
			//DEFEND_STATUS=DEFEND_CLICK_BEFORE;
			updateDraw(canvas);
			
		}else if(DEFEND_STATUS==DEFEND_CLICK_BEFORE){
			
			updateDraw(canvas);
		}else if(DEFEND_STATUS==DEFEND_CLICK_INIT){
			
			initDraw(canvas);
		}

		
		paint.setAntiAlias(true); // 设置画笔为无锯齿

		alpha_circle = alpha_circle - 10;
		if (alpha_circle <= 80) {
			alpha_circle = 160;
		}
			
		
		// 绘制进攻者点击的按钮
		//canvas.drawBitmap(bmpAttackBtn, init_ackhand_x, init_ackhand_y, null);

		// paint.setARGB(alpha_circle, 0, 0, 0);
		// canvas.drawCircle(dogpaw_x+bmpDogPot.getWidth()/2,
		// dogpaw_y+bmpDogPot.getHeight()/2, 40, paint);
		// 绘制蒙层
		paint.setARGB(100, 0, 0, 0);
		canvas.drawRect(0, 0,
		canvas.getWidth(),MASK_AREA_HEIGHT, paint);
		// 设置头像的大小
		scaleImageSize(bmpAvatarHost, null, canvas, 0, 0);
		scaleImageSize(bmpAvatarGuest, null, canvas, canvas.getWidth() - 80, 0);
		// canvas.drawBitmap(bmpAvatarHost, 0, 0, null);
		// canvas.drawBitmap(bmpAvatarGuest,
		// canvas.getWidth()-bmpAvatarGuest.getWidth() , 0, null);
		postInvalidate();// 重绘图形
		// Log.i("x:"+dogpaw_x,"y:"+dogpaw_y);
		super.setOnTouchListener(new OnTouchListenerImpl());
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);

		canvas.drawColor(Color.rgb(252, 169, 147));
		invalidate();
	}

	// 所有物品回到原来位置
	private void initDraw(Canvas canvas) {
		//初始化画布的长宽
		CANVAS_HEIGHT = canvas.getHeight();
		CANVAS_WIDTH = canvas.getWidth();
		// 定义进攻者的手的初始坐标
		hand_x=init_ackhand_x = canvas.getWidth() / 2 - bmpAttackBtn.getWidth() / 2;
		hand_y=init_ackhand_y = MASK_AREA_HEIGHT -bmpDogPot_rot.getHeight() - 30;//先当信息栏的高为100
		// 设置骨头的位置
		target.setInit_pos_x(canvas.getWidth() / 2 - bmpBone.getWidth() / 2);
		target.setInit_pos_y(MASK_AREA_HEIGHT+CANVAS_HEIGHT/2-bmpDogPot_rot.getHeight()/2);
		
		target.setPos_x(canvas.getWidth() / 2 - bmpBone.getWidth() / 2);
		target.setPos_y(target.getInit_pos_y());
		
		// 设置初始位置
		attackHand.setInit_pos_x(init_ackhand_x);
		attackHand.setInit_pos_y(init_ackhand_y);
		attackHand.setPos_x(init_ackhand_x);
		attackHand.setPos_y(init_ackhand_y);
		// 骨头
		// Log.i(target.getInit_pos_y()+"","------");
		canvas.drawBitmap(bmpBone, target.getInit_pos_x(),
				target.getInit_pos_y(), null);
		
		// 进攻者的手的初始位置
		canvas.drawBitmap(bmpDogPot_rot, attackHand.getInit_pos_x(),
				attackHand.getInit_pos_y(), null);
		//设置进攻者蒙层高度
		MASK_AREA_HEIGHT = canvas.getHeight()/3;
	}

	// canvas根据物品参数变化进行更新
	private void updateDraw(Canvas canvas) {
		if(hand_y<0){
			hand_y=canvas.getHeight();
		}
		 hand_y=hand_y-5;
		 attackHand.setPos_y(hand_y); 
		// 绘制进攻者的手新的位置
		//Log.i(attackHand.getPos_x()+"", attackHand.getPos_y()+"");
		canvas.drawBitmap(bmpDogPot_rot, attackHand.getPos_x(),
				attackHand.getPos_y(), null);
		// 骨头
		canvas.drawBitmap(bmpBone, target.getPos_x(), target.getPos_y(), null);
	}
	
	//sever的监听器
	private void initServerListener() {
		if (app.server == null) {
			return;
		}
		app.server.setMsgListener(new ServerMsgListener() {
			Message msg = null;

			@Override
			public void handlerHotMsg(String hotMsg) {
				msg = serverHandler.obtainMessage();
				msg.obj = hotMsg;
				serverHandler.sendMessage(msg);
			}

			@Override
			public void handlerErorMsg(String errorMsg) {
				// TODO Auto-generated method stub

			}
		});
	}
	//初始化server handler
		private void initServerHandler() {
			if (app.server == null) {
				return;
			}
			Log.i("asdfsadf","asdfasdf");
			serverHandler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					Toast toast = Toast.makeText(ct, "", Toast.LENGTH_SHORT);
					toast.setText("aaaa");
					toast.show();
				}
			};
		}
		
	private void initClientListener() {
		if (app.client == null) {
			return;
		}
		app.client.setMsgListener(new ClientMsgListener() {

			Message msg = null;

			@Override
			public void handlerHotMsg(String hotMsg) {
				msg = clientHandler.obtainMessage();
				msg.obj = hotMsg;
				clientHandler.sendMessage(msg);
			}

			@Override
			public void handlerErorMsg(String errorMsg) {
				// TODO Auto-generated method stub
			}
		});
	}	
		
	//初始化client handler
	private void initClientHandler() {
		if (app.client == null) {
			return;
		}
		//send hello
		String msg = this.structMessage(BOGlobalUserinfo.name, BOGlobalConst.MSG_ACTION_HELLO);
		app.client.sendMsg(msg);		
		clientHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				/**
				 * 连接成功后，显示对方
				 */
				Toast toast = Toast.makeText(ct, "", Toast.LENGTH_SHORT);
				toast.setText("aaaa");
				toast.show();
				/*Log.i("clienthandler", msg.toString());
				String text = (String) msg.obj;
				gson = new Gson();
				BOSocketMessage socketMsg = gson.fromJson(text, BOSocketMessage.class);*/
				
			}
		};
	}
	
	private String structMessage(String name, int action){
		BOSocketMessage msg = new BOSocketMessage();
		msg.setName(name);
		msg.setAction(action);
		gson = new Gson();
		return gson.toJson(msg);
	}

	// 每秒执行一次倒数
	class UpdateTime extends TimerTask {
		public void run() {
			if (current_time > 0) {
				// Log.i(current_time+"", "current");
				current_time--;
			} else {
				// Log.i("游戏时间结束","====");
				timer.cancel();
			}
		}
	}
	
	
}