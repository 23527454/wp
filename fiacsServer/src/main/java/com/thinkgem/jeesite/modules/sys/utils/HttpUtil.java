package com.thinkgem.jeesite.modules.sys.utils;

import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import net.sf.json.JSONObject;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.*;

public class HttpUtil {

    public static String VMS_URL = "http://10.233.93.55:334";

    public static String SCRS_URL= "http://10.233.93.110:381";
    //金库登录
    public static final String VMS_LOGIN="/VMS/role/AuthenticationProcessingAction_queryLogin.action";
    //款箱汇总查询
    public static final String VMS_BOXINFO="/VMS/packet/BoxinfoAction_getBoxinfoList.action";
    //款箱事件
    public static final String VMS_BOX_EVENT="/VMS/packet/BoxSelectAction_findBoxprocid.action";
    //款箱事件详情
    public static final String VMS_BOX_EVENT_DETAIL="/VMS/packet/BoxSelectAction_findBoxProcdBykey.action";

    public static final String VMS_ORGANIZATION = "/VMS/role/organizationUnitAction_findAllOrganization.action";

    public static final String VMS_STAFF = "/VMS/role/StaffAction_findStaffDetail.action";

    //加配钞登录
    public static final String SCRS_LOGIN="/scrs/rest/login";
    //线路明细
    public static final String SCRS_LINE_INFO = "/scrs/rest/cashrem/taskdispa/routeTask";
    //线路对应的任务明细
    public static final String SCRS_TASK_INFO = "/scrs/rest/cashrem/scanboxrcv/getTaskGrid";
    //待交接款箱
    public static final String SCRS_TASK_BOX_INFO = "/scrs/rest/cashrem/scanboxrcv/scanVoInfoGet";


    public static  Map<String,String> cookieMap = new HashMap<>();

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
                "application/json, text/plain, */*");
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
            Map.Entry parmEntry = (Map.Entry) it.next();
            param.add(new BasicNameValuePair((String) parmEntry.getKey(),(String) parmEntry.getValue()));
        }
        return param;
    }

    public static String getHtmlHttpPostContent(String url, Map<String,String> parameterMap) throws ClientProtocolException, IOException
    {
        String returnString = "";
        HttpClientContext context = new HttpClientContext();
        CloseableHttpClient client =Tools.createSSLClientDefault();
        HttpPost httpPost = new HttpPost(url);
        UrlEncodedFormEntity postEntity = new UrlEncodedFormEntity(getParam(parameterMap), "UTF-8");
        httpPost.setEntity(postEntity);
        RequestConfig requestConfig = RequestConfig.custom().setRedirectsEnabled(false).build();
        httpPost.setConfig(requestConfig);
        httpPost = setPostInfo(httpPost);
        HttpResponse createCodeKey = client.execute(httpPost, context);
        String content = EntityUtils.toString(createCodeKey.getEntity());
        if(url.indexOf("/VMS/")!=-1){
            JSONObject jsonObject = JSONObject.fromObject(content);
            if("success".equals(jsonObject.getJSONObject("msgCode").getString("type"))){
                String jessionId = createCodeKey.getFirstHeader("Set-Cookie").getValue().split(";")[0];
                String vmsId = createCodeKey.getLastHeader("Set-Cookie").getValue().split(";")[0];
                returnString = jessionId+";"+vmsId;
            }
        }else{
            if(StringUtils.isEmpty(content)){
                String jessionId = createCodeKey.getFirstHeader("Set-Cookie").getValue().split(";")[0];
                String vmsId = createCodeKey.getLastHeader("Set-Cookie").getValue().split(";")[0];
                returnString = jessionId+";"+vmsId;
            }
        }
        httpPost.abort();
        client.close();
        return returnString;
    }


    public static JSONObject getHtmlHttpPostJsonWithToken(String url,Map<String,String> parameterMap,String username) throws ClientProtocolException, IOException
    {
        HttpClientContext context = new HttpClientContext();
        CloseableHttpClient client = Tools.createSSLClientDefault();
        HttpPost httpPost = new HttpPost(url);
        //httpPost.setHeader("token", token);
        httpPost.setHeader("Cookie", cookieMap.get(username));
        UrlEncodedFormEntity postEntity = new UrlEncodedFormEntity(getParam(parameterMap), "UTF-8");
        httpPost.setEntity(postEntity);
        RequestConfig requestConfig = RequestConfig.custom().setRedirectsEnabled(false).build();
        httpPost.setConfig(requestConfig);
        httpPost = setPostInfo(httpPost);
        HttpResponse createCodeKey = client.execute(httpPost, context);
        String content = EntityUtils.toString(createCodeKey.getEntity());
        System.out.println(content);
        JSONObject jsonObject = JSONObject.fromObject(content);
        return jsonObject;
    }

    public static JSONObject getHtmlHttpGetContent(String url,String username) throws ClientProtocolException, IOException
    {
        HttpClientContext context = new HttpClientContext();
        CloseableHttpClient client =Tools.createSSLClientDefault();
        HttpGet httpGet = new HttpGet(url);
        httpGet = setGetInfo(httpGet);
        httpGet.setHeader("Cookie", cookieMap.get(username));

        HttpResponse createCodeKey = client.execute(httpGet, context);
        String content = EntityUtils.toString(createCodeKey.getEntity());
        System.out.println(content);
        JSONObject jsonObject = JSONObject.fromObject(content);
        return jsonObject;
    }

    public static boolean loginVms(String username,String password){
        Map<String,String> map = new HashMap<>();
        map.put("actorVO.id",username);
        map.put("actorVO.password",password);
        map.put("actorVO.logonTyp","2");

        try {
            String value = cookieMap.get("VMS_"+username);
            if(value==null||value.indexOf("day_"+ DateUtils.formatDate(new Date(),"yyyyMMdd"))==-1){
                String result = getHtmlHttpPostContent(VMS_URL+VMS_LOGIN,map);
                if(StringUtils.isNotEmpty(result)){
                    cookieMap.put("VMS_"+username,result+";"+"day_"+ DateUtils.formatDate(new Date(),"yyyyMMdd"));
                    return true;
                }
            }else{
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean loginSrcs(String username,String password){
        Map<String,String> map = new HashMap<>();
        map.put("userName",username);
        map.put("checkCode",password);

        try {
            String value = cookieMap.get("SCRS_"+username);
            if(value==null||value.indexOf("day_"+ DateUtils.formatDate(new Date(),"yyyyMMdd"))==-1){
                String result = getHtmlHttpPostContent(SCRS_URL+SCRS_LOGIN,map);
                if(StringUtils.isNotEmpty(result)){
                    cookieMap.put("SCRS_"+username,result+";"+"day_"+ DateUtils.formatDate(new Date(),"yyyyMMdd"));
                    return true;
                }
            }else{
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) throws Exception{
      /* if(!loginVms("31999999","123456")){
            System.out.println("登录失败");
            return;
        }*/

     /*   Map<String,String> map1 = new HashMap<>();
        map1.put("procVO.boxprocprocdat","20191025");
        map1.put("procVO.boxprocprocod","20191025");
        map1.put("procVO.boxprocbranch","319999");
       // map1.put("procVO.boxprockind","02");
        map1.put("procVO.boxprocsts","2");//必填 款箱类型
        map1.put("procVO.boxproctyp","4");
        map1.put("page.pageInfo.rowsOfPage","15");
        map1.put("page.pageInfo.currentPageNum","1");
        map1.put("order.sortOrder","desc");

        JSONObject j = getHtmlHttpPostJsonWithToken("http://10.233.93.55:334"+VMS_BOX_EVENT,map1,"VMS_31999999");
        System.out.println(j);*/

    /* Map<String,String> map1 = new HashMap<>();
        map1.put("page.pageInfo.rowsOfPage","15");
        map1.put("page.pageInfo.currentPageNum","1");
        map1.put("order.sortOrder","desc");
        map1.put("staffVO.orgId","999999");
        JSONObject j = getHtmlHttpPostJsonWithToken("http://10.233.93.55:334"+VMS_STAFF,map1,"VMS_31999999");
        System.out.println(j);*/


       /* Map<String,String> map2 = new HashMap<>();
        map2.put("organizationVO.orgId","999999");
        map2.put("page.pageInfo.rowsOfPage","15");
        map2.put("page.pageInfo.currentPageNum","1");
        map2.put("order.sortOrder","desc");
        JSONObject j1 = getHtmlHttpPostJsonWithToken("http://10.233.93.55:334"+VMS_ORGANIZATION,map2,"VMS_31999999");
        System.out.println(j1);*/
        //JSONObject b = getHtmlHttpGetContent("http://10.233.93.110:381/scrs/rest/basemanage/scuser/page?user_id=&user_branch=&branchId=&userRole=&pageInfo.currentPageNum=1&pageInfo.rowsOfPage=10","SCRS_31999902");
       // System.out.println(b);

        /*if(!loginSrcs("31010101","usap1234")){
            System.out.println("登录失败");
            return;
        }
        Map<String,String> map = new HashMap<>();
        map.put("route_id","");
        map.put("pageInfo.currentPageNum","1");
        map.put("pageInfo.rowsOfPage","100");
        map.put("flowId","1");
        map.put("pwd_holder","");
        map.put("route_id_para","");
        map.put("returner_id","");
        map.put("nextFunc","e100007");
        JSONObject j = getHtmlHttpPostJsonWithToken(SCRS_URL+SCRS_LINE_INFO,map,"SCRS_31010101");
        System.out.println(j);*/


    }
}


