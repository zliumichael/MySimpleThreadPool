package test.simplethreadpool;
/**
 * 
 * @author zliumichael
 * Simple task class for test
 */
public class SampleTask implements Task {
	public static int i=0;

	@Override
	public void execute(){
		System.out.println(Thread.currentThread().getName());
		System.out.println("begin task"+(++i));
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("end");
	}

}
