package com.ocs.gts.ui;

import org.springframework.beans.factory.annotation.Autowired;

import com.ocs.dynamo.domain.model.EntityModel;
import com.ocs.dynamo.service.ServiceLocatorFactory;
import com.ocs.dynamo.service.UserDetailsService;
import com.ocs.dynamo.ui.auth.Authorized;
import com.ocs.dynamo.ui.composite.layout.FormOptions;
import com.ocs.dynamo.ui.composite.layout.ServiceBasedSplitLayout;
import com.ocs.dynamo.ui.container.QueryType;
import com.ocs.dynamo.ui.view.BaseView;
import com.ocs.gts.domain.Person;
import com.ocs.gts.service.PersonService;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.Like;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Notification;

@UIScope
@SpringView(name = Views.PERSON_VIEW)
@Authorized(roles = "admin")
public class PersonView extends BaseView {

	private static final long serialVersionUID = 5368745165020200786L;

	@Autowired
	private PersonService personService;

	@Override
	public void enter(ViewChangeEvent event) {
		Layout main = initLayout();

		EntityModel<Person> em = getModelFactory().getModel(Person.class);
		FormOptions fo = new FormOptions().setShowRemoveButton(true).setShowQuickSearchField(true);
		ServiceBasedSplitLayout<Integer, Person> layout = new ServiceBasedSplitLayout<Integer, Person>(personService,
				em, QueryType.ID_BASED, fo, null) {

			private static final long serialVersionUID = 5626784911606313434L;

			protected Filter constructQuickSearchFilter(String value) {
				return new Like("firstName", "%" + value + "%", false);
			}

			@Override
			protected boolean isEditAllowed() {
				return ServiceLocatorFactory.getServiceLocator().getService(UserDetailsService.class)
						.isUserInRole("super");
			}

			@Override
			protected void postProcessButtonBar(Layout buttonBar) {
				Button notificationButton = new Button("Show name");
				notificationButton.addClickListener(
						e -> Notification.show(getSelectedItem().getFullName(), Notification.Type.ERROR_MESSAGE));
				buttonBar.addComponent(notificationButton);
				registerButton(notificationButton);
			}

		};

		layout.setFormTitleWidth(200);

		main.addComponent(layout);
	}
}
