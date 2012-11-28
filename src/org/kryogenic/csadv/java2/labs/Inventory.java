package org.kryogenic.csadv.java2.labs;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

public class Inventory {
	private final ArrayList<Item> inventory;
	
	public static void main(final String... args) {
		new Inventory();
	}
	
	public Inventory() {
		inventory = new ArrayList<Item>();
		
		// ---------- Initialize and declare JFrames and JPanels ---------- //
		final JFrame view = new JFrame();
		final JFrame edit = new JFrame();
		
		final JPanel cards = new JPanel(new CardLayout());
		final JPanel addCard, modifyCard;
	    
		// ---------- Initialize ComboBox for switching cards ---------- //
		String[] strActions = {"Add Item", "Modify Item"};
		JComboBox actions = new JComboBox(strActions);
		actions.setEditable(false);
		actions.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				CardLayout cl = (CardLayout) cards.getLayout();
			    cl.show(cards, (String)e.getItem());
			}
		});
		
		// ---------- Set up JPanel for the table of items ---------- //
		JPanel tablePanel = new JPanel(new BorderLayout());
		TableModel dataModel = new AbstractTableModel() {
			public static final long serialVersionUID = 0;
			public int getColumnCount() { return 3; }
	        public int getRowCount() { return inventory.size();}
	        @Override
	        public String getColumnName(int col) {
	        	switch(col) {
		        	case 0:
		        		return "Code";
		        	case 1:
		        		return "Name";
		        	case 2:
		        		return "In Stock";
		        	default:
		        		return "???";
	        	}
	        }
	        @Override
	        public Object getValueAt(int row, int col) {
	        	switch(col) {
		        	case 0:
		        		return inventory.get(row).getCode();
		        	case 1:
		        		return inventory.get(row).getName();
		        	case 2:
		        		return inventory.get(row).getStock();
		        	default:
		        		return 0;
	        	}
	        }
	    };
	    final JTable productTable = new JTable(dataModel) {
	    	public static final long serialVersionUID = 0;
	    	@Override
	    	   public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
	    	      JLabel label = (JLabel) super.prepareRenderer(renderer, row, column);
	    	      if (inventory.get(row).discontinued()) {
	    	         label.setBackground(Color.RED);
	    	      } else {
	    	    	  label.setBackground(Color.WHITE);
	    	      }
	    	      return label;
	    	   }
	    };
	    productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollpane = new JScrollPane(productTable);
		tablePanel.add(scrollpane, BorderLayout.CENTER);
		
		// ---------- Set up the JPanel for adding items ---------- //
		addCard = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		addCard.add(new JLabel("Item name: "), c);
		final JTextField name = new JTextField(10);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 0;
		addCard.add(name, c);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		addCard.add(new JLabel("Amount in stock: "), c);
		final JTextField stock = new JTextField("0", 4);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 1;
		addCard.add(stock, c);
		JButton add = new JButton("Add Item");
		add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ParseResult parse = parse(stock.getText());
				if(parse.success()) {
					inventory.add(new Item(name.getText(), parse.result()));
				} else {
					inventory.add(new Item(name.getText()));
				}
				productTable.revalidate();
			}
		});
		c.insets = new Insets(5, 0, 0, 0);
		c.ipady = 25;
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 2;
		addCard.add(add, c);
		cards.add(addCard, strActions[0]);
		
		// ---------- Set up the JPanel for modifying items ---------- //
		modifyCard = new JPanel(new GridBagLayout());
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		modifyCard.add(new JLabel("Item code: "), c);
		final JTextField code = new JTextField(8);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 0;
		modifyCard.add(code, c);
		ActionListener modifyAction = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String action = e.getActionCommand();
				ParseResult parse = parse(code.getText());
				if(parse.success()) {
					boolean searchSuccess = false;
					search: for(int i = 0; i < inventory.size(); i++) {
						if(inventory.get(i).getCode() == parse.result()) {
							searchSuccess = true;
							if(action.equals("delete")) {
								String name = inventory.remove(i).getName();
								productTable.revalidate();
								JOptionPane.showMessageDialog(edit, "Item '" + name + "' deleted");
							} else if (action.equals("discontinue")) {
								inventory.get(i).discontinue();
								productTable.repaint();
								JOptionPane.showMessageDialog(edit, "Item '" + inventory.get(i).getName() + "' discontinued");
							} else if (action.equals("stock")){
								ParseResult optionPaneParse;
								while(!(optionPaneParse = parse(JOptionPane.showInputDialog(edit, "The current stock is: " + inventory.get(i).getStock() + ". What would you like to change it to?"))).success()) {
									int response = JOptionPane.showConfirmDialog(edit, "Invalid stock number, try again?");
									if(response == JOptionPane.CANCEL_OPTION || response == JOptionPane.NO_OPTION)
										break search;
								}
								inventory.get(i).setStock(optionPaneParse.result());
								productTable.repaint();
								JOptionPane.showMessageDialog(edit, "Item '" + inventory.get(i).getName() + "' stock changed to " + inventory.get(i).getStock());
							}
							break search;
						}
					}
					if(!searchSuccess)
						JOptionPane.showMessageDialog(edit, "An item with the code " + parse.result() + " was not found");
				} else {
					JOptionPane.showMessageDialog(edit, "'" + code.getText() + "' is not a valid item code");
				}
			}
		};
		JButton del = new JButton("Delete Item");
		JButton dis = new JButton("Discontinue Item");
		JButton upd = new JButton("Update Stock");
		del.setActionCommand("delete");
		dis.setActionCommand("discontinue");
		upd.setActionCommand("stock");
		del.addActionListener(modifyAction);
		dis.addActionListener(modifyAction);
		upd.addActionListener(modifyAction);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 0, 1, 0);
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 1;
		modifyCard.add(del, c);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 0, 1, 0);
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 2;
		modifyCard.add(dis, c);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 0, 1, 0);
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 3;
		modifyCard.add(upd, c);
		cards.add(modifyCard, strActions[1]);
		
		// ---------- Set up the listener for selecting items from the table ---------- //
	    productTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
	    	public void valueChanged(ListSelectionEvent e) {
	    		if(e.getValueIsAdjusting())
	    			return;
	    		Item i = inventory.get(productTable.getSelectedRow());
	    		name.setText(i.getName());
	    		stock.setText(String.valueOf(i.getStock()));
	    		code.setText(String.valueOf(i.getCode()));
	    	}
	    });
		
		// ---------- Add the JPanels to the JFrames ---------- //
		edit.getContentPane().add(actions, BorderLayout.PAGE_START);
		edit.getContentPane().add(cards, BorderLayout.CENTER);
		
		view.getContentPane().add(tablePanel);
		
		// ---------- Set up the JFrames and setVisible ---------- //
		edit.setSize(250, 175);
		edit.setResizable(false);
		edit.setLocationRelativeTo(view);
		edit.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		view.pack();
		view.setLocationRelativeTo(null);
		view.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		view.setVisible(true);
		edit.setVisible(true);
	}
	
	private ParseResult parse(final String toParse) {
		if(toParse == null) {
			return new ParseResult(false);
		}
		boolean parseable = true;
		outer: for(char c : toParse.trim().toCharArray()) {
			for(char num : new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'}) {
				if(c == num) {
					continue outer;
				}
			}
			parseable = false;
		}
		if(parseable) {
			return new ParseResult(parseable, Integer.parseInt(toParse));
		} else {
			return new ParseResult(parseable);
		}
	}
}
class Item {
	private boolean discontinued = false;
	private int stock;
	private final int code;
	private String name;
	
	private static int currentCode = 1000;
	
	public Item(String itemName) {
		this(currentCode += 1, itemName, 0);
	}
	public Item(String itemName, int stockAmount) {
		this(currentCode += 1, itemName, stockAmount);
	}
	private Item(int itemCode, String itemName, int stockAmount) { // internal use
		this.stock = stockAmount;
		this.code = itemCode;
		this.name = itemName;
	}
	
	public void discontinue() {
		discontinued = true;
	}
	public boolean discontinued() {
		return discontinued;
	}
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCode() {
		return code;
	}
}
class ParseResult {
	private final boolean success;
	private final int result;
	ParseResult(final boolean success) {
		this(success, 0);
	}
	ParseResult(final boolean success, final int result) {
		this.success = success;
		this.result = result;
	}
	public boolean success() {
		return success;
	}
	public int result() {
		if(!success)
			throw new RuntimeException("Cannot get the value of a failed parse");
		return result;
	}
}