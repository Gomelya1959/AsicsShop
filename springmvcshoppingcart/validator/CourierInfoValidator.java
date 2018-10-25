package org.springmvcshoppingcart.validator;

import org.springmvcshoppingcart.dao.CourierDAO;
import org.springmvcshoppingcart.entity.Courier;
import org.springmvcshoppingcart.model.CourierInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

// @Component: As a Bean.
@Component
public class CourierInfoValidator implements Validator {

    @Autowired
    private CourierDAO courierDAO;

    // This Validator support ProductInfo class.
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == CourierInfo.class;
    }

    @Override
    public void validate(Object target, Errors errors) {
        CourierInfo courierInfo = (CourierInfo) target;

        // Check the fields of ProductInfo class.
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "id", "NotEmpty.courierForm.id");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "NotEmpty.courierForm.name");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "phone", "NotEmpty.courierForm.phone");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "user_name", "NotEmpty.courierForm.courier_name");

        String id = courierInfo.getId();
        if (id != null && id.length() > 0) {
            if (id.matches("\\s+")) {
                errors.rejectValue("id", "Pattern.courierForm.id");
            } else if(courierInfo.isNewCourier()) {
                Courier courier = courierDAO.findCourier(id);
                if (courier != null) {
                    errors.rejectValue("id", "Duplicate.courierForm.id");
                }
            }
        }
    }

}
