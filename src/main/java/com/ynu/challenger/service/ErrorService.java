package com.ynu.challenger.service;

import com.ynu.challenger.PO.Error;
import com.ynu.challenger.PO.ErrorExample;
import com.ynu.challenger.VO.ErrorEasy;
import com.ynu.challenger.mapper.ErrorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.ynu.challenger.util.ErrorStaticConst.*;

@Service
public class ErrorService {
    @Autowired
    private ErrorMapper errorMapper;

    public List<ErrorEasy> getRoughError(int account){
        // 1.首先挑出待解决的问题
        ErrorExample errorExample = new ErrorExample();
        errorExample.createCriteria().andStateEqualTo(ERROR_APPEAR);
        List<Error> errors = errorMapper.selectByExample(errorExample);
        // 2.要显示的异常不能太多,最多三条,选取前三条
        List<ErrorEasy> errorList = new ArrayList<>();
        for (int i = 0; i < errors.size()&& i < 3 ; i++) {
            ErrorEasy errorEasy = new ErrorEasy();
            Error error = errors.get(i);
            errorEasy.setId(error.getId());
            errorEasy.setName(error.getMachinename());
            switch (error.getUrgency()){
                case ERROR_URGENCY_LOW:
                    errorEasy.setUrgency("一般");
                    break;
                case ERROR_URGENCY_MID:
                    errorEasy.setUrgency("重要");
                    break;
                default: // 即ERROR_URGENCY_HIGH
                    errorEasy.setUrgency("紧急");
                    break;
            }
            errorList.add(errorEasy);
        }
        // 返回结果
        return errorList;
    }
}
