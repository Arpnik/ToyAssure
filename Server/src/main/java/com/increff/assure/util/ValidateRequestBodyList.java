package com.increff.assure.util;

import javax.validation.Valid;
import java.util.List;

public class ValidateRequestBodyList<T> {
    @Valid
    private List<T> requestBody;

    public List<T> getRequestBody() {
        return requestBody;
    }

}
