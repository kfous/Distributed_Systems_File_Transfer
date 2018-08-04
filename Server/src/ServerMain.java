//Fousekis Konstantinos
//icsd13196

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerMain {

    public static void main(String[] args) {
        
        //Δήλωση ροων για την ανταλλαγή μηνηματων μεταξυ εξυπηρετητη και client
        Socket sock = null;
        ServerSocket ss = null;
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        InputStream is = null;
        BufferedReader br = null;
        int current = 0;


        try {
             //Ανοιγμα πορτας για να δεχεται συνδεσεις απο clients
            ss = new ServerSocket(8080);
            System.out.println("Waiting for a Client..");

            sock = ss.accept();
            System.out.println("Client Connected ! !");

            //Αρχικοποίηση ροης ανάγνωσης μηνύματος του socket που συνδέθηκε
            br = new BufferedReader(new InputStreamReader(sock.getInputStream()));

            //Διαβασμα απο τον παραπάνω reader την ip και το ονομα του αρχειου που στελενται
            System.out.println("User with ip  " + sock.getInetAddress().toString() + " is sending a file with name " + br.readLine());
            
            //Αρχικοποίηση ροής αποδοχής αρχειου απο το πελατη
            is = sock.getInputStream();
            
            //fileoutputstream για τη δημιουργία αρχείου
            fos = new FileOutputStream("hi.txt");
            
            //Πίνακας των  bytes
            byte[] mybytearray = new byte[8192];
            
            //Δημιουργία BufferedOutputStream για μια ενέργεια στο fileoutputstream Που δεχεται σαν παραμετρο
            bos = new BufferedOutputStream(fos);
            
            //Αποθήκευση των byte του αρχειου που σταλθηκε σε bytesread
            int bytesRead = is.read(mybytearray, 0, mybytearray.length);

            current = bytesRead;

            do {//Αποθήκευση των Bytes που θα διαβαστούν στο αρχείο σε μια current
                bytesRead = is.read(mybytearray, current, (mybytearray.length - current));
                if (bytesRead >= 0) {
                    current += bytesRead;
                }
            } while (bytesRead > -1);
            
            //Και εν τελη εγγραφή τους στο νέο αρχείο
            bos.write(mybytearray, 0, current);
            bos.flush();

            System.out.println("File " + br.readLine()
                    + " downloaded (" + current + " bytes read)");

        } catch (IOException ex) {
            Logger.getLogger(ServerMain.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                //Για κάθε ροη και socket απαιτείται close
                br.close();
                bos.close();
                fos.close();
                is.close();
                sock.close();
                ss.close();

            } catch (IOException ex) {
                Logger.getLogger(ServerMain.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }
}
