package com.example.audiorecord.model;

/**
 * 上传音频的模型
 * @author zhangzhilai
 *
 */
public class UploadAudioModel {
	private int userID;
	private String url;        //上传的网络地址
	private String audioPath;  //音频本地地址
	private String suffix;     //音频后缀名
	private String token;      //token
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getAudioPath() {
		return audioPath;
	}
	public void setAudioPath(String audioPath) {
		this.audioPath = audioPath;
	}
	public String getSuffix() {
		return suffix;
	}
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
}
