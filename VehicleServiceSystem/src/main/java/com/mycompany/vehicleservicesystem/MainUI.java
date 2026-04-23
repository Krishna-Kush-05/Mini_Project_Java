package com.mycompany.vehicleservicesystem;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;

public class MainUI extends JFrame {

    // Sidebar buttons
    private JButton btnCustomer, btnVehicle, btnService, btnBill, btnExit;

    // Content area (cards)
    private JPanel contentPanel;
    private CardLayout cardLayout;

    // Color palette
    private static final Color SIDEBAR_BG   = new Color(28,  40,  70);
    private static final Color SIDEBAR_BTN  = new Color(40,  60, 100);
    private static final Color SIDEBAR_HOV  = new Color(37, 99, 235);
    private static final Color HEADER_BG    = new Color(37, 99, 235);
    private static final Color CONTENT_BG   = new Color(241,245,249);
    private static final Color WHITE        = Color.WHITE;
    private static final Color TEXT_DARK    = new Color(30,  41,  59);
    private static final Color TEXT_MUTED   = new Color(100,116,139);
    private static final Color TABLE_HEADER = new Color(37, 99, 235);
    private static final Color ROW_ALT      = new Color(248,250,252);
    private static final Color SUCCESS      = new Color(22, 163,  74);
    private static final Color DANGER       = new Color(220,  38,  38);

    // Fonts
    private static final Font FONT_TITLE  = new Font("Segoe UI", Font.BOLD,  20);
    private static final Font FONT_LABEL  = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font FONT_BTN    = new Font("Segoe UI", Font.BOLD,  13);
    private static final Font FONT_SIDE   = new Font("Segoe UI", Font.BOLD,  14);
    private static final Font FONT_TABLE  = new Font("Segoe UI", Font.PLAIN, 12);
    private static final Font FONT_THEAD  = new Font("Segoe UI", Font.BOLD,  12);
    private static final Font FONT_HINT   = new Font("Segoe UI", Font.ITALIC, 11);

    // DAOs
    private final CustomerDAO customerDAO = new CustomerDAO();
    private final VehicleDAO vehicleDAO = new VehicleDAO();
    private final ServiceRecordDAO serviceDAO = new ServiceRecordDAO();

    // =====================================================================
    //  CONSTRUCTOR
    // =====================================================================
    public MainUI() {
        setTitle("Vehicle Service Management System");
        setSize(1100, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(createHeader(),  BorderLayout.NORTH);
        add(createSidebar(), BorderLayout.WEST);
        add(createContent(), BorderLayout.CENTER);

        setVisible(true);
        showPanel("CUSTOMER");
    }

    // =====================================================================
    //  HEADER
    // =====================================================================
    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(HEADER_BG);
        header.setPreferredSize(new Dimension(0, 56));

        JLabel title = new JLabel("  Vehicle Service Management System");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(WHITE);

        JLabel sub = new JLabel("DBMS + Java Project  ");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sub.setForeground(new Color(186, 213, 255));

        header.add(title, BorderLayout.WEST);
        header.add(sub,   BorderLayout.EAST);
        return header;
    }

    // =====================================================================
    //  SIDEBAR
    // =====================================================================
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        sidebar.add(Box.createVerticalStrut(20));
        sidebar.add(sideLabel("MENU"));
        sidebar.add(Box.createVerticalStrut(10));

        btnCustomer = sideButton("Customers",       "CUSTOMER");
        btnVehicle  = sideButton("Vehicles",        "VEHICLE");
        btnService  = sideButton("Service Records", "SERVICE");
        btnBill     = sideButton("Bills",           "BILL");

        sidebar.add(btnCustomer);
        sidebar.add(Box.createVerticalStrut(6));
        sidebar.add(btnVehicle);
        sidebar.add(Box.createVerticalStrut(6));
        sidebar.add(btnService);
        sidebar.add(Box.createVerticalStrut(6));
        sidebar.add(btnBill);

        sidebar.add(Box.createVerticalGlue());
        sidebar.add(sideLabel(""));

        btnExit = sideButton("Exit", "EXIT");
        btnExit.setBackground(new Color(127, 29, 29));
        sidebar.add(btnExit);
        sidebar.add(Box.createVerticalStrut(16));

        return sidebar;
    }

    private JLabel sideLabel(String text) {
        JLabel lbl = new JLabel("  " + text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lbl.setForeground(new Color(148, 163, 184));
        lbl.setMaximumSize(new Dimension(200, 20));
        return lbl;
    }

    private JButton sideButton(String text, String panel) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_SIDE);
        btn.setForeground(new Color(203, 213, 225));
        btn.setBackground(SIDEBAR_BTN);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(200, 44));
        btn.setPreferredSize(new Dimension(200, 44));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(BorderFactory.createEmptyBorder(0, 18, 0, 0));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(SIDEBAR_HOV); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(SIDEBAR_BTN); }
        });

        btn.addActionListener(e -> {
            if (panel.equals("EXIT")) System.exit(0);
            else showPanel(panel);
        });

        return btn;
    }

    // =====================================================================
    //  CONTENT (CardLayout)
    // =====================================================================
    private JPanel createContent() {
        cardLayout   = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(CONTENT_BG);

        contentPanel.add(buildCustomerPanel(), "CUSTOMER");
        contentPanel.add(buildVehiclePanel(),  "VEHICLE");
        contentPanel.add(buildServicePanel(),  "SERVICE");
        contentPanel.add(buildBillPanel(),     "BILL");

        return contentPanel;
    }

    private void showPanel(String name) {
        cardLayout.show(contentPanel, name);
    }

    // =====================================================================
    //  REUSABLE UI HELPERS
    // =====================================================================
    private JTextField styledField(int cols) {
        JTextField tf = new JTextField(cols);
        tf.setFont(FONT_LABEL);
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(203,213,225), 1, true),
            BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        return tf;
    }

    private JComboBox<String> styledCombo(String[] items) {
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setFont(FONT_LABEL);
        cb.setBackground(WHITE);
        return cb;
    }

    private JButton styledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BTN);
        btn.setBackground(bg);
        btn.setForeground(WHITE);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btn.addMouseListener(new MouseAdapter() {
            Color orig = bg;
            public void mouseEntered(MouseEvent e) { btn.setBackground(orig.darker()); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(orig); }
        });
        return btn;
    }

    private JTable styledTable(DefaultTableModel model) {
        JTable table = new JTable(model) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table.setFont(FONT_TABLE);
        table.setRowHeight(28);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(219, 234, 254));
        table.setSelectionForeground(TEXT_DARK);

        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_THEAD);
        header.setBackground(TABLE_HEADER);
        header.setForeground(Color.BLACK);   // ← FIXED: Black text for readability
        header.setReorderingAllowed(false);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                if (!sel) setBackground(row % 2 == 0 ? WHITE : ROW_ALT);
                setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
                return this;
            }
        });
        return table;
    }

    private JLabel panelTitle(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FONT_TITLE);
        lbl.setForeground(TEXT_DARK);
        lbl.setBorder(BorderFactory.createEmptyBorder(16, 20, 8, 0));
        return lbl;
    }

    private void addFormRow(JPanel form, GridBagConstraints gbc,
                            String labelText, JComponent field,
                            int row, int col) {
        gbc.gridx = col * 2;
        gbc.gridy = row;
        gbc.weightx = 0;
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(FONT_LABEL);
        lbl.setForeground(TEXT_MUTED);
        form.add(lbl, gbc);

        gbc.gridx = col * 2 + 1;
        gbc.weightx = 1;
        form.add(field, gbc);
    }

    // Fixed hint method: places hint on the same y as next field's label? Better to increment row properly.
    private void addFormRowWithHint(JPanel form, GridBagConstraints gbc,
                                    String labelText, JComponent field,
                                    String hint, int row, int col) {
        addFormRow(form, gbc, labelText, field, row, col);
        // Add hint on a new row below the field
        gbc.gridx = col * 2 + 1;
        gbc.gridy = row + 1;
        gbc.insets = new Insets(0, 10, 2, 10);
        JLabel hintLabel = new JLabel(hint);
        hintLabel.setFont(FONT_HINT);
        hintLabel.setForeground(TEXT_MUTED);
        form.add(hintLabel, gbc);
        gbc.insets = new Insets(8, 10, 8, 10); // reset
    }

    private void popup(String msg, boolean success) {
        JOptionPane.showMessageDialog(this, msg,
            success ? "Success" : "Error",
            success ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
    }

    // =====================================================================
    //  PANEL 1 — CUSTOMERS
    // =====================================================================
    private JPanel buildCustomerPanel() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(CONTENT_BG);
        root.add(panelTitle("Customer Management"), BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(10, 16, 10, 16),
            BorderFactory.createLineBorder(new Color(226,232,240), 1, true)
        ));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets  = new Insets(8, 10, 8, 10);
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        gbc.anchor  = GridBagConstraints.WEST;

        JTextField tfName    = styledField(15);
        JTextField tfPhone   = styledField(12);
        JTextField tfEmail   = styledField(18);
        JTextField tfAddress = styledField(20);

        addFormRow(form, gbc, "Full Name",    tfName,    0, 0);
        addFormRow(form, gbc, "Phone",        tfPhone,   0, 1);
        addFormRow(form, gbc, "Email",        tfEmail,   1, 0);
        addFormRow(form, gbc, "Address",      tfAddress, 1, 1);

        JButton btnAdd     = styledButton("Add Customer",  SUCCESS);
        JButton btnRefresh = styledButton("Refresh",       HEADER_BG);
        JButton btnClear   = styledButton("Clear",         TEXT_MUTED);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        btnRow.setBackground(WHITE);
        btnRow.add(btnAdd);
        btnRow.add(btnRefresh);
        btnRow.add(btnClear);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 4; gbc.weightx = 1;
        form.add(btnRow, gbc);

        String[] cols = {"ID", "Name", "Phone", "Email", "Address"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        JTable table = styledTable(model);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 16, 16, 16));

        Runnable loadData = () -> {
            model.setRowCount(0);
            try (Connection c = DBConnection.getConnection();
                 ResultSet rs = c.createStatement().executeQuery("SELECT * FROM Customer")) {
                while (rs.next())
                    model.addRow(new Object[]{
                        rs.getInt("customer_id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("address")
                    });
            } catch (SQLException ex) { popup(ex.getMessage(), false); }
        };
        loadData.run();

        btnAdd.addActionListener(e -> {
            String name    = tfName.getText().trim();
            String phone   = tfPhone.getText().trim();
            String email   = tfEmail.getText().trim();
            String address = tfAddress.getText().trim();
            if (name.isEmpty()) { popup("Name is required.", false); return; }
            customerDAO.addCustomer(name, phone, email, address);
            popup("Customer added successfully!", true);
            loadData.run();
            tfName.setText(""); tfPhone.setText("");
            tfEmail.setText(""); tfAddress.setText("");
        });

        btnRefresh.addActionListener(e -> loadData.run());
        btnClear.addActionListener(e -> {
            tfName.setText(""); tfPhone.setText("");
            tfEmail.setText(""); tfAddress.setText("");
        });

        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(CONTENT_BG);
        center.add(form,   BorderLayout.NORTH);
        center.add(scroll, BorderLayout.CENTER);

        root.add(center, BorderLayout.CENTER);
        return root;
    }

    // =====================================================================
    //  PANEL 2 — VEHICLES
    // =====================================================================
    private JComboBox<ComboItem> customerCombo;

    private class ComboItem {
        private int id;
        private String name;
        public ComboItem(int id, String name) { this.id = id; this.name = name; }
        public int getId() { return id; }
        public String toString() { return name + " (ID: " + id + ")"; }
    }

    private JPanel buildVehiclePanel() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(CONTENT_BG);
        root.add(panelTitle("Vehicle Management"), BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(10, 16, 10, 16),
            BorderFactory.createLineBorder(new Color(226,232,240), 1, true)
        ));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill   = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        customerCombo = new JComboBox<>();
        customerCombo.setFont(FONT_LABEL);
        customerCombo.setBackground(WHITE);
        loadCustomerCombo();

        JTextField tfVehNo   = styledField(12);
        JTextField tfBrand   = styledField(10);
        JTextField tfModel   = styledField(10);
        JComboBox<String> cbType = styledCombo(
            new String[]{"Sedan","Hatchback","SUV","Bike","Truck","Van"});

        int row = 0;
        addFormRowWithHint(form, gbc, "Customer", customerCombo,
                "Select the owner", row++, 0);
        addFormRowWithHint(form, gbc, "Vehicle No", tfVehNo,
                "Registration number (e.g., MH12AB1234)", row++, 0);
        addFormRow(form, gbc, "Brand", tfBrand, row++, 0);
        addFormRow(form, gbc, "Model", tfModel, row++, 0);
        addFormRow(form, gbc, "Type", cbType, row++, 0);

        JButton btnAdd     = styledButton("Add Vehicle", SUCCESS);
        JButton btnRefresh = styledButton("Refresh",     HEADER_BG);
        JButton btnClear   = styledButton("Clear",       TEXT_MUTED);
        JButton btnReload  = styledButton("Reload Customers", TEXT_MUTED);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        btnRow.setBackground(WHITE);
        btnRow.add(btnAdd); btnRow.add(btnRefresh);
        btnRow.add(btnClear); btnRow.add(btnReload);
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 4; gbc.weightx = 1;
        form.add(btnRow, gbc);

        String[] cols = {"ID","Customer","Vehicle No","Brand","Model","Type"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        JTable table = styledTable(model);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 16, 16, 16));

        Runnable loadData = () -> {
            model.setRowCount(0);
            String sql = "SELECT v.vehicle_id, c.name, v.vehicle_number, v.brand, v.model, v.type " +
                         "FROM Vehicle v JOIN Customer c ON v.customer_id = c.customer_id";
            try (Connection c = DBConnection.getConnection();
                 ResultSet rs = c.createStatement().executeQuery(sql)) {
                while (rs.next())
                    model.addRow(new Object[]{
                        rs.getInt("vehicle_id"), rs.getString("name"),
                        rs.getString("vehicle_number"), rs.getString("brand"),
                        rs.getString("model"), rs.getString("type")
                    });
            } catch (SQLException ex) { popup(ex.getMessage(), false); }
        };
        loadData.run();

        btnAdd.addActionListener(e -> {
            ComboItem selected = (ComboItem) customerCombo.getSelectedItem();
            if (selected == null) { popup("Please select a customer.", false); return; }
            int custId = selected.getId();
            String vno = tfVehNo.getText().trim();
            String br  = tfBrand.getText().trim();
            String mo  = tfModel.getText().trim();
            String ty  = (String) cbType.getSelectedItem();
            if (vno.isEmpty()) { popup("Vehicle number required.", false); return; }
            vehicleDAO.addVehicle(custId, vno, br, mo, ty);
            popup("Vehicle added successfully!", true);
            loadData.run();
            tfVehNo.setText(""); tfBrand.setText(""); tfModel.setText("");
        });

        btnRefresh.addActionListener(e -> loadData.run());
        btnClear.addActionListener(e -> {
            tfVehNo.setText(""); tfBrand.setText(""); tfModel.setText("");
        });
        btnReload.addActionListener(e -> loadCustomerCombo());

        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(CONTENT_BG);
        center.add(form, BorderLayout.NORTH);
        center.add(scroll, BorderLayout.CENTER);
        root.add(center, BorderLayout.CENTER);
        return root;
    }

    private void loadCustomerCombo() {
        customerCombo.removeAllItems();
        try (Connection c = DBConnection.getConnection();
             ResultSet rs = c.createStatement().executeQuery("SELECT customer_id, name FROM Customer ORDER BY name")) {
            while (rs.next())
                customerCombo.addItem(new ComboItem(rs.getInt("customer_id"), rs.getString("name")));
        } catch (SQLException ex) { popup("Failed to load customers: " + ex.getMessage(), false); }
    }

    // =====================================================================
    //  PANEL 3 — SERVICE RECORDS (fixed layout)
    // =====================================================================
    private JComboBox<VehicleComboItem> vehicleCombo;
    private int lastRecordId = -1;
    private JLabel lblLastId;
    private JTextField tfInDate, tfSvcDate;
    private JComboBox<String> cbStatus;
    private DefaultTableModel serviceTableModel;

    private class VehicleComboItem {
        private int id;
        private String number;
        public VehicleComboItem(int id, String number) { this.id = id; this.number = number; }
        public int getId() { return id; }
        public String toString() { return number + " (ID: " + id + ")"; }
    }

    private JPanel buildServicePanel() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(CONTENT_BG);
        root.add(panelTitle("Service Records"), BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(10, 16, 10, 16),
            BorderFactory.createLineBorder(new Color(226,232,240), 1, true)
        ));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill   = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        vehicleCombo = new JComboBox<>();
        vehicleCombo.setFont(FONT_LABEL);
        vehicleCombo.setBackground(WHITE);
        loadVehicleCombo();

        tfInDate  = styledField(10);
        tfSvcDate = styledField(10);
        tfInDate.setText(LocalDate.now().toString());

        cbStatus = styledCombo(new String[]{"In Progress","Completed","Pending","Delivered"});

        JComboBox<String> cbService = styledCombo(
            new String[]{"1: Oil Change (500)", "2: Tyre Rotation (300)",
                         "3: Battery Check (200)", "4: Full Service (2000)",
                         "5: AC Repair (1500)", "6: Brake Inspection (400)"});

        JButton btnToday = new JButton("Today");
        btnToday.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        btnToday.setMargin(new Insets(2, 5, 2, 5));
        btnToday.addActionListener(e -> tfInDate.setText(LocalDate.now().toString()));

        JPanel inDatePanel = new JPanel(new BorderLayout(5, 0));
        inDatePanel.add(tfInDate, BorderLayout.CENTER);
        inDatePanel.add(btnToday, BorderLayout.EAST);

        int row = 0;
        addFormRow(form, gbc, "Vehicle", vehicleCombo,
                 row++, 0);
        addFormRow(form, gbc, "In Date", inDatePanel,
                 row++, 0);
        addFormRow(form, gbc, "Service Date", tfSvcDate,
                 row++, 0);
        addFormRow(form, gbc, "Status", cbStatus, row++, 0);
        addFormRow(form, gbc, "Service to Assign", cbService,
                row++, 0);

        JButton btnCreate   = styledButton("Create Record", SUCCESS);
        JButton btnAssign   = styledButton("Assign Service", HEADER_BG);
        JButton btnComplete = styledButton("Complete Service", new Color(245, 158, 11));
        JButton btnRefresh  = styledButton("Refresh", TEXT_MUTED);
        JButton btnReload   = styledButton("Reload Vehicles", TEXT_MUTED);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        btnRow.setBackground(WHITE);
        btnRow.add(btnCreate); btnRow.add(btnAssign); btnRow.add(btnComplete);
        btnRow.add(btnRefresh); btnRow.add(btnReload);
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 4; gbc.weightx = 1;
        form.add(btnRow, gbc);

        lblLastId = new JLabel("Last created Record ID: —");
        lblLastId.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblLastId.setForeground(HEADER_BG);
        lblLastId.setBorder(BorderFactory.createEmptyBorder(6, 16, 6, 0));

        String[] cols = {"Record ID","Vehicle No","Customer","In Date","Svc Date","Status"};
        serviceTableModel = new DefaultTableModel(cols, 0);
        JTable table = styledTable(serviceTableModel);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 16, 16, 16));

        Runnable loadData = this::refreshServiceTable;
        loadData.run();

        btnCreate.addActionListener(e -> {
            VehicleComboItem selVeh = (VehicleComboItem) vehicleCombo.getSelectedItem();
            if (selVeh == null) { popup("Select a vehicle.", false); return; }
            String ind = tfInDate.getText().trim();
            if (ind.isEmpty()) { popup("In Date required.", false); return; }
            int rid = serviceDAO.createServiceRecord(selVeh.getId(), ind,
                         tfSvcDate.getText().trim(), (String)cbStatus.getSelectedItem());
            lastRecordId = rid;
            lblLastId.setText("Last created Record ID: " + rid);
            popup("Record created! ID=" + rid, true);
            loadData.run();
        });

        btnAssign.addActionListener(e -> {
            if (lastRecordId == -1) { popup("Create a record first.", false); return; }
            String sel = (String) cbService.getSelectedItem();
            int svcId = Integer.parseInt(sel.split(":")[0]);
            serviceDAO.addServiceToRecord(lastRecordId, svcId);
            popup("Service assigned.", true);
        });

        btnComplete.addActionListener(e -> {
            if (lastRecordId == -1) { popup("No record selected.", false); return; }
            String today = LocalDate.now().toString();
            try (Connection c = DBConnection.getConnection();
                 PreparedStatement ps = c.prepareStatement(
                     "UPDATE Service_Record SET status='Completed', out_date=? WHERE record_id=?")) {
                ps.setString(1, today);
                ps.setInt(2, lastRecordId);
                ps.executeUpdate();
                serviceDAO.generateBill(lastRecordId, "Cash");
                popup("Service completed. Bill generated.", true);
                loadData.run();
            } catch (SQLException ex) { popup(ex.getMessage(), false); }
        });

        btnRefresh.addActionListener(e -> loadData.run());
        btnReload.addActionListener(e -> loadVehicleCombo());

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int r = table.getSelectedRow();
                    if (r >= 0) {
                        lastRecordId = (int) serviceTableModel.getValueAt(r, 0);
                        lblLastId.setText("Selected Record ID: " + lastRecordId);
                    }
                }
            }
        });

        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(CONTENT_BG);
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(CONTENT_BG);
        top.add(form, BorderLayout.NORTH);
        top.add(lblLastId, BorderLayout.SOUTH);
        center.add(top, BorderLayout.NORTH);
        center.add(scroll, BorderLayout.CENTER);
        root.add(center, BorderLayout.CENTER);
        return root;
    }

    private void loadVehicleCombo() {
        vehicleCombo.removeAllItems();
        try (Connection c = DBConnection.getConnection();
             ResultSet rs = c.createStatement().executeQuery(
                 "SELECT vehicle_id, vehicle_number FROM Vehicle ORDER BY vehicle_number")) {
            while (rs.next())
                vehicleCombo.addItem(new VehicleComboItem(rs.getInt("vehicle_id"), rs.getString("vehicle_number")));
        } catch (SQLException ex) { popup("Failed to load vehicles: " + ex.getMessage(), false); }
    }

    private void refreshServiceTable() {
        serviceTableModel.setRowCount(0);
        String sql = "SELECT sr.record_id, v.vehicle_number, c.name, sr.in_date, sr.service_date, sr.status " +
                     "FROM Service_Record sr JOIN Vehicle v ON sr.vehicle_id=v.vehicle_id " +
                     "JOIN Customer c ON v.customer_id=c.customer_id";
        try (Connection c = DBConnection.getConnection();
             ResultSet rs = c.createStatement().executeQuery(sql)) {
            while (rs.next())
                serviceTableModel.addRow(new Object[]{
                    rs.getInt("record_id"), rs.getString("vehicle_number"),
                    rs.getString("name"), rs.getString("in_date"),
                    rs.getString("service_date"), rs.getString("status")
                });
        } catch (SQLException ex) { popup(ex.getMessage(), false); }
    }

    // =====================================================================
    //  PANEL 4 — BILLS (unchanged but header fixed)
    // =====================================================================
    private JPanel buildBillPanel() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(CONTENT_BG);
        root.add(panelTitle("Bill Management"), BorderLayout.NORTH);

        JPanel instruction = new JPanel();
        instruction.setBackground(new Color(255, 251, 235));
        instruction.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(8, 16, 8, 16),
            BorderFactory.createLineBorder(new Color(253, 230, 138), 1, true)
        ));
        JLabel instrLabel = new JLabel("<html><b>How to use:</b> Select a record, click <b>Generate Bill</b>. " +
                "Bill auto-calculates from services. Use <b>Mark Paid</b> to update.</html>");
        instrLabel.setFont(FONT_LABEL);
        instruction.add(instrLabel);
        root.add(instruction, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(CONTENT_BG);

        String[] cols = {"Record ID","Vehicle No","Customer","Status","Bill Generated","Paid"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        JTable table = styledTable(model);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));

        Runnable loadData = () -> {
            model.setRowCount(0);
            String sql = "SELECT sr.record_id, v.vehicle_number, c.name, sr.status, " +
                         "CASE WHEN b.bill_id IS NOT NULL THEN 'Yes' ELSE 'No' END AS bill_gen, " +
                         "COALESCE(b.payment_status, 'Not Generated') AS pay_status " +
                         "FROM Service_Record sr JOIN Vehicle v ON sr.vehicle_id=v.vehicle_id " +
                         "JOIN Customer c ON v.customer_id=c.customer_id " +
                         "LEFT JOIN Bill b ON sr.record_id=b.record_id ORDER BY sr.record_id DESC";
            try (Connection c = DBConnection.getConnection();
                 ResultSet rs = c.createStatement().executeQuery(sql)) {
                while (rs.next())
                    model.addRow(new Object[]{ rs.getInt("record_id"), rs.getString("vehicle_number"),
                        rs.getString("name"), rs.getString("status"), rs.getString("bill_gen"),
                        rs.getString("pay_status") });
            } catch (SQLException ex) { popup(ex.getMessage(), false); }
        };
        loadData.run();

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnPanel.setBackground(WHITE);
        JButton btnGenerate = styledButton("Generate Bill for Selected", SUCCESS);
        JButton btnMarkPaid = styledButton("Mark as Paid", HEADER_BG);
        JButton btnView     = styledButton("View Bill Details", TEXT_MUTED);
        JButton btnRefresh  = styledButton("Refresh", TEXT_MUTED);
        btnPanel.add(btnGenerate); btnPanel.add(btnMarkPaid);
        btnPanel.add(btnView); btnPanel.add(btnRefresh);

        JTextArea billArea = new JTextArea(6, 50);
        billArea.setEditable(false);
        billArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        billArea.setBackground(new Color(248,250,252));
        billArea.setBorder(BorderFactory.createTitledBorder("Bill Details"));

        btnGenerate.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) { popup("Select a record.", false); return; }
            int rid = (int) model.getValueAt(row, 0);
            if ("Yes".equals(model.getValueAt(row, 4))) { popup("Bill already exists.", false); return; }
            String[] modes = {"Cash","Card","UPI","Net Banking"};
            String mode = (String) JOptionPane.showInputDialog(this, "Select Payment Mode:",
                    "Generate Bill", JOptionPane.QUESTION_MESSAGE, null, modes, modes[0]);
            if (mode != null) {
                serviceDAO.generateBill(rid, mode);
                popup("Bill generated!", true);
                loadData.run();
            }
        });

        btnMarkPaid.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) return;
            int rid = (int) model.getValueAt(row, 0);
            String status = (String) model.getValueAt(row, 5);
            if ("Paid".equals(status)) { popup("Already paid.", false); return; }
            if ("Not Generated".equals(status)) { popup("Generate bill first.", false); return; }
            try (Connection c = DBConnection.getConnection();
                 PreparedStatement ps = c.prepareStatement(
                     "UPDATE Bill SET payment_status='Paid' WHERE record_id=?")) {
                ps.setInt(1, rid);
                ps.executeUpdate();
                popup("Marked as Paid.", true);
                loadData.run();
            } catch (SQLException ex) { popup(ex.getMessage(), false); }
        });

        btnView.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) return;
            int rid = (int) model.getValueAt(row, 0);
            String sql = "SELECT b.bill_id, b.total_amount, b.payment_mode, b.payment_status, " +
                         "GROUP_CONCAT(s.service_name SEPARATOR ', ') AS services " +
                         "FROM Bill b JOIN Service_Details sd ON b.record_id=sd.record_id " +
                         "JOIN Service s ON sd.service_id=s.service_id WHERE b.record_id=? GROUP BY b.bill_id";
            try (Connection c = DBConnection.getConnection();
                 PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setInt(1, rid);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    billArea.setText(String.format(
                        "Bill ID: %d\nRecord ID: %d\nServices: %s\nTotal: Rs. %.2f\nMode: %s\nStatus: %s",
                        rs.getInt("bill_id"), rid, rs.getString("services"),
                        rs.getDouble("total_amount"), rs.getString("payment_mode"),
                        rs.getString("payment_status")));
                } else billArea.setText("No bill found.");
            } catch (SQLException ex) { popup(ex.getMessage(), false); }
        });

        btnRefresh.addActionListener(e -> loadData.run());

        mainPanel.add(scroll, BorderLayout.CENTER);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);
        root.add(mainPanel, BorderLayout.CENTER);
        root.add(billArea, BorderLayout.SOUTH);
        return root;
    }

    // =====================================================================
    //  LAUNCH
    // =====================================================================
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception ignored) {}
        SwingUtilities.invokeLater(MainUI::new);
    }
}