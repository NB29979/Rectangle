package shoot;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

//効果音を扱う
public class SoundEffect {
	private Clip clip;

	public SoundEffect(String soundFilePath){
		try{
			//waveファイルの読み込み
			File soundFile = new File(soundFilePath);
			AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundFile);

			//音声再生クリップの取得
			clip = AudioSystem.getClip();

			//再生可能に
			clip.open(audioInput);
		}catch(UnsupportedAudioFileException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}catch(LineUnavailableException e){
			e.printStackTrace();
		}
	}

	public Clip getClip(){
			return clip;
	}

	//効果音再生メソッド
	public void play(){
		//再生中なら停止
		if(clip.isRunning()){
			clip.stop();
		}
		//効果音の巻き戻し
		clip.setFramePosition(0);
		//再生
		clip.start();
	}
	
	public void loop(){
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}
	public void stop(){
		clip.stop();
		//効果音の巻き戻し
		clip.setFramePosition(0);
	}
}
