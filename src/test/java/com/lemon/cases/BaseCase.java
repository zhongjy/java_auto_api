package com.lemon.cases;

import cn.binarywang.tools.generator.ChineseIDCardNumberGenerator;
import cn.binarywang.tools.generator.ChineseMobileNumberGenerator;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.lemon.constants.Constants;
import com.lemon.pojo.CaseInfo;
import com.lemon.pojo.WriteBackData;
import com.lemon.utils.AuthenticationUtils;
import com.lemon.utils.ExcelUtils;
import io.qameta.allure.Step;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * @author luojie
 * @date 2020/6/20 - 10:43
 * 柠檬班创新教育极致服务
 *
 * 用例父类
 */
public class BaseCase {
    public static void main(String[] args) throws Exception {
//        String mobileNumber = ChineseMobileNumberGenerator.getInstance().generate();
//        String iDCardNumber = ChineseIDCardNumberGenerator.getInstance().generate();
//        System.out.println("mobileNumber = " + mobileNumber);
//        System.out.println("iDCardNumber = " + iDCardNumber);

        Properties prop = new Properties();//Map
        //获取配置路径
        String path = BaseCase.class.getClassLoader().getResource("./params.properties").getPath();
        FileInputStream fis = new FileInputStream(path);
        //读取配置文件中的内容并添加到prop中
        prop.load(fis);
        fis.close();
        //把prop中所有的内容一次性放入VARS
        AuthenticationUtils.VARS.putAll((Map)prop);
        logger.info("AuthenticationUtils.VARS==========="+AuthenticationUtils.VARS);

    }
    //读取testng.xml sheetIndex参数
    private static Logger logger = Logger.getLogger(BaseCase.class);


    public int sheetIndex;

    @BeforeSuite
    public void init() throws Exception {
        logger.info("======================init===================");
        Constants.HEADERS.put("X-Lemonban-Media-Type","lemonban.v2");
        Constants.HEADERS.put("Content-Type","application/json");
        //存入参数的变量
//        AuthenticationUtils.VARS.put("${amount}","3000");
//        AuthenticationUtils.VARS.put("${register_mb}","3000");
//        AuthenticationUtils.VARS.put("${register_pwd}","3000");
//        AuthenticationUtils.VARS.put("${login_mb}","3000");
//        AuthenticationUtils.VARS.put("${login_pwd}","3000");
        //创建properties对象
        Properties prop = new Properties();//Map
        //获取配置路径
        String path = BaseCase.class.getClassLoader().getResource("./params.properties").getPath();
        FileInputStream fis = new FileInputStream(path);
        //读取配置文件中的内容并添加到prop中
        prop.load(fis);
        fis.close();
        //把prop中所有的内容一次性放入VARS
        AuthenticationUtils.VARS.putAll((Map)prop);
        logger.info("AuthenticationUtils.VARS==========="+AuthenticationUtils.VARS);
    }

    @AfterSuite
    public void finish() {
        logger.info("======================finish===================");
        //执行批量回写
        ExcelUtils.batchWrite();
    }

    @BeforeClass
    @Parameters({"sheetIndex"})
    public void beforeClass(int sheetIndex) {
        //System.out.println("LoginCase ===== sheetIndex:=============" + sheetIndex);
        this.sheetIndex = sheetIndex;
    }

    /**
     * 创建回写对象,添加到批量回写集合中
     * @param rownum            行号
     * @param cellnum           列号
     * @param content           回写内容
     */
    public void addWriteBackData(int rownum,int cellnum, String content) {
        WriteBackData wbd =
                new WriteBackData(sheetIndex,rownum, cellnum,content);
        //添加到回写集合
        ExcelUtils.wbdList.add(wbd);
    }

    /**
     * 接口响应断言
     * @param body                  接口响应字符串
     * @param expectResult          excel中响应期望值
     * @return                      断言结果
     */
    @Step("assertResponse")
    public boolean assertResponse(String body, String expectResult) {
        //json转成map
        Map<String,Object> map = JSONObject.parseObject(expectResult, Map.class);
        Set<String> keySet = map.keySet();
        boolean assertResponseFlag = true;
        for (String expression : keySet) {
            //1、获取期望值
            Object expectValue = map.get(expression);
            //2、通过jsonpath找到实际值
            Object actualValue = JSONPath.read(body,expression);
            //3、比较期望值和实际值
            if(expectValue == null && actualValue != null) {
                assertResponseFlag = false;
                break;
            }
            if(expectValue == null && actualValue == null) {
                continue;
            }
            if(!expectValue.equals(actualValue)) {
                assertResponseFlag = false;
                break;
            }
        }
        System.out.println("响应断言结果：" + assertResponseFlag);
        return assertResponseFlag;
    }

    /**
     * 参数化替换方法
     * 1、excel填写占位符${member_id}
     * 2、BaseCase init准备占位符和实际值的对应关系，并保存在VARS  ${member_id} = 1
     * 3、每个Case第一行执行参数化替换操作。
     *
     * @param caseInfo      caseInfo对象
     */
    public void paramsReplace(CaseInfo caseInfo) {
        //sql:select leave_amount from member a where id = ${member_id};    11
        //params:{"member_id":"${member_id}","amount":"${amount}"}         11  3000
        //获取VARS中所有的key
        Set<String> keySet = AuthenticationUtils.VARS.keySet();
        //参数化替换遍历
        for (String key : keySet) {
            //key = ${member_id} value = 11
            //key是占位符，value是实际值
            String value = AuthenticationUtils.VARS.get(key).toString();
            //sql、params、expectResult
            //替换sql
            if(StringUtils.isNotBlank(caseInfo.getSql())) {
                String sql = caseInfo.getSql().replace(key, value);
                //把替换之后的sql重新设置到caseInfo中
                caseInfo.setSql(sql);
            }
            //替换params
            if(StringUtils.isNotBlank(caseInfo.getParams())) {
                String params = caseInfo.getParams().replace(key, value);
                //把替换之后的params重新设置到caseInfo中
                caseInfo.setParams(params);
            }
            //替换expectResult
            if(StringUtils.isNotBlank(caseInfo.getExpectResult())) {
                String expectResult = caseInfo.getExpectResult().replace(key, value);
                //把替换之后的expectResult重新设置到caseInfo中
                caseInfo.setExpectResult(expectResult);
            }
            //替换url
            if(StringUtils.isNotBlank(caseInfo.getUrl())) {
                String url = caseInfo.getUrl().replace(key, value);
                //把替换之后的expectResult重新设置到caseInfo中
                caseInfo.setUrl(url);
            }
        }
    }
}
