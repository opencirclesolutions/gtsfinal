package com.ocs.gts.ui;

import com.ocs.dynamo.domain.model.AttributeModel;
import com.ocs.dynamo.domain.model.EntityModel;
import com.ocs.dynamo.ui.composite.form.DetailsEditTable;
import com.ocs.dynamo.ui.composite.form.FormOptions;
import com.ocs.dynamo.ui.composite.form.ModelBasedEditForm;
import com.ocs.dynamo.ui.composite.layout.FlexibleSearchLayout;
import com.ocs.dynamo.ui.composite.type.AttributeGroupMode;
import com.ocs.dynamo.ui.container.QueryType;
import com.ocs.dynamo.ui.view.LazyBaseView;
import com.ocs.gts.domain.Organization;
import com.ocs.gts.domain.Person;
import com.ocs.gts.service.OrganizationService;
import com.ocs.gts.service.PersonService;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.Like;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.Layout;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

@UIScope
@SpringView(name = Views.ORGANIZATION_VIEW)
public class OrganizationView extends LazyBaseView {

	@Autowired
	private OrganizationService organizationService;

	@Autowired
	private PersonService personService;

	private static final long serialVersionUID = 3310122000037867336L;

	private FlexibleSearchLayout<Integer, Organization> layout;

	@Override
	public Component build() {

		EntityModel<Organization> em = getModelFactory().getModel(Organization.class);
		FormOptions fo = new FormOptions().setOpenInViewMode(true).setShowEditButton(true)
		        .setAttributeGroupMode(AttributeGroupMode.PANEL).setShowRemoveButton(true).setTableExportAllowed(true);
		layout = new FlexibleSearchLayout<Integer, Organization>(organizationService, em, QueryType.ID_BASED, fo, null) {

			private static final long serialVersionUID = 7072140085846781415L;

			@Override
			protected boolean isEditAllowed() {
				return true;
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
				navigateButton.addClickListener( event -> {
						GtsUI ui = (GtsUI) UI.getCurrent();
						ui.setSelectedOrganization(getSelectedItem());
						navigate(Views.ORGANIZATION_DETAIL_VIEW);
					}
				);

				buttonBar.addComponent(navigateButton);
				registerButton(navigateButton);
			}

			@Override
			protected Field<?> constructCustomField(EntityModel<Organization> entityModel,
			        AttributeModel attributeModel, boolean viewMode, boolean searchMode) {
				if ("members".equals(attributeModel.getName()) && !searchMode) {
					FormOptions fo = new FormOptions().setReadOnly(true);

					// create the table - notice how we pass the "viewMode" parameter
					DetailsEditTable<Integer, com.ocs.gts.domain.Person> dt = new DetailsEditTable<Integer, Person>(
					        getSelectedItem().getMembers(), getEntityModelFactory().getModel(Person.class), viewMode,
					        fo) {

						private static final long serialVersionUID = 7684008744369426053L;

						@Override
						protected Person createEntity() {
							// not needed
							return null;
						}

						@Override
						protected void removeEntity(Person toRemove) {
							// not needed
						}

					};
					dt.setService(personService);
					return dt;
				}
				return null;
			}

			@Override
			protected void postProcessEditFields(ModelBasedEditForm<Integer, Organization> editForm) {
				Field<?> reputation = editForm.getField("reputation");
				final Field<?> yearlyMortality = editForm.getField("yearlyMortalityRate");

				reputation.addValueChangeListener( event -> yearlyMortality.setValue(null));
			}

		};

		Map<String, Filter> fieldFilters = new HashMap<>();
		fieldFilters.put("countryOfOrigin", new Like("name", "%au%", false));
		layout.setFieldFilters(fieldFilters);

		layout.addBasicStringFilterProperty("name");

		return layout;
	}

	@Override
	protected void refresh() {
		layout.refresh();
	}
}
