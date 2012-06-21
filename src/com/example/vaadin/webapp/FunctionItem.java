package com.example.vaadin.webapp;

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.terminal.Resource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class FunctionItem extends CustomComponent {

	private final String functionName;
	private final Resource functionImage;
	private VerticalLayout mainLayout;

	/**
	 * The constructor should first build the main layout, set the composition root and then do any custom
	 * initialization.
	 * 
	 */
	public FunctionItem(String functionName, Resource functionImage) {
		this.functionName = functionName;
		this.functionImage = functionImage;
		buildMainLayout();
		setCompositionRoot(mainLayout);
	}

	private VerticalLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new VerticalLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		//mainLayout.setStyleName("v-my-function"); // set border to see what was dragged

		Embedded image = new Embedded(this.functionName, this.functionImage);
		mainLayout.addListener(new LayoutClickListener() {
			public void layoutClick(LayoutClickEvent event) {
				getWindow().showNotification(functionName + " clicked.");
			}
		});

		mainLayout.addComponent(image);

		mainLayout.setComponentAlignment(image, Alignment.TOP_CENTER);

		return mainLayout;
	}

}
