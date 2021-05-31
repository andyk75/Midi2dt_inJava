package Midi;

import java.io.IOException;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Transmitter;

import Config.MidiConfiguration;

public class MidiDeviceObjekt {

    private MidiDevice MyMidiDevice;
    private MidiReceiver myReceiver;
    private MidiConfiguration MyConfiguration;

    public MidiDeviceObjekt( MidiConfiguration config, String MidiDeviceName ) throws MidiUnavailableException, IOException {
        myReceiver = new MidiReceiver( config, System.out );
        MyConfiguration = config;
        MidiDeviceObjekt.listTransmitterDevices();
        MyMidiDevice = MidiDeviceObjekt.getInputDevice( MidiDeviceName );

        if( MyMidiDevice == null ){
            System.out.println("Failed to connect to midi-device: " + MidiDeviceName );
            System.out.println("Leaving application! \nGood bye.\n");

            System.out.println("Currently available Midi devices in this system: ");
            listTransmitterDevices();
            System.exit( 0 );
        } else {

            // just to make sure that i got the right one
            System.out.println(MyMidiDevice.getDeviceInfo().getName().toString());
            System.out.println(MyMidiDevice.getMaxTransmitters());

            // opening the device
            System.out.println("open inputDevice: "
                    + MyMidiDevice.getDeviceInfo().toString());
            MyMidiDevice.open();
            System.out.println("connect Transmitter to Receiver");

            // Creating a Dumpreceiver and setting up the Midi wiring
            // Receiver r = new DumpReceiver(System.out);
            Transmitter t = MyMidiDevice.getTransmitter();
            t.setReceiver(myReceiver);

            System.out.println("connected.");
            System.out.println("running...");
            System.in.read();
            // at this point the console should print out at least something, as the
            // send method of the receiver should be called when i hit a key on my
            // keyboard
            /* System.out.println("close inputDevice: "
                    + MyMidiDevice.getDeviceInfo().toString());
            MyMidiDevice.close();
            System.out.println(("Received " + ((DumpReceiver) r).seCount
                    + " sysex messages with a total of "
                    + ((DumpReceiver) r).seByteCount + " bytes"));
            System.out.println(("Received " + ((DumpReceiver) r).smCount
                    + " short messages with a total of "
                    + ((DumpReceiver) r).smByteCount + " bytes"));
            System.out.println(("Received a total of "
                    + (((DumpReceiver) r).smByteCount +
                    ((DumpReceiver) r).seByteCount) + " bytes"));
            */
        }


    }


    public static void listTransmitterDevices() throws MidiUnavailableException {
        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
        for (int i = 0; i < infos.length; i++) {
            MidiDevice device = MidiSystem.getMidiDevice(infos[i]);
            if (device.getMaxTransmitters() != 0)
                System.out.println(device.getDeviceInfo().getName().toString()
                        + " has transmitters");
        }
    }

    // should get me my USB MIDI Interface. There are two of them but only one
    // has Transmitters so the if statement should get me the one i want
    public static MidiDevice getInputDevice( String MidiDeviceName ) throws MidiUnavailableException {
        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
        for (int i = 0; i < infos.length; i++) {
            MidiDevice device = MidiSystem.getMidiDevice(infos[i]);
            if (device.getMaxTransmitters() != 0
                    && device.getDeviceInfo().getName().contains(MidiDeviceName)) {
                System.out.println(device.getDeviceInfo().getName().toString()
                        + " was chosen");
                return device;
            }
        }
        return null;
    }

}
