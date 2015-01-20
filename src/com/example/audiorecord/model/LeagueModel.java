package com.example.audiorecord.model;

import java.io.Serializable;

public  class LeagueModel implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5468528703468714188L;
	public static String TopicID = "TopicID"; //
	public static  String BoardID = "BoardID";
	public static String Title = "Title"; // 贴子题目
	public static String Summary = "Summary"; // 贴子内容
	public static String IsTop = "IsTop"; // 贴子是否置顶
	public static String IsBest = "IsBest"; // 贴子是否
	public static String IsHot = "IsHot"; // 贴子是否为热帖
	public static String ReplyNum = "ReplyNum"; // 贴子回复数目
	public static String ThumbImg = "ThumbImg";// 贴子图片地址
	public static String RateSum = "RateSum"; //点赞数量
	public static String LastReplyDate = "LastReplyDate";// 贴子最后回复的
	public static String LastReplyUserID = "LastReplyUserID"; // 贴子最后一个回复的id
	public static String LastReplyUserName = "LastReplyUserName";// 贴子最后一个回复的name
	public static String AudioPath = "AudioPath";// 贴子音频地址
	public static String AudioTime = "AudioTime";// 贴子音频时间
	public static String IsRate = "IsRate";
}
