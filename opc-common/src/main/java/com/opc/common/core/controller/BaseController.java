package com.opc.common.core.controller;

import java.beans.PropertyEditorSupport;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.opc.common.constant.HttpStatus;
import com.opc.common.core.domain.AjaxResult;
import com.opc.common.core.domain.model.LoginUser;
import com.opc.common.core.page.PageDomain;
import com.opc.common.core.page.TableDataInfo;
import com.opc.common.core.page.TableSupport;
import com.opc.common.utils.DateUtils;
import com.opc.common.utils.PageUtils;
import com.opc.common.utils.SecurityUtils;
import com.opc.common.utils.StringUtils;
import com.opc.common.utils.sql.SqlUtil;

/**
 * web层通用数据处理
 * 
 * @author opc
 */
public class BaseController
{
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 将前台传递过来的日期格式的字符串，自动转化为Date类型
     */
    @InitBinder
    public void initBinder(WebDataBinder binder)
    {
        // Date 类型转换
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport()
        {
            @Override
            public void setAsText(String text)
            {
                setValue(DateUtils.parseDate(text));
            }
        });
    }

    /**
     * 设置请求分页数据
     */
    protected void startPage()
    {
        PageUtils.startPage();
    }

    /**
     * 设置请求排序数据
     */
    protected void startOrderBy()
    {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        if (StringUtils.isNotEmpty(pageDomain.getOrderBy()))
        {
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            PageHelper.orderBy(orderBy);
        }
    }

    /**
     * 清理分页的线程变量
     */
    protected void clearPage()
    {
        PageUtils.clearPage();
    }

    /**
     * 响应请求分页数据
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected TableDataInfo getDataTable(List<?> list)
    {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(HttpStatus.SUCCESS);
        rspData.setMsg("查询成功");
        rspData.setRows(list);
        rspData.setTotal(new PageInfo(list).getTotal());
        return rspData;
    }

    /**
     * 返回成功
     */
    public AjaxResult success()
    {
        return AjaxResult.success();
    }

    /**
     * 返回失败消息
     */
    public AjaxResult error()
    {
        return AjaxResult.error();
    }

    /**
     * 返回成功消息
     */
    public AjaxResult success(String message)
    {
        return AjaxResult.success(message);
    }
    
    /**
     * 返回成功消息
     */
    public AjaxResult success(Object data)
    {
        return AjaxResult.success(data);
    }

    /**
     * 返回失败消息
     */
    public AjaxResult error(String message)
    {
        return AjaxResult.error(message);
    }

    /**
     * 返回警告消息
     */
    public AjaxResult warn(String message)
    {
        return AjaxResult.warn(message);
    }

    /**
     * 响应返回结果
     * 
     * @param rows 影响行数
     * @return 操作结果
     */
    protected AjaxResult toAjax(int rows)
    {
        return rows > 0 ? AjaxResult.success() : AjaxResult.error();
    }

    /**
     * 响应返回结果
     * 
     * @param result 结果
     * @return 操作结果
     */
    protected AjaxResult toAjax(boolean result)
    {
        return result ? success() : error();
    }

    /**
     * 页面跳转
     */
    public String redirect(String url)
    {
        return StringUtils.format("redirect:{}", url);
    }

    /**
     * 获取用户缓存信息
     */
    public LoginUser getLoginUser()
    {
        return SecurityUtils.getLoginUser();
    }

    /**
     * 获取登录用户id
     */
    public Long getUserId()
    {
        return getLoginUser().getUserId();
    }

    /**
     * 获取登录部门id
     */
    public Long getDeptId()
    {
        return getLoginUser().getDeptId();
    }

    /**
     * 获取登录用户名
     */
    public String getUsername()
    {
        return getLoginUser().getUsername();
    }

    /**
     * 检查套餐权限
     *
     * @param currentPackageLevel 当前会员套餐等级
     * @param category 素材分类
     * @return 是否有权限
     */
    protected boolean checkPackagePermission(Integer currentPackageLevel, String category)
    {
        // 如果会员没有套餐等级，则只能查看免费内容
        if (currentPackageLevel == null)
        {
            currentPackageLevel = 0;
        }

        // 获取分类所需的套餐等级
        Integer requiredLevel = getCategoryRequiredLevel(category);

        // 判断当前套餐等级是否满足要求
        return currentPackageLevel >= requiredLevel;
    }

    /**
     * 获取分类所需的套餐等级
     *
     * @param category 素材分类
     * @return 所需套餐等级
     */
    protected Integer getCategoryRequiredLevel(String category)
    {
        // 定义分类和套餐等级的映射关系
        Map<String, Integer> categoryLevelMap = new HashMap<>();
        categoryLevelMap.put("free", 0);              // 免费内容
        categoryLevelMap.put("normal", 1);            // 普通会员
        categoryLevelMap.put("morning_report", 1);    // 晨报
        categoryLevelMap.put("vip", 2);               // VIP会员内容
        categoryLevelMap.put("svip", 3);              // 超级VIP内容

        // 默认返回普通会员等级
        return categoryLevelMap.getOrDefault(category, 1);
    }
}
