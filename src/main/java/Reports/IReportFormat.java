package Reports;

import Entities.Mission;

import javax.swing.*;

public interface IReportFormat {
    void render(Mission mission, JPanel panel, IReportActions actions);
}
