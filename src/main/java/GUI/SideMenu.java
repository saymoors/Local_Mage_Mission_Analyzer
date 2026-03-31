package GUI;

import Entities.Mission;
import Reports.DetailedReportFormat;
import Reports.IReportActions;
import Reports.IReportFormat;

import javax.swing.*;
import java.awt.*;

public class SideMenu extends JDialog {

    public SideMenu(Mission mission) {
        this(mission, new DetailedReportFormat());
    }

    public SideMenu(Mission mission, IReportFormat format) {
        IReportActions dialogActions = new TableDialogActions(this);
        JPanel panel = new JPanel();

        panel.setLayout(new GridLayout(0, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        format.render(mission, panel, dialogActions);

        getContentPane().add(panel);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("Локальный анализатор миссий");
        setModalityType(ModalityType.APPLICATION_MODAL);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
