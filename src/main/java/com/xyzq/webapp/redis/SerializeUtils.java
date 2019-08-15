package com.xyzq.webapp.redis;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

/**
 * Package: com.xyzq.webapp.redis
 * Description： 线程安全用户类
 * Author: linkan
 * Date: Created in 2019/8/1 14:55
 * Company: 兴业证券
 * Copyright: Copyright (c) 2019
 * Version: 0.0.1
 */
public class SerializeUtils implements RedisSerializer<Object> {

	private static Logger logger = LoggerFactory.getLogger(SerializeUtils.class);

    private static boolean isEmpty(byte[] data) {
        return (data == null || data.length == 0);
    }

	/**
	 * Title: serialize
	 * Description:序列化
	 * @param object 序列化对象
	 * @return byte[]
	 */
	@Override
	public byte[] serialize(Object object) throws SerializationException {
        byte[] result = null;

        if (object == null) {
            return new byte[0];
        }
        try (
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream(128);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteStream)
        ){

            if (!(object instanceof Serializable)) {
                throw new IllegalArgumentException(SerializeUtils.class.getSimpleName() + " requires a Serializable payload " +
                        "but received an object of type [" + object.getClass().getName() + "]");
            }

            objectOutputStream.writeObject(object);
            objectOutputStream.flush();
            result =  byteStream.toByteArray();
        } catch (Exception ex) {
            logger.error("Failed to serialize",ex);
        }
        return result;
	}

	/**
	 * Title: deserialize
	 * Description: 反序列化
	 * @param bytes 反序列化字节流
	 * @return Object
	 */
	@Override
	public Object deserialize(byte[] bytes) throws SerializationException {
        Object result = null;

        if (isEmpty(bytes)) {
            return null;
        }

        try (
            ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteStream)
        ){
            result = objectInputStream.readObject();
        } catch (Exception e) {
            logger.error("Failed to deserialize",e);
        }
        return result;
    }
}
