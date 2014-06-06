/*
 * Implementação de exclusão mútua
 * Suporte a timeout
 * Marcos Medeiros
 */
package sync;
import java.util.concurrent.atomic.*;

public class Mutex {
	private AtomicBoolean locked;
	private Thread owner;
	
	Mutex() {
		locked = new AtomicBoolean(false);
		owner = null;
	}
	
	/**
	 * Tenta adquirir o lock do mutex
	 * @param timeout Se timeout for maior que zero, então esperamos pelo tempo,
	 *                 caso contrário continuaremos indefinidamente tentando obter o lock.
	 * @return Se obtiver sucesso vai retornar true, caso contrário false.
	 */
	boolean lock(int timeout) {
		long start = System.currentTimeMillis();
		boolean got = false;
		if (timeout <= 0) {
			while (!locked.compareAndSet(false, true)) {
				/* não faz nada */
			}
			got = true;
		} else {
			while (!locked.compareAndSet(false, true)) {
				// Verifica se o tempo de espera já expirou
				if ((System.currentTimeMillis() - start) >= timeout) {
					got = false;
					break;
				}
			}
		}
		// Referência o dono do lock atual, para que outras threads
		// não possam chamar unlock
		if (got) {
			owner = Thread.currentThread();
		}
		return got;
	}
	
	/**
	 * Syntax sugar, já que java não aceita valores padrões :P
	 * @return
	 */
	boolean lock() {
		return lock(0);
	}
	
	/**
	 * Tenta liberar o lock desse mutex. É necessário ter adquirido o lock para
	 * chamar esta função.
	 * @return Se obtiver sucesso vai retornar true, caso contrário false.
	 */
	boolean unlock() {
		if (owner != Thread.currentThread()) {
			return false;
		}
		locked.set(false);
		return true;
	}
}
