package shoot;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Enemy extends Rectangle {

	private int e_x,e_y;
	public boolean live_flg=false,sp_ene=false;

	public Enemy(int ar_x,int ar_y) {
		setSize(20, 15);
		setLocation(ar_x,ar_y);
		live_flg=true;

		double dct = Math.random()*5;

		switch((int)dct){

			case 0:
				e_x=Main_Panel.ENEMY_V;
				break;
			case 1:
				e_x=-Main_Panel.ENEMY_V;
				break;
			case 2:
				e_y=Main_Panel.ENEMY_V;
				break;
			case 3:
				e_y=-Main_Panel.ENEMY_V;
				break;
			default:
				double rdm_a = Math.random();
				double rdm_b = Math.random();
				if(rdm_a<0.5){
					e_x=Main_Panel.ENEMY_V;
					if(rdm_b<0.5)
						e_y=Main_Panel.ENEMY_V;
					else
						e_y=-Main_Panel.ENEMY_V;
				}else{
					e_x=-Main_Panel.ENEMY_V;
					if(rdm_b<0.5)
						e_y=Main_Panel.ENEMY_V;
					else
						e_y=-Main_Panel.ENEMY_V;
				}
				sp_ene = true;
					break;
		}
	}


	public void e_move(){
		this.x+=e_x;
		this.y+=e_y;
		setLocation(x, y);
		if(x<-20||x>Main_Panel.WIDTH||y>Main_Panel.HEIGHT||y<-15){
			live_flg=false;
		}

	}

	public void drawEnemy(Graphics g) {
		g.setColor(Color.RED);
		g.fillRect(x, y, width, height);
	}

}
