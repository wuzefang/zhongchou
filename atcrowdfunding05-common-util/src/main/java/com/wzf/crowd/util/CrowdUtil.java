package com.wzf.crowd.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;

import com.aliyun.api.gateway.demo.util.HttpUtils;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.comm.ResponseMessage;
import com.aliyun.oss.model.PutObjectResult;
import com.wzf.crowd.constant.CrowdConstant;

public class CrowdUtil {
	

	/**
	 * 专门负责上传文件到 OSS 服务器的工具方法
	 * 
	 * @param endpoint
	 *            OSS 参数
	 * @param accessKeyId
	 *            OSS 参数
	 * @param accessKeySecret
	 *            OSS 参数
	 * @param inputStream
	 *            要上传的文件的输入流
	 * @param bucketName
	 *            OSS 参数
	 * @param bucketDomain
	 *            OSS 参数
	 * @param originalName
	 *            要上传的文件的原始文件名
	 * @return 包含上传结果以及上传的文件在 OSS 上的访问路径
	 */
	public static ResultEntity<String> uploadFileToOss(String endpoint, String accessKeyId, String accessKeySecret,

			InputStream inputStream, String bucketName, String bucketDomain, String originalName) {
		// 创建 OSSClient 实例。
		OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
		// 生成上传文件的目录
		String folderName = new SimpleDateFormat("yyyyMMdd").format(new Date());
		// 生成上传文件在 OSS 服务器上保存时的文件名
		// 原始文件名：beautfulgirl.jpg
		// 生成文件名：wer234234efwer235346457dfswet346235.jpg
		// 使用 UUID 生成文件主体名称
		String fileMainName = UUID.randomUUID().toString().replace("-", "");
		// 从原始文件名中获取文件扩展名
		String extensionName = originalName.substring(originalName.lastIndexOf("."));
		// 使用目录、文件主体名称、文件扩展名称拼接得到对象名称
		String objectName = folderName + "/" + fileMainName + extensionName;
		try {
			// 调用 OSS 客户端对象的方法上传文件并获取响应结果数据
			PutObjectResult putObjectResult = ossClient.putObject(bucketName, objectName, inputStream);
			// 从响应结果中获取具体响应消息
			ResponseMessage responseMessage = putObjectResult.getResponse();
			// 根据响应状态码判断请求是否成功
			if (responseMessage == null) {
				// 拼接访问刚刚上传的文件的路径
				String ossFileAccessPath = bucketDomain + "/" + objectName;
				// 当前方法返回成功
				return ResultEntity.successWithData(ossFileAccessPath);
			} else {
				// 获取响应状态码
				int statusCode = responseMessage.getStatusCode();

				// 如果请求没有成功，获取错误消息
				String errorMessage = responseMessage.getErrorResponseAsString();
				// 当前方法返回失败
				return ResultEntity.error(" 当 前 响 应 状 态 码 =" + statusCode + " 错 误 消 息=" + errorMessage);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 当前方法返回失败
			return ResultEntity.error(e.getMessage());
		} finally {
			if (ossClient != null) {
				// 关闭 OSSClient。
				ossClient.shutdown();
			}
		}
	}

	/**
	 * 调用第三方短信接口，发送验证码
	 * 
	 * @param host
	 *            短信接口调用的url地址
	 * @param path
	 *            具体发送短信功能的地址
	 * @param method
	 *            请求方式
	 * @param appcode
	 *            下单的短信接口的AppCode
	 * @param receive
	 *            手机号
	 * @param templateId
	 *            模板号
	 * @return 返回调用结果是否成功，以及失败的消息 状态码： 200 成功 400 参数错误 404 请求资源不存在 500
	 *         系统内部错误，请联系服务商 501 第三方服务异常 604 接口停用
	 */
	public static ResultEntity<String> sendCodeByShortMessage(String host, String path, String method, String appcode,
			String receive, String templateId) {
		Map<String, String> headers = new HashMap<String, String>();
		// 最后在header中的格式(中间是英文空格)为Authorization:APPCODE
		// 83359fd73fe94948385f570e3c139105

		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < 4; i++) {
			int random = (int) (Math.random() * 10);
			builder.append(random);
		}
		String tag = builder.toString();
		headers.put("Authorization", "APPCODE " + appcode);
		// 封装其他参数
		Map<String, String> querys = new HashMap<String, String>();
		querys.put("receive", receive);
		querys.put("tag", tag);
		querys.put("templateId", templateId);
		Map<String, String> bodys = new HashMap<String, String>();

		try {
			/**
			 * 重要提示如下: HttpUtils请从
			 * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/
			 * src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java 下载
			 *
			 * 相应的依赖请参照
			 * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/
			 * pom.xml
			 */
			HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);

			// 获取封装了响应状态信息的对象
			StatusLine statusLine = response.getStatusLine();
			// 响应状态码
			int statusCode = statusLine.getStatusCode();
			// 响应信息
			String reasonPhrase = statusLine.getReasonPhrase();

			if (statusCode == 200) {
				return ResultEntity.successWithData(tag);
			}
			return ResultEntity.error(reasonPhrase);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultEntity.error(e.getMessage());
		}

	}

	/**
	 * md5加密方法
	 * 
	 * @param source
	 *            ：传入的明文字符串
	 * @return
	 */
	public static String md5(String source) {
		// 1、先判断输入的密码是否为空串
		if (source == null || source.length() == 0) {
			// 2、如果字符串为空抛出异常
			throw new RuntimeException(CrowdConstant.MESSAGE_STRING_INVALIDATE);
		}

		try {
			// 3、获取MessageDigest对象
			String algorithm = "md5";
			MessageDigest messageDigest = MessageDigest.getInstance(algorithm);

			// 4、获取明文字符串对应的字节数组
			byte[] input = source.getBytes();

			// 5、进行加密
			byte[] output = messageDigest.digest(input);

			// 6、创建一个BigInteger对象,将加密后的字节数组转换为一个BigInteger类型的正整数
			int signum = 1; // 标记为正整数
			BigInteger bigInteger = new BigInteger(signum, output);

			// 7、按照16进制，将bigInteger的值转为字符串
			int radix = 16;
			String encode = bigInteger.toString(radix).toUpperCase();

			// 8、返回加密结果
			return encode;

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 判断当前请求是否为 Ajax 请求
	 * 
	 * @param request
	 *            * @return
	 */
	public static boolean judgeRequestType(HttpServletRequest request) {
		// 1.获取请求消息头信息
		String acceptInformation = request.getHeader("Accept");
		String xRequestInformation = request.getHeader("X-Requested-With");
		// 2.检查并返回
		return (acceptInformation != null && acceptInformation.length() > 0
				&& acceptInformation.contains("application/json"))
				|| (xRequestInformation != null && xRequestInformation.length() > 0
						&& xRequestInformation.equals("XMLHttpRequest"));
	}
}
