package test.simplethreadpool;

public class ThreadPoolTest {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
	
		ThreadPool threadpool = new ThreadPool(5);
		threadpool.start();
		
		for(int i=0;i<20000;i++){
			SampleTask t = new SampleTask();
			threadpool.addTask(t);
		}
		threadpool.stopAfterAllTasksFinished();
	}

}
