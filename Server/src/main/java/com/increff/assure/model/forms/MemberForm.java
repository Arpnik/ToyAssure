package com.increff.assure.model.forms;


import com.increff.assure.model.constants.MemberTypes;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Getter
@Setter
public class MemberForm {

    @NotBlank(message = "Please enter the correct client/customer name")
    private String name;
    @NotNull(message="Select an appropriate type")
    private MemberTypes type;

}
