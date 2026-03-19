package com.opc.core.service;

import java.util.List;
import com.opc.core.domain.CorePackage;

public interface ICorePackageService
{
    public List<CorePackage> selectPackageList(CorePackage pkg);

    public CorePackage selectPackageById(Long id);

    public int insertPackage(CorePackage pkg);

    public int updatePackage(CorePackage pkg);

    public int deletePackageById(Long id);

    public int deletePackageByIds(Long[] ids);
}
