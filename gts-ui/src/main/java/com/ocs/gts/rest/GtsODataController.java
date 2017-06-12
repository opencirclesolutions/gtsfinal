package com.ocs.gts.rest;

import com.ocs.dynamo.domain.model.EntityModel;
import com.ocs.dynamo.domain.model.EntityModelFactory;
import com.ocs.dynamo.odata.ODataController;
import com.ocs.gts.domain.Gift;
import com.ocs.gts.service.GiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GtsODataController extends ODataController{

    @Autowired
    private EntityModelFactory entityModelFactory;

    @Autowired
    private GiftService giftService;

    @Override
    protected void init(){
        EntityModel<Gift> giftEM = entityModelFactory.getModel(Gift.class);

    }


}
