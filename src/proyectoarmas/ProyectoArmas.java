/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoarmas;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.DateConverter;
import com.thoughtworks.xstream.io.xml.DomDriver;
import datos.Arma;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import controladordatos.SQLiteUtil;


class Filtro extends FileFilter {

    private String ext;

    public Filtro(String extension) {
        ext = extension;
    }

    @Override
    public boolean accept(File f) {
        return f.isDirectory() || f.getName().toLowerCase().endsWith("." + ext);
    }

    @Override
    public String getDescription() {
        return "Archivos (." + ext + ")";
    }
}

public class ProyectoArmas extends javax.swing.JFrame {

    private JFileChooser archivo;
    private Integer armaId;
    private DefaultTableModel tablaModel;
    private XStream xstream;
    private List<Arma> listaArmas;
    private ResultSet datos;
    private SQLiteUtil sqlU;

    public ProyectoArmas() throws SQLException, ClassNotFoundException {
        initComponents();
        sqlU = new SQLiteUtil();
        
        archivo = new JFileChooser();
        archivo.setDialogType(JFileChooser.SAVE_DIALOG);
        archivo.setDialogTitle("Guardar archivo");

        archivo.setApproveButtonText("Guardar mi Archivo");
        archivo.setFileFilter(new Filtro("xml"));


        try {
            llenarTabla();
        } catch (SQLException ex) {
        }
        xstream = new XStream(new DomDriver());
        xstream.autodetectAnnotations(true);
    }

    private void llenarTabla() throws SQLException {

        String[] col = {"Pk", "MARCA", "CALIBRE", "SERIE", "FECHA", "TIPO"};
        String[][] data = {{"", "", ""}};
        tablaModel = new DefaultTableModel(data, col);
        tablaModel.setRowCount(0);


        tablaArmas.setModel(tablaModel);

        //Ocultar una columna por su indice
        tablaArmas.getColumnModel().getColumn(0).setMaxWidth(0);
        tablaArmas.getColumnModel().getColumn(0).setMinWidth(0);
        tablaArmas.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(0);
        tablaArmas.getTableHeader().getColumnModel().getColumn(0).setMinWidth(0);
        
        // Definir que se pueda organizar por columnas
        //TableRowSorter<TableModel> tablaOrdena = new TableRowSorter<TableModel>(model);
        tablaArmas.setRowSorter(new TableRowSorter<TableModel>(tablaModel));

        mensaje.setText("Espere ...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                String marca = "";
                String calibre = "";
                String serie = "";
                Date fecha_registro;
                String tipo = "";

                
                int i = 0;
                if (listaArmas == null) {
                    listaArmas = new ArrayList<Arma>();
                } else {
                    listaArmas.clear();
                }
                try {
                    datos = sqlU.consulta(0);
                    while (datos.next()) {
                        Thread.sleep(10);
                        marca = datos.getObject(2).toString();
                        calibre = datos.getObject(3).toString();
                        serie = datos.getObject(4).toString();
                        fecha_registro = new SimpleDateFormat("yyyy-MM-dd").parse(datos.getObject(5).toString());
                        tipo = datos.getObject(6).toString();

                        if (filtro.getText().equals("") || calibre.contains(filtro.getText())) {
                            listaArmas.add(new Arma(Integer.parseInt(
                                    datos.getObject(1).toString()),
                                    marca,
                                    calibre,
                                    serie,
                                    fecha_registro,
                                    tipo));


                            tablaModel.insertRow(i++, new Object[]{
                                datos.getObject(1).toString(),
                                marca,
                                calibre,
                                serie,
                                new SimpleDateFormat("dd/MM/yyyy").format(fecha_registro),
                                tipo
                            });
                        }
                    }
                } catch (SQLException | InterruptedException | ParseException | NumberFormatException e) {
                }
                finally {
                    try {
                        sqlU.desconectar();
                    } catch (SQLException ex) {
                        Logger.getLogger(ProyectoArmas.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                mensaje.setText(
                        "Terminado.");
            }
        }).start();
    }

    private void llamarFormulario() throws SQLException, ParseException, ClassNotFoundException {
        FormularioArma pS = new FormularioArma(ProyectoArmas.this, true, armaId);
        pS.setLocationRelativeTo(ProyectoArmas.this);
        pS.setVisible(true);
       
        llenarTabla();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tablaArmas = new javax.swing.JTable();
        insertar = new javax.swing.JButton();
        exportar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        filtro = new javax.swing.JTextField();
        filtrar = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        mensaje = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tablaArmas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tablaArmas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaArmasMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablaArmas);

        insertar.setText("Insertar");
        insertar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertarActionPerformed(evt);
            }
        });

        exportar.setText("Exportar XML");
        exportar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportarActionPerformed(evt);
            }
        });

        jLabel1.setText("Filtrar por calibre");

        filtrar.setText("Filtrar");
        filtrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filtrarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(mensaje, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(358, 358, 358))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mensaje, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(filtro, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(filtrar)
                                        .addGap(170, 170, 170))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(insertar)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(exportar, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(filtro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(filtrar))
                .addGap(22, 22, 22)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(insertar)
                    .addComponent(exportar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tablaArmasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaArmasMouseClicked
        try {
            int i = tablaArmas.getSelectedRow();
            armaId = Integer.parseInt((String) tablaArmas.getValueAt(i, 0));
            try {
                llamarFormulario();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ProyectoArmas.class.getName()).log(Level.SEVERE, null, ex);
            }


        } catch (SQLException | ParseException ex) {
            Logger.getLogger(ProyectoArmas.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_tablaArmasMouseClicked

    private void insertarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insertarActionPerformed
        try {
            armaId = 0;
            try {
                llamarFormulario();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ProyectoArmas.class.getName()).log(Level.SEVERE, null, ex);
            }


        } catch (SQLException | ParseException ex) {
            Logger.getLogger(ProyectoArmas.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_insertarActionPerformed

    private void exportarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportarActionPerformed
        if (archivo.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                xstream.alias("armas", List.class);
                xstream.alias("arma", Arma.class);
                xstream.aliasField("fechaRegistro", Arma.class, "fecha_registro");
                xstream.registerConverter(new DateConverter("dd/MM/yyyy",new String [] {"dd-MMM-yyyy", 
                                                                               "dd-MMM-yy", 
                                                                               "yyyy-MMM-dd",
                                                                               "yyyy-MM-dd", 
                                                                               "yyyy-dd-MM", 
                                                                               "yyyy/MM/dd", 
                                                                               "yyyy.MM.dd",
                                                                               "MM-dd-yy", 
                                                                               "dd-MM-yyyy"}));
                // xstream.toXML(listaArmas, new FileOutputStream(archivo.getSelectedFile().getCanonicalPath().toString())); 

                File file = new File(archivo.getSelectedFile().getAbsolutePath()+".xml");
                String xml = xstream.toXML(listaArmas);
                try (BufferedWriter buf = new BufferedWriter(new FileWriter(file))) {
                    buf.write(xml);
                    buf.flush();
                }
            } catch (Exception ex) {
                Logger.getLogger(ProyectoArmas.class
                        .getName()).log(Level.SEVERE, null, ex);
            }


        }
    }//GEN-LAST:event_exportarActionPerformed

    private void filtrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filtrarActionPerformed
        try {
            llenarTabla();
        } catch (SQLException ex) {
            Logger.getLogger(ProyectoArmas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_filtrarActionPerformed

    public static void main(String args[]) {
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
            java.util.logging.Logger.getLogger(ProyectoArmas.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ProyectoArmas.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ProyectoArmas.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ProyectoArmas.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new ProyectoArmas().setVisible(true);
                } catch (        SQLException | ClassNotFoundException ex) {
                    Logger.getLogger(ProyectoArmas.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton exportar;
    private javax.swing.JButton filtrar;
    private javax.swing.JTextField filtro;
    private javax.swing.JButton insertar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel mensaje;
    private javax.swing.JTable tablaArmas;
    // End of variables declaration//GEN-END:variables
}
