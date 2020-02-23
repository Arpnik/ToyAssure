package com.increff.assure.model.forms;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
public class OrderFilterForm {
    Date startDate;
    Date endDate;
}
