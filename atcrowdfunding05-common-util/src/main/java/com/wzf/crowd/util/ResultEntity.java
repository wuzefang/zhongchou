package com.wzf.crowd.util;
/**
 * 用来统一管理返回结果的类
 * @author wuzf
 *
 * @param <T> ：泛型，将需要返回的数据封装到类中
 */
public class ResultEntity<T> {
	//请求成功的常量
	public static final String SUCCESS ="SUCCESS";
	//请求失败的常量
	public static final String ERROR = "ERROR";
	
	//返回信息没信息时
	public static final String NO_MESSAGE = "NO_MESSAGE";
	//返回信息没数据时
	public static final String NO_RESULTDATA = "NO_RESULTDATA";
	
	//请求的响应结果
	private String result;
	//请求的响应信息
	private String message;
	//请求返回的数据
	private T data;
	
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	public ResultEntity(String result, String message, T data) {
		super();
		this.result = result;
		this.message = message;
		this.data = data;
	}
	public ResultEntity() {
		super();
	}
	@Override
	public String toString() {
		return "ResultEntity [result=" + result + ", message=" + message + ", data=" + data + "]";
	}
	/**
	 * 用来返回 请求处理成功，并带回数据的方法
	 * @param resultData 带回的数据
	 * @return
	 */
	public static <E> ResultEntity<E> successWithData(E resultData){
		return new ResultEntity<E>(SUCCESS,NO_MESSAGE,resultData);
	}
	/**
	 * 用来处理请求成功，不带回数据的方法
	 * @return
	 */
	public static <E> ResultEntity<E> successWithoutData(){
		return new ResultEntity<E>(SUCCESS, NO_MESSAGE, null);
	}
	/**
	 * 处理请求失败时，返回错误信息的方法
	 * @param errorMessage 错误信息
	 * @return
	 */
	public static <E> ResultEntity<E> error(String errorMessage){
		return new ResultEntity<E>(ERROR, errorMessage, null);
	}

}
