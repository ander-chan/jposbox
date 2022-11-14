/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.takepos.jposbox;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import gnu.io.CommPortIdentifier;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.ResultSet;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author Jove
 */
public class PosBoxFrame extends javax.swing.JFrame {

    public static String DriverDB = "org.apache.derby.jdbc.EmbeddedDriver";
    static String connectionURL = "jdbc:derby:" + System.getProperty("user.home") + "/jPosBox/db;create=true";
    Statement stmt = null;
     private static final String IPV4_PATTERN_SHORTEST =
          "^((192|(168|1[0-9]|[1-9]|)[0-9])(\\.(?!$)|$)){4}$";
     private static final Pattern pattern = Pattern.compile(IPV4_PATTERN_SHORTEST);

    public static boolean isValid(final String email) {
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    /**
     * Creates new form PosBoxFrame
     */
    public PosBoxFrame() {
        initComponents();
        //serial
        WeighingScaleEventListener ws = new WeighingScaleEventListener("/home/ander/virtual-tty");
        try {
            ws.initializePort();
        } catch (Exception ex) {
            Logger.getLogger(PosBoxFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
listPorts();
        
        try {
            boolean any = false;
            Enumeration<NetworkInterface> nics = NetworkInterface
                    .getNetworkInterfaces();
            while (nics.hasMoreElements()) {
                NetworkInterface nic = nics.nextElement();
                Enumeration<InetAddress> addrs = nic.getInetAddresses();
                while (addrs.hasMoreElements()) {
                    InetAddress addr = addrs.nextElement();
                    System.out.printf("%s %s%n", nic.getName(),
                           addr.getHostAddress());
                    if(isValid(addr.getHostAddress())){
                        comboInterfaces.addItem(addr.getHostAddress());
                        any = true;
                    }
                }
            }
           
            int min = Math.min(labelQr.getHeight(), labelQr.getWidth());
             if(any){
                 try {
                    String ip_ =comboInterfaces.getSelectedItem().toString();
                    labelQr.setIcon(new ImageIcon(createQR("https://" + ip_ + ":8111", "UTF-8", min, min)));
                } catch (WriterException | IOException ex) {
                    Logger.getLogger(PosBoxFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
             }

            comboInterfaces.addActionListener ((ActionEvent e) -> {
                try {
                    String ip_ =comboInterfaces.getSelectedItem().toString();
                    labelQr.setIcon(new ImageIcon(createQR("https://" + ip_ + ":8111", "UTF-8", min, min)));
                } catch (WriterException | IOException ex) {
                    Logger.getLogger(PosBoxFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

           
           
        } catch (IOException ex) {
            Logger.getLogger(PosBoxFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        PosBoxesComboBox = new javax.swing.JComboBox<>();
        jCheckBox1 = new javax.swing.JCheckBox();
        jButton6 = new javax.swing.JButton();
        startminimized = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        ComboPrinter1 = new javax.swing.JComboBox<>();
        SaveButton1 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        ComboPrinter2 = new javax.swing.JComboBox<>();
        jButton3 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        ComboPrinter3 = new javax.swing.JComboBox<>();
        jButton4 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        labelQr = new javax.swing.JLabel();
        comboInterfaces = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        comboPort = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        labelWeight = new javax.swing.JLabel();
        jToggleButton1 = new javax.swing.JToggleButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("TakePOS Connector");

        jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane1StateChanged(evt);
            }
        });

        jLabel1.setText("Printers:");

        PosBoxesComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0", "1", "2", "3" }));

        jCheckBox1.setText("Run connector on system startup");
        jCheckBox1.setEnabled(false);

        jButton6.setText("Save");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        startminimized.setText("Start minimized");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(PosBoxesComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jCheckBox1))
                        .addGap(0, 142, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(startminimized)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(PosBoxesComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheckBox1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 64, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton6)
                    .addComponent(startminimized))
                .addContainerGap())
        );

        jTabbedPane1.addTab("General", jPanel1);

        jLabel2.setText("Printer:");

        SaveButton1.setText("Save");
        SaveButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SaveButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(ComboPrinter1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(SaveButton1))
                .addContainerGap(257, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(ComboPrinter1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 98, Short.MAX_VALUE)
                .addComponent(SaveButton1)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Main printer", jPanel2);

        jLabel4.setText("Printer:");

        jButton3.setText("Save");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(ComboPrinter2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton3))
                .addContainerGap(257, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(ComboPrinter2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 98, Short.MAX_VALUE)
                .addComponent(jButton3)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Printer 2", jPanel3);

        jLabel5.setText("Printer:");

        jButton4.setText("Save");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(ComboPrinter3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton4))
                .addContainerGap(257, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(ComboPrinter3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 98, Short.MAX_VALUE)
                .addComponent(jButton4)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Printer 3", jPanel5);

        jLabel3.setText("IP:");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(labelQr, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(comboInterfaces, 0, 241, Short.MAX_VALUE))
                .addContainerGap(119, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboInterfaces, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelQr, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Cert.", jPanel4);

        jLabel6.setText("Port:");

        jLabel7.setText("Weight:");

        labelWeight.setText("...");

        jToggleButton1.setText("Connect");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comboPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jToggleButton1))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelWeight)))
                .addContainerGap(188, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(comboPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jToggleButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(labelWeight))
                .addContainerGap(104, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("tab6", jPanel6);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );

        getAccessibleContext().setAccessibleDescription("jPosBox");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTabbedPane1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane1StateChanged
        // TODO add your handling code here:
        if (jTabbedPane1.getSelectedIndex() == 3) {
            //   jPanel4FocusGained(new FocusEvent(jTabbedPane1, 0));
            //do something
        }
    }//GEN-LAST:event_jTabbedPane1StateChanged

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        SaveAll();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void SaveButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaveButton1ActionPerformed
        SaveAll();
    }//GEN-LAST:event_SaveButton1ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        SaveAll();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
         SaveAll();
    }//GEN-LAST:event_jButton4ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new PosBoxFrame().setVisible(true);
        });
    }

    // Function to create the QR code
    public static BufferedImage createQR(String data,
            String charset,
            int height, int width)
            throws WriterException, IOException {
        HashMap<EncodeHintType, ErrorCorrectionLevel> map = new HashMap<>();
        map.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

        BitMatrix matrix = new MultiFormatWriter().encode(
                new String(data.getBytes(charset), charset),
                BarcodeFormat.QR_CODE, width, height, map);

        // Make the BufferedImage that are to hold the QRCode
        int matrixWidth = matrix.getWidth();
        BufferedImage image = new BufferedImage(matrixWidth, matrixWidth, BufferedImage.TYPE_INT_RGB);
        image.createGraphics();

        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, matrixWidth, matrixWidth);
        // Paint and save the image using the ByteMatrix
        graphics.setColor(Color.BLACK);

        for (int i = 0; i < matrixWidth; i++) {
            for (int j = 0; j < matrixWidth; j++) {
                if (matrix.get(i, j)) {
                    graphics.fillRect(i, j, 1, 1);
                }
            }
        }
        return image;

    }

    public void LoadDB() {
        try {
            Class.forName(DriverDB);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PosBoxFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            Connection conn = DriverManager.getConnection(PosBoxFrame.connectionURL, "PosBoxFrame", "PosBoxFrame");
            stmt = conn.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(PosBoxFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            stmt.execute("CREATE TABLE \"conf\"\n"
                    + "(\n"
                    + "\"id\" INT not null primary key\n"
                    + "        GENERATED ALWAYS AS IDENTITY\n"
                    + "        (START WITH 1, INCREMENT BY 1),   \n"
                    + "\"name\" VARCHAR(80),     \n"
                    + "\"value\" VARCHAR(300)     \n"
                    + ")");
        } catch (SQLException e) {
            /*If is created do nothing*/
        }
        //PosBoxes
        PosBoxesComboBox.setSelectedIndex(Integer.parseInt(getconf("PosBoxes")));
        switch (getconf("PosBoxes")) {
            case "0":
                jTabbedPane1.setEnabledAt(1, false);
                jTabbedPane1.setEnabledAt(2, false);
                jTabbedPane1.setEnabledAt(3, false);
                break;
            case "1":
                jTabbedPane1.setEnabledAt(1, true);
                jTabbedPane1.setEnabledAt(2, false);
                jTabbedPane1.setEnabledAt(3, false);
                break;
            case "2":
                jTabbedPane1.setEnabledAt(1, true);
                jTabbedPane1.setEnabledAt(2, true);
                jTabbedPane1.setEnabledAt(3, false);
                break;
            case "3":
                jTabbedPane1.setEnabledAt(1, true);
                jTabbedPane1.setEnabledAt(2, true);
                jTabbedPane1.setEnabledAt(3, true);
                break;
            default:
                break;
        }
        //Printers
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
        for (PrintService printer : printServices) {
            ComboPrinter1.addItem(printer.getName());
            ComboPrinter2.addItem(printer.getName());
            ComboPrinter3.addItem(printer.getName());
        }
        ComboPrinter1.setSelectedItem(getconf("Printer1"));
        ComboPrinter2.setSelectedItem(getconf("Printer2"));
        ComboPrinter3.setSelectedItem(getconf("Printer3"));

        //Run PosBoxes automatically
        if (getconf("auto").equals("on")) {
            if (getconf("PosBoxes").equals("1") || getconf("PosBoxes").equals("2")) {
                StartPosBox1();
            }
            if (getconf("PosBoxes").equals("2")) {
                StartPosBox2();
            }
           
        }

        //For Dolibarr
        StartPosBoxDolibarr();

        if (getconf("startminimized").equals("yes")) {
            startminimized.setSelected(true);
        }

    }

    public void tray() {
        Image img = Toolkit.getDefaultToolkit().createImage("takepos.png");
       final TrayIcon trayIcon = new TrayIcon(img);
       final SystemTray systemTray = SystemTray.getSystemTray();
        //create components of system tray
        trayIcon.addActionListener((ActionEvent actionEvent) -> {
            System.out.println("In here!");
            trayIcon.displayMessage("Test","Some action happened",TrayIcon.MessageType.INFO);
        });

        try{
            systemTray.add(trayIcon);
        }catch(AWTException e){
            System.out.println("TrayIcon could not be added.");
        }
        //boolean istray = tray.tray();
       /* systemTray.
        if (istray) {
            setVisible(false);
        }*/
    }

    public void StartPosBox1() {
        Web web = new Web();
        int error = web.StartServer(Integer.parseInt(getconf("PortPrinter1")), getconf("Printer1"));
        if (error > 0) {
            //int dialogButton = JOptionPane.YES_OPTION;
            JOptionPane.showMessageDialog(this, "Port " + error + " is in use");
        }
    }

    public void StartPosBox2() {
        Web web = new Web();
        int error = web.StartServer(Integer.parseInt(getconf("PortPrinter2")), getconf("Printer2"));
        if (error > 0) {
            //int dialogButton = JOptionPane.YES_OPTION;
            JOptionPane.showMessageDialog(this, "Port " + error + " is in use");
        }
    }

    public void StartPosBoxDolibarr() {
        Web web = new Web();
        int error = web.StartServer(8111, "");
        if (error > 0) {
            //int dialogButton = JOptionPane.YES_OPTION;
            JOptionPane.showMessageDialog(this, "Port " + error + " is in use");
        }
    }

    public void saveconf(String name, String value) {
        int count = 0;
        try {
            count = stmt.executeUpdate("UPDATE PosBoxFrame.\"conf\" SET \"value\" ='" + value + "' WHERE \"name\"='" + name + "'");
        } catch (SQLException ex) {
            Logger.getLogger(PosBoxFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (count == 0) try {
            stmt.execute("INSERT INTO PosBoxFrame.\"conf\" (\"name\", \"value\") values ('" + name + "','" + value + "')");
        } catch (SQLException ex) {
            Logger.getLogger(PosBoxFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getconf(String name) {
        ResultSet rs = null;
        String result = "nothing";
        try {
            rs = stmt.executeQuery("SELECT \"value\" from PosBoxFrame.\"conf\" where \"name\"='" + name + "'");
        } catch (SQLException ex) {
            Logger.getLogger(PosBoxFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            if (rs != null && rs.next()) {
                result = rs.getString("value");
            }
        } catch (SQLException ex) {
            Logger.getLogger(PosBoxFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        // VALORES POR DEFECTO
        if (result.equals("nothing")) {
            if (null != name) {
                switch (name) {
                    case "PosBoxes":
                        return "0";
                    case "PortPrinter1":
                        return "8069";
                    case "PortPrinter2":
                        return "8070";
                    default:
                        break;
                }
            }
        }
        // FIN VALORES POR DEFECTO
        return result;
    }

    public void SaveAll() {
        saveconf("PosBoxes", Integer.toString(PosBoxesComboBox.getSelectedIndex()));
        switch (getconf("PosBoxes")) {
            case "0":
                jTabbedPane1.setEnabledAt(1, false);
                jTabbedPane1.setEnabledAt(2, false);
                jTabbedPane1.setEnabledAt(3, false);
                break;
            case "1":
                jTabbedPane1.setEnabledAt(1, true);
                jTabbedPane1.setEnabledAt(2, false);
                jTabbedPane1.setEnabledAt(3, false);
                break;
            case "2":
                jTabbedPane1.setEnabledAt(1, true);
                jTabbedPane1.setEnabledAt(2, true);
                jTabbedPane1.setEnabledAt(3, false);
                break;
            case "3":
                jTabbedPane1.setEnabledAt(1, true);
                jTabbedPane1.setEnabledAt(2, true);
                jTabbedPane1.setEnabledAt(3, true);
                break;
            default:
                break;
        }
        if (startminimized.isSelected()) {
            saveconf("startminimized", "yes");
        } else {
            saveconf("startminimized", "no");
        }
        saveconf("Printer1", ComboPrinter1.getSelectedItem().toString());
        saveconf("Printer2", ComboPrinter2.getSelectedItem().toString());
        saveconf("Printer3", ComboPrinter3.getSelectedItem().toString());
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JComboBox<String> ComboPrinter1;
    public static javax.swing.JComboBox<String> ComboPrinter2;
    public static javax.swing.JComboBox<String> ComboPrinter3;
    private javax.swing.JComboBox<String> PosBoxesComboBox;
    private javax.swing.JButton SaveButton1;
    private javax.swing.JComboBox<String> comboInterfaces;
    private javax.swing.JComboBox<String> comboPort;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton6;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JLabel labelQr;
    private javax.swing.JLabel labelWeight;
    private javax.swing.JCheckBox startminimized;
    // End of variables declaration//GEN-END:variables
 static void listPorts()
    {
        java.util.Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier.getPortIdentifiers();
        while ( portEnum.hasMoreElements() ) 
        {
            CommPortIdentifier portIdentifier = portEnum.nextElement();
            System.out.println(portIdentifier.getName()  +  " - " +  getPortTypeName(portIdentifier.getPortType()) );
        }        
    }
 static String getPortTypeName ( int portType )
    {
        switch ( portType )
        {
            case CommPortIdentifier.PORT_I2C:
                return "I2C";
            case CommPortIdentifier.PORT_PARALLEL:
                return "Parallel";
            case CommPortIdentifier.PORT_RAW:
                return "Raw";
            case CommPortIdentifier.PORT_RS485:
                return "RS485";
            case CommPortIdentifier.PORT_SERIAL:
                return "Serial";
            default:
                return "unknown type";
        }
    }
}