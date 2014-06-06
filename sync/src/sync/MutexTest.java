package sync;

import java.util.ArrayList;
import java.util.List;
// Não existe coisa mais horrível que ter que ficar digitando System.out.blahblah...
import static java.lang.System.out;

public class MutexTest {

	private Mutex mutex;
	private List<Thread> threadList;
	private static final int maxThreads = 4;

	private void run() {
		threadList = new ArrayList<Thread>();
		// Cria o mutex que será disputado entre as threads
		mutex = new Mutex();

		// Instância todas as threads
		for (int i = 0; i < maxThreads; i++) {
			Thread t = new Process();
			t.setName(String.format("Thread %d", i));
			threadList.add(t);
			t.start();
		}
		try {
			// Espera até as threads terminarem
			for (Thread t : threadList)
				t.join();
		} catch (InterruptedException e) {
		}
	}

	public static void main(String[] args) {
		MutexTest test = new MutexTest();
		test.run();
	}
	
	// Nossa classe worker
	private class Process extends Thread {

		@Override
		public void run() {
			for (;;) {
				mutex.lock();
				out.println("Thread " + getName() + " conseguiu o lock!");
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				mutex.unlock();
				// Vamos dar chance de outras thread pegarem o lock
				//Thread.yield();
			}
		}

	}

}
