package com.example.audiorecord.util;

public class ContextStrategy {
	private INetworkRequestStrategy networkRequestStrategy;
	
	//构造方法传递进来使用那个策略：如httppost，httpget等
	public ContextStrategy(INetworkRequestStrategy networkRequestStrategy){
		this.networkRequestStrategy = networkRequestStrategy;
	}
	
	public String  networkRequestStrategy(String url, byte[] contentBody){
		return this.networkRequestStrategy.getRequestResult(url, contentBody);
	} 
}
