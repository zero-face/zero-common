package com.zero.common.validator;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * @author: Zero
 * @Date: 2021/4/3 - 22:42
 * @since: jdk 1.8
 */
@Component
public class ValidatorImpl implements InitializingBean {

    private Validator validator;

    //是实现校验方法并且返回校验结果
    public ValidationResult validate(Object bean) {
        final ValidationResult validationResult = new ValidationResult();
        //该方法中如果有违背了bean中的定义的话，就会将该信息进行返回
        Set<ConstraintViolation<Object>> constraintViolationsSet = validator.validate(bean);
        if(constraintViolationsSet.size() > 0) {
            validationResult.setHasErrs(true);
            constraintViolationsSet.forEach(constraintViolation->{
                //获取错误信息
                String errMsg = constraintViolation.getMessage();
                //获取出错的字段
                String propertyName =constraintViolation.getPropertyPath().toString();

                validationResult.getErrMsgMap().put(propertyName,errMsg);
            });
        }
        return validationResult;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        //将hibernate validator通过工厂的初始化方式使其实例化
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }
}
