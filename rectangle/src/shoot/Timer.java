package shoot;

public class Timer {
	 private long start;
	 private long stop;
	 private long time;

	 private long sleepTime;

	 Timer(){
		 sleepTime = 0;
	 }

	 //一時停止
	 public String strSleepTime(){
		 sleepTime = time;
		 return sleepTime  / 1000000000 / 60 + " ' " + sleepTime / 1000000000 % 60 + " '' " + sleepTime % 1000000000 / 10000000;
	 }
	 public void resetSleepTime(){
		 sleepTime = 0;
	 }

	 public void start() {
		 start = System.nanoTime();
	}
	 public void stop() {
		 stop = System.nanoTime();
	}
	 public String strTime() {
		 time = (stop - start)+sleepTime;
		 return time / 1000000000 / 60 + " ' " + time / 1000000000 % 60 + " '' " + time % 1000000000 / 10000000;
	}

	public long longTime(){
		time = (stop - start)+sleepTime;
		return time;
	}
	public int getSec() {
		 return (int) time / 1000000000;
	}
}

