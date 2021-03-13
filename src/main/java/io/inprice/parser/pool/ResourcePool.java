package io.inprice.parser.pool;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generic blocking pool for any kind of heavy resource
 * 
 * @author mdpinar
 * @since 2021-03-12
 *
 */
public abstract class ResourcePool<T> {

	private static final Logger log = LoggerFactory.getLogger(ResourcePool.class);

	private final BlockingQueue<T> pool;
	private final Set<T> set;

	private final String name;
	private final int size;
	
	private boolean isPoolActive;

	ResourcePool(String name, int size) {
		this.pool = new ArrayBlockingQueue<>(size, true);
		this.set = new HashSet<>(size);
		this.name = name;
		this.size = size;
	}

	abstract T createNewOne();
	abstract void setFree(T resource);
	abstract boolean isHealthy(T resource);

	public void setup() {
		if (isPoolActive == false) {
  		for (int i = 0; i < size; ++i) {
  			pool.add(createNewOne());
  		}
  		isPoolActive = true;
  		log.info("{} pool is created with {} resources!", name, pool.size());
		} else {
			log.warn("A {} pool is already created!", name);
		}
	}

	public void shutdown() {
		if (isPoolActive == false) return;
		isPoolActive = false;
		pool.drainTo(set);
		set.forEach(t -> setFree(t));
		set.clear();
		log.info("{} pool is shut down!", name);
	}

	public T acquire() {
		if (isPoolActive == false) return null;
		T resource;
		try {
			resource = pool.take();
		} catch (InterruptedException e) {
			log.error("Failed to acquire a {}!", e);
			return null;
		}

		boolean healthy = isHealthy(resource);
		if (healthy == false) {
			resource = createNewOne();
			log.info("A {} is expired so a new one is created!", name);
		}
		set.add(resource);
		return resource;
	}

	public void release(T resource) {
		if (isPoolActive == false) return;
		if (resource != null && set.contains(resource)) {
  		pool.add(resource);
  		set.remove(resource);
		} else {
			log.warn("Invalid resource!");
		}
	}
	
	public boolean isPoolActive() {
		return isPoolActive;
	}

}
