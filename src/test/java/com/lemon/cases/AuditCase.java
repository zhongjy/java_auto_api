package com.lemon.cases;

import com.lemon.constants.Constants;
import com.lemon.pojo.CaseInfo;
import com.lemon.utils.AuthenticationUtils;
import com.lemon.utils.ExcelUtils;
import com.lemon.utils.HttpUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Map;

/**
 * @author luojie
 * @date 2020/6/11 - 21:34
 * 柠檬班创新教育极致服务
 *
 * 新增项目用例
 */
public class AuditCase extends BaseCase {

    @Test(dataProvider = "datas")
    public void test(CaseInfo caseInfo) {
        //  1、参数化替换
        paramsReplace(caseInfo);
        //	2、数据库前置查询结果(数据断言必须在接口执行前后都查询)
        //  2.1、获取带token的请求头
        Map<String, String> headers = AuthenticationUtils.getTokenHeader();
        //	3、调用接口
        HttpResponse response = HttpUtils.call(caseInfo.getMethod(),caseInfo.getContentType(),
                caseInfo.getUrl(),caseInfo.getParams(),headers);
        String body = HttpUtils.printResponse(response);
        //	4、断言响应结果
        //{"$.code":0,"$.msg":"OK","$.data.mobile_phone":"15670890431"}
        boolean assertResponseFlag = assertResponse(body, caseInfo.getExpectResult());

        //	5、添加接口响应回写内容
        addWriteBackData(caseInfo.getId(),Constants.RESPONSE_WRTIE_BACK_CELLNUM,body);
        //	6、数据库后置查询结果
        //	7、据库断言
        //	8、添加断言回写内容
        String assertResult = assertResponseFlag ? "passed" : "failed";
        addWriteBackData(caseInfo.getId(),Constants.ASSERT_WRTIE_BACK_CELLNUM,assertResult);
        //	9、添加日志

        //	10、报表断言
        Assert.assertEquals(assertResult,"passed");
    }

    /**
     * 数据库断言
     * @param caseInfo              caseInfo 对象
     * @param beforeSqlResult       sql前置查询结果
     * @param afterSqlResult        sql后置查询结果
     * @return                      断言结果
     */
    public boolean sqlAssert(CaseInfo caseInfo, Object beforeSqlResult, Object afterSqlResult) {
        boolean flag = false;
        if(StringUtils.isNotBlank(caseInfo.getSql())) {
            if (beforeSqlResult == null || afterSqlResult == null) {
                System.out.println("数据库断言失败");
            } else {
                Long l1 = (Long) beforeSqlResult;
                Long l2 = (Long) afterSqlResult;
                //接口执行之前查询结果为0，接口执行之后查询结果为1
                if (l1 == 0 && l2 == 1) {
                    System.out.println("数据库断言成功");
                    flag = true;
                } else {
                    System.out.println("数据库断言失败");
                }
            }
        }else {
            System.out.println("sql为空，不需要数据库断言");
        }
        return flag;
    }

    @DataProvider
    public Object[] datas() {
        Object[] datas = ExcelUtils.getDatas(sheetIndex, 1, CaseInfo.class);
        return datas;
    }

}
