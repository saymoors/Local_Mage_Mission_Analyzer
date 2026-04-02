package GUI;

import Entities.Mission;
import Factories.ParserFactory;
import Factories.ReportFormatFactory;
import Parsers.IParser;
import Reports.IReportFormat;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
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
        JRadioButton firstRadioButton = new JRadioButton("Краткое резюме");
        JRadioButton secondRadioButton = new JRadioButton("Детализированный отчет");
        JRadioButton thirdRadioButton = new JRadioButton("Отчет по рискам");
        JRadioButton fourthRadioButton = new JRadioButton("Статистический отчет");

        firstRadioButton.setActionCommand("summary");
        secondRadioButton.setActionCommand("detailed");
        thirdRadioButton.setActionCommand("risk");
        fourthRadioButton.setActionCommand("statistics");
        secondRadioButton.setSelected(true);

        buttonGroup.add(firstRadioButton);
        buttonGroup.add(secondRadioButton);
        buttonGroup.add(thirdRadioButton);
        buttonGroup.add(fourthRadioButton);

        JFileChooser chooser = getJFileChooser();

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
        panel.add(firstRadioButton);
        panel.add(secondRadioButton);
        panel.add(thirdRadioButton);
        panel.add(fourthRadioButton);
        getContentPane().add(panel);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Локальный анализатор миссий");
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    private JFileChooser getJFileChooser() {
        JFileChooser chooser = new JFileChooser();
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.isDirectory()) {
                    return true;
                }

                String fileName = file.getName().toLowerCase();
                return fileName.endsWith(".json")
                        || fileName.endsWith(".txt")
                        || fileName.endsWith(".xml")
                        || fileName.endsWith(".yaml")
                        || !fileName.contains(".");
            }

            @Override
            public String getDescription() {
                return "Доступные руны: json, xml, yaml, txt, без расширения";
            }
        });
        return chooser;
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
