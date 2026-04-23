package Reports;

import Entities.Mission;

public interface IReportFormat {
    String render(Mission mission) throws Exception;
}
