package File;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import Config.MidiConfiguration;

public class ConfigReader {

    public static MidiConfiguration ReadFile(File file ) throws IOException {
        MidiConfiguration Configuration = new MidiConfiguration();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                // process the line.
                // System.out.println( line );
                Configuration.AddConfigurationLineString( line );
            }
        }
        System.out.println( "Added: " + Configuration.GetKeysCount() + " Keys." );

        return Configuration;
    }

}
