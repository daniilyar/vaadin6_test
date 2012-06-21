package com.example.vaadin.webapp;

import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;

import de.steinwedel.vaadin.MessageBox;
import de.steinwedel.vaadin.MessageBox.ButtonType;
import de.steinwedel.vaadin.MessageBox.Icon;

@SuppressWarnings("serial")
public class CustomMenuBar extends CustomComponent {

	private Activator application;

	private HorizontalLayout mainLayout;

	public CustomMenuBar(Application application) {
		this.application = (Activator) application;
		buildMainLayout();
		setCompositionRoot(mainLayout);
	}

	private void buildMainLayout() {

		mainLayout = new HorizontalLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		mainLayout.setMargin(false);

		MenuBar menuBar = new MenuBar();
		menuBar.setImmediate(false);
		menuBar.setWidth("100.0%");
		menuBar.setHeight("-1px");
		menuBar.setDescription("Menu bar");

		MenuItem menuItem1 = menuBar.addItem("Menu", null);

		Command storeSessionToDisk = new Command() {
			public void menuSelected(MenuItem selectedItem) {
				MessageBox messageBox = new MessageBox(getWindow(), "Dialog", Icon.INFO, "Session size is: "
						+ application.writeSession() + " (stored in your tmp dir).", new MessageBox.ButtonConfig(
						ButtonType.OK, "Ok"));
				messageBox.show(true);
			}
		};

		Command closeRestartSession = new Command() {
			public void menuSelected(MenuItem selectedItem) {
				application.close();
			}
		};

		final WebApplicationContext context = (WebApplicationContext) application.getContext();

		Command reinitializeSession = new Command() {
			public void menuSelected(MenuItem selectedItem) {
				context.reinitializeSession();
			}
		};

		menuItem1.addItem("Store session to disk", storeSessionToDisk);
		menuItem1.addSeparator();
		menuItem1.addItem("Reinitialize session", reinitializeSession);
		menuItem1.addItem("Close/Restart application", closeRestartSession);
		menuItem1.addSeparator();
		menuItem1.addItem("Sample author", new Command() {

			public void menuSelected(MenuItem selectedItem) {
				MessageBox messageBox = new MessageBox(getWindow(), "Dialog", Icon.INFO,
						"Author: Daniil Yaroslavtsev, SevTeam",
						new MessageBox.ButtonConfig(
								ButtonType.OK, "Ok"));
				messageBox.show(true);
			}
		});

		MenuItem menuHelp = menuBar.addItem("Help", null);

		menuHelp.setDescription("<h2> <img src=\"http://www.veryicon.com/icon/preview/System/On%20Stage/Help%20Icon.jpg\""
				+ " width=\"40\" height=\"40\" /> Custom tooltip example.</h2>"
				+ "<iframe width=\"560\" height=\"315\" src=\"http://www.youtube.com/embed/Xy_9bx6U8_0\" "
				+ "frameborder=\"0\" allowfullscreen></iframe>"
				+ "<ul>"
				+ "  <li>You can use rich formatting in tooltips (XHTML)</li>"
				+ "  <li>So, you can include images/resources</li>"
				+ "  <li>Insert some hyperlinks, etc.</li>"
				+ "</ul>");
		mainLayout.addComponent(menuBar);
	}
}
