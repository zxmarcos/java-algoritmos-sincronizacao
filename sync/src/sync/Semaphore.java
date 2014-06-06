/*
 * Implementação de semáforos
 * Marcos Medeiros
 */
package sync;

import java.util.LinkedList;
import java.util.Queue;

public class Semaphore {
	// Lista de espera
	private final Queue<Object> waitQueue;
	private volatile int counter;
	// Vamos usar nossa própria implementação de mutex
	// para garantir a atomicidade das operações UP e DOWN
	private Mutex mutex;

	public Semaphore(int initValue) {
		waitQueue = new LinkedList<Object>();
		counter = initValue;
		mutex = new Mutex();
	}

	public void up() {
		mutex.lock();
		
		// Se houver processos esperando, acorda o primeiro da lista,
		// e não incrementamos o contador!
		if (waitQueue.size() > 0) {
			Object monitor;
			monitor = waitQueue.remove();
			synchronized (monitor) {
				monitor.notifyAll();
			}
		} else {
			counter++;
		}
		mutex.unlock();
	}

	public void down() {
		mutex.lock();

		if (counter == 0) {
			Object monitor = new Object();
			waitQueue.add(monitor);
			try {
				// Suspende a thread atual
				synchronized (monitor) {
					// libera o lock, não queremos que a thread seja suspensa com o lock :P
					// isso causaria uma deadlock fácilmente.
					mutex.unlock();
					monitor.wait();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		} else {
			counter--;
			mutex.unlock();
		}
	}

}
