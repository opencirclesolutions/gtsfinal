package com.ocs.gts.ui;

import java.util.ArrayList;
import java.util.List;

import com.ocs.dynamo.functional.domain.Country;
import com.ocs.dynamo.functional.domain.Domain;
import com.ocs.dynamo.functional.domain.Region;
import com.ocs.dynamo.functional.ui.MultiDomainEditLayout;
import com.ocs.dynamo.ui.composite.layout.FormOptions;
import com.ocs.dynamo.ui.view.LazyBaseView;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;

@UIScope
@SpringView(name = Views.DOMAIN_VIEW)
public class DomainView extends LazyBaseView {

	private static final long serialVersionUID = -8724439604070238890L;

	private MultiDomainEditLayout layout;

	@Override
	protected Component build() {
		final List<Class<? extends Domain>> domainClasses = new ArrayList<>();
		domainClasses.add(Country.class);
		domainClasses.add(Region.class);
		final FormOptions fo = new FormOptions().setTableExportAllowed(true).setShowRemoveButton(true);
		layout = new MultiDomainEditLayout(fo, domainClasses) {

			private static final long serialVersionUID = 6876613808059544376L;

			@Override
			protected boolean isDeleteAllowed(Class<?> clazz) {
				return Country.class.equals(clazz);
			}

		};
		return layout;
	}

	@Override
	protected void refresh() {
		layout.reload();
	}

}
