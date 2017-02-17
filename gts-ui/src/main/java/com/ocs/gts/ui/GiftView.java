package com.ocs.gts.ui;

import org.springframework.beans.factory.annotation.Autowired;

import com.ocs.dynamo.dao.query.FetchJoinInformation;
import com.ocs.dynamo.domain.model.EntityModel;
import com.ocs.dynamo.ui.composite.form.FormOptions;
import com.ocs.dynamo.ui.view.BaseView;
import com.ocs.gts.domain.Gift;
import com.ocs.gts.service.GiftService;
import com.ocs.gts.ui.layout.GiftLayout;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Layout;

@UIScope
@SpringView(name = Views.GIFT_VIEW)
public class GiftView extends BaseView {

	@Autowired
	private GiftService giftService;

	private static final long serialVersionUID = 3310122000037867336L;

	@Override
	public void enter(ViewChangeEvent event) {
		Layout main = initLayout();

		// EntityModel<Gift> em = getModelFactory().getModel(Gift.class);
		// FormOptions fo = new FormOptions();
		// ServiceBasedSplitLayout<Integer, Gift> layout = new ServiceBasedSplitLayout<Integer,
		// Gift>(giftService, em, fo,
		// null) {
		//
		// private static final long serialVersionUID = -6355732460885333319L;
		//
		// };
		// layout.setDetailJoins(new FetchJoinInformation[] { new FetchJoinInformation("logo") });

		EntityModel<Gift> em = getModelFactory().getModel(Gift.class);
		FormOptions fo = new FormOptions().setShowRemoveButton(true);
		GiftLayout layout = new GiftLayout(giftService, em, fo, null);
		layout.setDetailJoins(new FetchJoinInformation[] { new FetchJoinInformation("logo"),
				        new FetchJoinInformation("translations") });
		
		main.addComponent(layout);
	}

}
