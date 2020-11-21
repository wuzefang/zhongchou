package com.wzf.crowd.test;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.aliyun.api.gateway.demo.util.HttpUtils;
import com.wzf.crowd.CrowdMainClass;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class TestCrowd {

	@Test
	public void testMessage() {

		// 短信接口调用的url地址
		String host = "https://smssend.shumaidata.com";
		// 具体发送短信功能的地址
		String path = "/sms/send";
		// 请求方式
		String method = "POST";
		// 下单的短信接口的AppCode
		String appcode = "33799400e7f64d9f9b8a85fd07eb3f05";
		Map<String, String> headers = new HashMap<String, String>();
		// 最后在header中的格式(中间是英文空格)为Authorization:APPCODE
		// 83359fd73fe94948385f570e3c139105
		headers.put("Authorization", "APPCODE " + appcode);
		// 封装其他参数
		Map<String, String> querys = new HashMap<String, String>();
		querys.put("receive", "1777530188367");
		querys.put("tag", "hanhan");
		querys.put("templateId", "M09DD535F4");
		Map<String, String> bodys = new HashMap<String, String>();

		try {
			/**
			 * 重要提示如下: HttpUtils请从
			 * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java 下载
			 *
			 * 相应的依赖请参照
			 * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
			 */
			HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
			System.out.println(response.toString()+"~~~~~~~~");
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			String reasonPhrase = statusLine.getReasonPhrase();
			System.out.println("code=" + statusCode);
			System.out.println("reasonPhrase=" + reasonPhrase);
			// 获取response的body
			System.out.println(EntityUtils.toString(response.getEntity())+"~~~~~~~~");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
