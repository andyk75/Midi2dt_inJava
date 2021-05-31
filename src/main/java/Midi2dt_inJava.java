import java.io.File;
import java.io.IOException;

import javax.sound.midi.MidiUnavailableException;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import Config.MidiConfiguration;
import Midi.MidiDeviceObjekt;
import File.FileSelectorDialog;
import File.ConfigReader;
import File.GetConfiguration;

public class Midi2dt_inJava implements GetConfiguration {

    MidiConfiguration MyConfiguration;
    MidiDeviceObjekt MyMidiDevice;

    public static void main(String[] arg)  {
        Midi2dt_inJava MainClass = new Midi2dt_inJava();
        Boolean ConfigFileProvided = false;
        String DefaultMidiName = "MINI";    // For the XTouch Mini

        System.out.println( "Working Directory = " + System.getProperty( "user.dir" ) );
        // System.out.println( "Trying to use: \"" + arg[0] + "\" as configuration file." );
        // String path = System.getProperty( "user.dir" );
        for( String parameter : arg ){
            if( parameter.startsWith("-c")){
                // Argument is the configuration-file
                ConfigFileProvided = MainClass.TryToReadConfigFile( parameter.substring( parameter.indexOf("=") + 1 ) );
            } else if ( parameter.startsWith("-midi") ){
                // Argument is name for midi device
                DefaultMidiName = parameter.substring( parameter.indexOf("=") + 1 );
            }
        }

        if( !ConfigFileProvided ) {
            MainClass.StartFileDialog( DefaultMidiName );
        } else {
            MainClass.ConnectToMidiDevice( DefaultMidiName );
        }
    }

    private boolean TryToReadConfigFile( String configfilename ){
        try {
            this.MyConfiguration = ConfigReader.ReadFile( new File( System.getProperty("user.dir") + File.separator + configfilename ) );
            return true;
        } catch ( IOException e ) {
            e.printStackTrace();
        }
        return false;
    }

    private void StartFileDialog( String MidiDeviceName ){
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Turn off metal's use of bold fonts
                UIManager.put("swing.boldMetal", Boolean.FALSE);
                FileSelectorDialog.createAndShowGUI( Midi2dt_inJava.this::UseConfiguration, MidiDeviceName );
            }
        });
    }

    private void ConnectToMidiDevice( String MidiDeviceName ) {
        try {
            MyMidiDevice = new MidiDeviceObjekt( MyConfiguration, MidiDeviceName );
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void UseConfiguration(MidiConfiguration config,  String MidiDeviceName)  {
        MyConfiguration = config;
        ConnectToMidiDevice( MidiDeviceName );
    }
}
