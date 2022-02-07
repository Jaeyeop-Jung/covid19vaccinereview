package com.teamproject.covid19vaccinereview.utils;

import com.teamproject.covid19vaccinereview.aop.exception.customException.ParameterBindingException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

@Component
public class BindingParameterUtil {

    public void checkParameterBindingException(BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            StringBuilder stringBuilder = new StringBuilder();
            bindingResult.getFieldErrors().forEach(
                    error -> stringBuilder.append(error.getField() + " ")
            );
            throw new ParameterBindingException(stringBuilder.toString());
        }
    }

}
