package com.echoii.cloud.platform.service.impl;

import com.echoii.cloud.platform.service.CacheService;
import com.echoii.cloud.platform.util.Config;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
public class CacheServiceRedisImpl implements CacheService {

	private static volatile CacheService SERVICE = null;
	private Config config = Config.getInstance();
	private JedisPool pool;

	private String host;
	private int timeout;
	private int checktime;
	private int port;

	public static CacheService getInstance() {
		if (SERVICE == null) {
			synchronized (CacheServiceRedisImpl.class) {
				// when more than two threads run into the first null check same
				// time, to avoid instanced more than one time, it needs to be
				// checked again.
				if (SERVICE == null) {
					SERVICE = new CacheServiceRedisImpl();
				}
			}
		}
		return SERVICE;
	}

	CacheServiceRedisImpl() {
		host = config.getStringValue("cahce.redis.host", "172.21.7.169");
		port = config.getIntValue("cahce.redis.port", 6379);
		timeout = config.getIntValue("cache.redis.timeout", 60 * 1000);
		checktime = config.getIntValue("cache.redis.checktime", 5 * 1000);

		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxActive(config.getIntValue("cache.redis.maxActive", 100));
		poolConfig.setMaxIdle(config.getIntValue("cache.redis.maxIdle", 20));
		poolConfig.setMaxWait(config.getLongValue("cache.redis.maxWait", 1000));

		pool = new JedisPool(poolConfig, host, port, timeout);// 线程数量限制，IP地址，端口，超时时间
	}

	@Override
	public void setIdcode(String key, String data, int sec) {
		Jedis jedis = pool.getResource();
		try {
			jedis.expire(key, sec);
			jedis.set(key, data);
		} finally {
			if (jedis != null)
				pool.returnResource(jedis);
		}
	}

	@Override
	public String getIdcode(String key) {
		Jedis jedis = pool.getResource();
		try {
			return jedis.get(key);
		} finally {
			if (jedis != null)
				pool.returnResource(jedis);
		}
	}

	@Override
	public String getToken(String key) {
		Jedis jedis = pool.getResource();
		try {
			return jedis.get(key);
		} finally {
			if (jedis != null)
				pool.returnResource(jedis);
		}
	}

	@Override
	public void setToken(String key, String token) {
		Jedis jedis = pool.getResource();
		try {
			jedis.expire(key, timeout);
			jedis.set(key, token);
		} finally {
			if (jedis != null)
				pool.returnResource(jedis);
		}
	}

	@Override
	public boolean validate(String key, String token) {
		Jedis jedis = pool.getResource();
		try {
			if(jedis.get(key) == null){
				return false;
			}
			
			if(jedis.get(key).equals(token)){
				return isTimeVaild(key);
			}else
				return false;
		} finally {
			if (jedis != null)
				pool.returnResource(jedis);
		}
	}
	
	private boolean isTimeVaild(String key) {
		Jedis jedis = pool.getResource();
		try {
			Long time = jedis.ttl(key);
			if (time >= (long) checktime) {
				return true;
			} else {
				if (time < (long) checktime) {
					setTokenTime(key);
					return true;
				} else
					return false;
			}
		} catch (Exception e) {
			return false;
		} finally {
			if (jedis != null)
				pool.returnResource(jedis);
		}
	}
	private void setTokenTime(String key) {
		Jedis jedis = pool.getResource();
		try {	
		    jedis.expire(key, timeout);
		} finally {
			if (jedis != null)
				pool.returnResource(jedis);
		}

	}

	@Override
	public boolean delete(String key, String token) {
		Jedis jedis = pool.getResource();
		try {
			if(jedis.get(key) == null){
				System.out.println("key not exit");
				return false;
				
			}
			
			if(jedis.get(key).equals(token)){
				jedis.del(key);
				System.out.println("delete sucess");
				return true;
			}
			System.out.println("does not fit");
			return false;
		} finally {
			if (jedis != null)
				pool.returnResource(jedis);
		}
	}

}
