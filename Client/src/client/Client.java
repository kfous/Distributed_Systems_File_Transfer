//Fousekis Konstantinos
//icsd13196

package client;

import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Kostas
 */
public class Client extends JFrame implements ActionListener {
    
    //Δήλωση γραμμων,ετικετων,κουμπιων και chooser για την επιλογη αρχειου
    private JPanel line1;
    private JPanel line2;
    private JButton SelectButton;
    private JButton SendButton;
    private JLabel TextName;
    private JFileChooser chooser;

    //Constructor για την δημιοργία των γραφικών της κλάσης μέσω JFrame
    public Client() {
        super("Choose File to Send to Server");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        
        //Αρχικοποίηση κουμπιών επιλογής και αποστολής αρχείου
        SelectButton = new JButton("Select File");
        SendButton = new JButton("Send File");

        //τα οποία θα ακούν σε εναν actionlistener κατά την χρήση τους
        SelectButton.addActionListener(this);
        SendButton.addActionListener(this);
        
        //Ετικέτα που θα περιέχει το όνομα του αρχείου
        TextName = new JLabel("EmptyLabel");

        Container con = getContentPane();

        //Αρχικοποίηση γραμμών
        line1 = new JPanel();
        line2 = new JPanel();

        //Δημιουργία ενός GridLayout 2 γραμμών και 1 στήλης
        con.setLayout(new GridLayout(2, 2));

        //Προσθήκη layout σε αυτές
        line1.setLayout(new FlowLayout());
        line2.setLayout(new FlowLayout());

        //Προσθήκη επιθυμητών κουμπιών και ετικετών στις παραπάνω γραμμές
        line1.add(SelectButton);
        line1.add(TextName);
        line2.add(SendButton);

        //Δίνονται components στiς γραμμές για να στοιχιστούν μέσω της παρακάτω pack
        con.add(line1);
        con.add(line2);

        pack();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();

        
        //Στη περίπτωση που επιλεγεί το κουμπί για την επιλογή αρχείου
        if (obj == SelectButton) {
            
            //Αρχικοποιώ τον chooser
            chooser = new JFileChooser();

            Component parent = null;
            int returnVal = chooser.showOpenDialog(parent);//Εμφάνιση παραθύρου για την επιλογή του αρχείου
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                System.out.println("You chose to open this file: " + chooser.getSelectedFile().getName());
            }
            //Προσθήκη στην αρχική ετικέτα το όνομα του αρχείου που επιλέχθηκε
            TextName.setText(chooser.getSelectedFile().getName());

        }

        //Στη περίπτωση που επιλεγεί το κουμπί για την αποστολή αρχείου
        if (obj == SendButton) {
            FileInputStream fis = null;
            BufferedInputStream bis = null;
             OutputStream os = null;
             Socket sock = null;
             BufferedWriter bw = null;
             BufferedReader br = null;
            try {
                //Πορτα επικοινωνίας με το server στην ip που αναγράφεται
                sock = new Socket("localhost", 8080);
                
                bw = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
                
                br = new BufferedReader(new InputStreamReader(sock.getInputStream()));

                //Στέλνει τo όνομα του αρχέιου
                bw.write("'" + chooser.getSelectedFile().getName() + "'");
                bw.newLine();
                bw.flush();
                
                os = sock.getOutputStream();
                
                //Αποθήκευση των byte του αρχείου που επιλέχθηκε σε ένα fis
                fis = new FileInputStream(chooser.getSelectedFile());

                //Πινακας bytes μεγέθους όσο το αρχείο
                byte[] mybytearray = new byte[(int) chooser.getSelectedFile().length()];
                
                //read data from fis
                bis = new BufferedInputStream(fis);
                bis.read(mybytearray, 0, mybytearray.length);

                System.out.println("Sending " + chooser.getSelectedFile().getName() + "(" + mybytearray.length + " bytes)");
                
                //Αποστολή των byte μέσω της παρακάτω ροής στο server
                os.write(mybytearray, 0, mybytearray.length);
                os.flush();
                System.out.println("Done.");
 

                
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }finally{
                try {
                    bw.close();
                    bis.close();
                    fis.close();
                    os.close();
                    sock.close();
                    
                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }

        }
    }
}
