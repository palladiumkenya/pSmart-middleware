package card;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.TerminalFactory;

/**
 *
 * @author sa
 */
public class NFC_Test {

    /**
     * @param args the command line arguments
     */
    static Timer timer;

    public static void main(String[] args) {

        try {


            timer = new Timer();  //At this line a new Thread will be created

            timer.scheduleAtFixedRate(new NFC_Test.MyTask(), 0, 1000);


        } catch (Exception ex) {
            Logger.getLogger(NFC_Test.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    static class MyTask extends TimerTask {

        public void run() {

            try {
                String comm[] = { "find", "/usr", "/lib", "-name",
                        "libpcsclite.so.1" };
                Process p = Runtime.getRuntime().exec(comm);

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(p.getInputStream()));
                String line = "";
                while ((line = reader.readLine()) != null && !line.equals("")) {
                    if (line.contains("libpcsclite.so.1")) {
                        System.setProperty("sun.security.smartcardio.library",line);
                        break;
                    }

                }
                p.waitFor();

            } catch (Exception e) {

                e.printStackTrace();
            }

            ///////////////////This fix applied after reading thread at http://stackoverflow.com/a/16987873/1411888
            try {
                Class pcscterminal =
                        Class.forName("sun.security.smartcardio.PCSCTerminals");
                Field contextId = pcscterminal.getDeclaredField("contextId");
                contextId.setAccessible(true);

                if (contextId.getLong(pcscterminal) != 0L) {
                    Class pcsc =
                            Class.forName("sun.security.smartcardio.PCSC");

                    Method SCardEstablishContext = pcsc.getDeclaredMethod(
                            "SCardEstablishContext", new Class[]{Integer.TYPE});
                    SCardEstablishContext.setAccessible(true);



                    Field SCARD_SCOPE_USER =
                            pcsc.getDeclaredField("SCARD_SCOPE_USER");
                    SCARD_SCOPE_USER.setAccessible(true);

                    long newId = ((Long) SCardEstablishContext.invoke(pcsc, new Object[]{Integer.valueOf(SCARD_SCOPE_USER.getInt(pcsc))})).longValue();
                    contextId.setLong(pcscterminal, newId);
                }
            } catch (Exception ex) {
            }
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////

            TerminalFactory factory = null;
            List<CardTerminal> terminals = null;
            try {
                factory = TerminalFactory.getDefault();

                terminals = factory.terminals().list();
            } catch (Exception ex) { //
                Logger.getLogger(NFC_Test.class.getName()).log(Level.SEVERE,null, ex);
            }

            if (factory != null && factory.terminals() != null && terminals
                    != null && terminals.size() > 0) {
                try {
                    CardTerminal terminal = terminals.get(0);

                    if (terminal != null) {

                        System.out.println(terminal);
                        if (terminal.isCardPresent()) {
                            System.out.println("Card");
                        } else {
                            System.out.println("No Card");
                        }

                    } else {
                        System.out.println("No terminal");
                    }

                    terminal = null;
                } catch (Exception e) {
                    Logger.getLogger(NFC_Test.class.getName()).log(Level.SEVERE,null, e);
                }
                factory = null;

                terminals = null;

                Runtime.getRuntime().gc();

            } else {
                System.out.println("No terminal");
            }

        }
    }
}