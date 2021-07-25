/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalproject.ui;

import java.awt.CardLayout;
import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import worker.ui.DashBoardDaftar;
import worker.ui.DashboardData;
import worker.ui.WorkerList;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Faisal
 */
public class AdminDashboard extends javax.swing.JFrame {

    PreparedStatement pst = null;
    ResultSet rs = null;
    /**
     * Creates new form WorkerDashboard
     */
    CardLayout cardLayout;

    public AdminDashboard() {
        initComponents();
        fetchData();
        getLabelSeniorWebDev();
        getLabelJuniorWebDev();
        getLabelDesainGrafis();
        getLabelFullstack();
        getAllWorker();

        setLocationRelativeTo(null);
        updateCombo();
        getLabelUI();
        fetchDataProfesi();
        fetchDataUser();
        usertypeLb.setText(LoginSession.UserType);
        nicknameLb.setText(LoginSession.Nickname);

        cardLayout = (CardLayout) (pnlCard.getLayout());
    }

    private void fetchData() {
        ArrayList<WorkerList> list = workerList();
        DefaultTableModel model = (DefaultTableModel) workerData.getModel();
        Object[] row = new Object[7];
        for (int i = 0; i < list.size(); i++) {

            row[0] = list.get(i).getId();
            row[1] = list.get(i).getNama();
            row[2] = "https://wa.me/" + list.get(i).getNo_whatsapp();
            row[3] = list.get(i).getAlamat();
            row[4] = list.get(i).getEmail();
            row[5] = list.get(i).getNama_kategori();
            row[6] = list.get(i).isIsAcc() == true ? "Diterima" : "Ditolak";
            model.addRow(row);
        }
    }

    private void fetchDataProfesi() {
        ArrayList<ProfesiList> list = profesiLists();
        DefaultTableModel model = (DefaultTableModel) dataProfesi.getModel();
        Object[] row = new Object[3];
        for (int i = 0; i < list.size(); i++) {

            row[0] = list.get(i).getId();
            row[1] = list.get(i).getNama_profesi();
            model.addRow(row);
        }
    }

    public ArrayList<ProfesiList> profesiLists() {
        ArrayList<ProfesiList> profesiList = new ArrayList<>();
        try {
            Connection con = MySqlConnection.getConnection();
            String sql = "SELECT * FROM kategori";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            ProfesiList profesi;
            while (rs.next()) {
                profesi = new ProfesiList(rs.getInt("id_kategori"), rs.getString("nama_kategori"));
                profesiList.add(profesi);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());

        }
        return profesiList;
    }

    public ArrayList<WorkerList> workerList() {
        ArrayList<WorkerList> workerList = new ArrayList<>();
        try {//SELECT REPLACE(worker.isAcc,'1','Diterima') from worker
            Connection con = MySqlConnection.getConnection();
            String sql = "SELECT id_worker, nama,no_whatsapp,alamat,email,nama_kategori,isAcc FROM `worker` INNER JOIN kategori WHERE worker.id_kategori = kategori.id_kategori";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            WorkerList worker;
            while (rs.next()) {
                worker = new WorkerList(rs.getInt("id_worker"), rs.getString("nama"), rs.getString("no_whatsapp"), rs.getString("alamat"), rs.getString("email"), rs.getString("nama_kategori"), rs.getBoolean("isAcc"));
                workerList.add(worker);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
        return workerList;
    }

    private void fetchDataUser() {
        ArrayList<UserList> list = userList();
        DefaultTableModel model = (DefaultTableModel) userTabel.getModel();
        Object[] row = new Object[6];
        for (int i = 0; i < list.size(); i++) {

            row[0] = list.get(i).getUID();
            row[1] = list.get(i).getUsername();
            row[2] = list.get(i).getUserType();
            row[3] = list.get(i).getNickname();
            row[4] = list.get(i).getEmail();
            model.addRow(row);
        }
    }

    public ArrayList<UserList> userList() {
        ArrayList<UserList> userList = new ArrayList<>();
        try {//SELECT REPLACE(worker.isAcc,'1','Diterima') from worker
            Connection con = MySqlConnection.getConnection();
            String Admin = "Admin";
            String sql = "SELECT UID, Username,Usertype,Nickname,email FROM `login` WHERE Usertype =  ?";
            pst = con.prepareStatement(sql);
            pst.setString(1, Admin);
            rs = pst.executeQuery();
            UserList user;
            while (rs.next()) {
                user = new UserList(rs.getInt("UID"), rs.getString("Username"), rs.getString("Usertype"), rs.getString("Nickname"), rs.getString("email"));
                userList.add(user);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
        return userList;
    }

    public void getLabelSeniorWebDev() {
        try {
            Connection con = MySqlConnection.getConnection();
            String query = "SELECT COUNT(worker.id_kategori) as id_kategori FROM worker INNER JOIN kategori ON worker.id_kategori = kategori.id_kategori WHERE worker.id_kategori = 1";
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();
            while (rs.next()) {
                seniorWebLb.setText(rs.getString("id_kategori"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, e.getMessage());
        }
    }

    //select data combo box melalui database
    private void updateCombo() {
        String sql = "Select * from kategori";
        try {
            Connection con = MySqlConnection.getConnection();
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
//                comboBoxProfesi.addItem(rs.getString("id_kategori"));
                comboBoxProfesi.addItem(rs.getString("id_kategori") + ") " + rs.getString("nama_kategori"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error" + e.getMessage());
        }
    }

    public void getLabelJuniorWebDev() {
        try {
            Connection con = MySqlConnection.getConnection();
            String query = "SELECT COUNT(worker.id_kategori) as id_kategori FROM worker INNER JOIN kategori ON worker.id_kategori = kategori.id_kategori WHERE worker.id_kategori = 2";
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();
            while (rs.next()) {
                juniorWebLb.setText(rs.getString("id_kategori"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, e.getMessage());
        }
    }

    public void getLabelDesainGrafis() {
        try {
            Connection con = MySqlConnection.getConnection();
            String query = "SELECT COUNT(worker.id_kategori) as id_kategori FROM worker INNER JOIN kategori ON worker.id_kategori = kategori.id_kategori WHERE worker.id_kategori = 3";
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();
            while (rs.next()) {
                desainGrafisLb.setText(rs.getString("id_kategori"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, e.getMessage());
        }
    }

    public void getLabelFullstack() {
        try {
            Connection con = MySqlConnection.getConnection();
            String query = "SELECT COUNT(worker.id_kategori) as id_kategori FROM worker INNER JOIN kategori ON worker.id_kategori = kategori.id_kategori WHERE worker.id_kategori = 4";
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();
            while (rs.next()) {
                fullstackLb.setText(rs.getString("id_kategori"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, e.getMessage());
        }
    }

    public void getLabelUI() {
        try {
            Connection con = MySqlConnection.getConnection();
            String query = "SELECT COUNT(worker.id_kategori) as id_kategori FROM worker INNER JOIN kategori ON worker.id_kategori = kategori.id_kategori WHERE worker.id_kategori = 5";
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();
            while (rs.next()) {
                uiuxLb.setText(rs.getString("id_kategori"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, e.getMessage());
        }
    }

    public void isTerima() {
        try {
            Connection con = MySqlConnection.getConnection();
            int row = workerData.getSelectedRow();
            String value = (workerData.getModel().getValueAt(row, 0).toString());
            String sql = "UPDATE worker SET isAcc = 1 WHERE id_worker=" + value;
            pst = con.prepareStatement(sql);
            pst.executeUpdate();
            DefaultTableModel model = (DefaultTableModel) workerData.getModel();
            model.setRowCount(0);
            fetchData();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, "Pilih row dulu!");
        }
    }

    public void isTolak() {
        try {
            Connection con = MySqlConnection.getConnection();
            int row = workerData.getSelectedRow();
            String value = (workerData.getModel().getValueAt(row, 0).toString());
            String sql = "UPDATE worker SET isAcc = 0 WHERE id_worker=" + value;
            pst = con.prepareStatement(sql);
            pst.executeUpdate();
            DefaultTableModel model = (DefaultTableModel) workerData.getModel();
            model.setRowCount(0);
            fetchData();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, "Pilih row dulu !");
        }
    }

    public void getAllWorker() {
        try {
            Connection con = MySqlConnection.getConnection();
            String query = "SELECT COUNT(worker.id_kategori) as id_kategori FROM worker INNER JOIN kategori ON worker.id_kategori = kategori.id_kategori ";
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();
            while (rs.next()) {
                totalLb.setText(rs.getString("id_kategori"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, e.getMessage());
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

        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        emailBtn = new javax.swing.JButton();
        pnlCard = new javax.swing.JPanel();
        pnlCard1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        nicknameLb = new javax.swing.JLabel();
        usertypeLb = new javax.swing.JLabel();
        logout = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        desainGrafisLb = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        totalLb = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        juniorWebLb = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        seniorWebLb = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        fullstackLb = new javax.swing.JLabel();
        refreshLbBtn = new javax.swing.JButton();
        labelTgl = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        uiuxLb = new javax.swing.JLabel();
        pnlCard2 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        namaTxt = new javax.swing.JTextField();
        alamatTxt = new javax.swing.JTextField();
        noWhatsappTxt = new javax.swing.JTextField();
        emailTxt = new javax.swing.JTextField();
        comboBoxProfesi = new javax.swing.JComboBox<>();
        daftarBtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        workerData = new javax.swing.JTable();
        refreshBtn = new javax.swing.JButton();
        editBtn = new javax.swing.JButton();
        HapusBtn = new javax.swing.JButton();
        resetBtn = new javax.swing.JButton();
        tolakBtn = new javax.swing.JButton();
        terimaBtn = new javax.swing.JButton();
        cariTxt = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        pnlCard3 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        dataProfesi = new javax.swing.JTable();
        jLabel19 = new javax.swing.JLabel();
        profesiTxt = new javax.swing.JTextField();
        editProfesiBtn = new javax.swing.JButton();
        deleteProfesiBtn = new javax.swing.JButton();
        tambahProfesiBtn = new javax.swing.JButton();
        resetBtnProfesi = new javax.swing.JButton();
        pnlCard4 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        usernameTxt = new javax.swing.JTextField();
        nicknameTxt = new javax.swing.JTextField();
        emailTxt1 = new javax.swing.JTextField();
        passwordtxt = new javax.swing.JPasswordField();
        daftarBtn1 = new javax.swing.JButton();
        comboBoxUserType = new javax.swing.JComboBox<>();
        jScrollPane3 = new javax.swing.JScrollPane();
        userTabel = new javax.swing.JTable();
        deleteUserBtn = new javax.swing.JButton();
        resetAdminBtn = new javax.swing.JButton();
        pnlCard5 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        txtToEmail = new javax.swing.JTextField();
        txtToFrom = new javax.swing.JTextField();
        txtSubject = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        emailKirimBtn = new javax.swing.JButton();
        resetEmailBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jSplitPane1.setLeftComponent(jPanel1);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jButton1.setBackground(new java.awt.Color(204, 204, 255));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/dashboard.png"))); // NOI18N
        jButton1.setText("Dashboard");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(255, 51, 255));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/pekerja.png"))); // NOI18N
        jButton2.setText("Daftar Pekerja");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel11.setText("Selamat Datang Admin");

        jButton3.setBackground(new java.awt.Color(0, 255, 0));
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/profesi.png"))); // NOI18N
        jButton3.setText("Profesi");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(0, 102, 153));
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/registeradmin.png"))); // NOI18N
        jButton4.setText("Register Admin");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        emailBtn.setBackground(new java.awt.Color(0, 255, 255));
        emailBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/mail-send.png"))); // NOI18N
        emailBtn.setText("Kirim Email");
        emailBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(emailBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11)
                .addGap(97, 97, 97)
                .addComponent(jButton1)
                .addGap(30, 30, 30)
                .addComponent(jButton2)
                .addGap(31, 31, 31)
                .addComponent(jButton3)
                .addGap(28, 28, 28)
                .addComponent(jButton4)
                .addGap(32, 32, 32)
                .addComponent(emailBtn)
                .addContainerGap(151, Short.MAX_VALUE))
        );

        jSplitPane1.setLeftComponent(jPanel5);

        pnlCard.setLayout(new java.awt.CardLayout());

        pnlCard1.setBackground(new java.awt.Color(204, 255, 255));

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setText("Nickname :");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setText("UserType :");

        nicknameLb.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        usertypeLb.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        logout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/exit.png"))); // NOI18N
        logout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                logoutMouseClicked(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Jumlah pendaftar Junior web developer");

        desainGrafisLb.setFont(new java.awt.Font("Swis721 Cn BT", 1, 36)); // NOI18N
        desainGrafisLb.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        desainGrafisLb.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setText("Jumlah pendaftar Fullstack developer");

        totalLb.setFont(new java.awt.Font("Swis721 Cn BT", 1, 36)); // NOI18N
        totalLb.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        totalLb.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setText("Jumlah pendaftar Desain Grafis");

        juniorWebLb.setFont(new java.awt.Font("Swis721 Cn BT", 1, 36)); // NOI18N
        juniorWebLb.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        juniorWebLb.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setText("Jumlah pendaftar senior web developer");

        seniorWebLb.setFont(new java.awt.Font("Swis721 Cn BT", 1, 36)); // NOI18N
        seniorWebLb.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        seniorWebLb.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel12.setText("Total pekerja yang mendaftar");

        fullstackLb.setFont(new java.awt.Font("Swis721 Cn BT", 1, 36)); // NOI18N
        fullstackLb.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        fullstackLb.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));

        refreshLbBtn.setBackground(new java.awt.Color(0, 255, 0));
        refreshLbBtn.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        refreshLbBtn.setText("Refresh");
        refreshLbBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshLbBtnActionPerformed(evt);
            }
        });

        labelTgl.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel20.setText("Jumlah pendaftar UI UX");

        uiuxLb.setFont(new java.awt.Font("Swis721 Cn BT", 1, 36)); // NOI18N
        uiuxLb.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        uiuxLb.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));

        javax.swing.GroupLayout pnlCard1Layout = new javax.swing.GroupLayout(pnlCard1);
        pnlCard1.setLayout(pnlCard1Layout);
        pnlCard1Layout.setHorizontalGroup(
            pnlCard1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCard1Layout.createSequentialGroup()
                .addGroup(pnlCard1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlCard1Layout.createSequentialGroup()
                        .addGap(700, 700, 700)
                        .addGroup(pnlCard1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(38, 38, 38)
                        .addGroup(pnlCard1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlCard1Layout.createSequentialGroup()
                                .addComponent(usertypeLb, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 163, Short.MAX_VALUE))
                            .addGroup(pnlCard1Layout.createSequentialGroup()
                                .addComponent(nicknameLb, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(123, 123, 123)))
                        .addComponent(logout, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlCard1Layout.createSequentialGroup()
                        .addGap(68, 68, 68)
                        .addComponent(jLabel5)
                        .addGroup(pnlCard1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlCard1Layout.createSequentialGroup()
                                .addGap(81, 81, 81)
                                .addComponent(jLabel1))
                            .addGroup(pnlCard1Layout.createSequentialGroup()
                                .addGap(117, 117, 117)
                                .addComponent(jLabel12)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(pnlCard1Layout.createSequentialGroup()
                .addGap(137, 137, 137)
                .addComponent(fullstackLb, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(totalLb, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(206, 206, 206)
                .addComponent(uiuxLb, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(141, 141, 141))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCard1Layout.createSequentialGroup()
                .addGap(134, 134, 134)
                .addComponent(seniorWebLb, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlCard1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlCard1Layout.createSequentialGroup()
                        .addComponent(refreshLbBtn)
                        .addGap(178, 178, 178)
                        .addGroup(pnlCard1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelTgl, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pnlCard1Layout.createSequentialGroup()
                                .addGap(31, 31, 31)
                                .addComponent(jLabel20))
                            .addComponent(jLabel7))
                        .addGap(97, 97, 97))
                    .addGroup(pnlCard1Layout.createSequentialGroup()
                        .addComponent(juniorWebLb, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(203, 203, 203)
                        .addComponent(desainGrafisLb, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(142, 142, 142))))
            .addGroup(pnlCard1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlCard1Layout.createSequentialGroup()
                    .addGap(55, 55, 55)
                    .addComponent(jLabel9)
                    .addContainerGap(751, Short.MAX_VALUE)))
        );
        pnlCard1Layout.setVerticalGroup(
            pnlCard1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCard1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlCard1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlCard1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(refreshLbBtn))
                    .addGroup(pnlCard1Layout.createSequentialGroup()
                        .addGroup(pnlCard1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(logout)
                            .addGroup(pnlCard1Layout.createSequentialGroup()
                                .addGroup(pnlCard1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(nicknameLb, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnlCard1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(usertypeLb, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGap(86, 86, 86)
                        .addGroup(pnlCard1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(pnlCard1Layout.createSequentialGroup()
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(13, 13, 13))
                            .addGroup(pnlCard1Layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, 18)))
                        .addGroup(pnlCard1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(juniorWebLb, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(desainGrafisLb, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(seniorWebLb, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(pnlCard1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlCard1Layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(uiuxLb, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCard1Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(pnlCard1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnlCard1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(totalLb, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(fullstackLb, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                        .addComponent(labelTgl, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(26, 26, 26))
            .addGroup(pnlCard1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlCard1Layout.createSequentialGroup()
                    .addGap(137, 137, 137)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(441, Short.MAX_VALUE)))
        );

        pnlCard.add(pnlCard1, "pnlCard1");

        jPanel2.setBackground(new java.awt.Color(204, 255, 204));

        jLabel4.setFont(new java.awt.Font("Swis721 BT", 0, 18)); // NOI18N
        jLabel4.setText("Pendaftaran Pekerja");

        jLabel6.setText("Nama : ");

        jLabel8.setText("Alamat : ");

        jLabel10.setText("No Whatsapp : ");

        jLabel13.setText("Email : ");

        jLabel15.setText("Profesi : ");

        emailTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailTxtActionPerformed(evt);
            }
        });

        comboBoxProfesi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxProfesiActionPerformed(evt);
            }
        });

        daftarBtn.setBackground(new java.awt.Color(204, 255, 204));
        daftarBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/user(1).png"))); // NOI18N
        daftarBtn.setText("Daftar");
        daftarBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                daftarBtnActionPerformed(evt);
            }
        });

        jScrollPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jScrollPane1MouseClicked(evt);
            }
        });

        workerData.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No", "Nama", "Whatsapp", "Alamat", "Email", "Profesi", "isAcc"
            }
        ));
        workerData.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                workerDataMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(workerData);

        refreshBtn.setBackground(new java.awt.Color(204, 204, 255));
        refreshBtn.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        refreshBtn.setText("Refresh");
        refreshBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshBtnActionPerformed(evt);
            }
        });

        editBtn.setBackground(new java.awt.Color(102, 102, 255));
        editBtn.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        editBtn.setText("Edit");
        editBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editBtnActionPerformed(evt);
            }
        });

        HapusBtn.setBackground(new java.awt.Color(255, 51, 51));
        HapusBtn.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        HapusBtn.setText("Hapus");
        HapusBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HapusBtnActionPerformed(evt);
            }
        });

        resetBtn.setText("Reset");
        resetBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetBtnActionPerformed(evt);
            }
        });

        tolakBtn.setBackground(new java.awt.Color(255, 102, 102));
        tolakBtn.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        tolakBtn.setText("Tolak");
        tolakBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tolakBtnActionPerformed(evt);
            }
        });

        terimaBtn.setBackground(new java.awt.Color(51, 255, 255));
        terimaBtn.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        terimaBtn.setText("Terima");
        terimaBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                terimaBtnActionPerformed(evt);
            }
        });

        cariTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cariTxtActionPerformed(evt);
            }
        });
        cariTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cariTxtKeyReleased(evt);
            }
        });

        jLabel18.setText("Cari :");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(422, 422, 422))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jLabel8)
                    .addComponent(jLabel10)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                        .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(49, 49, 49)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(resetBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(daftarBtn))
                    .addComponent(emailTxt, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE)
                    .addComponent(noWhatsappTxt, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE)
                    .addComponent(alamatTxt, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE)
                    .addComponent(namaTxt, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(comboBoxProfesi, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(terimaBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(37, 37, 37)
                                .addComponent(tolakBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(refreshBtn)
                                .addGap(47, 47, 47)
                                .addComponent(editBtn)
                                .addGap(18, 18, 18)
                                .addComponent(HapusBtn)
                                .addGap(54, 54, 54))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cariTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(475, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 671, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(78, 78, 78)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(namaTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(13, 13, 13))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cariTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18))))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel8)
                                    .addComponent(alamatTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel10)
                                    .addComponent(noWhatsappTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel13)
                                    .addComponent(emailTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel15)
                                    .addComponent(comboBoxProfesi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(resetBtn)
                                    .addComponent(daftarBtn)))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(44, 44, 44)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(terimaBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tolakBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(160, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(refreshBtn)
                            .addComponent(editBtn)
                            .addComponent(HapusBtn))
                        .addGap(191, 191, 191))))
        );

        javax.swing.GroupLayout pnlCard2Layout = new javax.swing.GroupLayout(pnlCard2);
        pnlCard2.setLayout(pnlCard2Layout);
        pnlCard2Layout.setHorizontalGroup(
            pnlCard2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlCard2Layout.setVerticalGroup(
            pnlCard2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pnlCard.add(pnlCard2, "pnlCard2");

        pnlCard3.setBackground(new java.awt.Color(204, 204, 255));

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel14.setText("Tambah Profesi");

        dataProfesi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "id_profesi", "Nama Profesi"
            }
        ));
        dataProfesi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dataProfesiMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(dataProfesi);

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel19.setText("Nama Profesi : ");

        editProfesiBtn.setBackground(new java.awt.Color(51, 102, 255));
        editProfesiBtn.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        editProfesiBtn.setText("Edit");
        editProfesiBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editProfesiBtnActionPerformed(evt);
            }
        });

        deleteProfesiBtn.setBackground(new java.awt.Color(255, 51, 51));
        deleteProfesiBtn.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        deleteProfesiBtn.setText("Hapus");
        deleteProfesiBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteProfesiBtnActionPerformed(evt);
            }
        });

        tambahProfesiBtn.setBackground(new java.awt.Color(102, 255, 102));
        tambahProfesiBtn.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tambahProfesiBtn.setText("Tambah");
        tambahProfesiBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tambahProfesiBtnActionPerformed(evt);
            }
        });

        resetBtnProfesi.setText("Reset");
        resetBtnProfesi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetBtnProfesiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlCard3Layout = new javax.swing.GroupLayout(pnlCard3);
        pnlCard3.setLayout(pnlCard3Layout);
        pnlCard3Layout.setHorizontalGroup(
            pnlCard3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCard3Layout.createSequentialGroup()
                .addGroup(pnlCard3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlCard3Layout.createSequentialGroup()
                        .addGap(449, 449, 449)
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlCard3Layout.createSequentialGroup()
                        .addGap(237, 237, 237)
                        .addGroup(pnlCard3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(pnlCard3Layout.createSequentialGroup()
                                .addComponent(tambahProfesiBtn)
                                .addGap(18, 18, 18)
                                .addComponent(editProfesiBtn)
                                .addGap(13, 13, 13)
                                .addComponent(deleteProfesiBtn))
                            .addGroup(pnlCard3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 632, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(pnlCard3Layout.createSequentialGroup()
                                    .addComponent(jLabel19)
                                    .addGap(18, 18, 18)
                                    .addComponent(profesiTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(26, 26, 26)
                                    .addComponent(resetBtnProfesi))))))
                .addContainerGap(223, Short.MAX_VALUE))
        );
        pnlCard3Layout.setVerticalGroup(
            pnlCard3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCard3Layout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(66, 66, 66)
                .addGroup(pnlCard3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(profesiTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(resetBtnProfesi))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnlCard3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(editProfesiBtn)
                    .addComponent(deleteProfesiBtn)
                    .addComponent(tambahProfesiBtn))
                .addContainerGap(131, Short.MAX_VALUE))
        );

        pnlCard.add(pnlCard3, "pnlCard3");

        jPanel3.setBackground(new java.awt.Color(255, 204, 204));

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel16.setText("Register Admin");

        jLabel21.setText("Username : ");

        jLabel22.setText("Password : ");

        jLabel23.setText("Usertype : ");

        jLabel24.setText("Nickname : ");

        jLabel25.setText("Email : ");

        usernameTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                usernameTxtActionPerformed(evt);
            }
        });

        nicknameTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nicknameTxtActionPerformed(evt);
            }
        });

        daftarBtn1.setBackground(new java.awt.Color(51, 255, 102));
        daftarBtn1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        daftarBtn1.setText("Daftar");
        daftarBtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                daftarBtn1ActionPerformed(evt);
            }
        });

        comboBoxUserType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Admin" }));

        userTabel.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "User ID", "Username", "UserType", "Nickname", "Email"
            }
        ));
        userTabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                userTabelMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(userTabel);

        deleteUserBtn.setBackground(new java.awt.Color(255, 0, 51));
        deleteUserBtn.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        deleteUserBtn.setText("Delete");
        deleteUserBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteUserBtnActionPerformed(evt);
            }
        });

        resetAdminBtn.setBackground(new java.awt.Color(255, 0, 255));
        resetAdminBtn.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        resetAdminBtn.setText("Reset");
        resetAdminBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetAdminBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(446, 446, 446))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(64, 64, 64)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(resetAdminBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(daftarBtn1))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(usernameTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(emailTxt1, javax.swing.GroupLayout.DEFAULT_SIZE, 264, Short.MAX_VALUE)
                            .addComponent(nicknameTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 264, Short.MAX_VALUE)
                            .addComponent(passwordtxt)
                            .addComponent(comboBoxUserType, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 89, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 508, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(deleteUserBtn)
                        .addGap(29, 29, 29)))
                .addGap(38, 38, 38))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(78, 78, 78)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel21)
                            .addComponent(usernameTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(32, 32, 32)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel22)
                            .addComponent(passwordtxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(30, 30, 30)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel23)
                                    .addComponent(comboBoxUserType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(32, 32, 32)
                                .addComponent(jLabel24))
                            .addComponent(nicknameTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(34, 34, 34)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel25)
                            .addComponent(emailTxt1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(101, 101, 101)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(daftarBtn1)
                            .addComponent(resetAdminBtn)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(deleteUserBtn)))
                .addContainerGap(187, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlCard4Layout = new javax.swing.GroupLayout(pnlCard4);
        pnlCard4.setLayout(pnlCard4Layout);
        pnlCard4Layout.setHorizontalGroup(
            pnlCard4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlCard4Layout.setVerticalGroup(
            pnlCard4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pnlCard.add(pnlCard4, "pnlCard4");

        pnlCard5.setBackground(new java.awt.Color(255, 255, 204));

        jLabel17.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel17.setText("Kirim Email Kepada Pekerja ");

        jLabel26.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel26.setText("Message :");

        jLabel27.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel27.setText("To : ");

        jLabel28.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel28.setText("From : ");

        jLabel29.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel29.setText("Subject :");

        txtToEmail.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtToEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtToEmailActionPerformed(evt);
            }
        });

        txtToFrom.setEditable(false);
        txtToFrom.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtToFrom.setText("sixeyes991@gmail.com");

        txtSubject.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtSubject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSubjectActionPerformed(evt);
            }
        });

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane4.setViewportView(jTextArea1);

        emailKirimBtn.setBackground(new java.awt.Color(51, 102, 255));
        emailKirimBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/mail-send.png"))); // NOI18N
        emailKirimBtn.setText("Kirim Email");
        emailKirimBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailKirimBtnActionPerformed(evt);
            }
        });

        resetEmailBtn.setBackground(new java.awt.Color(255, 102, 102));
        resetEmailBtn.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        resetEmailBtn.setText("Reset ");
        resetEmailBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetEmailBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlCard5Layout = new javax.swing.GroupLayout(pnlCard5);
        pnlCard5.setLayout(pnlCard5Layout);
        pnlCard5Layout.setHorizontalGroup(
            pnlCard5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCard5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(401, 401, 401))
            .addGroup(pnlCard5Layout.createSequentialGroup()
                .addGap(86, 86, 86)
                .addComponent(resetEmailBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addGroup(pnlCard5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlCard5Layout.createSequentialGroup()
                        .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(59, 59, 59)
                        .addComponent(txtToFrom, javax.swing.GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE))
                    .addGroup(pnlCard5Layout.createSequentialGroup()
                        .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(59, 59, 59)
                        .addComponent(txtToEmail))
                    .addGroup(pnlCard5Layout.createSequentialGroup()
                        .addGroup(pnlCard5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(59, 59, 59)
                        .addGroup(pnlCard5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtSubject, javax.swing.GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE)
                            .addComponent(jScrollPane4))))
                .addGap(98, 98, 98)
                .addComponent(emailKirimBtn)
                .addGap(45, 45, 45))
        );
        pnlCard5Layout.setVerticalGroup(
            pnlCard5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCard5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(89, 89, 89)
                .addGroup(pnlCard5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtToEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlCard5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtToFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlCard5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSubject, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(pnlCard5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlCard5Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(pnlCard5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pnlCard5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(resetEmailBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(39, Short.MAX_VALUE))
                    .addGroup(pnlCard5Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(emailKirimBtn)
                        .addGap(30, 30, 30))))
        );

        pnlCard.add(pnlCard5, "pnlCard5");

        jSplitPane1.setRightComponent(pnlCard);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 603, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        cardLayout.show(pnlCard, "pnlCard1");
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        cardLayout.show(pnlCard, "pnlCard2");
    }//GEN-LAST:event_jButton2ActionPerformed

    private void logoutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutMouseClicked
        // TODO add your handling code here:
        Login login = new Login();
        Logout.logOut(this, login);
    }//GEN-LAST:event_logoutMouseClicked

    private void refreshLbBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshLbBtnActionPerformed
        // TODO add your handling code here:
        AdminDashboard admin = new AdminDashboard();
        admin.setVisible(true);
        dispose();
    }//GEN-LAST:event_refreshLbBtnActionPerformed

    private void emailTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_emailTxtActionPerformed

    private void comboBoxProfesiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxProfesiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboBoxProfesiActionPerformed

    private void daftarBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_daftarBtnActionPerformed
        // TODO add your handling code here:

        //ini buat combo box, misal data kalian gak make combo box, gak usah bikin code ini
        String professionItem = comboBoxProfesi.getSelectedItem().toString();
        String profession = professionItem.split("\\)", 2)[0];

        //bagian masukkan data ke dalam database ada di action BUTTON DAFTAR
        try {
            Connection con = MySqlConnection.getConnection();
            //query insert data ke dalam database mysql
            pst = con.prepareStatement("INSERT INTO worker (nama, no_whatsapp, alamat, email, id_kategori) VALUES(?,?,?,?,?)");
            //validasi ketika textField kosong

            if (namaTxt.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nama tidak boleh kosong");
            } else if (noWhatsappTxt.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "No Whatsapp tidak boleh kosong");
            } else if (alamatTxt.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Alamat tidak boleh kosong");
            } else if (emailTxt.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Email tidak boleh kosong");
            } else {
                //digunakan untuk memasukkan data ke masing2 variabel textfield seperti namaTxt, dll

                //misal bingung pst sama rs bisa diliat di variabel diatas
                pst.setString(1, namaTxt.getText());
                pst.setString(2, noWhatsappTxt.getText());
                pst.setString(3, alamatTxt.getText());
                pst.setString(4, emailTxt.getText());
                pst.setString(5, profession);
                pst.executeUpdate();

                DefaultTableModel model = (DefaultTableModel) workerData.getModel();
                model.setRowCount(0);
                fetchData();
                //setelah nginput data kasih ini biar textfieldnya kosong lagi
                namaTxt.setText("");
                noWhatsappTxt.setText("");
                alamatTxt.setText("");
                emailTxt.setText("");
                comboBoxProfesi.setSelectedItem("");

                //setelah daftar muncul pop up dafatar berhasil dan akan tampil frame baru
                JOptionPane.showMessageDialog(null, "Berhasil Menambah Data");

            }

        } catch (Exception e) {
            //            JOptionPane.showMessageDialog(null, "Error " + e.getMessage());
            e.printStackTrace();
        }
    }//GEN-LAST:event_daftarBtnActionPerformed

    private void workerDataMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_workerDataMouseClicked
        // TODO add your handling code here:

        //action ini didapat dari tabel caranya klik kanan tabel terus cari ke mouse click isi code ini,
        //code ini berfungsi ngambil value di dalem tabel habisi itu dimasukin lagi ke textfield biar bisa di edit
        int i = workerData.getSelectedRow();
        TableModel model = workerData.getModel();
        namaTxt.setText(model.getValueAt(i, 1).toString());
        alamatTxt.setText(model.getValueAt(i, 3).toString());
        noWhatsappTxt.setText(model.getValueAt(i, 2).toString());
        emailTxt.setText(model.getValueAt(i, 4).toString());
        String comboSelect = model.getValueAt(i, 5).toString();
        switch (comboSelect) {
            case "Senior Web Developer":
                comboBoxProfesi.setSelectedIndex(0);
                break;
            case "Junior Web Developer":
                comboBoxProfesi.setSelectedIndex(1);
                break;
            case "Desain Grafis":
                comboBoxProfesi.setSelectedIndex(2);
                break;
            case "Fullstack Developer":
                comboBoxProfesi.setSelectedIndex(3);
                break;
        }
    }//GEN-LAST:event_workerDataMouseClicked

    private void jScrollPane1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane1MouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_jScrollPane1MouseClicked

    private void refreshBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshBtnActionPerformed
        // TODO add your handling code here:

        //untuk me refresh tabel
        DefaultTableModel model = (DefaultTableModel) workerData.getModel();
        model.setRowCount(0);
        fetchData();
    }//GEN-LAST:event_refreshBtnActionPerformed

    private void editBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editBtnActionPerformed
        // TODO add your handling code here:
        String professionItem = comboBoxProfesi.getSelectedItem().toString();
        String profession = professionItem.split("\\)", 2)[0];
        String data = "";

        //action button untuk mengedit data
        try {

            Connection con = MySqlConnection.getConnection();
            int row = workerData.getSelectedRow();
            String value = (workerData.getModel().getValueAt(row, 0).toString());
            String sql = "UPDATE worker SET nama=?,no_whatsapp=?,alamat=?,email=?,id_kategori=? WHERE id_worker=" + value;
            if (namaTxt.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nama tidak boleh kosong");
            } else if (noWhatsappTxt.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "No Whatsapp tidak boleh kosong");
            } else if (alamatTxt.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Alamat tidak boleh kosong");
            } else if (emailTxt.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Email tidak boleh kosong");
            } else {
                pst = con.prepareStatement(sql);
                pst.setString(1, namaTxt.getText());
                pst.setString(2, noWhatsappTxt.getText());
                pst.setString(3, alamatTxt.getText());
                pst.setString(4, emailTxt.getText());
                pst.setString(5, profession);
                pst.executeUpdate();

                DefaultTableModel model = (DefaultTableModel) workerData.getModel();
                model.setRowCount(0);
                fetchData();
                namaTxt.setText("");
                noWhatsappTxt.setText("");
                alamatTxt.setText("");
                emailTxt.setText("");
                comboBoxProfesi.setSelectedItem("");
                JOptionPane.showMessageDialog(null, "Update Selesai");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, e.getMessage());
        }
    }//GEN-LAST:event_editBtnActionPerformed

    private void HapusBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HapusBtnActionPerformed
        // TODO add your handling code here:
        try {
            int pilihan = JOptionPane.showConfirmDialog(this, " Apakah anda yakin akan menghapus data ini ?", "Konfirmasi", JOptionPane.WARNING_MESSAGE);
            if (pilihan == JOptionPane.YES_OPTION) {
                Connection con = MySqlConnection.getConnection();
                int row = workerData.getSelectedRow();
                String value = (workerData.getModel().getValueAt(row, 0).toString());
                String sql = "DELETE FROM worker WHERE id_worker=" + value;
                pst = con.prepareStatement(sql);
                pst.executeUpdate();
                namaTxt.setText("");
                noWhatsappTxt.setText("");
                alamatTxt.setText("");
                emailTxt.setText("");
                comboBoxProfesi.setSelectedItem("");
                DefaultTableModel model = (DefaultTableModel) workerData.getModel();
                model.setRowCount(0);
                fetchData();
                JOptionPane.showMessageDialog(rootPane, "Data Dihapus");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, "Pilih Baris Dulu");
        }

    }//GEN-LAST:event_HapusBtnActionPerformed

    private void resetBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetBtnActionPerformed
        // TODO add your handling code here:
        namaTxt.setText("");
        noWhatsappTxt.setText("");
        alamatTxt.setText("");
        emailTxt.setText("");
        comboBoxProfesi.setSelectedItem("");
    }//GEN-LAST:event_resetBtnActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        cardLayout.show(pnlCard, "pnlCard3");
    }//GEN-LAST:event_jButton3ActionPerformed

    private void editProfesiBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editProfesiBtnActionPerformed
        // TODO add your handling code here:
        try {

            Connection con = MySqlConnection.getConnection();
            int row = dataProfesi.getSelectedRow();
            String value = (dataProfesi.getModel().getValueAt(row, 0).toString());
            String sql = "UPDATE kategori SET nama_kategori=? WHERE id_kategori=" + value;
            if (profesiTxt.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nama profesi tidak boleh kosong");
            } else {
                pst = con.prepareStatement(sql);
                pst.setString(1, profesiTxt.getText());

                pst.executeUpdate();

                DefaultTableModel model = (DefaultTableModel) dataProfesi.getModel();
                model.setRowCount(0);
                fetchDataProfesi();
                profesiTxt.setText("");

                JOptionPane.showMessageDialog(null, "Edit Selesai");
                dispose();
                AdminDashboard admin = new AdminDashboard();
                admin.setVisible(true);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, e.getMessage());
        }

    }//GEN-LAST:event_editProfesiBtnActionPerformed

    private void deleteProfesiBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteProfesiBtnActionPerformed
        // TODO add your handling code here:
        try {
            int pilihan = JOptionPane.showConfirmDialog(this, " Apakah anda yakin akan menghapus data ini ?", "Konfirmasi", JOptionPane.WARNING_MESSAGE);
            if (pilihan == JOptionPane.YES_OPTION) {
                Connection con = MySqlConnection.getConnection();
                int row = dataProfesi.getSelectedRow();
                String value = (dataProfesi.getModel().getValueAt(row, 0).toString());
                String sql = "DELETE FROM kategori WHERE id_kategori=" + value;
                pst = con.prepareStatement(sql);
                pst.executeUpdate();
                profesiTxt.setText("");
                DefaultTableModel model = (DefaultTableModel) dataProfesi.getModel();
                model.setRowCount(0);
                fetchDataProfesi();
                dispose();
                AdminDashboard admin = new AdminDashboard();
                admin.setVisible(true);
                JOptionPane.showMessageDialog(rootPane, "Data Profesi Dihapus");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, e);
        }
    }//GEN-LAST:event_deleteProfesiBtnActionPerformed

    private void tambahProfesiBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tambahProfesiBtnActionPerformed
        // TODO add your handling code here:
        try {

            Connection con = MySqlConnection.getConnection();
            //query insert data ke dalam database mysql
            pst = con.prepareStatement("INSERT INTO kategori (nama_kategori) VALUES(?)");
            //validasi ketika textField kosong

            if (profesiTxt.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nama profesi tidak boleh kosong");
            } else {
                //digunakan untuk memasukkan data ke masing2 variabel textfield seperti namaTxt, dll

                //misal bingung pst sama rs bisa diliat di variabel diatas
                pst.setString(1, profesiTxt.getText());

                pst.executeUpdate();

                //setelah nginput data kasih ini biar textfieldnya kosong lagi
                profesiTxt.setText("");

                //setelah daftar muncul pop up dafatar berhasil dan akan tampil frame baru
                JOptionPane.showMessageDialog(null, "Berhasil menambah data");
                dispose();
                AdminDashboard admin = new AdminDashboard();
                admin.setVisible(true);

            }

        } catch (Exception e) {
            //            JOptionPane.showMessageDialog(null, "Error " + e.getMessage());
            e.printStackTrace();
        }

    }//GEN-LAST:event_tambahProfesiBtnActionPerformed

    private void dataProfesiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dataProfesiMouseClicked
        // TODO add your handling code here:
        int i = dataProfesi.getSelectedRow();
        TableModel model = dataProfesi.getModel();
        profesiTxt.setText(model.getValueAt(i, 1).toString());
    }//GEN-LAST:event_dataProfesiMouseClicked

    private void terimaBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_terimaBtnActionPerformed
        // TODO add your handling code here:
        isTerima();
    }//GEN-LAST:event_terimaBtnActionPerformed

    private void tolakBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tolakBtnActionPerformed
        // TODO add your handling code here:
        isTolak();
    }//GEN-LAST:event_tolakBtnActionPerformed

    private void cariTxtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cariTxtKeyReleased
        // TODO add your handling code here:
        DefaultTableModel table = (DefaultTableModel) workerData.getModel();
        String search = cariTxt.getText().toLowerCase();
        TableRowSorter<DefaultTableModel> tr = new TableRowSorter<DefaultTableModel>(table);
        workerData.setRowSorter(tr);
        tr.setRowFilter(RowFilter.regexFilter(search));
    }//GEN-LAST:event_cariTxtKeyReleased

    private void resetBtnProfesiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetBtnProfesiActionPerformed
        // TODO add your handling code here:
        profesiTxt.setText("");
    }//GEN-LAST:event_resetBtnProfesiActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        cardLayout.show(pnlCard, "pnlCard4");
    }//GEN-LAST:event_jButton4ActionPerformed

    private void usernameTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_usernameTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_usernameTxtActionPerformed

    private void nicknameTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nicknameTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nicknameTxtActionPerformed

    private void daftarBtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_daftarBtn1ActionPerformed
        // TODO add your handling code here:
        String userTypeCombo = comboBoxUserType.getSelectedItem().toString();
        try {
            Connection con = MySqlConnection.getConnection();
            pst = con.prepareStatement("INSERT INTO login (Username, Password, Usertype, Nickname, Email, Date) VALUES (?,?,?,?,?,CURRENT_DATE) ");
            if (usernameTxt.getText().isEmpty()) {
                JOptionPane.showMessageDialog(rootPane, "Username tidak boleh kosong");
            } else if (passwordtxt.getText().isEmpty()) {
                JOptionPane.showMessageDialog(rootPane, "Password tidak boleh kosong");
            } else if (nicknameTxt.getText().isEmpty()) {
                JOptionPane.showMessageDialog(rootPane, "Nickname tidak boleh kosong");
            } else if (emailTxt1.getText().isEmpty()) {
                JOptionPane.showMessageDialog(rootPane, "Email tidak boleh kosong");
            } else {
                pst.setString(1, usernameTxt.getText());
                pst.setString(2, passwordtxt.getText());
                pst.setString(3, userTypeCombo);
                pst.setString(4, nicknameTxt.getText());
                pst.setString(5, emailTxt1.getText());

                pst.executeUpdate();
                usernameTxt.setText("");
                passwordtxt.setText("");
                nicknameTxt.setText("");
                emailTxt1.setText("");
                JOptionPane.showMessageDialog(null, "Daftar Berhasil");
                DefaultTableModel model = (DefaultTableModel) userTabel.getModel();
                model.setRowCount(0);
                fetchDataUser();

            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, e.getMessage());
        }
    }//GEN-LAST:event_daftarBtn1ActionPerformed

    private void userTabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_userTabelMouseClicked
        // TODO add your handling code here


    }//GEN-LAST:event_userTabelMouseClicked

    private void deleteUserBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteUserBtnActionPerformed
        // TODO add your handling code here:

        try {
            int pilihan = JOptionPane.showConfirmDialog(this, " Apakah anda yakin akan menghapus data ini ?", "Konfirmasi", JOptionPane.WARNING_MESSAGE);
            if (pilihan == JOptionPane.YES_OPTION) {
                Connection con = MySqlConnection.getConnection();
                int row = userTabel.getSelectedRow();
                String value = (userTabel.getModel().getValueAt(row, 0).toString());
                String sql = "DELETE FROM login WHERE UID=" + value;
                pst = con.prepareStatement(sql);
                pst.executeUpdate();
                DefaultTableModel model = (DefaultTableModel) userTabel.getModel();
                model.setRowCount(0);
                fetchDataUser();
                JOptionPane.showMessageDialog(rootPane, "Data User Dihapus");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, e.getMessage());
        }
    }//GEN-LAST:event_deleteUserBtnActionPerformed

    private void emailBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailBtnActionPerformed
        // TODO add your handling code here:
        cardLayout.show(pnlCard, "pnlCard5");
    }//GEN-LAST:event_emailBtnActionPerformed

    private void emailKirimBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailKirimBtnActionPerformed
        // TODO add your handling code here:
        String ToEmail = txtToEmail.getText();
        String FromEmail = txtToFrom.getText();//studyviral2@gmail.com
        String FromEmailPassword = "SixEyes()12";//You email Password from you want to send email
        String Subjects = txtSubject.getText();

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FromEmail, FromEmailPassword);
            }
        });
        if (txtToEmail.getText().isEmpty()) {
            JOptionPane.showMessageDialog(rootPane, "Email tidak boleh kosong");
        } else if (txtSubject.getText().isEmpty()) {
            JOptionPane.showMessageDialog(rootPane, "Subjek tidak boleh kosong");
        } else if (jTextArea1.getText().isEmpty()) {
            JOptionPane.showMessageDialog(rootPane, "Pesan tidak boleh kosong");
        } else {

            try {
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(FromEmail));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(ToEmail));
                message.setSubject(Subjects);
                message.setText(jTextArea1.getText());
                Transport.send(message);
                txtToEmail.setText("");
                txtSubject.setText("");
                jTextArea1.setText("");
                JOptionPane.showMessageDialog(rootPane, "Email terkirim");
            } catch (Exception ex) {
                System.out.println("" + ex);
            }
        }

    }//GEN-LAST:event_emailKirimBtnActionPerformed

    private void txtToEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtToEmailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtToEmailActionPerformed

    private void txtSubjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSubjectActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSubjectActionPerformed

    private void resetEmailBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetEmailBtnActionPerformed
        // TODO add your handling code here:
        txtToEmail.setText("");
        txtSubject.setText("");
        jTextArea1.setText("");
    }//GEN-LAST:event_resetEmailBtnActionPerformed

    private void cariTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cariTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cariTxtActionPerformed

    private void resetAdminBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetAdminBtnActionPerformed
        // TODO add your handling code here:
        usernameTxt.setText("");
        passwordtxt.setText("");
        nicknameTxt.setText("");
        emailTxt1.setText("");
    }//GEN-LAST:event_resetAdminBtnActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AdminDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AdminDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AdminDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AdminDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AdminDashboard().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton HapusBtn;
    private javax.swing.JTextField alamatTxt;
    private javax.swing.JTextField cariTxt;
    private javax.swing.JComboBox<String> comboBoxProfesi;
    private javax.swing.JComboBox<String> comboBoxUserType;
    private javax.swing.JButton daftarBtn;
    private javax.swing.JButton daftarBtn1;
    private javax.swing.JTable dataProfesi;
    private javax.swing.JButton deleteProfesiBtn;
    private javax.swing.JButton deleteUserBtn;
    private javax.swing.JLabel desainGrafisLb;
    private javax.swing.JButton editBtn;
    private javax.swing.JButton editProfesiBtn;
    private javax.swing.JButton emailBtn;
    private javax.swing.JButton emailKirimBtn;
    private javax.swing.JTextField emailTxt;
    private javax.swing.JTextField emailTxt1;
    private javax.swing.JLabel fullstackLb;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JLabel juniorWebLb;
    private javax.swing.JLabel labelTgl;
    private javax.swing.JLabel logout;
    private javax.swing.JTextField namaTxt;
    private javax.swing.JLabel nicknameLb;
    private javax.swing.JTextField nicknameTxt;
    private javax.swing.JTextField noWhatsappTxt;
    private javax.swing.JPasswordField passwordtxt;
    private javax.swing.JPanel pnlCard;
    private javax.swing.JPanel pnlCard1;
    private javax.swing.JPanel pnlCard2;
    private javax.swing.JPanel pnlCard3;
    private javax.swing.JPanel pnlCard4;
    private javax.swing.JPanel pnlCard5;
    private javax.swing.JTextField profesiTxt;
    private javax.swing.JButton refreshBtn;
    private javax.swing.JButton refreshLbBtn;
    private javax.swing.JButton resetAdminBtn;
    private javax.swing.JButton resetBtn;
    private javax.swing.JButton resetBtnProfesi;
    private javax.swing.JButton resetEmailBtn;
    private javax.swing.JLabel seniorWebLb;
    private javax.swing.JButton tambahProfesiBtn;
    private javax.swing.JButton terimaBtn;
    private javax.swing.JButton tolakBtn;
    private javax.swing.JLabel totalLb;
    private javax.swing.JTextField txtSubject;
    private javax.swing.JTextField txtToEmail;
    private javax.swing.JTextField txtToFrom;
    private javax.swing.JLabel uiuxLb;
    private javax.swing.JTable userTabel;
    private javax.swing.JTextField usernameTxt;
    private javax.swing.JLabel usertypeLb;
    private javax.swing.JTable workerData;
    // End of variables declaration//GEN-END:variables
}
