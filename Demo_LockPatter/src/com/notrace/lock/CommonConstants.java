package com.notrace.lock;


/**
 * APP中所有的公共常量定义
 * 
 * 
 */
public class CommonConstants
{
	
	public static final String ROOT_PATH="/wobaifu";
	public static final String IMAGE_CACHDIR="/cache";
	
	public static int FIRST_VAL = 13;
	public static final int CALL_FUNCTION = FIRST_VAL++;
	// CALL ID : 一个图片加载成功
	public static final int CALL_IMG_LOADED = FIRST_VAL++;
	
	public static final int WHAT_UPDATE_IMG = FIRST_VAL++;
	public static final int WHAT_SHOW_TOAST_TEXT = FIRST_VAL++;
	public static final int WHAT_CALL_FUNCTION = FIRST_VAL++;
	
	public static final String LOGINFO="login";

	public static int SCREENWIDTH;

	public static int SCREENHEIGHT;


	public static int REQUESTCODE_SUCCESS=1001;
	public static int REQUESTCODE_FAILURE=1002;

	public static int RESULTCODE_SUCCESS=1003;
	public static int RESULTCODE_FAILURE=1004;

	public static int REQUESTCODE_REGIST=1005;
	public static int RESULTCODE_REGIST=1006;

	
	

}
