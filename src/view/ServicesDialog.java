package view;

import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import entity.Service;
import manage.ManagerFactory;
import model.ServiceModel;
import net.miginfocom.swing.MigLayout;
import view.addEdit.ServiceAddEditDialog;

public class ServicesDialog extends JDialog {
	private static final long serialVersionUID = -8887742518203077982L;
	
	private JTable servicesTable;

	public ServicesDialog(ManagerFactory managerFactory) {
		setTitle("Services");
		setResizable(false);
		setSize(600, 250);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModal(true);
		setLayout(new MigLayout("wrap", "[grow, center]", "[]20[]20[][]20[]"));
		
		JMenuBar menuBar = new JMenuBar();
		JMenu actionsMenu = new JMenu("Actions");
		menuBar.add(actionsMenu);
		
		JMenuItem addMenuItem = new JMenuItem("Add");
		actionsMenu.add(addMenuItem);
		JMenuItem editMenuItem = new JMenuItem("Edit");
		actionsMenu.add(editMenuItem);
		JMenuItem deleteMenuItem = new JMenuItem("Delete");
		actionsMenu.add(deleteMenuItem);
		
		setJMenuBar(menuBar);
		
		this.servicesTable = new JTable(new ServiceModel(managerFactory.getServiceManager()));
		this.servicesTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.servicesTable.getTableHeader().setReorderingAllowed(false);
		JScrollPane servicesTableScrollPane = new JScrollPane(this.servicesTable);
		servicesTableScrollPane.setPreferredSize(new Dimension(600, 250));
		add(servicesTableScrollPane);
		
		
		addMenuItem.addActionListener(e -> {
			ServiceAddEditDialog serviceAddEditDialog = new ServiceAddEditDialog(managerFactory, null);
			serviceAddEditDialog.setVisible(true);
			updateTable();
		});
		
		editMenuItem.addActionListener(e -> {
			Service service = getSelectedService();
			if (service == null) {
				JOptionPane.showMessageDialog(null, "You must select a service.", "", JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			ServiceAddEditDialog serviceAddEditDialog = new ServiceAddEditDialog(managerFactory, service);
			serviceAddEditDialog.setVisible(true);
			updateTable();
		});
		
		deleteMenuItem.addActionListener(e -> {
			Service service = getSelectedService();
			if (service != null) {
				int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete the selected service?", "Delete confirmation", JOptionPane.ERROR_MESSAGE, JOptionPane.YES_NO_OPTION);
				if (choice == JOptionPane.YES_OPTION) {
					try {
						managerFactory.getServiceManager().remove(service.getId());
						updateTable();
						JOptionPane.showMessageDialog(null, "Service successfully deleted.", "", JOptionPane.INFORMATION_MESSAGE);
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null, ex.getMessage(), "", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
			else {
				JOptionPane.showMessageDialog(null, "You must select a service.", "", JOptionPane.WARNING_MESSAGE);
			}
		});
	}
	
	private Service getSelectedService() {
		Service service = null;
		int row = servicesTable.getSelectedRow();
		if (row != -1) {
			service = ((ServiceModel)servicesTable.getModel()).getService(row);
		}
		return service;
	}
	
	private void updateTable() {
		((ServiceModel)servicesTable.getModel()).fireTableDataChanged();
	}
}
