package hbe.fitness_zone.gui;

import hbe.fitness_zone.model.Client;
import hbe.fitness_zone.service.ClientService;
import hbe.fitness_zone.service.IClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@Component
public class FitnessZoneForm extends JFrame{
    private JPanel mainPanel;
    private JTable clientsTable;
    private JTextField firstnameText;
    private JTextField lastnameText;
    private JTextField membershipText;
    private JButton saveButton;
    private JButton deleteButton;
    private JButton cleanButton;
    IClientService clientService;
    private DefaultTableModel clientsModelTable;
    private Integer idClient;

    @Autowired
    public FitnessZoneForm(ClientService clientService){
        this.clientService = clientService;
        initializeForm();
        saveButton.addActionListener(e -> saveClient());
        clientsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                loadSelectedClient();
            }
        });
        deleteButton.addActionListener(e ->
            deleteSelectedClient());

        cleanButton.addActionListener(e ->
                cleanForm());
    }

    private void initializeForm(){
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900,700);
        setLocationRelativeTo(null); // centers the window
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        // this.clientsModelTable = new DefaultTableModel(0,4);
        // Disable editing of cell values in the table
        this.clientsModelTable = new DefaultTableModel(0, 4){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        String[] headings = {"Id",  "Firstname", "Lastname", "Membership"};
        this.clientsModelTable.setColumnIdentifiers(headings);
        this.clientsTable = new JTable(clientsModelTable);
        // Disable multiple selection in the table
        this.clientsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // Load clients list
        listClients();
    }

    private void listClients(){
        this.clientsModelTable.setRowCount(0);
        var clients = this.clientService.listAllClients();
        clients.forEach(client -> {
            Object[] clientRow = {
                    client.getId(),
                    client.getFirstname(),
                    client.getLastname(),
                    client.getMembership()
            };
            this.clientsModelTable.addRow(clientRow);
        });
    }

    private void saveClient(){
        // validation
        if(firstnameText.getText().trim().isEmpty()){
            showMessage("Provide a firstname");
            firstnameText.requestFocusInWindow();
            return;
        }

        if(lastnameText.getText().trim().isEmpty()){
            showMessage("Provide a lastname");
            lastnameText.requestFocusInWindow();
            return;
        }

        if(membershipText.getText().trim().isEmpty()){
            showMessage("Provide a membership number");
            membershipText.requestFocusInWindow();
            return;
        }

        // validate if membership number is a valid number
        int membershipNumber;
        try {
            membershipNumber = Integer.parseInt(membershipText.getText());
        } catch (NumberFormatException e) {
            showMessage("Membership number must be a valid number");
            membershipText.requestFocusInWindow();
            return;
        }

        // get local attribute idClient
        var id = this.idClient;

        // get text fields values
        var firstname = firstnameText.getText();
        var lastname = lastnameText.getText();
        var membership = membershipNumber;
        var client = new Client(id, firstname, lastname, membership);
        this.clientService.saveClient(client);
        if(this.idClient == null)
            showMessage("A new client was added");
        else
            showMessage("Client was updated");
        cleanForm();
        listClients();
    }

    private void cleanForm(){
        firstnameText.setText("");
        lastnameText.setText("");
        membershipText.setText("");
        // clean client id selected
        this.idClient = null;
        // unselect table register
        this.clientsTable.getSelectionModel().clearSelection();
    }

    private void loadSelectedClient(){
        var row = clientsTable.getSelectedRow();
        if(row != -1){ // -1 means there is not a client selected
            var id = clientsTable.getModel().getValueAt(row, 0).toString();
            this.idClient = Integer.parseInt(id);
            var firstname = clientsTable.getModel().getValueAt(row, 1).toString();
            this.firstnameText.setText(firstname);
            var lastname = clientsTable.getModel().getValueAt(row, 2).toString();
            this.lastnameText.setText(lastname);
            var membershipTable = clientsTable.getModel().getValueAt(row, 3).toString();
            this.membershipText.setText(membershipTable);
        }
    }

    private void deleteSelectedClient(){
        var row = clientsTable.getSelectedRow();

        if(row != -1){
            // get local attribute idClient
            var idClientStr = clientsTable.getModel().getValueAt(row, 0).toString();
            this.idClient = Integer.parseInt(idClientStr);
            var client = new Client();
            client.setId(this.idClient);
            this.clientService.deleteClient(client);
            showMessage("Client with id " + this.idClient + " was deleted");
            cleanForm();
            listClients();
        }
        else {
            showMessage("Select a client to delete");
        }
    }

    private void showMessage(String message){
        JOptionPane.showMessageDialog(this, message);
    }
}
