package view.play;

public class AttackHand{

	private int pos_x;
	private int pos_y;
	private int init_pos_x;
	private int init_pos_y;
	public int getInit_pos_x() {
		return init_pos_x;
	}

	public void setInit_pos_x(int init_pos_x) {
		this.init_pos_x = init_pos_x;
	}

	public int getInit_pos_y() {
		return init_pos_y;
	}

	public void setInit_pos_y(int init_pos_y) {
		this.init_pos_y = init_pos_y;
	}

	public int getPos_x() {
		return pos_x;
	}

	public void setPos_x(int pos_x) {
		this.pos_x = pos_x;
	}

	public int getPos_y() {
		return pos_y;
	}

	public void setPos_y(int pos_y) {
		this.pos_y = pos_y;
	}

	//设置狗爪的移动速度
	private int move_speed;
	
	public int getMove_speed() {
		return move_speed;
	}

	public void setMove_speed(int move_speed) {
		this.move_speed = move_speed;
	}
	
}
