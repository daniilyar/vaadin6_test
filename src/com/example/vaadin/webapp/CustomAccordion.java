package com.example.vaadin.webapp;

import com.vaadin.Application;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.Tab;

import fi.jasoft.dragdroplayouts.DDAccordion;

@SuppressWarnings("serial")
public class CustomAccordion extends HorizontalLayout implements
		Accordion.SelectedTabChangeListener {

	private DDAccordion accordion;

	private Application application;

	public CustomAccordion(Application application) {

		this.application = application;

		setSpacing(true);

		accordion = new DDAccordion();

		// Enable dragging tabs
		//accordion.setDragMode(LayoutDragMode.CLONE);

		//accordion.setComponentVerticalDropRatio(0.3f);

		// Enable dropping components
		//accordion.setDropHandler(new DefaultAccordionDropHandler());

		accordion.setHeight("400px");
		accordion.setWidth("500px");

		accordion.addTab(new FunctionsPanel(), "Favorite Functions");

		addTab("Append");
		addTab("Filter");
		addTab("Display");
		addTab("Analitic/Scores");
		addTab("Chart/Visuals");

		accordion.addListener(this);

		Embedded embedded = new Embedded("Revere web site",
				new ExternalResource("http://www.reveredata.com/about.html"));
		embedded.setType(Embedded.TYPE_BROWSER);
		embedded.setWidth("100%");
		embedded.setHeight("100%");

		accordion.addTab(embedded, "Revere site content");

		addComponent(accordion);
	}

	private Tab addTab(String tabName) {
		// Label label = new Label("Empty");
		return accordion.addTab(new CustomMenuBar(application), tabName);
	}

	public void selectedTabChange(SelectedTabChangeEvent event) {
		TabSheet tabsheet = event.getTabSheet();
		Tab tab = tabsheet.getTab(tabsheet.getSelectedTab());
		if (tab != null) {
			getWindow().showNotification("Selected tab: " + tab.getCaption());
		}
	}
}