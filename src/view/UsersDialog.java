package view;

import java.awt.Dimension;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import entity.User;
import manage.ManagerFactory;
import model.ClientModel;
import model.EmployeeModel;
import net.miginfocom.swing.MigLayout;
import view.addEdit.ClientAddEditDialog;

public class UsersDialog extends JDialog {
	private static final long serialVersionUID = 5443140471807417339L;
	
	private boolean clients = true;
	
	JTable usersTable;

	public UsersDialog(ManagerFactory managerFactory) {
		setTitle("Users");
		setResizable(false);
		setSize(1400, 500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModal(true);
		setLayout(new MigLayout("wrap", "[grow, center]", "[]20[]20[][]20[]"));
		
		JRadioButton usersRadioButton = new JRadioButton("Users");
		usersRadioButton.setSelected(true);
		JRadioButton employeesRadioButton = new JRadioButton("Employees");
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(usersRadioButton);
		buttonGroup.add(employeesRadioButton);
		
		add(usersRadioButton, "split 2");
		add(employeesRadioButton);
		
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
		
		this.usersTable = new JTable(new ClientModel(managerFactory.getUserManager()));
		this.usersTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.usersTable.getTableHeader().setReorderingAllowed(false);
		JScrollPane clientTableScrollPane = new JScrollPane(this.usersTable);
		clientTableScrollPane.setPreferredSize(new Dimension(1400, 350));
		add(clientTableScrollPane);
		
		JButton loyaltyCardButton = new JButton("Loyalty Card");
		add(loyaltyCardButton);
		JButton bonusButton = new JButton("Bonus");
		bonusButton.setVisible(false);
		add(bonusButton);
		
		
		usersRadioButton.addActionListener(e -> {
			this.clients = true;
			loyaltyCardButton.setVisible(true);
			bonusButton.setVisible(false);
			usersTable.setModel(new ClientModel(managerFactory.getUserManager()));
		});
		
		employeesRadioButton.addActionListener(e -> {
			this.clients = false;
			bonusButton.setVisible(true);
			loyaltyCardButton.setVisible(false);
			usersTable.setModel(new EmployeeModel(managerFactory.getUserManager()));
		});
		
		deleteMenuItem.addActionListener(e -> {
			User user = getSelectedUser();
			if (user != null) {
				int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete the selected user?", "Delete confirmation", JOptionPane.ERROR_MESSAGE, JOptionPane.YES_NO_OPTION);
				if (choice == JOptionPane.YES_OPTION) {
					try {
						managerFactory.getUserManager().remove(user.getId());
						updateTable();
						JOptionPane.showMessageDialog(null, "User successfully deleted.", "", JOptionPane.INFORMATION_MESSAGE);
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null, ex.getMessage(), "", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
	}
	
	private User getSelectedUser() {
		User user = null;
		int row = usersTable.getSelectedRow();
		if (row == -1)
			JOptionPane.showMessageDialog(null, "You must select a user.", "", JOptionPane.WARNING_MESSAGE);
		else {
			if (this.clients) {
				user = ((ClientModel)this.usersTable.getModel()).getClient(row);
			}
			else {
				user = ((EmployeeModel)this.usersTable.getModel()).getEmployee(row);
			}
		}
		return user;
	}
	
	private void updateTable() {
		AbstractTableModel tableModel;
		if (this.clients)
			tableModel = (ClientModel)usersTable.getModel();
		else
			tableModel = (EmployeeModel)usersTable.getModel();
		tableModel.fireTableDataChanged();
	}
}
