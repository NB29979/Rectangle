package shoot;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JPanel;

public class Main_Panel extends JPanel implements Runnable, KeyListener{

	enum State{
		TITLE,
		SELECTSCREEN,
		CREDITSSCREEN,
		DIFFICULTYSCREEN,
		INITSCREEN,
		PLAYSCREEN,
		PAUSESCREEN,
		QUITSELECTSCREEN,
		RESULTSCREEN,
		RANKINGSCREEN
	}

	final static int WIDTH = 500;
	final static int HEIGHT = 520;
	static int ENEMY_NUM = 1;
	static int ENEMY_MAX =80;
	static int ENEMY_V;
	static int DAMAGE_R;

	static int SLT = 0;
	static int RSM = 0;
	static int QSL = 0;

	static int DIF = 1;
	final static int DIF_NUM = 2;


	String result_time;
	long e_start,e_end,e_hit_start,e_hit_end;
	long result_rank_time;

	boolean rank_flg=true;

	private static boolean first_flg = true;

	// ダブルバッファリング（db）用
	private Graphics dbg;
	private Image dbImage = null;

	private Thread thread;

	private Player player;
	private Enemy enemy[];

	private Player_Life player_life;

	private Timer timer;

	private SoundEffect hitSound;
	private SoundEffect moveSound;
	private SoundEffect playerEx;
	private SoundEffect enemy_Appear;
	private SoundEffect bgm;
	private SoundEffect pauseSound;

	private long[][] ranking;

	private State state;


	Main_Panel() {
		setSize(WIDTH, HEIGHT);

		player_life = new Player_Life();

		timer=new Timer();
		timer.start();

		player = new Player();
		enemy=new Enemy[ENEMY_MAX];
		createEnemy();

		bgm = new SoundEffect("data/hometown.wav");
		hitSound = new SoundEffect("data/Hit.wav");
		moveSound = new SoundEffect("data/Move.wav");
		playerEx = new SoundEffect("data/Explosion.wav");
		enemy_Appear = new SoundEffect("data/E_a.wav");
		pauseSound = new SoundEffect("data/pause.wav");

		state = State.TITLE;

		thread = new Thread(this);
		thread.start();

		setFocusable(true);
		addKeyListener(this);

		e_start = System.nanoTime();


	}

	private void createEnemy() {
		for (int i = 0; i < enemy.length; i++) {
				enemy[i] = new Enemy(-100,-100);
			}
	}

	private void startEnemy(){
		for (int i = 0; i < enemy.length; i++) {
			enemy[i].setLocation(-100,-100);
		}
	}


	private void paintScreen() {
		try {
		// グラフィックオブジェクトを取得
			Graphics g = getGraphics();
			if ((g != null) && (dbImage != null)) {
				// バッファイメージを画面に描画
					g.drawImage(dbImage, 0, 0, null);
				}
				Toolkit.getDefaultToolkit().sync();
				if (g != null) {
				// グラフィックオブジェクトを破棄
					g.dispose();
			}
		} catch (Exception e) {
		e.printStackTrace();
		}
	}

	private void gameRender() {
		// 初回の呼び出し時にダブルバッファリング用オブジェクトを作成
		if (dbImage == null) {
			// バッファイメージ
			dbImage = createImage(WIDTH, HEIGHT);
			if (dbImage == null) {
				//System.out.println("dbImage is null");---------------------------------
				return;
			} else {
			// バッファイメージの描画オブジェクト
				dbg = dbImage.getGraphics();
			}
		}
		dbg.setColor(Color.WHITE);
		dbg.fillRect(0, 0, WIDTH, HEIGHT);
		dbg.setColor(Color.BLACK);


		if(state == State.PAUSESCREEN || state == State.PLAYSCREEN||state == State.QUITSELECTSCREEN){

			player.drawPlayer(dbg);

			if(player.hit_flg)
			player.hitPlayer(dbg);

			for(int i=0;i<enemy.length;i++){
				enemy[i].drawEnemy(dbg);
			}

			if(state == State.PAUSESCREEN||state == State.QUITSELECTSCREEN){
				dbg.setFont(new Font(null, Font.BOLD,25));
				dbg.setColor(Color.BLACK);
				dbg.drawString("PAUSE", WIDTH/2-45, HEIGHT/2-100);
				switch(RSM){
					case 0:
						dbg.setFont(new Font(null, Font.ITALIC,25));
						dbg.drawString("resume",208,212);
						dbg.drawString("-              -",192,212);
						dbg.setFont(new Font(null, Font.ITALIC,20));
						dbg.drawString("quit",226,252);
						break;
					case 1:
						dbg.setFont(new Font(null, Font.ITALIC,25));
						dbg.drawString("quit",225,252);
						dbg.drawString("-              -",193,252);
						dbg.setFont(new Font(null, Font.ITALIC,20));
						dbg.drawString("resume",212,212);
					default:
						break;
				}
				if(state == State.QUITSELECTSCREEN){
					dbg.setFont(new Font(null, Font.ITALIC,25));
					dbg.drawString("Really?",350,340);
					switch(QSL){
					case 0:
						dbg.drawString("No",350,375);
						dbg.drawString("-     -",340,375);
						dbg.setFont(new Font(null, Font.ITALIC,20));
						dbg.drawString("Yes",350,405);
						break;
					case 1:
						dbg.drawString("Yes",350,405);
						dbg.drawString("-       -",340,405);
						dbg.setFont(new Font(null, Font.ITALIC,20));
						dbg.drawString("No",350,375);
					default:
						break;
				}
				}
			}
		}
		else if(state == State.TITLE||state == State.SELECTSCREEN){

			dbg.setFont(new Font(null, Font.BOLD,25));
			dbg.drawString("Rectangle.", 50, 250);

			if(state == State.TITLE){

				e_end = System.nanoTime();

					if(e_end-e_start>800000000&&e_end-e_start<=1000000000){}
					else if(e_end-e_start>1000000000)
						e_start = System.nanoTime();
					else{
						dbg.setFont(new Font(null, Font.ITALIC,15));
						dbg.drawString("press Z key",207, 360);
					}
			}
			else if(state == State.SELECTSCREEN){

				switch(SLT){
					case 0:
						dbg.setFont(new Font(null, Font.ITALIC,25));
						dbg.drawString("Play",340, 335);
						dbg.drawString("-          -", 322, 335);
						dbg.setFont(new Font(null, Font.ITALIC,15));
						dbg.drawString("Credits",340,380);
						dbg.drawString("Quit",340,425);
						break;
					case 1:
						dbg.setFont(new Font(null, Font.ITALIC,15));
						dbg.drawString("Play",340, 335);
						dbg.drawString("Quit",340,425);
						dbg.setFont(new Font(null, Font.ITALIC,25));
						dbg.drawString("Credits",340,380);
						dbg.drawString("-               -", 320, 380);
						break;
					case 2:
						dbg.setFont(new Font(null, Font.ITALIC,15));
						dbg.drawString("Play",340, 335);
						dbg.drawString("Credits",340,380);
						dbg.setFont(new Font(null, Font.ITALIC,25));
						dbg.drawString("Quit",340,425);
						dbg.drawString("-          -", 322, 425);
						break;
					default:
						break;
				}
			}
		}
		else if(state == State.CREDITSSCREEN){
			dbg.setFont(new Font(null,Font.BOLD,25));
			dbg.drawString("Rectangle.", 50, 100);
			dbg.setFont(new Font(null,Font.BOLD,20));
			dbg.drawString("Program: ", 50, 200);
			dbg.drawString("Sound: ", 50, 300);
			dbg.drawString("BGM: ", 50, 400);
			dbg.setFont(new Font(null,Font.ITALIC,20));
			dbg.drawString("newbie29", 150, 200);
			dbg.drawString("newbie29", 150, 300);
			dbg.drawString("SHW Free music , http://shw.in", 150, 400);
		}
		else if(state == State.DIFFICULTYSCREEN){

			dbg.setFont(new Font(null, Font.ITALIC,35));
			dbg.drawString("difficulty:", 50, 200);

			e_end = System.nanoTime();
			if(e_end-e_start>800000000&&e_end-e_start<=1000000000){}
			else if(e_end-e_start>1000000000)
				e_start = System.nanoTime();
			else{
				dbg.setFont(new Font(null, Font.ITALIC,35));
				dbg.drawString("<<              >>",240,355);
				}

				switch(DIF){
					case 0:
						dbg.drawString(" easy  ", 305, 350);
						break;
					case 1:
						dbg.drawString("normal", 300, 350);
						break;
					case 2:
						dbg.drawString("  hard  ", 300, 350);
						break;
					default:
						break;
				}

		}

		else if(state == State.RESULTSCREEN){

			dbg.setFont(new Font(null, Font.ITALIC,25));
			dbg.drawString("Your score is...", 250, 350);
			dbg.drawString(result_time+" !", 330, 400);

		}
		else if(state == State.RANKINGSCREEN){

			dbg.setFont(new Font(null, Font.BOLD,20));
			for(int i=0;i<5;i++){
				if(ranking[DIF][i]==result_rank_time){
					dbg.setColor(Color.RED);
					dbg.drawString((i+1)+": "+ranking[DIF][i] / 1000000000 / 60 + " ' "
							+ ranking[DIF][i] / 1000000000 % 60 + " '' " +
								ranking[DIF][i] % 1000000000 / 10000000, 180, 140+i*52);
				}
				else{
				dbg.setColor(Color.BLACK);
				dbg.drawString((i+1)+": "+ranking[DIF][i] / 1000000000 / 60 + " ' "
						+ ranking[DIF][i] / 1000000000 % 60 + " '' " +
							ranking[DIF][i] % 1000000000 / 10000000, 180, 140+i*52);
				}
			}
		}
	}

	private void gameUpdate() {

		switch(state){
			case TITLE:
				break;
			case SELECTSCREEN:
				bgm.stop();
				DIF = 1;
				break;
			case CREDITSSCREEN:
				break;
			case DIFFICULTYSCREEN:
				break;
			case INITSCREEN:
				initGame();
				break;
			case PLAYSCREEN:
				playGame();
				break;
			case PAUSESCREEN:
				pauseGame();
				break;
			case QUITSELECTSCREEN:
				break;
			case RESULTSCREEN:
				resultGame();
				break;
			case RANKINGSCREEN:
				gameranking();
				break;
		}
	}

	@Override
	public void run() {
		double elapsedTime = 0;
		while (true) {
			long start = System.nanoTime();
			if (elapsedTime > 16.6667 * 1000000) {
				// ゲーム状態を更新（ex: ボールの移動）
				gameUpdate();
				// バッファにレンダリング（ダブルバッファリング）
				gameRender();
				// バッファを画面に描画（repaint()を自分でする！）
				paintScreen();
				elapsedTime = 0;
			}
			long end = System.nanoTime();
			elapsedTime += (end-start);
		}
	}

	public void keyTyped(KeyEvent e) {}
	public void keyPressed(KeyEvent e) {

		int key = e.getKeyCode();

		if(player_life.getLife()!=0 && state == State.PLAYSCREEN){
			if (key == KeyEvent.VK_RIGHT){
				player.p_move(10,0);
				moveSound.play();
			}
			else if (key == KeyEvent.VK_D){
				player.p_move(10,0);
				moveSound.play();
			}
			else if (key == KeyEvent.VK_LEFT){
				player.p_move(-10, 0);
				moveSound.play();
			}
			else if (key == KeyEvent.VK_A){
				player.p_move(-10, 0);
				moveSound.play();
			}
			else if (key == KeyEvent.VK_UP){
				player.p_move(0,-10);
				moveSound.play();
			}
			else if (key == KeyEvent.VK_W){
				player.p_move(0,-10);
				moveSound.play();
			}
			else if (key == KeyEvent.VK_DOWN){
				player.p_move(0,10);
				moveSound.play();
			}
			else if (key == KeyEvent.VK_S){
				player.p_move(0,10);
				moveSound.play();
			}
		}
		if(state == State.DIFFICULTYSCREEN){
			if (key == KeyEvent.VK_LEFT&&DIF>0){
				DIF--;
				moveSound.play();
			}
			else if (key == KeyEvent.VK_A&&DIF>0){
				DIF--;
				moveSound.play();
			}
			else if(key == KeyEvent.VK_RIGHT&&DIF_NUM>DIF){
				DIF++;
				moveSound.play();
			}
			else if(key == KeyEvent.VK_D&&DIF_NUM>DIF){
				DIF++;
				moveSound.play();
			}
		}
		if(state == State.SELECTSCREEN){
			if (key == KeyEvent.VK_UP&&(SLT==1||SLT==2)){
				SLT--;
				moveSound.play();
			}
			else if (key == KeyEvent.VK_W&&(SLT==1||SLT==2)){
				SLT--;
				moveSound.play();
			}
			else if(key == KeyEvent.VK_DOWN&&(SLT==0||SLT==1)){
				SLT++;
				moveSound.play();
			}
			else if(key == KeyEvent.VK_S&&(SLT==0||SLT==1)){
				SLT++;
				moveSound.play();
			}
		}
		if(state == State.PAUSESCREEN){
			if (key == KeyEvent.VK_UP&&RSM==1){
				RSM--;
				moveSound.play();
			}
			else if (key == KeyEvent.VK_W&&RSM==1){
				RSM--;
				moveSound.play();
			}
			else if(key == KeyEvent.VK_DOWN&&RSM==0){
				RSM++;
				moveSound.play();
			}
			else if(key == KeyEvent.VK_S&&RSM==0){
				RSM++;;
				moveSound.play();
			}
		}
		if(state == State.QUITSELECTSCREEN){
			if (key == KeyEvent.VK_UP&&QSL==1){
				QSL--;
				moveSound.play();
			}
			else if (key == KeyEvent.VK_W&&QSL==1){
				QSL--;
				moveSound.play();
			}
			else if(key == KeyEvent.VK_DOWN&&QSL==0){
				QSL++;
				moveSound.play();
			}
			else if(key == KeyEvent.VK_S&&QSL==0){
				QSL++;
				moveSound.play();
			}
		}
		if (key == KeyEvent.VK_Z) {
			if(state != State.PLAYSCREEN&&state != State.CREDITSSCREEN)
				moveSound.play();
			if (state == State.TITLE) {
				state = State.SELECTSCREEN;
			}
			else if (state == State.SELECTSCREEN&&SLT==0) {
				state = State.DIFFICULTYSCREEN;
			}
			else if (state == State.SELECTSCREEN&&SLT==1){
				state = State.CREDITSSCREEN;
			}
			else if(state == State.SELECTSCREEN&&SLT==2){
				System.exit(0);
			}
			else if (state == State.DIFFICULTYSCREEN){
				state = State.INITSCREEN;
			}
			else if (state == State.RESULTSCREEN) {
				state = State.RANKINGSCREEN;
			}
			else if (state == State.RANKINGSCREEN) {
				state = State.SELECTSCREEN;
			}
			else if (state == State.PAUSESCREEN&&RSM==0) {
				timer.start();
				state = State.PLAYSCREEN;
			}
			else if(state == State.PAUSESCREEN&&RSM==1){
				state = State.QUITSELECTSCREEN;
			}
			else if(state == State.QUITSELECTSCREEN&&QSL==0){
				state = State.PAUSESCREEN;
			}
			else if(state == State.QUITSELECTSCREEN&&QSL==1){
				main_game.lifeLabel.setText("");
				main_game.timerLabel.setText("");
				state = State.SELECTSCREEN;
			}
		}
		if (key == KeyEvent.VK_X){
			if(state == State.PLAYSCREEN){
				pauseSound.play();
				state = State.PAUSESCREEN;
			}
			else if(state == State.DIFFICULTYSCREEN||state == State.CREDITSSCREEN){
				pauseSound.play();
				state = State.SELECTSCREEN;
			}
			else if(state == State.RANKINGSCREEN){
				pauseSound.play();
				state = State.RESULTSCREEN;
			}
			else if(state == State.QUITSELECTSCREEN){
				QSL = 0;
				pauseSound.play();
				state = State.PAUSESCREEN;
			}
		}
	}
	public void keyReleased(KeyEvent e) {}

	private void initGame(){

		if(DIF == 0){
			ENEMY_MAX =20;
			ENEMY_V=2;
			DAMAGE_R=4;
		}
		else if(DIF == 1){
			ENEMY_MAX=40;
			ENEMY_V=2;
			DAMAGE_R=4;
		}
		else if(DIF == 2){
			ENEMY_MAX=80;
			ENEMY_V=6;
			DAMAGE_R=2;
		}
		first_flg=true;
		ENEMY_NUM = 1;
		e_hit_start = e_start = System.nanoTime();
		timer.resetSleepTime();
		timer.start();
		player.init();
		player_life.init();
		startEnemy();
		main_game.lifeLabel.setText("");
		main_game.timerLabel.setText("");
		rank_flg=true;
		RSM = 0;
		QSL = 0;

		state = State.PLAYSCREEN;
	}

	private void pauseGame(){
		main_game.timerLabel.setText(timer.strSleepTime());
	}

	private void playGame(){
		bgm.loop();

		for(int i=0;i<ENEMY_NUM;i++){
				if(enemy[i].live_flg==false){
					final int ie_x;
					final int ie_y;
					int ie_flg=0;
					ie_x= (int) (Math.random()*WIDTH)-20;
					ie_y= (int) (Math.random()*HEIGHT)-20;
					if(ie_flg==0&&ie_x>player.centerPos_x+player.P_WIDTH/2+20||ie_x<player.centerPos_x-player.P_WIDTH/2-20||ie_y>player.centerPos_y+player.P_HEIGHT+20||ie_y<player.centerPos_y-player.P_HEIGHT-20){
						enemy[i] = new Enemy(ie_x,ie_y);
						ie_flg=1;
					}
				}
			if(enemy[i].sp_ene){
				enemy_Appear.play();
				enemy[i].sp_ene = false;
			}
			enemy[i].e_move();

			player.checkhit(enemy[i]);

			e_hit_end = System.nanoTime();

			if((player.hit_flg==true)&&(e_hit_end - e_hit_start>200000000)){

				player_life.damage();

				if(player_life.getLife()>DAMAGE_R)
					hitSound.play();
				player.hit_flg=false;
				e_hit_end += e_hit_end - e_hit_start;
				e_hit_start = System.nanoTime();
			}
			e_end = System.nanoTime();
			if(e_end-e_start>2000000000)
				first_flg = false;
			if((e_end-e_start>1000000000)&&(ENEMY_NUM<ENEMY_MAX)&&(first_flg==false)){
				ENEMY_NUM++;
				e_start = System.nanoTime();
			}
			if(player_life.getLife()==0)
				break;
		}

		if(player_life.getLife()==0){
			result_time=timer.strTime();
			result_rank_time=timer.longTime();
			try{
				Thread.sleep(340);
			}
			catch(InterruptedException e){}
			playerEx.play();
			try{
				Thread.sleep(1500);
			}
			catch(InterruptedException e){}
			state = State.RESULTSCREEN;
		}
		main_game.lifeLabel.setText("HP:"+player_life.getLife());
		main_game.timerLabel.setText(timer.strTime());
		 if(player_life.getLife()<30)
				main_game.lifeLabel.setForeground(Color.RED);
		 else if(player_life.getLife()<50)
			main_game.lifeLabel.setForeground(Color.YELLOW);
		 else if(player_life.getLife()<=100)
			main_game.lifeLabel.setForeground(Color.GREEN);

		timer.stop();
	}

	private void resultGame(){
		main_game.lifeLabel.setText("");
		main_game.timerLabel.setText("");
	}

	private void gameranking(){
		if(rank_flg){
		String input = "data/rank.txt";
		ranking = new long[DIF_NUM+1][6];
		int n=0,m=0;
		// ファイル読み込み
		try {

			BufferedReader reader = new BufferedReader(new FileReader(input));
			String line;
			for(n=0;n<DIF_NUM+1;n++){
				for(m=0;m<5;m++){
					line = reader.readLine();
					ranking[n][m] = Long.parseLong(line);
					if((ranking[n][m]%97)!=0)
						ranking[n][m] = 0;
					else
						ranking[n][m] = Long.parseLong(line)/97;
				}
			}
			reader.close();

		} catch (FileNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		ranking[DIF][5] = result_rank_time;

		// sort

			for(int i=0;i<ranking[DIF].length;i++){
				for(int j=i+1;j<ranking[DIF].length;j++){
					if(ranking[DIF][i]<ranking[DIF][j]){
						long temp = ranking[DIF][i];
						ranking[DIF][i] = ranking[DIF][j];
						ranking[DIF][j] = temp;
					}
				}
			}


		// ファイル書き出し
		try {

			BufferedWriter writer = new BufferedWriter(new FileWriter(input));

			for(n=0;n<DIF_NUM+1;n++){
				for(m=0;m<5;m++){
					writer.write(Long.toString(ranking[n][m]*97));
					writer.newLine();
				}
			}

			writer.close();

		} catch (FileNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		rank_flg=false;
		}
	}

}

