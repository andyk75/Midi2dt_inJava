package File;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;

import Config.MidiConfiguration;

public class FileSelectorDialog extends JPanel implements ActionListener{

    static private final String newline = "\n";
    JButton openButton, saveButton;
    JTextArea log;
    JFileChooser fc;

    MidiConfiguration Configuration;
    String MidiDeviceName;
    GetConfiguration DeviceConfigurationListener;

    public FileSelectorDialog( GetConfiguration listener , String MidiDeviceName)  {
        super(new BorderLayout());

        DeviceConfigurationListener = listener;
        this.MidiDeviceName = MidiDeviceName;
        //Create the log first, because the action listeners
        //need to refer to it.
        log = new JTextArea(10,30);
        log.setMargin(new Insets(5,5,5,5));
        log.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(log);

        //Create a file chooser
        fc = new JFileChooser();

        FileNameExtensionFilter filter = new FileNameExtensionFilter("Xtouch Configuration Files", "cfg");
        fc.addChoosableFileFilter(filter);
        fc.setAcceptAllFileFilterUsed(false);

        //Create the open button.  We use the image from the JLF
        //Graphics Repository (but we extracted it from the jar).
        openButton = new JButton("Load Configuration File",
                createImageIcon("images/Open16.gif"));
        openButton.addActionListener( this );

        //For layout purposes, put the buttons in a separate panel
        JPanel buttonPanel = new JPanel(); //use FlowLayout
        buttonPanel.add(openButton);

        //Add the buttons and the log to this panel.
        add(buttonPanel, BorderLayout.PAGE_START);
        add(logScrollPane, BorderLayout.CENTER);
    }


    /** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = FileSelectorDialog.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }


    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    public static void createAndShowGUI( GetConfiguration listener, String MidiDeviceName ) {

        //Create and set up the window.
        JFrame frame = new JFrame("Midi Key Translator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add content to the window.
        frame.add(new FileSelectorDialog( listener, MidiDeviceName ));

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //Handle open button action.
        if (e.getSource() == openButton) {
            int returnVal = fc.showOpenDialog(FileSelectorDialog.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                //This is where a real application would open the file.
                log.append("Using: " + file.getName() + "." + newline);
                log.append("Leave this window open and start working with the midi device." + newline);
                // log.append("There will be a debug output here!" + newline);
                try {
                    Configuration =  ConfigReader.ReadFile( file );
                    DeviceConfigurationListener.UseConfiguration( Configuration, MidiDeviceName );
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            } else {
                log.append("Open command cancelled by user." + newline);
            }
            log.setCaretPosition(log.getDocument().getLength());
        }
    }

}
