package view.play;

import static global.BOGlobalConst.*;

import global.BOGlobalConst;
import global.BOGlobalUserinfo;

import java.util.Currency;
import java.util.Timer;
import java.util.TimerTask;

import tools.Socket.SocketClient.ClientMsgListener;
import tools.Socket.SocketServer.ServerMsgListener;
import tools.Wifi.WifiApplication;

import com.google.gson.Gson;

import controller.Activity.BOGamePlayAttackerActivity;
import controller.Activity.R;
import data.BOSocketMessage;
import android.R.integer;
import android.content.Context;
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
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

public class GameAttackerView extends SurfaceView implements SurfaceHolder.Callback {

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

	// 攻击者的手
	public AttackHand attackHand;
	// 目标物
	public Target target;
	// 计时器
	public GameAttackerViewThread gvt;
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
	// 设置定时器
	public Timer timer;
	// 用户控制进攻者的手进攻速度
	public static int ACK_HAND_SPEED;
	// 进攻者的手返回速度
	public static int ACK_HAND_BACK_SPEED;
	//放弃进攻的手返回速度
	public static int ACK_GIVEUP_BACK_SPEED;
	// 目标物体返回速度
	public static int TARGET_BACK_SPEED;
	//进攻者蒙层高度
	public static int MASK_AREA_HEIGHT;
	//画布的高
	public static int CANVAS_HEIGHT;
	//画布的宽
	public static int CANVAS_WIDTH;
	
	private WifiApplication app;
	private Handler serverHandler;
	private Handler clientHandler;
	private Gson gson;
	public Context ct;
	

	public GameAttackerView(Context context,WifiApplication appk) {
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
		gvt = new GameAttackerViewThread(this, getHolder());// 创建后台刷屏线程
		timer = new Timer();
		// 绑定定时器功能 每一秒执行一次
		timer.schedule(new UpdateTime(), 0, 1 * 1000);
		// gvt.start();
	}

	// 检测碰撞
	private boolean checkIsCollision() {
		// Log.i("x:",dogpaw_x+"");
		// Log.i("x__:",bmpDogPot.getWidth()/2+"");
		/*if (attackHand.getPos_y() <= bmpBone.getHeight()) {
			attackHand.setPos_y(bmpBone.getHeight());
			return true;
		}*/
		if(attackHand.getPos_y() <= target.getPos_y()){
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
			// 如果进攻状态不是在拿完目标物之后的状态才开始监听
			if (ATTACK_STATUS != GOING_BACK&&ATTACK_STATUS!=GIVEUP_ATTACK) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					touch_x = (int) event.getX();
					touch_y = (int) event.getY();
					if (touch_x >= attackHand.getInit_pos_x()
							&& touch_x <= attackHand.getInit_pos_y()
									+ bmpAttackBtn.getWidth()
							&& touch_y >= CANVAS_HEIGHT - bmpAttackBtn.getHeight()
							&& touch_y <= CANVAS_HEIGHT) {
						// 只有用户首先点击在控制图标范围内才允许手移动
						is_ready_move = true;
						attackHand.setPos_x(attackHand.getInit_pos_x());
						attackHand.setPos_y(attackHand.getInit_pos_y());
						// Log.i("init_dogpaw_y",init_dogpaw_y+"");
					}
				} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
					/*
					 * if(is_ready_move){
					 * dogpaw_x=(int)event.getX()-bmpDogPot.getWidth()/2;
					 * dogpaw_y=(int)event.getY()-bmpDogPot.getHeight()/2;
					 * checkIsCollision();
					 * 
					 * }
					 */
					if (is_ready_move) {
						// dogpaw_x=dogpaw_x+20;
						ATTACK_STATUS=ATTACK_MOVING;
						attackHand.setPos_y(attackHand.getPos_y()
								- attackHand.getMove_speed());
						// Log.i("ackbtn_pos_y",ackbtn_pos_y+"");
					}
					if (checkIsCollision()) {
//						Log.i("good", "win!!");
						ATTACK_STATUS = GOING_BACK;
						is_ready_move = false;
					}

					// Log.i("==========x:"+dogpaw_x,"y:"+dogpaw_y);
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					is_ready_move = false;
					if(ATTACK_STATUS!=GOING_BACK){
						ATTACK_STATUS=GIVEUP_ATTACK;
					}
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

	// 初始化资源 获取所有图片对象
	public void initBitmap(Context context) {// 方法：初始化图片
		Resources r = context.getResources(); // 获得Resources对象
		bmpDogPot = BitmapFactory.decodeResource(r, R.drawable.dog_paw); // 狗瓜子初始化
		bmpBone = BitmapFactory.decodeResource(r, R.drawable.bone); // 骨头初始化
		bmpAttackBtn = BitmapFactory.decodeResource(r, R.drawable.attack_btn);// 进攻按钮初始化
		bmpAvatarHost = BitmapFactory.decodeResource(r, R.drawable.avater); // 头像初始化
		bmpAvatarGuest = BitmapFactory.decodeResource(r, R.drawable.guest_dog);// 头像初始化
	}

	// 屏幕渲染方法
	protected void doDraw(Canvas canvas) {

		// ********绘制界面
		// 绘制背景
		canvas.drawColor(Color.rgb(252, 169, 147));
		
		// 设置头像的大小
		scaleImageSize(bmpAvatarHost, null, canvas, 0, 0);
		scaleImageSize(bmpAvatarGuest, null, canvas, canvas.getWidth() - 80, 0);
		// 绘制剩余时间
		paint.setColor(Color.WHITE);
		paint.setTextSize(40);
		canvas.drawText(current_time + "", canvas.getWidth() / 2, 120, paint);

		// 进攻者的手
		if (ATTACK_STATUS == GOING_BACK || ATTACK_STATUS == ATTACK_MOVING||ATTACK_STATUS==GIVEUP_ATTACK
				) {
			// 如果进攻状态为获取完目标物返回，则做一个手与目标物一起返回的动画
			if (ATTACK_STATUS == GOING_BACK) {
				attackHand.setPos_y(attackHand.getPos_y() + ACK_HAND_BACK_SPEED);
				target.setPos_y(target.getPos_y() + TARGET_BACK_SPEED);
				// 如果手和骨头回到原点则把进攻者状态置回开始
				if (attackHand.getPos_y() >= attackHand.getInit_pos_y()) {
					ATTACK_STATUS = ATTACK_READY;
				}
			}else if(ATTACK_STATUS==GIVEUP_ATTACK){
				
				//设置进攻的手返回
				attackHand.setPos_y(attackHand.getPos_y() + ACK_GIVEUP_BACK_SPEED);
				// 如果手和骨头回到原点则把进攻者状态置回开始
				if (attackHand.getPos_y() >= attackHand.getInit_pos_y()) {
					ATTACK_STATUS = ATTACK_READY;
				}
			}
			updateDraw(canvas);
		} else if (ATTACK_STATUS == ATTACK_READY) {
			initDraw(canvas);
		}

		// 绘制蒙层
		paint.setAntiAlias(true); // 设置画笔为无锯齿

		alpha_circle = alpha_circle - 10;
		if (alpha_circle <= 80) {
			alpha_circle = 160;
		}

		//进攻者蒙层
		paint.setARGB(100, 0, 0, 0);
		canvas.drawRect(0, canvas.getHeight() - MASK_AREA_HEIGHT,
				canvas.getWidth(), canvas.getHeight(), paint);
		// 绘制进攻者点击的按钮
		canvas.drawBitmap(bmpAttackBtn, init_ackhand_x, canvas.getHeight()-bmpAttackBtn.getHeight(), null);

		// paint.setARGB(alpha_circle, 0, 0, 0);
		// canvas.drawCircle(dogpaw_x+bmpDogPot.getWidth()/2,
		// dogpaw_y+bmpDogPot.getHeight()/2, 40, paint);
		

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
		init_ackhand_x = canvas.getWidth() / 2 - bmpAttackBtn.getWidth() / 2;
		init_ackhand_y = canvas.getHeight() - MASK_AREA_HEIGHT + 30;
		// 设置骨头的位置
		target.setInit_pos_x(canvas.getWidth() / 2 - bmpBone.getWidth() / 2);
		target.setInit_pos_y(CANVAS_HEIGHT-MASK_AREA_HEIGHT-CANVAS_HEIGHT/2);
		
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
		canvas.drawBitmap(bmpDogPot, attackHand.getInit_pos_x(),
				attackHand.getInit_pos_y(), null);
		
		//设置进攻者蒙层高度
		MASK_AREA_HEIGHT = canvas.getHeight()/3;
		
		//设置进攻者的手伸出速度,因为要适应每个屏幕的大小所以只能在拿到canvas后设置
		ACK_HAND_SPEED = canvas.getHeight()/80;
		attackHand.setMove_speed(ACK_HAND_SPEED);
		//设置进攻者与物体返回的速度
		ACK_HAND_BACK_SPEED = canvas.getHeight()/15;
		ACK_GIVEUP_BACK_SPEED = ACK_HAND_BACK_SPEED;
		TARGET_BACK_SPEED = ACK_HAND_BACK_SPEED;
		
	}

	// canvas根据物品参数变化进行更新
	private void updateDraw(Canvas canvas) {
		// 绘制进攻者的手新的位置
		//Log.i(attackHand.getPos_x()+"", attackHand.getPos_y()+"");
		canvas.drawBitmap(bmpDogPot, attackHand.getPos_x(),
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