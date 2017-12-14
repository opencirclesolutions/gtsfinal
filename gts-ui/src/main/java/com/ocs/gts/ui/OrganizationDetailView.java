package com.ocs.gts.ui;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.ocs.dynamo.domain.model.EntityModel;
import com.ocs.dynamo.ui.composite.layout.FormOptions;
import com.ocs.dynamo.ui.composite.layout.LazyTabLayout;
import com.ocs.dynamo.ui.composite.layout.ServiceBasedDetailLayout;
import com.ocs.dynamo.ui.composite.layout.SimpleEditLayout;
import com.ocs.dynamo.ui.container.QueryType;
import com.ocs.dynamo.ui.view.BaseView;
import com.ocs.gts.domain.Delivery;
import com.ocs.gts.domain.Organization;
import com.ocs.gts.domain.Person;
import com.ocs.gts.service.DeliveryService;
import com.ocs.gts.service.OrganizationService;
import com.ocs.gts.service.PersonService;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;
import com.vaadin.ui.Layout;
import com.vaadin.ui.UI;

@UIScope
@SpringView(name = Views.ORGANIZATION_DETAIL_VIEW)
public class OrganizationDetailView extends BaseView {

	private static final long serialVersionUID = 5368745165020200786L;

	@Autowired
	private OrganizationService organizationService;

	@Autowired
	private PersonService personService;

	@Autowired
	private DeliveryService deliveryService;

	@Override
	public void enter(ViewChangeEvent event) {
		Layout main = initLayout();

		final EntityModel<Organization> em = getModelFactory().getModel(Organization.class);

		GtsUI ui = ((GtsUI) UI.getCurrent());
		Organization org = ui.getSelectedOrganization();

		LazyTabLayout<Integer, Organization> tabLayout = new LazyTabLayout<Integer, Organization>(org) {

			private static final long serialVersionUID = 4843502488691996908L;

			@Override
			protected String createTitle() {
				return "View Organization";
			}

			@Override
			protected String[] getTabCaptions() {
				return new String[] { "Organization Details", "Members", "Deliveries" };
			}

			@Override
			protected Component initTab(int index) {
				switch (index) {
				case 0:
					FormOptions fo = new FormOptions().setOpenInViewMode(true).setEditAllowed(true);
					SimpleEditLayout<Integer, Organization> editLayout = new SimpleEditLayout<>(getEntity(),
							organizationService, em, fo);
					return editLayout;
				case 1:
					ServiceBasedDetailLayout<Integer, Person, Integer, Organization> layout = new ServiceBasedDetailLayout<Integer, Person, Integer, Organization>(
							personService, getEntity(), organizationService,
							getEntityModelFactory().getModel("OrganizationPerson", Person.class), QueryType.ID_BASED,
							new FormOptions(), null) {

						private static final long serialVersionUID = -2898632662487449765L;

						protected com.vaadin.data.Container.Filter constructFilter() {
							return new com.vaadin.data.util.filter.Compare.Equal("organization", getEntity());
						}

						protected Person createEntity() {
							Person p = super.createEntity();
							p.setOrganization(getEntity());
							return p;
						}
					};
					return layout;
				case 2:
					ServiceBasedDetailLayout<Integer, Delivery, Integer, Organization> deliveryLayout = new ServiceBasedDetailLayout<Integer, Delivery, Integer, Organization>(
							deliveryService, getEntity(), organizationService,
							getEntityModelFactory().getModel(Delivery.class), QueryType.ID_BASED, new FormOptions(),
							null) {

						private static final long serialVersionUID = -2898632662487449765L;

						protected com.vaadin.data.Container.Filter constructFilter() {
							return new com.vaadin.data.util.filter.Compare.Equal("fromPerson.organization",
									getEntity());
						}
					};

					Map<String, Filter> fieldFilters = new HashMap<>();
					fieldFilters.put("fromPerson", new Compare.Equal("organization", getEntity()));
					deliveryLayout.setFieldFilters(fieldFilters);

					return deliveryLayout;
				default:
					return null;
				}
			}
		};

		main.addComponent(tabLayout);
		setCompositionRoot(main);
	}
}
