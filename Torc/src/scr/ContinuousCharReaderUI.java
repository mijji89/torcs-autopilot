package scr;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;

/**
 * La classe permette di gestire i comandi manuali da tastiera comunicando con la classe SimpleDriver
 * I comandi vengono presi tramite un interfaccia grafica Swing.
 */
public class ContinuousCharReaderUI extends JFrame {
    private JTextField inputField;
    private final SimpleDriver sd; 

    /**
     * Costruisce un'interfaccia grafica che consente di catturare i comandi da tastiera
     * 
     * @param sd istanza di SimpleDriver che riceve i comandi da tastiera 
     */
    public ContinuousCharReaderUI(SimpleDriver sd) {
        this.sd=sd; 
        // Set up the frame
        setTitle("Continuous Character Reader");
        setSize(300, 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        // Initialize the text field for input
        inputField = new JTextField(20);
        add(inputField);

        // Add key listener to the text field
        inputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char ch = e.getKeyChar();
                System.out.println("You pressed: " + ch);
                // Clear the text field
                inputField.setText("");
                sd.setPressed(ch);
                // Exit if 'z' is pressed
                if (ch == 'z') {
                    System.exit(0);
                }
            }

            /**
             * Sgnala al driver che non c'è alcun tasto premuto quando il pulsante viene rilasciato
             * 
             * @param e evento da tastiera che segnala il rilascio del tasto
             */
            @Override
            public void keyReleased(KeyEvent e) {
                sd.setPressed(' ');
                System.out.println("Tasto rilasciato - reset");
            }

        });

        // Make the frame visible
        setVisible(true);
    }
}

