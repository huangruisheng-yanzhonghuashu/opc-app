package com.opc.core.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.opc.core.domain.CorePackage;
import com.opc.core.mapper.CorePackageMapper;
import com.opc.core.service.ICorePackageService;

@Service
public class CorePackageServiceImpl implements ICorePackageService
{
    @Autowired
    private CorePackageMapper packageMapper;

    @Override
    public List<CorePackage> selectPackageList(CorePackage pkg)
    {
        return packageMapper.selectPackageList(pkg);
    }

    @Override
    public CorePackage selectPackageById(Long id)
    {
        return packageMapper.selectPackageById(id);
    }

    @Override
    public int insertPackage(CorePackage pkg)
    {
        if (pkg.getStatus() == null || pkg.getStatus().isEmpty()) {
            pkg.setStatus("0");
        }
        return packageMapper.insertPackage(pkg);
    }

    @Override
    public int updatePackage(CorePackage pkg)
    {
        return packageMapper.updatePackage(pkg);
    }

    @Override
    public int deletePackageById(Long id)
    {
        return packageMapper.deletePackageById(id);
    }

    @Override
    public int deletePackageByIds(Long[] ids)
    {
        return packageMapper.deletePackageByIds(ids);
    }
}
