package com.example.vaadin.webapp;

import com.example.vaadin.webapp.dnd.MyHorizontalLayoutDropHandler;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;

import fi.jasoft.dragdroplayouts.DDHorizontalLayout;
import fi.jasoft.dragdroplayouts.client.ui.LayoutDragMode;

@SuppressWarnings("serial")
public class FunctionsPanel extends CustomComponent {

	private DDHorizontalLayout mainLayout;

	/**
	 * The constructor should first build the main layout, set the composition root and then do any custom
	 * initialization.
	 * 
	 */
	public FunctionsPanel() {
		buildMainLayout();
		setCompositionRoot(mainLayout);
	}

	private HorizontalLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new DDHorizontalLayout();

		// Enable dragging components
		mainLayout.setDragMode(LayoutDragMode.CLONE);

		// Enable dropping components
		mainLayout.setDropHandler(new MyHorizontalLayoutDropHandler(com.vaadin.ui.Alignment.MIDDLE_CENTER));

		mainLayout.setImmediate(false);
		mainLayout.setSizeFull();
		mainLayout.setMargin(true);
		mainLayout.setSpacing(false);

		mainLayout.addComponent(new FunctionItem("Function 1",
				new ThemeResource("img/add.png")));
		mainLayout.addComponent(new FunctionItem("Function 2",
				new ThemeResource("img/arrow_branch.png")));
		mainLayout.addComponent(new FunctionItem("Function 3",
				new ThemeResource("img/auction_hammer.png")));
		mainLayout.addComponent(new FunctionItem("Function 4",
				new ThemeResource("img/help.png")));

		return mainLayout;
	}

}
