# Midi2dt_inJava
With this small java program you can control any software with you midi keyboard by the use of keyboard shortcuts.

I started this software to interface the Behringer XTouch Mini, but any other keyboard should also work. I used this midi-keyboard to control darktable for raw-photo development. 

## Build

Clone the project to your local drive and open it in Android Studio or Intellij IDEA IDE. 
The project should build without further adjustments. <br>

Connect the Behringer XTouch Mini to your PC, hit the RUN button or start the software with ALT+SHIFT+F10 and start to generate key strokes with the midi-keyboard. 

In the folder ./out/artifacts/XTouchInterface_jar/ the packed Midi2dt_inJava.jar file should be created. You can now directly use this jar to start the software without the IDE.


## Usage
start the jar-file with:

```
java -jar Midi2dt_inJava.jar -c=darktable.cfg -midi=MINI 
```

## Configuration
There are essentially two important configuration parameters: <br>
-c=ConfigFile.cfg<br>
-midi=MINI<br>

Explanation:
* with "-c=" you can provide a configuration file, for an example of the structure look at: [Template Config File](darktable.cfg) 
* with "-midi=" you can provide a string with the name or a part of it of the midi device.  If no -midi parameter is provided, the string MINI is used by default. If the wanted device can not be found the software provides a list of available midi devices in the system on the commandline.
* To identify the IDs of the buttons on the Behringer XTouch Mini I provided a pdf file with a complete listing of all buttons and knobs. See here [Complete List of all IDs for XTouch Mini](BehringerXTouchIDs.pdf).
* **MOST IMPORTANT** Of course you have to configure the respective application (darktable, Lightroom, etc...) you want to control with the selected KEYBOARD shortcuts! The sequence of IDs in the [Template Config File](darktable.cfg) is aligned with the buttons on the XTouch Mini and the function groups in darktable. You can go through the config file and adjust darktable to use these keyboard shortcuts. Some are already linked to other functions, if you want to keep them change the respective value in the config file! Currently there is no warning in this application if a key is already used in another ID! 

## Concept description

### Midi interface

The basic midi ID detection happens in MidiReceiver.java. Pushbuttons and turnknobs differ by the command byte of a midi-message but not necessarily by the ID.
The XTouch Mini has 46 different push buttons (Layer A and B) and 16 turnknobs which all operate in channel zero.
The Arturia Beatstep has 16 turnknobs and 16 velocity sensitive push buttons. All of these can be configured with different IDs saved in 16 different sets and different channels.
I decided to map all pushbutton IDs of all channels to the same internal key.
I mapped the turnknobs-IDs of the first channel to a range of 100 + ID, the second channel to 200 + ID and the third channel to 300 + ID.
With the up-direction always as +1 of the respective down direction of the same turnknob.

If you want to use a different midi device you might have to remap the knobs and buttons in MidiReceiver.java.

### Config-File 

As you can see in the code the configuration file is interpreted line by line with the comma as the separator. 
The first value in the line has to be the integer of the ID for which this configuration should be used. 
After that may follow one to many key commands of: 
* ALT
* STRG
* a-z
* A-Z
* 0-9

The keys given are Case sensitive! 
And once ALT or STRG is pressed it will remain pressed for all following keys! 

The line-interpretation ends at the first "#" character. Everything after that is a comment and is not used. 


