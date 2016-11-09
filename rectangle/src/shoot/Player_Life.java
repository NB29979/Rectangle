package shoot;

public class Player_Life {
	static int MAX_HP=100;

	
	public static int life;

	Player_Life(){
		init();
	}
	public void damage(){
		life-=Main_Panel.DAMAGE_R;
	}
	public int getLife(){
		return life;
	}
	public void init(){
		life=MAX_HP;
	}
}
