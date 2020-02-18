//package com.increff.assure.dto;
//
//import com.increff.assure.dao.BinDao;
//import com.increff.assure.model.forms.BinFilterForm;
//import com.increff.assure.model.forms.CreateBinForm;
//import com.increff.assure.model.Exception.ApiException;
//import com.increff.assure.service.BinService;
//import com.increff.assure.util.AbstractUnitTest;
//import com.increff.assure.util.dataUtil;
//import org.junit.Before;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import static org.junit.Assert.assertEquals;
//
//public class BinDtoTest extends AbstractUnitTest {
//    @Autowired
//    BinDto dto;
//
//    @Autowired
//    BinService service;
//
//    @Autowired
//    BinDao binDao;
//
//    private CreateBinForm form;
//
//    @Before
//    public void init()
//    {
//        form= dataUtil.createNumberBinForm(12);
//    }
//
//    @Test
//    public void testCreateBin(){
//      //  dto.createBins(form);
//       assertEquals(form.getNumberOfBins(),binDao.selectAll().size());
//    }
//
//    @Test(expected = ApiException.class)
//    public void testValidateFormInvalid() throws ApiException {
//        BinFilterForm form=dataUtil.createBinFilterForm(0,0,null);
//        dto.validateForm(form);
//    }
//
//    @Test(expected = ApiException.class)
//    public void testValidateFormInvalidClientSku() throws ApiException {
//        BinFilterForm form=dataUtil.createBinFilterForm(1,1,"");
//        dto.validateForm(form);
//    }
//
//    @Test(expected = ApiException.class)
//    public void testValidateFormInvalidClientId() throws ApiException {
//        BinFilterForm form=dataUtil.createBinFilterForm(1,0,"shoes_black");
//        dto.validateForm(form);
//    }
//
//    @Test
//    public void testValidateForm() throws ApiException {
//        BinFilterForm form=dataUtil.createBinFilterForm(1,10,"shoes_black");
//        dto.validateForm(form);
//    }
//
//
//
//
//
//
//}
