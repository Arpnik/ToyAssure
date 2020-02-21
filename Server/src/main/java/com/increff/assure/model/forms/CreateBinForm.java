package com.increff.assure.model.forms;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;


@Getter @Setter
public class CreateBinForm {
    @NotNull(message = "please enter a valid number of Bins to be created")
    @Positive(message = "Entered number of bins cannot be less than equal to zero")
    private Long numberOfBins;
}
