package com.opc.web.controller.core;

import java.util.List;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.opc.common.annotation.Log;
import com.opc.common.core.controller.BaseController;
import com.opc.common.core.domain.AjaxResult;
import com.opc.common.enums.BusinessType;
import com.opc.core.domain.CoreActivationCode;
import com.opc.core.service.ICoreActivationCodeService;
import com.opc.common.utils.poi.ExcelUtil;
import com.opc.common.core.page.TableDataInfo;

/**
 * 激活码Controller
 *
 * @author opc
 */
@RestController
@RequestMapping("/core/activationCode")
public class CoreActivationCodeController extends BaseController {

    @Autowired
    private ICoreActivationCodeService coreActivationCodeService;

    /**
     * 查询激活码列表
     */
    @PreAuthorize("@ss.hasPermi('core:activationCode:list')")
    @GetMapping("/list")
    public TableDataInfo list(CoreActivationCode coreActivationCode) {
        startPage();
        List<CoreActivationCode> list = coreActivationCodeService.selectCoreActivationCodeList(coreActivationCode);
        return getDataTable(list);
    }

    /**
     * 导出激活码列表
     */
    @PreAuthorize("@ss.hasPermi('core:activationCode:export')")
    @Log(title = "激活码", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CoreActivationCode coreActivationCode) {
        List<CoreActivationCode> list = coreActivationCodeService.selectCoreActivationCodeList(coreActivationCode);
        ExcelUtil<CoreActivationCode> util = new ExcelUtil<>(CoreActivationCode.class);
        util.exportExcel(response, list, "激活码数据");
    }

    /**
     * 获取激活码详细信息
     */
    @PreAuthorize("@ss.hasPermi('core:activationCode:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(coreActivationCodeService.selectCoreActivationCodeById(id));
    }

    /**
     * 批量生成激活码
     *
     * @param count      生成数量
     * @param validDays  有效天数
     * @param channelTag 渠道标签
     */
    @PreAuthorize("@ss.hasPermi('core:activationCode:add')")
    @Log(title = "激活码", businessType = BusinessType.INSERT)
    @PostMapping("/generate")
    public AjaxResult generate(@RequestParam("count") int count,
                               @RequestParam("validDays") int validDays,
                               @RequestParam("channelTag") String channelTag) {
        if (count <= 0 || count > 1000) {
            return error("生成数量必须在1-1000之间");
        }
        if (validDays <= 0) {
            return error("有效天数必须大于0");
        }
        int result = coreActivationCodeService.batchGenerateActivationCode(count, validDays, channelTag);
        return toAjax(result);
    }

    /**
     * 修改激活码（主要用于修改渠道标签）
     */
    @PreAuthorize("@ss.hasPermi('core:activationCode:edit')")
    @Log(title = "激活码", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CoreActivationCode coreActivationCode) {
        return toAjax(coreActivationCodeService.updateCoreActivationCode(coreActivationCode));
    }

    /**
     * 删除激活码
     */
    @PreAuthorize("@ss.hasPermi('core:activationCode:remove')")
    @Log(title = "激活码", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(coreActivationCodeService.deleteCoreActivationCodeByIds(ids));
    }

    /**
     * 发送激活码
     */
    @PreAuthorize("@ss.hasPermi('core:activationCode:send')")
    @Log(title = "激活码", businessType = BusinessType.UPDATE)
    @PutMapping("/send/{ids}")
    public AjaxResult send(@PathVariable Long[] ids) {
        return toAjax(coreActivationCodeService.sendActivationCode(ids));
    }

    /**
     * 注销激活码
     */
    @PreAuthorize("@ss.hasPermi('core:activationCode:cancel')")
    @Log(title = "激活码", businessType = BusinessType.UPDATE)
    @PutMapping("/cancel/{ids}")
    public AjaxResult cancel(@PathVariable Long[] ids) {
        return toAjax(coreActivationCodeService.cancelActivationCode(ids));
    }

}
