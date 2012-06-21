package com.example.vaadin.webapp;

import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class CustomTable extends VerticalLayout {

    public CustomTable() {

        Table table = new Table("Suppliers (sorted by market cap)");
        table.setPageLength(6);
        table.setWidth("340px");
        table.setHeight("400px");
        table.setDescription("Sample table");

        table.setColumnReorderingAllowed(true);
        table.setColumnCollapsingAllowed(true);

        // table.setSizeFull();

        ColumnGenerator columnGenerator = new ColumnGenerator() {
            public Object generateCell(Table source, Object itemId, Object columnId) {
                return "data";
            }
        };

        table.addGeneratedColumn("Supplier", columnGenerator);

        table.addGeneratedColumn("...", columnGenerator);

        table.addGeneratedColumn("Technology", columnGenerator);

        // Set alignments
        table.setColumnAlignments(new String[] { Table.ALIGN_CENTER,
                Table.ALIGN_CENTER, Table.ALIGN_CENTER });

        // Set column widths
        table.setColumnExpandRatio(null, 1);

        // Enable footer
        // table.setFooterVisible(true);

        addComponent(table);
    }
}
