package com.thinkgem.jeesite.modules.sys.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.Map.Entry;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


/**
 * 
 * @author Administrator
 *
 */
public class HttpClientUtil 
{
	private static final String CHARSET = "UTF-8";

	/**
	 * 以Get方式获取网页
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public static String getHtmlHttpGetContent(String url,String type) throws ClientProtocolException, IOException
	{
		HttpClientContext context = new HttpClientContext();
		CloseableHttpClient client =Tools.createSSLClientDefault();
		HttpGet httpGet = new HttpGet(url);
		httpGet = setGetInfo(httpGet);
		httpGet.setHeader("Cookie", "Cookie: jeesite.session.id=a83f8c1e-df25-40e6-839c-dd23f8f3373e; JSESSIONID=C6F0BEB66621BB8376920EF7F97E8302; pageNo=1; pageSize=15");

		HttpResponse createCodeKey = client.execute(httpGet, context);
		String content = EntityUtils.toString(createCodeKey.getEntity());
		
		System.out.println(content);
		return content;
	}
	
	/**
	 * 以Post方式获取网页
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public static JSONObject getHtmlHttpPostContent(String url,Map<String,String> parameterMap) throws ClientProtocolException, IOException
	{
		HttpClientContext context = new HttpClientContext();
		CloseableHttpClient client =Tools.createSSLClientDefault();
		HttpPost httpPost = new HttpPost(url);
		UrlEncodedFormEntity postEntity = new UrlEncodedFormEntity(getParam(parameterMap), "UTF-8");
		httpPost.setEntity(postEntity);
		RequestConfig requestConfig = RequestConfig.custom().setRedirectsEnabled(false).build();
		httpPost.setConfig(requestConfig);
		httpPost = setPostInfo(httpPost);
		HttpResponse createCodeKey = client.execute(httpPost, context);
		System.out.println(createCodeKey.getFirstHeader("Set-Cookie").getValue().split(";")[0]);
		Header[] headers = createCodeKey.getAllHeaders();
		for(Header h : headers){
			System.out.println(h.getName()+":"+h.getValue());
		}
		String content = EntityUtils.toString(createCodeKey.getEntity());
		System.out.println(content);
		JSONObject jsonObject = JSONObject.fromObject(content);  
		httpPost.abort();
		client.close();
		return jsonObject;
	}
	/**
	 *以Post方式获取网页
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public static JSONObject getHtmlHttpPostJson(String url,String parameterMap) throws ClientProtocolException, IOException
	{
		HttpClientContext context = new HttpClientContext();
		CloseableHttpClient client = Tools.createSSLClientDefault();
		HttpPost httpPost = new HttpPost(url);
		StringEntity myEntity = new StringEntity(parameterMap,ContentType.APPLICATION_JSON);
		httpPost.setEntity(myEntity);
		RequestConfig requestConfig = RequestConfig.custom().setRedirectsEnabled(false).build();
		httpPost.setConfig(requestConfig);
		httpPost = setPostInfo(httpPost);
		HttpResponse createCodeKey = client.execute(httpPost, context);
		String content = EntityUtils.toString(createCodeKey.getEntity());
		System.out.println(content);
		JSONObject jsonObject = JSONObject.fromObject(content);  
		return jsonObject;
	}

	/**
	 *以Post方式获取网页
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static JSONObject getHtmlHttpPostJsonWithToken(String url,Map<String,String> parameterMap,String token) throws ClientProtocolException, IOException
	{
		HttpClientContext context = new HttpClientContext();
		CloseableHttpClient client = Tools.createSSLClientDefault();
		HttpPost httpPost = new HttpPost(url);
		//httpPost.setHeader("token", token);
		httpPost.setHeader("Cookie", "JSESSIONID=0000RTV4STb08fPO86NJKSjaWJF:1aujhdvbf; BIGipServerpool-VMS-02-d=3416974602.30755.0000");
		UrlEncodedFormEntity postEntity = new UrlEncodedFormEntity(getParam(parameterMap), "UTF-8");
		httpPost.setEntity(postEntity);
		RequestConfig requestConfig = RequestConfig.custom().setRedirectsEnabled(false).build();
		httpPost.setConfig(requestConfig);
		httpPost = setPostInfo(httpPost);
		HttpResponse createCodeKey = client.execute(httpPost, context);
		String content = EntityUtils.toString(createCodeKey.getEntity());
		System.out.println(content);
		//JSONObject jsonObject = JSONObject.fromObject(content);
		return null;
	}

	public static HttpPost setPostInfo(HttpPost httpPost) {
		httpPost.addHeader("Accept",
				"application/json, text/plain, */*");
		httpPost.addHeader("Accept-Language", "zh-cn");
		httpPost.addHeader(
				"User-Agent",
				"Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.0.3) Gecko/2008092417 Firefox/3.0.3");
		httpPost.addHeader("Accept-Charset", "utf-8");
		httpPost.addHeader("Keep-Alive", "300");
		httpPost.addHeader("Connection", "Keep-Alive");
		httpPost.addHeader("Cache-Control", "no-cache");
		return httpPost;
	}

	public static HttpGet setGetInfo(HttpGet httpGet) {
		httpGet.addHeader("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
		httpGet.addHeader("Accept-Language", "zh-cn");
		httpGet.addHeader(
				"User-Agent",
				"Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.0.3) Gecko/2008092417 Firefox/3.0.3");
		httpGet.addHeader("Accept-Charset", "utf-8");
		httpGet.addHeader("Keep-Alive", "300");
		httpGet.addHeader("Connection", "Keep-Alive");
		httpGet.addHeader("Cache-Control", "no-cache");
		return httpGet;
	}
	
	public static List<NameValuePair> getParam(Map parameterMap) {
	    List<NameValuePair> param = new ArrayList<NameValuePair>();
	    Iterator it = parameterMap.entrySet().iterator();
	    while (it.hasNext()) 
	    {
	      Entry parmEntry = (Entry) it.next();
	      param.add(new BasicNameValuePair((String) parmEntry.getKey(),(String) parmEntry.getValue()));
	    }
	    return param;
   }
	
	public static void main(String[] args) {
		try {
		/*	String url = "http://localhost/web/login";
		//	JSONObject j = HttpClientUtil.getHtmlHttpPostJson(url, "");
			Map<String,String> map = new HashMap<>();
			map.put("username","admin");
			map.put("password","admin");
			JSONObject j1 = HttpClientUtil.getHtmlHttpPostContent(url, map);
			System.out.println(j1);*/
		System.out.println(getHtmlHttpGetContent("http://localhost/web/sys/area/",""));

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static JSONObject getCliet(String url) {

		String responseMsg = "";
		org.apache.commons.httpclient.HttpClient pi=new org.apache.commons.httpclient.HttpClient();
		GetMethod getMethod=new GetMethod(url);  
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new DefaultHttpMethodRetryHandler());
		try {
			pi.executeMethod(getMethod);
			responseMsg=UpdateString(getMethod);
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			 getMethod.releaseConnection();
		}
		JSONObject jsonObject = JSONObject.fromObject(responseMsg);  
		return jsonObject;
	}
	
	/**
	 * 乱码问题
	 * @return
	 */
	public static String UpdateString(GetMethod getMethod) {
		String responseMsg = "";
		try {
			InputStream ins = getMethod.getResponseBodyAsStream();
			//按指定的字符集构建文件流
			BufferedReader br = new BufferedReader(new InputStreamReader(ins,CHARSET));
			StringBuffer sbf = new StringBuffer();
			String line = null;
				while ((line = br.readLine()) != null)
				{
				     sbf.append(line);
				}
			/** 回收资源 */
			br.close();
			getMethod.releaseConnection();
			/** 页面源文件 */
			responseMsg = sbf.toString();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return responseMsg;
	}
}
