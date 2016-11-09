package shoot;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Player extends Rectangle {
	public int P_WIDTH=40;
	public int P_HEIGHT=15;
	public int centerPos_x;
	public int centerPos_y;
	public boolean hit_flg=false;

	Player() {
		init();
	}

	public int PosX(){
		return this.x;
	}
	public int PosY(){
		return this.y;
	}

	public void p_move(int x,int y) {
		centerPos_x += x;
		centerPos_y += y;
		if ((centerPos_x - width / 2 )< 0)
			centerPos_x = width / 2;
		else if ((centerPos_x + width / 2) > Main_Panel.WIDTH-5)
			centerPos_x = Main_Panel.WIDTH-5 - width / 2;
		else if ((centerPos_y - height / 2)< 0)
			centerPos_y = height / 2;
		else if ((centerPos_y - height /2) > Main_Panel.HEIGHT-37)
			centerPos_y = Main_Panel.HEIGHT-37;


		setLocation(centerPos_x - width / 2, centerPos_y- height / 2);


	}

	public void checkhit(Enemy enemy) {
		 if (intersects(enemy))
		 	hit_flg=true;
	}

	public void drawPlayer(Graphics g) {
		g.setColor(Color.BLUE);
		g.fillRect(x, y, width, height);
	}

	public void hitPlayer(Graphics g){
		g.setColor(Color.CYAN);
		g.fillRect(x, y, width, height);
	}

	public void init(){
		setSize(P_WIDTH,P_HEIGHT);
		centerPos_x = Main_Panel.WIDTH / 2;
		centerPos_y = Main_Panel.HEIGHT / 2;
		setLocation(centerPos_x - width / 2, centerPos_y - height / 2);
	}

}