package com.livk.stream.entity;

import lombok.Data;

/**
 * <p>
 * KafkaMessage
 * </p>
 *
 * @author livk
 * @date 2022/2/15
 */
@Data
public class StreamMessage<T> {

    private String id;

    private String msg;

    private T data;

}
