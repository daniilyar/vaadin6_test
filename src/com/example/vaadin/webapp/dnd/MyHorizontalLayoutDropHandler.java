package com.example.vaadin.webapp.dnd;

import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.terminal.gwt.client.ui.dd.HorizontalDropLocation;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;

import fi.jasoft.dragdroplayouts.DDHorizontalLayout.HorizontalLayoutTargetDetails;
import fi.jasoft.dragdroplayouts.drophandlers.AbstractDefaultLayoutDropHandler;
import fi.jasoft.dragdroplayouts.events.LayoutBoundTransferable;

@SuppressWarnings("serial")
public class MyHorizontalLayoutDropHandler extends AbstractDefaultLayoutDropHandler {

	private Alignment dropAlignment;

	/**
	 * Constructor
	 * 
	 * @param dropCellAlignment
	 *            The cell alignment of the component after it has been dropped
	 */
	public MyHorizontalLayoutDropHandler(Alignment dropCellAlignment) {
		this.dropAlignment = dropCellAlignment;
	}

	/**
	 * Called when a component changed location within the layout
	 * 
	 * @param event
	 *            The drag and drop event
	 */
	@Override
	protected void handleComponentReordering(DragAndDropEvent event) {
		// Component re-ordering
		LayoutBoundTransferable transferable = (LayoutBoundTransferable) event
				.getTransferable();
		HorizontalLayoutTargetDetails details = (HorizontalLayoutTargetDetails) event
				.getTargetDetails();
		AbstractOrderedLayout layout = (AbstractOrderedLayout) details
				.getTarget();
		Component comp = transferable.getComponent();
		int idx = details.getOverIndex();

		// Detach
		// layout.removeComponent(comp);
		// idx--;

		// Increase index if component is dropped after or above a previous
		// component
		HorizontalDropLocation loc = details.getDropLocation();
		if (loc == HorizontalDropLocation.CENTER
				|| loc == HorizontalDropLocation.RIGHT) {
			idx++;
		}

		// Add component
		if (idx >= 0) {
			layout.addComponent(comp, idx);
		} else {
			layout.addComponent(comp);
		}

		// Add component alignment if given
		if (dropAlignment != null) {
			layout.setComponentAlignment(comp, dropAlignment);
		}
	}

	/**
	 * Handle a drop from another layout
	 * 
	 * @param event
	 *            The drag and drop event
	 */
	@Override
	protected void handleDropFromLayout(DragAndDropEvent event) {
		LayoutBoundTransferable transferable = (LayoutBoundTransferable) event
				.getTransferable();
		HorizontalLayoutTargetDetails details = (HorizontalLayoutTargetDetails) event
				.getTargetDetails();
		AbstractOrderedLayout layout = (AbstractOrderedLayout) details
				.getTarget();
		Component source = event.getTransferable().getSourceComponent();
		int idx = details.getOverIndex();
		Component comp = transferable.getComponent();

		// Check that we are not dragging an outer layout into an inner
		// layout
		Component parent = layout.getParent();
		while (parent != null) {
			if (parent == comp) {
				return;
			}
			parent = parent.getParent();
		}

		// If source is an instance of a component container then remove
		// it
		// from there,
		// the component cannot have two parents.
		if (source instanceof ComponentContainer) {
			ComponentContainer sourceLayout = (ComponentContainer) source;
			sourceLayout.removeComponent(comp);
		}

		// Increase index if component is dropped after or above a
		// previous
		// component
		HorizontalDropLocation loc = details.getDropLocation();
		if (loc == HorizontalDropLocation.CENTER
				|| loc == HorizontalDropLocation.RIGHT) {
			idx++;
		}

		// Add component
		if (idx >= 0) {
			layout.addComponent(comp, idx);
		} else {
			layout.addComponent(comp);
		}

		// Add component alignment if given
		if (dropAlignment != null) {
			layout.setComponentAlignment(comp, dropAlignment);
		}
	}
}
