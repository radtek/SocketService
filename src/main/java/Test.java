import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by zcf on 2015/5/20.
 */
public class Test {
    public static String sendRequest(String requestUrl,String requestContext,String paramName){

        try{
            System.out.println("fct requestJson:" + requestContext);
//            byte[] sendData = requestContext.getBytes();
            String param = paramName+"=" + URLEncoder.encode(requestContext, "utf-8");
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(1000000);
            // 如果通过post提交数据，必须设置允许对外输出数据

            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Charset", "UTF-8");
//            conn.setRequestProperty("Content-Length", String.valueOf(sendData.length));
            OutputStream outStream = conn.getOutputStream();
            DataOutputStream dataout = new DataOutputStream(outStream);
            dataout.writeBytes(param);
//            outStream.write(param);
            dataout.flush();
            dataout.close();
            System.out.println("fct getResponseCode:" + conn.getResponseCode());
            if (conn.getResponseCode() == 200) {
                // 获得服务器响应的数据
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                // 返回的数据
                String retData = null;
                String responseData = "";
                while ((retData = in.readLine()) != null) {
                    responseData += retData;
                }
                in.close();
                System.out.println("fct responseData" + responseData);

                return responseData;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws Exception
    {
        String authority = "{'sysid':'GD_TZ_DB','businessid':'','conectid':'ar3CNYAroSB3rDlFtfWxQGJd4auVv4D9GW9aIx2A3H+rCjDrNAC5hZM+BQbh7+Zy'}";
        String verifyCodeContext = "{'sysid':'GD_TZ_DB','orderid':'12313','mobileid':'13720387128','code':'','yz_code':'','zn_code':'','startime':'2015-05-23 14:03:10'}";
        String authorityUrl = "http://localhost:8080/api/kf/AuthoritySvc";
        String verifyCodeUrl = "http://localhost:8080/api/kf/SendVerifyCode";
        String scoreDeUrl = "http://localhost:8080/api/kf/ScoreDeductGTD";

//        Test.httpURLConnectionPOST(authorityUrl,authority,"authority");
        String aa = Test.sendRequest(authorityUrl, authority,"authority");
//        System.out.println("用户认证："+aa);
        JSONObject jsonObject2 = new JSONObject(aa);
        String result = jsonObject2.get("result").toString();
//        if(Integer.parseInt(result) == 1) {
//            String bb = Test.sendRequest(verifyCodeUrl, verifyCodeContext, "issueGdString");
//            System.out.println("验证码：" + bb);
//
//            JSONObject jsonObject = new JSONObject(bb);
//            System.out.println(jsonObject.get("yz_code"));
//
//            String code = jsonObject.get("yz_code").toString();
//            if(!code.isEmpty()) {
//                String coreDeContext = "{'sysid':'GD_TZ_DB','orderid':'12315','mobileid':'13720387128','code':'','yz_code':'22222'," +
//                        "'zn_code':'','startime':'2015-05-23 14:03:10','totalmoney':'','money':'100'}";
//                System.out.println("积分扣减：" + coreDeContext);
//                String cc = Test.sendRequest(scoreDeUrl, coreDeContext, "issueGdString");
//                System.out.println("积分扣减：" + cc);
//            }
//
//        }

//        int aa = (int) Math.ceil(1.25*13);
//       System.out.println(aa);

    }
}
