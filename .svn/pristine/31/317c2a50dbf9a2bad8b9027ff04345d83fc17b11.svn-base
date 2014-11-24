package view.play;

import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;


/*
 * 该类继承自Thread，主要实现欢迎界面的后台数据
 * 的修改以实现动画效果
 */
public class GameAttackerViewThread extends Thread{
	public GameAttackerView father;				//WelcomeView对象的引用
	public SurfaceHolder surfaceHolder;		//WelcomeView对象的SurfaceHolder
	public Handler touchHandler;
	int sleepSpan = 50;				//休眠时间
	boolean flag;						//线程执行标志位
	
	//构造器：初始化主要成员变量	
	public GameAttackerViewThread(GameAttackerView father,SurfaceHolder surfaceHolder){
		this.father = father;
		this.surfaceHolder = surfaceHolder;
		flag=true;
	}	
	public void run(){//线程的执行方法
		while(flag){
			Canvas canvas = null;			//创建一个Canvas对象
			try{
				canvas = surfaceHolder.lockCanvas(null);	//为画布加锁
				synchronized(surfaceHolder){
					father.doDraw(canvas);				//重新绘制屏幕
				}
			}
			catch(Exception e){
				e.printStackTrace();		//捕获异常并打印
			}
			finally{
				if(canvas != null){//释放画布并将其传回
					surfaceHolder.unlockCanvasAndPost(canvas);
				}
			}
			try{
				Thread.sleep(sleepSpan );		//休眠一段时间
			}
			catch(Exception e){
				e.printStackTrace();			//捕获并打印异常
			}
		}
	}
	
}