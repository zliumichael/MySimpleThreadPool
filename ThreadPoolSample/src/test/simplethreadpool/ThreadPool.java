package test.simplethreadpool;

import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;

public class ThreadPool {
	
	/*Default thread amount when initialize this pool */
	private static int DEFAULT_THREAD_NUM=5;
	
	/* Thread array*/
	private TaskThread[] taskThreads;
	
	/*Task Queue */
	private LinkedBlockingQueue<Task> taskQueue = new LinkedBlockingQueue<Task>();
	
	/**
	 * 
	 * @param poolSize
	 *  Construct a thread pool with the default thread amount
	 */
	public ThreadPool(){
		this(DEFAULT_THREAD_NUM);
	}
	/**
	 * 
	 * @param poolSize
	 *  Construct a thread pool with the given thread amount
	 */
	public ThreadPool(int poolSize){
		taskThreads = new TaskThread[poolSize];
		for(int i =0;i<poolSize;i++){
			taskThreads[i] = new TaskThread();
		}
	}
	
	/**
	 * Start this pool
	 */
	public void start(){
		for(TaskThread t:this.taskThreads){
				new Thread(t).start();
		}
	}
	
	/**
	 * Stop this pool after all task finished
	 */
	public void stopAfterAllTasksFinished(){
		if(taskQueue.isEmpty()){
			stop();
		}
		else{
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					if(taskQueue.isEmpty()){
						stop();
						this.cancel();
					}
				}
			}, 
			100, 100);
		}
	}
	
	/**
	 * Stop all thread in this pool and clear all tasks in task queue immediately
	 */
	public void stop(){
		for(TaskThread t:taskThreads){
			t.stop();
		}
		if(!taskQueue.isEmpty()){
			taskQueue.clear();
		}
	}
	
	/**
	 * Add multiple tasks
	 */
	public void addTask(Collection<SampleTask> tasks){
		 synchronized(taskQueue){
		     taskQueue.addAll(tasks);
		     taskQueue.notifyAll();
		 }
	}
	/**
	 * Add task one by one
	 */
	public void addTask(SampleTask t){
		 synchronized(taskQueue){
			 taskQueue.offer(t);
			 taskQueue.notifyAll();
		 }
	}
	
	
	/**
	 * 
	 * @author zliumichael
	 * Inner thread class to execute tasks 
	 */
	class TaskThread implements Runnable {
		
		private boolean stop=false;

		public void stop(){
			this.stop = true;
		}
		
		@Override
		public void run() {
			while(!stop){
				synchronized (taskQueue) {
					while(!stop&&taskQueue.isEmpty()){
						try {
							taskQueue.wait(20);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				   if(!taskQueue.isEmpty()){
					   Task t = taskQueue.poll();
					   t.execute();
				   }
				}
			}
		}
	}
	
}
