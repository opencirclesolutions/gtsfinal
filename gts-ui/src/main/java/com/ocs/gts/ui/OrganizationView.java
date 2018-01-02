package com.ocs.gts.ui;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.ocs.dynamo.domain.model.EntityModel;
import com.ocs.dynamo.ui.composite.dialog.EntityPopupDialog;
import com.ocs.dynamo.ui.composite.layout.FormOptions;
import com.ocs.dynamo.ui.composite.layout.SimpleSearchLayout;
import com.ocs.dynamo.ui.composite.type.AttributeGroupMode;
import com.ocs.dynamo.ui.container.QueryType;
import com.ocs.dynamo.ui.view.LazyBaseView;
import com.ocs.gts.domain.Organization;
import com.ocs.gts.service.OrganizationService;
import com.vaadin.data.Container.Filter;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Layout;
import com.vaadin.ui.UI;

@UIScope
@SpringView(name = Views.ORGANIZATION_VIEW)
public class OrganizationView extends LazyBaseView {

	@Autowired
	private OrganizationService organizationService;

	private static final long serialVersionUID = 3310122000037867336L;

	private SimpleSearchLayout<Integer, Organization> layout;

	@Override
	public Component build() {

		EntityModel<Organization> em = getModelFactory().getModel(Organization.class);
		FormOptions fo = new FormOptions().setEditAllowed(true).setAttributeGroupMode(AttributeGroupMode.PANEL)
				.setShowRemoveButton(true).setTableExportAllowed(true).setShowIterationButtons(true)
				.setOpenInViewMode(true);
		layout = new SimpleSearchLayout<Integer, Organization>(organizationService, em, QueryType.ID_BASED, fo, null) {

			private static final long serialVersionUID = 7072140085846781415L;

			@Override
			protected String[] getParentGroupHeaders() {
				return new String[] { "gts.organization.extra.1", "gts.organization.extra.2" };
			}

			@Override
			protected String getParentGroup(String childGroup) {
				if ("organization.first".equals(childGroup)) {
					return "gts.organization.extra.1";
				}
				return "gts.organization.extra.2";
			}

			@Override
			protected Organization createEntity() {
				Organization org = super.createEntity();
				org.setAddress("1234 Willoughby Road");
				return org;
			}

			@Override
			protected void postProcessButtonBar(Layout buttonBar) {
				Button navigateButton = new Button("Details");
				navigateButton.addClickListener(event -> {
					GtsUI ui = (GtsUI) UI.getCurrent();
					ui.setSelectedOrganization(getSelectedItem());
					navigate(Views.ORGANIZATION_DETAIL_VIEW);
				});

				buttonBar.addComponent(navigateButton);
				registerButton(navigateButton);

				Button popupButton = new Button("Popup");
				popupButton.addClickListener(event -> {
					FormOptions fo = new FormOptions().setOpenInViewMode(true).setEditAllowed(true);
					EntityPopupDialog<Integer, Organization> popupDialog = new EntityPopupDialog<Integer, Organization>(
							organizationService, getSelectedItem(),
							getEntityModelFactory().getModel(Organization.class), fo);
					popupDialog.build();
					UI.getCurrent().addWindow(popupDialog);
				});
				buttonBar.addComponent(popupButton);
				registerButton(popupButton);
			}

		};

		Map<String, Filter> fieldFilters = new HashMap<>();
		layout.setFieldFilters(fieldFilters);
		layout.setFormTitleWidth(400);
		layout.setNrOfColumns(2);
		return layout;
	}

	@Override
	protected void refresh() {
		layout.refresh();
	}
}
