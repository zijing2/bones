package controller.Activity;

import java.util.List;

import global.BOGlobalConst;
import global.BOGlobalUserinfo;
import tools.Socket.SocketClient;
import tools.Socket.SocketServer;
import tools.Socket.SocketServer.ServerMsgListener;

import tools.Socket.SocketClient.ClientMsgListener;
import com.google.gson.Gson;

import tools.Wifi.WifiApplication;
import tools.Wifi.WifiHotManager;
import controller.BOActivityAbstract;
import data.BOSocketMessage;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

//准备游戏
public class BOGameReadyActivity extends BOActivityAbstract {

	private static final String TAG = "BOGameReadyActivity";
	private WifiApplication app;
	private Handler serverHandler;
	private Button readyBtn;
	private Button startBtn;
	private Handler clientHandler;
	private Gson gson;
	private TextView otherView;
	private TextView yourselfView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i("test ready", "onCreate");
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_ready);		
		app = (WifiApplication) this.getApplication();
		yourselfView = (TextView) findViewById(R.id.ready_host);
		otherView = (TextView) findViewById(R.id.ready_guest);	
		yourselfView.setText(BOGlobalUserinfo.name);
			
		readyBtn = (Button)findViewById(R.id.ready_ready);
		startBtn = (Button)findViewById(R.id.ready_start);
		
		readyBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String msg = structMessage(BOGlobalUserinfo.name, BOGlobalConst.MSG_ACTION_READY);
				app.client.sendMsg(msg);
				readyBtn.setEnabled(false);
			}
		});
		
		startBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String msg = structMessage(BOGlobalUserinfo.name, BOGlobalConst.MSG_ACTION_START);
				app.server.sendMsgToAllCLients(msg);
				gotoPlayActivity();
			}
		});
		initServerHandler();
		initClientHandler();
		initServerListener();
		initClientListener();
	}
	
	//初始化server handler
	private void initServerHandler() {
		if (app.server == null) {
			return;
		}
		serverHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				/**
				 * 连接成功后，显示对方
				 */
//				ActivityManager am= (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//		        List<RunningTaskInfo> runningTasks = am.getRunningTasks(1);
//		        RunningTaskInfo rti = runningTasks.get(0);
//		        ComponentName component = rti.topActivity;
//				if(component.getClassName() != BOGameReadyActivity.class.getName()){
//					Intent intent = new Intent(BOGameReadyActivity.this, BOGameReadyActivity.class);
//					intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//					intent.setClass(BOGameReadyActivity.this, BOGameReadyActivity.class);
//					startActivity(intent);
//				}
                
				String text = (String) msg.obj;
				gson = new Gson();
				Log.i("gson", text);
				if(text != BOGlobalConst.INT_SERVER_SUCCESS && text != BOGlobalConst.INT_SERVER_FAIL){
				BOSocketMessage socketMsg = gson.fromJson(text, BOSocketMessage.class);
				
				switch(socketMsg.getAction()){				
				case BOGlobalConst.MSG_ACTION_HELLO:
					otherView.setText(socketMsg.getName());
					app.server.sendMsgToAllCLients(structMessage(BOGlobalUserinfo.name, BOGlobalConst.MSG_ACTION_HELLO));
					break;
				case BOGlobalConst.MSG_ACTION_READY:
					startBtn.setEnabled(true);
					break;
				case BOGlobalConst.MSG_ACTION_START:
					break;
				case BOGlobalConst.MSG_ACTION_LEAVE:
					app.server.clearAllClient();
					otherView.setText("等待其他玩家中...");
					startBtn.setEnabled(false);
					break;
					}
				}
			}
		};
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
				Log.i(TAG, "into initClientHandler() handleMessage(Message msg)");
				Log.i("clienthandler", msg.toString());
				String text = (String) msg.obj;
				gson = new Gson();
				BOSocketMessage socketMsg = gson.fromJson(text, BOSocketMessage.class);
				switch(socketMsg.getAction()){				
				case BOGlobalConst.MSG_ACTION_HELLO:
					otherView.setText(socketMsg.getName());
					break;
				case BOGlobalConst.MSG_ACTION_READY:
					break;
				case BOGlobalConst.MSG_ACTION_START:
					gotoPlayActivity();
					break;
				case BOGlobalConst.MSG_ACTION_LEAVE:
					app.client.stopAcceptMessage();
					app.client.clearClient();
					app.client = null;
					app.wifiHotM.deleteMoreCon(app.SSID);
					gotoConnActivity();
					break;
				}
				
			}
		};
	}
		
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
	
	@Override
	protected void onDestroy() {
		if(app.server != null){
			app.server.stopListner();
			app.server.clearServer();
			app.server = null;
			app.wifiHotM.disableWifiHot();
		}
		if(app.client != null){
			app.client.stopAcceptMessage();
			app.client.clearClient();
			app.client = null;
			app.wifiHotM.deleteMoreCon(app.SSID);
		}
		super.onDestroy();
	}
	
	protected void onStop(){
		Log.i(TAG, "onstop");
		super.onStop();
	}
	
	protected void onPause(){
		Log.i(TAG, "onpause");
		super.onPause();
	}

	protected void onResume(){
		Log.i(TAG, "onResume");
		if(app.client != null){
			readyBtn.setVisibility(0);
			startBtn.setVisibility(8);
		}else if(app.server != null){
			startBtn.setEnabled(false);
			startBtn.setVisibility(0);
			readyBtn.setVisibility(8);
		}
		super.onResume();
	}
	
	// 监听返回键，退出程序
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Log.i(TAG, "into onBackPressed()");
			if(app.server != null){
				//server离开发送离开的消息到所有client，所有的client接收到此信息回到connect Activity
		    String msg = structMessage(BOGlobalUserinfo.name, BOGlobalConst.MSG_ACTION_LEAVE);
		    app.server.sendMsgToAllCLients(msg);
			app.server.stopListner();
			app.server.clearServer();
			app.server = null;
			app.wifiHotM.disableWifiHot();
			SocketServer.destoryInstance();
			}			
			if(app.client != null){
				//client发送离开信息给server，server接口道信息后更改Ready Activity的状态
				String msg = structMessage(BOGlobalUserinfo.name, BOGlobalConst.MSG_ACTION_LEAVE);
				app.client.sendMsg(msg);
				
				app.client.stopAcceptMessage();
				app.client.clearClient();
				app.client = null;
				app.wifiHotM.deleteMoreCon(app.SSID);
				SocketClient.destoryInstance();
			}
			this.finish();
			Log.i(TAG, "out onBackPressed()");
			return true;
		}
		return true;
	}
	
	private String structMessage(String name, int action){
		BOSocketMessage msg = new BOSocketMessage();
		msg.setName(name);
		msg.setAction(action);
		gson = new Gson();
		return gson.toJson(msg);
	}
	
	/**
	 * 规则：房主先攻，客人先守
	 * 所以这个时候房主进攻的页面，客人进守的页面
	 */
	private void gotoPlayActivity(){
		Intent intent = new Intent();
		if(app.server != null){
			intent.setClass(this, BOGamePlayAttackerActivity.class);
		}else if (app.client != null){
			intent.setClass(this, BOGamePlayDefenderActivity.class);
		}else{
			Toast.makeText(BOGameReadyActivity.this, "socket未连接", Toast.LENGTH_SHORT).show();
			return;
		}
		this.startActivity(intent);
	}
	private void gotoConnActivity(){
		Intent intent = new Intent();
		intent.setClass(this, BOGameConnectActivity.class);
		this.startActivity(intent);
	}
}