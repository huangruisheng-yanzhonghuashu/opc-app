package com.opc.core.mapper;

import java.util.List;
import com.opc.core.domain.CoreFeedback;

public interface CoreFeedbackMapper
{
    public List<CoreFeedback> selectFeedbackList(CoreFeedback feedback);

    public CoreFeedback selectFeedbackById(Long id);

    public int insertFeedback(CoreFeedback feedback);

    public int updateFeedback(CoreFeedback feedback);

    public int replyFeedback(CoreFeedback feedback);

    public int deleteFeedbackById(Long id);

    public int deleteFeedbackByIds(Long[] ids);
}
