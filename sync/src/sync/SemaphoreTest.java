package sync;

import static java.lang.System.out;

import java.util.ArrayList;
import java.util.List;

public class SemaphoreTest {
	private Semaphore semaphore;
	private List<Thread> threadList;
	private static final int maxThreads = 4;

	private void run() {
		threadList = new ArrayList<Thread>();
		// Cria o semáforo que será disputado entre as threads
		semaphore = new Semaphore(1);

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
		SemaphoreTest test = new SemaphoreTest();
		test.run();
	}
	
	// Nossa classe worker
	private class Process extends Thread {

		@Override
		public void run() {
			for (;;) {
				semaphore.down();
				out.println("Thread " + getName() + " conseguiu o lock!");
				
				long start = System.currentTimeMillis();
				while ((System.currentTimeMillis() - start) < 1000) {
					// não faz nada, busy wait
				}
				semaphore.up();
			}
		}

	}
}
