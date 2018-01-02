package com.ocs.gts.ui;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.ocs.dynamo.domain.model.AttributeModel;
import com.ocs.dynamo.domain.model.EntityModel;
import com.ocs.dynamo.ui.composite.dialog.ModelBasedSearchDialog;
import com.ocs.dynamo.ui.composite.form.DetailsEditTable;
import com.ocs.dynamo.ui.composite.form.ModelBasedEditForm;
import com.ocs.dynamo.ui.composite.layout.FlexibleSearchLayout;
import com.ocs.dynamo.ui.composite.layout.FormOptions;
import com.ocs.dynamo.ui.composite.type.AttributeGroupMode;
import com.ocs.dynamo.ui.container.QueryType;
import com.ocs.dynamo.ui.view.LazyBaseView;
import com.ocs.gts.domain.Organization;
import com.ocs.gts.domain.Person;
import com.ocs.gts.service.OrganizationService;
import com.ocs.gts.service.PersonService;
import com.vaadin.data.Container.Filter;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.UI;

@UIScope
@SpringView(name = Views.ORGANIZATION_FLEX_VIEW)
public class OrganizationFlexView extends LazyBaseView {

	@Autowired
	private OrganizationService organizationService;

	@Autowired
	private PersonService personService;

	private static final long serialVersionUID = 3310122000037867336L;

	private FlexibleSearchLayout<Integer, Organization> layout;

	private Button addButton;

	@Override
	public Component build() {

		EntityModel<Organization> em = getModelFactory().getModel("OrganizationFlex", Organization.class);
		FormOptions fo = new FormOptions().setEditAllowed(true).setAttributeGroupMode(AttributeGroupMode.PANEL)
				.setShowRemoveButton(true).setTableExportAllowed(true).setShowIterationButtons(true)
				.setOpenInViewMode(true);
		layout = new FlexibleSearchLayout<Integer, Organization>(organizationService, em, QueryType.ID_BASED, fo,
				null) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void afterModeChanged(boolean viewMode, ModelBasedEditForm<Integer, Organization> editForm) {
				addButton.setVisible(!viewMode);
			}

			@Override
			protected Field<?> constructCustomField(EntityModel<Organization> entityModel,
					AttributeModel attributeModel, boolean viewMode, boolean searchMode) {
				if ("members".equals(attributeModel.getPath())) {
					DetailsEditTable<Integer, Person> dt = new DetailsEditTable<Integer, Person>(
							getSelectedItem().getMembers(), getEntityModelFactory().getModel(Person.class), true,
							new FormOptions()) {

						private static final long serialVersionUID = 8981968594671156215L;

						@Override
						protected Person createEntity() {
							// TODO Auto-generated method stub
							return null;
						}

						@Override
						protected void removeEntity(Person toRemove) {
							// TODO Auto-generated method stub

						}

						protected void postProcessButtonBar(com.vaadin.ui.Layout buttonBar) {
							addButton = new Button("Add");
							addButton.addClickListener(e -> {
								ModelBasedSearchDialog<Integer, Person> sf = new ModelBasedSearchDialog<Integer, Person>(
										personService, getEntityModelFactory().getModel(Person.class), null, null, true,
										false) {

									private static final long serialVersionUID = -8935034587122661521L;

									@Override
									public boolean doClose() {
										for (Person p : getSelectedItems()) {
											layout.getSelectedItem().addMember(p);
											setValue(layout.getSelectedItem().getMembers());
										}

										return true;
									}
								};

								sf.build();
								UI.getCurrent().addWindow(sf);
							});
							buttonBar.addComponent(addButton);
							addButton.setVisible(!isViewMode());
						};
					};
					dt.setService(personService);

					return dt;
				}
				return null;
			}
		};

		Map<String, Filter> fieldFilters = new HashMap<>();
		layout.setFieldFilters(fieldFilters);
		layout.setFormTitleWidth(400);
		return layout;
	}

	@Override
	protected void refresh() {
		layout.refresh();
	}
}
