package GUI;

import Entities.Mission;
import Reports.IReportFormat;

import javax.swing.*;
import java.awt.*;

public class SideMenu extends JDialog {
    public SideMenu(Mission mission, IReportFormat format) throws Exception {
        JTextArea reportArea = new JTextArea(format.render(mission));
        reportArea.setEditable(false);
        reportArea.setLineWrap(true);
        reportArea.setWrapStyleWord(true);
        reportArea.setCaretPosition(0);
        reportArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        reportArea.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JScrollPane scrollPane = new JScrollPane(reportArea);
        scrollPane.setPreferredSize(new Dimension(760, 560));

        getContentPane().add(scrollPane);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("Локальный анализатор миссий");
        setModalityType(ModalityType.APPLICATION_MODAL);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
