package com.example.audiorecord.util;

/**
 * 定义一个策略接口，用来处理网络请求
 * @author zhangzhilai
 *
 */
public interface INetworkRequestStrategy {
	public abstract String getRequestResult(String url, byte[] contentBody);
}
