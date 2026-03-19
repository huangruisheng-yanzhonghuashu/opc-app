package com.opc.mobile.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.opc.common.core.controller.BaseController;

/**
 * 移动端接口基础控制器
 * 
 * @author opc
 */
@RestController
@RequestMapping("/mobile")
public class MobileBaseController extends BaseController
{
    // 移动端接口公共方法可以放在这里
}
