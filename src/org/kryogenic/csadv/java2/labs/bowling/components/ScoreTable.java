package org.kryogenic.csadv.java2.labs.bowling.components;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import org.kryogenic.csadv.java2.labs.bowling.Players;

public class ScoreTable extends JTable {
	public static final long serialVersionUID = 0;
	public ScoreTable(Players players) {
		super(new TableModel(players));
		this.setSize(300, 100);
	}
	private static class TableModel extends AbstractTableModel {
		public static final long serialVersionUID = 0;
		private final Players players;
		public TableModel(Players players) {
			this.players = players;
		}
		public int getColumnCount() { return 12; }
        public int getRowCount() { return players.size(); }
        @Override
        public String getColumnName(int col) {
        	return "Frame " + col;
        }
        @Override
        public Object getValueAt(int row, int col) {
        	if(col == 0) {
        		return "Player " + (row + 1);
        	} else if(col == 11) {
        		return "Total: " + players.get(row).total();
        	} else {
        		return players.get(row).getScore(col - 1);
        	}
        }
    };
	/*@Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
       JLabel label = (JLabel) super.prepareRenderer(renderer, row, column);
       if (inventory.get(row).discontinued()) {
          label.setBackground(Color.RED);
       } else {
     	  label.setBackground(Color.WHITE);
       }
       return label;
    }*/
}
