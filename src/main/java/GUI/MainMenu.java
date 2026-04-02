package GUI;

import Entities.Mission;
import Factories.ParserFactory;
import Factories.ReportFormatFactory;
import Parsers.IParser;
import Reports.IReportFormat;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class MainMenu extends JFrame {
    private final ParserFactory parserFactory;
    private final ReportFormatFactory reportFormatFactory;

    public MainMenu() {
        parserFactory = new ParserFactory();
        reportFormatFactory = new ReportFormatFactory();

        JPanel panel = new JPanel();
        JLabel label = new JLabel("Выберите миссию:");
        JButton button = new JButton("Открыть магический поисковик");

        ButtonGroup buttonGroup = new ButtonGroup();
        JFileChooser chooser = new JFileChooser();

        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setPreferredSize(new Dimension(260, 225));
        button.setPreferredSize(new Dimension(240, 40));

        button.addActionListener(e -> {
            try {
                int choice = chooser.showOpenDialog(this);

                if (choice == JFileChooser.APPROVE_OPTION) {
                    File file = chooser.getSelectedFile();
                    Mission mission = parseSelectedFile(file);
                    IReportFormat format = returnSelectedFormat(buttonGroup);
                    new SideMenu(mission, format);
                } else {
                    throw new Exception("Вы не выбрали миссию!");
                }
            } catch (Exception exception) {
                JOptionPane.showMessageDialog(
                        this,
                        exception.getMessage(),
                        "Предупреждение",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        });

        panel.add(label);
        panel.add(button);

        for (String reportFormat : reportFormatFactory.getReportFormats().keySet()) {
            JRadioButton radioButton = new JRadioButton(reportFormat);
            radioButton.setActionCommand(reportFormat);

            if (reportFormat.equals(reportFormatFactory.getDefaultReportType())) {
                radioButton.setSelected(true);
            }

            buttonGroup.add(radioButton);
            panel.add(radioButton);
        }

        getContentPane().add(panel);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Локальный анализатор миссий");
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    private Mission parseSelectedFile(File file) throws Exception {
        String extension = getExtension(file);
        IParser parser = parserFactory.createParser(extension);
        return parser.parse(file.getAbsolutePath());
    }

    private IReportFormat returnSelectedFormat(ButtonGroup buttonGroup) throws Exception {
        ButtonModel selection = buttonGroup.getSelection();
        return reportFormatFactory.createReportFormat(selection.getActionCommand());
    }

    private String getExtension(File file) {
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');
        return dotIndex < 0 ? "" : fileName.substring(dotIndex + 1);
    }
}
