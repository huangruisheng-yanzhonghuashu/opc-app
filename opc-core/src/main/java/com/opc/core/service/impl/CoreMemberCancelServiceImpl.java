package com.opc.core.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.opc.core.domain.CoreMemberCancel;
import com.opc.core.mapper.CoreMemberCancelMapper;
import com.opc.core.service.ICoreMemberCancelService;

@Service
public class CoreMemberCancelServiceImpl implements ICoreMemberCancelService
{
    @Autowired
    private CoreMemberCancelMapper memberCancelMapper;

    @Override
    public List<CoreMemberCancel> selectMemberCancelList(CoreMemberCancel memberCancel)
    {
        return memberCancelMapper.selectMemberCancelList(memberCancel);
    }

    @Override
    public CoreMemberCancel selectMemberCancelById(Long id)
    {
        return memberCancelMapper.selectMemberCancelById(id);
    }

    @Override
    public int insertMemberCancel(CoreMemberCancel memberCancel)
    {
        return memberCancelMapper.insertMemberCancel(memberCancel);
    }
}
