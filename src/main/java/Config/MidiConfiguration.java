package Config;

import java.awt.AWTException;
import java.util.HashMap;

import Support.SupportFunctions;

public class MidiConfiguration {

    private Keyboard MyKeyboard;

    private HashMap<Integer, String[]> Configuration;

    public MidiConfiguration() {
        this.Configuration = new HashMap<>();
        try {
            MyKeyboard = new Keyboard();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public int GetKeysCount(){
        return Configuration.size();
    }

    public boolean AddNoteToConfiguration( int note, String[] key ){
        boolean result = false;
        if( Configuration.containsKey( note )){
            result = false;
        } else {
            result = true;
        }
        Configuration.put( note, key );
        return true;
    }

    public boolean AddConfigurationLineString( String input ){
        String InputWithoutCommentandWhitespaces = input;
        if( input.indexOf( "#" ) > 0 ){
            InputWithoutCommentandWhitespaces = input.substring( 0, input.indexOf( "#" ) );
            do{ // remove all whitespaces at the end, but keep whitespaces inside command strings.
                InputWithoutCommentandWhitespaces = InputWithoutCommentandWhitespaces.substring(0, InputWithoutCommentandWhitespaces.lastIndexOf(" ") );
            } while( InputWithoutCommentandWhitespaces.lastIndexOf(" " ) == InputWithoutCommentandWhitespaces.length() - 1 );

        }
        String[] parts = InputWithoutCommentandWhitespaces.split(",", -1 );
        if( parts[0].length() == 0 ){
            return true;    // skip empty lines
        }
        int note = Integer.valueOf( parts[0] );
        if( !Configuration.containsKey( note ) ){
            Configuration.put( note, SupportFunctions.subArray( parts, 1, parts.length - 1 ));
            // System.out.println( "Note: " + String.valueOf( note ) + "  keys: " + this.GetKeysAsString( note ) );
            return true;
        } else {
            return false;
        }
    }

    public String SendKeyStrokes( int note ){
        if( Configuration.containsKey( note )){
            for( String key : Configuration.get( note ) ) {
                MyKeyboard.typeAltStrgShift( key );
            }
            // Reset all "Modifiers"
            MyKeyboard.releaseModifierkeys();
            return "Done";
        } else {
            return "Key not found!";
        }
    }
}
