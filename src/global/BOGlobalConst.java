package global;

import android.R.integer;

public class BOGlobalConst {
	
	public static String INT_SERVER_FAIL = "INTSERVER_FAIL";

	public static String INT_SERVER_SUCCESS = "INTSERVER_SUCCESS";
	
	public static String INT_CLIENT_FAIL = "INTCLIENT_FAIL";

	public static String INT_CLIENT_SUCCESS = "INTCLIENT_SUCCESS";
	
	//热点名
	public static String SSID = "YRCCONNECTION";
	//热点创建密码
	public static String PASSWORD = "123456789";
	//热点SSID后缀
	public static String SUFFIX = "_bones";
	
	public static int PORT = 12345;
	
	/**
	 * for message actions 
	 */
	public final static int MSG_ACTION_HELLO = 1;//连接
	public final static int MSG_ACTION_READY = 2;//准备
	public final static int MSG_ACTION_START = 3;//开始
	public final static int MSG_ACTION_LEAVE = 4;//离开

	//蒙层初始化位置
	//public static int MASK_AREA_HEIGHT = 300;
	
	//骨头初始化位置
	public static int BONE_INIT_POS_X = 100;
	public static int BONE_INIT_POS_Y = 100;
	
	/*是否已经拿到骨头**/
	public boolean IS_GET_BONE=false;
	
	//进攻者四种状态
	public static int ATTACK_READY=1;//开始状态
	public static int ATTACK_MOVING=2;//进攻中
	public static int GETTING_OBJECT=3;//获取物品
	public static int GOING_BACK=4;//取完物品返回状态
	public static int GIVEUP_ATTACK=5;//放弃进攻，即松手状态
	
	//进攻者状态
	public static int ATTACK_STATUS=ATTACK_READY;
	
	
	//防守者四种状态
	public static int DEFEND_CLICK_INIT=0;
	public static int DEFEND_CLICK_BEFORE=1;//防守者点击前
	public static int DEFEND_CLICKING=2;//防守者正在点击中
	public static int DEFEND_CLICK_AFTER=3;//防守者点击后
	public static int DEFEND_CLICKED_TARGET=4;//防守者已经命中目标
	public static int DEFEND_MISSED_TARGET=5;//防守者没命中目标
	
	//防守者状态
	public static int DEFEND_STATUS=DEFEND_CLICK_INIT;
		
	
	
}