package com.example.vaadin.webapp.dnd;

import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.AbsoluteLayout.ComponentPosition;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;

import fi.jasoft.dragdroplayouts.DDAbsoluteLayout;
import fi.jasoft.dragdroplayouts.DDAbsoluteLayout.AbsoluteLayoutTargetDetails;
import fi.jasoft.dragdroplayouts.drophandlers.AbstractDefaultLayoutDropHandler;
import fi.jasoft.dragdroplayouts.events.LayoutBoundTransferable;

@SuppressWarnings("serial")
public class MyAbsoluteLayoutDropHandler extends AbstractDefaultLayoutDropHandler {

	/**
	 * Called when a component changed location within the layout
	 * 
	 * @param event
	 *            The drag and drop event
	 */
	@Override
	protected void handleComponentReordering(DragAndDropEvent event) {
		AbsoluteLayoutTargetDetails details = (AbsoluteLayoutTargetDetails) event
				.getTargetDetails();
		DDAbsoluteLayout layout = (DDAbsoluteLayout) details.getTarget();
		LayoutBoundTransferable transferable = (LayoutBoundTransferable) event
				.getTransferable();
		Component component = transferable.getComponent();

		// Get top-left pixel position
		int leftPixelPosition = details.getRelativeLeft();
		int topPixelPosition = details.getRelativeTop();

		ComponentPosition position = layout.getPosition(component);

		position.setLeft((float) leftPixelPosition, Sizeable.UNITS_PIXELS);
		position.setTop((float) topPixelPosition, Sizeable.UNITS_PIXELS);
	}

	/**
	 * Handle a drop from another layout
	 * 
	 * @param event
	 *            The drag and drop event
	 */
	@Override
	protected void handleDropFromLayout(DragAndDropEvent event) {
		AbsoluteLayoutTargetDetails details = (AbsoluteLayoutTargetDetails) event
				.getTargetDetails();
		LayoutBoundTransferable transferable = (LayoutBoundTransferable) event
				.getTransferable();
		Component component = transferable.getComponent();
		Component source = event.getTransferable().getSourceComponent();
		DDAbsoluteLayout layout = (DDAbsoluteLayout) details.getTarget();
		int leftPixelPosition = details.getRelativeLeft();
		int topPixelPosition = details.getRelativeTop();

		// Check that we are not dragging an outer layout into an
		// inner
		// layout
		Component parent = source.getParent();
		while (parent != null) {
			parent = parent.getParent();
		}

		// remove component from source using filter
		if (source instanceof ComponentContainer) {
			ComponentContainer sourceLayout = (ComponentContainer) source;
			sourceLayout.removeComponent(component);
		}

		// Add component to absolute layout
		layout.addComponent(component, "left:" + leftPixelPosition + "px;top:"
				+ topPixelPosition + "px");
	}
}