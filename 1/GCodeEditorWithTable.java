// GCodeEditorWithTable.java

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.*;
import java.io.*;
import java.nio.file.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.*;

public class GCodeEditorWithTable {
    private static final Pattern GCODE_PATTERN = Pattern.compile("\\b(G\\d+|M\\d+)\\b");

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("✍️ G-code Editor | Chèn mã M/G, Lưu file & Trích xuất thông tin");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1100, 800);
            frame.setLayout(new BorderLayout());

            JTextPane textPane = new JTextPane();
            textPane.setFont(new Font("Consolas", Font.PLAIN, 16));
            JScrollPane textScroll = new JScrollPane(textPane);
            textScroll.setBorder(BorderFactory.createTitledBorder("📝 Soạn thảo G-code"));

            StyledDocument doc = textPane.getStyledDocument();
            Style defaultStyle = textPane.addStyle("default", null);
            StyleConstants.setForeground(defaultStyle, Color.BLACK);
            Style gcodeStyle = textPane.addStyle("gcode", null);
            StyleConstants.setForeground(gcodeStyle, Color.BLUE);
            StyleConstants.setBold(gcodeStyle, true);

            String[] columnNames = {"Dòng", "Nội dung"};
            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
            JTable infoTable = new JTable(tableModel);
            infoTable.setEnabled(false);
            infoTable.setRowHeight(24);
            JScrollPane tableScroll = new JScrollPane(infoTable);
            tableScroll.setBorder(BorderFactory.createTitledBorder("📋 Nội dung hiện tại"));

            String[] metaColumns = {"Thông tin", "Giá trị"};
            DefaultTableModel metaModel = new DefaultTableModel(metaColumns, 0);
            JTable metaTable = new JTable(metaModel);
            metaTable.setEnabled(false);
            metaTable.setRowHeight(24);
            JScrollPane metaScroll = new JScrollPane(metaTable);
            metaScroll.setBorder(BorderFactory.createTitledBorder("📎 Thông tin chương trình"));

            JButton refreshMeta = new JButton("🔄 Làm mới thông tin");
            refreshMeta.addActionListener(e -> {
                Map<String, String> meta = parseMetadata(textPane.getText());
                updateMetaTable(metaModel, meta);
            });

            JButton resetButton = new JButton("🧹 Xóa văn bản");
            resetButton.addActionListener(e -> {
                textPane.setText("");
                tableModel.setRowCount(0);
                metaModel.setRowCount(0);
            });

            JButton exportButton = new JButton("📤 Xuất văn bản");
            exportButton.addActionListener(e -> {
                JFileChooser fc = new JFileChooser();
                fc.setSelectedFile(new File("exported_cleaned_gcode.tap"));
                if (fc.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                    try {
                        String cleanedText = textPane.getText()
                                .replaceAll("\\bN\\d+\\b", "")
                                .replaceAll(" +", " ")
                                .replaceAll("(?m)^\\s+", "");
                        Files.writeString(fc.getSelectedFile().toPath(), cleanedText);
                        JOptionPane.showMessageDialog(frame, "Đã xuất văn bản thành công!");
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame, "Lỗi khi xuất: " + ex.getMessage());
                    }
                }
            });

            JPanel southPanel = new JPanel(new GridLayout(1, 2));
            southPanel.add(tableScroll);
            southPanel.add(metaScroll);

            JPanel bottomButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            bottomButtons.add(refreshMeta);
            bottomButtons.add(resetButton);
            bottomButtons.add(exportButton);

            JPanel bottomPanel = new JPanel(new BorderLayout());
            bottomPanel.add(southPanel, BorderLayout.CENTER);
            bottomPanel.add(bottomButtons, BorderLayout.SOUTH);
            bottomPanel.setPreferredSize(new Dimension(100, 220));

            JPanel buttonPanel = new JPanel(new GridLayout(0, 2, 10, 10));
            JScrollPane buttonScroll = new JScrollPane(buttonPanel);
            buttonScroll.setPreferredSize(new Dimension(400, 100));
            buttonScroll.setBorder(BorderFactory.createTitledBorder("📌 Chèn mã M / G-code"));

            JSplitPane centerSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, textScroll, buttonScroll);
            centerSplit.setDividerLocation(700);

            JSplitPane verticalSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, centerSplit, bottomPanel);
            verticalSplit.setResizeWeight(0.8);
            verticalSplit.setDividerSize(5);
            frame.add(verticalSplit, BorderLayout.CENTER);

            LinkedHashMap<String, String> codes = new LinkedHashMap<>();
            codes.put("G00", "Định vị dao nhanh"); codes.put("G01", "Nội suy đường thẳng");
            codes.put("G02", "Cung tròn thuận chiều"); codes.put("G03", "Cung tròn ngược chiều");
            codes.put("G28", "Về điểm gốc"); codes.put("M03", "Trục xoay thuận chiều");
            codes.put("M05", "Dừng trục chính"); codes.put("M30", "Kết thúc chương trình");
            codes.put("G95", "Đơn vị tốc độ mm/phút"); codes.put("G96", "Tốc độ cắt không đổi");
            codes.put("G97", "Tắt tốc độ cắt không đổi");
          codes.put("M07", "Bật khí làm mát");
codes.put("M29", "Ghi dữ liệu trục chính cho ren");
codes.put("M47", "Lặp lại chương trình");
codes.put("M60", "Đổi pallet");
codes.put("M78", "Bật trục quay phụ");
codes.put("M79", "Tắt trục quay phụ");
codes.put("M82", "Bật súng thổi khí");
codes.put("M84", "Tắt động cơ bước");
codes.put("M140", "Đặt nhiệt độ giường in");
codes.put("M190", "Chờ giường đạt nhiệt độ");
codes.put("M104", "Đặt nhiệt độ đầu in");
codes.put("M109", "Chờ đầu in đạt nhiệt độ");

// === G-CODE mở rộng ===
codes.put("G04", "Dừng tạm thời (dwell)");
codes.put("G20", "Đơn vị inch");
codes.put("G21", "Đơn vị mm");
codes.put("G33", "Cắt ren");
codes.put("G70", "Lập trình theo inch");
codes.put("G71", "Lập trình theo mm");
codes.put("G73", "Khoan thô tốc độ cao");
codes.put("G81", "Chu trình khoan cơ bản");
codes.put("G82", "Chu trình khoan có dừng");
codes.put("G83", "Chu trình khoan sâu");
codes.put("G84", "Chu trình taro");
codes.put("G85", "Khoan không dừng");
codes.put("G86", "Khoan và dừng trục chính");
codes.put("G87", "Khoan ngược");
codes.put("G88", "Khoan và dừng tay");
codes.put("G89", "Chu trình khoan có giữ thời gian");

codes.put("G92", "Thiết lập vị trí hiện tại");
codes.put("G94", "Đơn vị tốc độ mm/phút");
codes.put("G95", "Đơn vị tốc độ mm/vòng");
codes.put("G96", "Tốc độ cắt không đổi");
codes.put("G97", "Tắt tốc độ cắt không đổi");
          

            for (String code : codes.keySet()) {
                JButton btn = new JButton("<html><b>" + code + "</b><br><small>" + codes.get(code) + "</small></html>");
                btn.addActionListener(e -> {
                    int pos = textPane.getCaretPosition();
                    try {
                        doc.insertString(pos, code + " ", null);
                        textPane.requestFocus();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
                buttonPanel.add(btn);
            }

            JMenuBar menuBar = new JMenuBar();
            JMenu fileMenu = new JMenu("📁 File");

            JMenuItem openItem = new JMenuItem("Mở file...");
            openItem.addActionListener(e -> {
                JFileChooser fc = new JFileChooser();
                fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("G-code", "tap", "gcode"));
                if (fc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                    try {
                        File file = fc.getSelectedFile();
                        String text = Files.readString(file.toPath());
                        textPane.setText(text);
                        frame.setTitle("✍️ G-code Editor - " + file.getName());
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame, "Lỗi: " + ex.getMessage());
                    }
                }
            });

            JMenuItem saveItem = new JMenuItem("💾 Lưu file...");
            saveItem.addActionListener(e -> {
                JFileChooser fc = new JFileChooser();
                fc.setSelectedFile(new File("machining.tap"));
                if (fc.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                    try {
                        File file = fc.getSelectedFile();
                        Files.writeString(file.toPath(), textPane.getText());
                        frame.setTitle("✍️ G-code Editor - " + file.getName());
                        JOptionPane.showMessageDialog(frame, "Đã lưu!");
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame, "Lỗi: " + ex.getMessage());
                    }
                }
            });

            fileMenu.add(openItem);
            fileMenu.add(saveItem);
            menuBar.add(fileMenu);
            frame.setJMenuBar(menuBar);

            textPane.setDropTarget(new DropTarget() {
                public synchronized void drop(DropTargetDropEvent evt) {
                    try {
                        evt.acceptDrop(DnDConstants.ACTION_COPY);
                        java.util.List<File> droppedFiles = (java.util.List<File>) evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                        for (File file : droppedFiles) {
                            if (file.getName().endsWith(".tap") || file.getName().endsWith(".gcode")) {
                                String content = Files.readString(file.toPath());
                                textPane.setText(content);
                                frame.setTitle("✍️ G-code Editor - " + file.getName());
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });

            javax.swing.Timer debounceTimer = new javax.swing.Timer(300, e -> {
                String[] lines = textPane.getText().split("\n");
                tableModel.setRowCount(0);
                for (int i = 0; i < lines.length; i++) {
                    tableModel.addRow(new Object[]{i + 1, lines[i]});
                }
                applyHighlighting(doc, gcodeStyle, defaultStyle);
                updateMetaTable(metaModel, parseMetadata(textPane.getText()));
            });
            debounceTimer.setRepeats(false);

            doc.addDocumentListener(new DocumentListener() {
                void scheduleUpdate() { debounceTimer.restart(); }
                public void insertUpdate(DocumentEvent e) { scheduleUpdate(); }
                public void removeUpdate(DocumentEvent e) { scheduleUpdate(); }
                public void changedUpdate(DocumentEvent e) { scheduleUpdate(); }
            });

            frame.setVisible(true);
        });
    }

    private static void applyHighlighting(StyledDocument doc, Style gcodeStyle, Style defaultStyle) {
        SwingUtilities.invokeLater(() -> {
            doc.setCharacterAttributes(0, doc.getLength(), defaultStyle, true);
            try {
                String text = doc.getText(0, doc.getLength());
                Matcher matcher = GCODE_PATTERN.matcher(text);
                while (matcher.find()) {
                    doc.setCharacterAttributes(matcher.start(), matcher.end() - matcher.start(), gcodeStyle, true);
                }
                Style paramStyle = doc.getStyle("param");
                if (paramStyle == null) {
                    paramStyle = doc.addStyle("param", null);
                    StyleConstants.setForeground(paramStyle, new Color(128, 0, 128));
                    StyleConstants.setBold(paramStyle, true);
                }
                Matcher axisMatcher = Pattern.compile("\\b([XYZFST])[-+]?\\d+(\\.\\d+)?").matcher(text);
                while (axisMatcher.find()) {
                    doc.setCharacterAttributes(axisMatcher.start(), axisMatcher.end() - axisMatcher.start(), paramStyle, true);
                }
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        });
    }

    private static Map<String, String> parseMetadata(String text) {
        Map<String, String> map = new LinkedHashMap<>();
        String[] lines = text.split("\n");
        Pattern commentPattern = Pattern.compile("\\(\\s*(.*?)\\s*\\)");
        Pattern keyValuePattern = Pattern.compile("([^:]+):\\s*(.*)");
        for (String line : lines) {
            Matcher cm = commentPattern.matcher(line);
            if (cm.find()) {
                String content = cm.group(1).trim();
                Matcher kv = keyValuePattern.matcher(content);
                if (kv.find()) {
                    String key = kv.group(1).trim();
                    String val = kv.group(2).trim();
                    map.put(key, val);
                }
            }
            if (line.contains("F")) {
                Matcher fMatch = Pattern.compile("F(\\d+(\\.\\d+)?)").matcher(line);
                if (fMatch.find()) {
                    map.put("Feedrate (F)", fMatch.group(1));
                }
            }
            if (line.contains("TOOL DIA")) {
                Matcher td = Pattern.compile("TOOL DIA\\s*:\\s*(\\d+(\\.\\d+)?)").matcher(line);
                if (td.find()) {
                    map.put("TOOL DIA", td.group(1));
                }
                Matcher len = Pattern.compile("LENGTH\\s*(\\d+(\\.\\d+)?)").matcher(line);
                if (len.find()) {
                    map.put("LENGTH", len.group(1));
                }
            }
        }
        return map;
    }

    private static void updateMetaTable(DefaultTableModel model, Map<String, String> meta) {
        model.setRowCount(0);
        for (Map.Entry<String, String> entry : meta.entrySet()) {
            model.addRow(new Object[]{entry.getKey(), entry.getValue()});
        }
    }
}
