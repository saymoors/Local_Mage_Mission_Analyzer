package GUI;

import Entities.Mission;
import Parsers.IParser;
import Parsers.ParserJSON;
import Parsers.ParserTXT;
import Parsers.ParserXML;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

public class MainMenu extends JFrame {
    private File file;

    public MainMenu() {
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Выберите миссию:");
        JButton button = new JButton("Открыть магический поисковик");
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Доступные руны: json, txt, xml", "json", "txt", "xml");

        chooser.addChoosableFileFilter(filter);
        chooser.setFileFilter(filter);

        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setPreferredSize(new Dimension(260, 90));
        button.setPreferredSize(new Dimension(240, 40));

        button.addActionListener(e -> {
            try {
                int choice = chooser.showOpenDialog(this);

                if (choice == JFileChooser.APPROVE_OPTION) {
                    file = chooser.getSelectedFile();
                    Mission mission = parseSelectedFile(file);
                    SideMenu sideMenu = new SideMenu(mission);
                } else {
                    throw new Exception("Вы не выбрали миссию!");
                }
            } catch (Exception exception) {
                JOptionPane.showMessageDialog(
                        this,
                        exception.getMessage(),
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        panel.add(label);
        panel.add(button);
        getContentPane().add(panel);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Локальный анализатор миссий");
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    private Mission parseSelectedFile(File file) throws Exception {
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');
        String extension = fileName.substring(dotIndex + 1);

        IParser parser = switch (extension) {
            case "json" -> new ParserJSON();
            case "xml" -> new ParserXML();
            case "txt" -> new ParserTXT();
            default -> throw new Exception("Вы выбрали иную руну!");
        };

        return parser.parse(file.getAbsolutePath());
    }
}



