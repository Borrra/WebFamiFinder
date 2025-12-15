
package githubborrra.service;

import javax.swing.*;
import java.awt.*;

/* Служебный Класс, для нумерования строк в Окнах, методов: 13, 14 */

public class NumberedListCellRenderer extends DefaultListCellRenderer {

	@Override

	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {

		JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

		label.setText(String.format("%03d. %s", index + 1, value.toString()));

		return label;
	}
	
} // конец 2-го класса NumberedListCellRenderer
