import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import core.Gap;

public class TestBuffer extends JPanel implements ActionListener {
    protected JTextField textField;
    protected JTextArea textArea;
    static Gap text = new Gap(128);

    public TestBuffer() {
        super(new GridBagLayout());

        textField = new JTextField(20);
        textField.addActionListener(this);
        textField.addKeyListener(new KeyListener() {
            Character c;

            @Override
            public void keyTyped(KeyEvent e) {

                c = e.getKeyChar();
                if (c.equals('\b')) {
                    text.backspace();
                } else if (!Character.isISOControl(c) || c.equals('\n')){
                    text.insert(c);
                    System.out.println("Status: " + text.getTextLength() + " / " + text.getCapacity());
                }
                textArea.setText(text.toString());
                textField.selectAll();
                System.out.println("Nr. of lines: " + text.getNumberOfLines());
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT)
                    text.moveKeyLeft();
                else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
                    text.moveKeyRight();

                if (e.getKeyCode() == KeyEvent.VK_CONTROL)
                    text.addCursor(0);
                if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    text.delete();
                    System.out.println("Nr. of lines: " + text.getNumberOfLines());
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        textArea = new JTextArea(5, 20);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        //Add Components to this panel.
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;

        c.fill = GridBagConstraints.HORIZONTAL;
        add(textField, c);

        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        add(scrollPane, c);
    }

    public void actionPerformed(ActionEvent evt) {

            //Make sure the new text is visible, even if there
            //was a selection in the text area.
            textArea.setCaretPosition(textArea.getDocument().getLength());
            //textArea.setText("");
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("TextDemo");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //Add contents to the window.
        frame.add(new TestBuffer());

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }


    public static void main(String[] args) throws IOException, InterruptedException{

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });

    }
}
