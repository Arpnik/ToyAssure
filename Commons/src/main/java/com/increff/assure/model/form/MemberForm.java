package com.increff.assure.model.form;


import com.increff.assure.model.constants.MemberTypes;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Getter @Setter
public class MemberForm {

    @NotBlank(message = "Enter a valid Client/Customer Name")
    private String name;
    @NotNull(message="Select an appropriate Type")
    private MemberTypes type;

}
