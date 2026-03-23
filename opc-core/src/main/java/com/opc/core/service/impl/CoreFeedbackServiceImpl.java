package com.opc.core.service.impl;

import java.time.Instant;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.opc.core.domain.CoreFeedback;
import com.opc.core.mapper.CoreFeedbackMapper;
import com.opc.core.service.ICoreFeedbackService;

@Service
public class CoreFeedbackServiceImpl implements ICoreFeedbackService
{
    @Autowired
    private CoreFeedbackMapper feedbackMapper;

    @Override
    public List<CoreFeedback> selectFeedbackList(CoreFeedback feedback)
    {
        return feedbackMapper.selectFeedbackList(feedback);
    }

    @Override
    public CoreFeedback selectFeedbackById(Long id)
    {
        return feedbackMapper.selectFeedbackById(id);
    }

    @Override
    public int insertFeedback(CoreFeedback feedback)
    {
        feedback.setStatus("0");
        return feedbackMapper.insertFeedback(feedback);
    }

    @Override
    public int updateFeedback(CoreFeedback feedback)
    {
        return feedbackMapper.updateFeedback(feedback);
    }

    @Override
    public int replyFeedback(CoreFeedback feedback)
    {
        feedback.setReplyTime(Instant.now());
        return feedbackMapper.replyFeedback(feedback);
    }

    @Override
    public int deleteFeedbackById(Long id)
    {
        return feedbackMapper.deleteFeedbackById(id);
    }

    @Override
    public int deleteFeedbackByIds(Long[] ids)
    {
        return feedbackMapper.deleteFeedbackByIds(ids);
    }
}
