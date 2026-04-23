package Reports.Support;

import Entities.Mission;
import Reports.IReportFormat;

public class EmptyReportFormat implements IReportFormat {
    @Override
    public String render(Mission mission) {
        return "";
    }
}
