package com.example.vaadin.webapp;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.example.vaadin.webapp.dnd.MyAbsoluteLayoutDropHandler;
import com.vaadin.Application;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

import fi.jasoft.dragdroplayouts.DDAbsoluteLayout;
import fi.jasoft.dragdroplayouts.client.ui.LayoutDragMode;

@SuppressWarnings("serial")
public class MainPageContent extends CustomComponent {

	private VerticalLayout mainLayout;

	private Application application;

	private DDAbsoluteLayout workflowDragDropPanel;

	public MainPageContent(Application application) {
		this.application = application;
		buildMainLayout();
		setCompositionRoot(mainLayout);
	}

	private VerticalLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new VerticalLayout();
		mainLayout.setImmediate(false);
		mainLayout.setSizeFull();
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);

		// top-level component properties
		setWidth("100.0%");
		setHeight("100.0%");

		// site logo
		Embedded logoImage = new Embedded("", new ThemeResource("img/logo-reveredata.gif"));
		logoImage.setType(Embedded.TYPE_IMAGE);

		mainLayout.addComponentAsFirst(logoImage);

		// Menu bar
		CustomMenuBar customMenuBar = new CustomMenuBar(application);

		mainLayout.addComponent(customMenuBar);
		mainLayout.setExpandRatio(customMenuBar, 1.0f);
		mainLayout.setComponentAlignment(customMenuBar, Alignment.BOTTOM_CENTER);

		HorizontalLayout mainPanel = new HorizontalLayout();
		mainPanel.setSpacing(true);

		VerticalLayout leftPanel = new VerticalLayout();
		leftPanel.setStyleName("v-my-panel");
		leftPanel.setWidth("400px");
		leftPanel.setHeight("400px");

		workflowDragDropPanel = new DDAbsoluteLayout();
		workflowDragDropPanel.setSizeFull();

		// Enable dragging components
		workflowDragDropPanel.setDragMode(LayoutDragMode.CLONE);

		// Enable dropping components

		workflowDragDropPanel.setDropHandler(new MyAbsoluteLayoutDropHandler());

		workflowDragDropPanel.setMargin(true);

		Button btnRun = new Button("Run");
		Button btnSave = new Button("Save");
		Button btnClear = new Button("Clear");

		final HorizontalLayout buttonsPanel = new HorizontalLayout();

		buttonsPanel.addComponent(btnRun);
		buttonsPanel.addComponent(btnSave);
		buttonsPanel.addComponent(btnClear);

		btnClear.addListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {

				final List<Component> toRemove = new LinkedList<Component>();

				Iterator<Component> components = workflowDragDropPanel.getComponentIterator();
				while (components.hasNext()) {
					Component component = (Component) components.next();
					if (!component.equals(buttonsPanel)) {
						toRemove.add(component);
					}
				}
				for (Component component : toRemove) {
					workflowDragDropPanel.removeComponent(component);
				}
			}
		});

		ClickListener clickListener = new ClickListener() {
			public void buttonClick(ClickEvent event) {
				getWindow().showNotification("'" + event.getButton().getCaption() + "' button was clicked.");
			}
		};

		btnRun.addListener(clickListener);
		btnSave.addListener(clickListener);
		btnClear.addListener(clickListener);

		buttonsPanel.setSpacing(true);

		workflowDragDropPanel.addComponent(buttonsPanel);

		leftPanel.addComponent(workflowDragDropPanel);

		mainPanel.addComponent(leftPanel);

		mainPanel.addComponent(new CustomAccordion(application));

		mainPanel.addComponent(new TreeTable());
		mainPanel.addComponent(new CustomTable());

		mainLayout.addComponent(mainPanel);
		return mainLayout;
	}

	@Override
	public void addComponent(Component component) {
		mainLayout.addComponent(component);
	}

}
