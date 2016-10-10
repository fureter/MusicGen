/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package musicgen.MidiGen;

import java.util.HashMap;
import javax.sound.midi.ShortMessage;

/**
 *
 * @author FurEterinUmbrae
 */
public class MessageCreator {
    
    private static HashMap notes = new HashMap();
    
    /*
    * Creates the notes hashmap, that matches the string representation for the input pitch, to its midi integer value.
    */
    public static void init(){
        notes.put("c0", 0);
        notes.put("c#0", 1);
        notes.put("d0", 2);
        notes.put("d#0", 3);
        notes.put("e0", 4);
        notes.put("f0", 5);
        notes.put("f#0",6);
        notes.put("g0", 7);
        notes.put("g#0", 8);
        notes.put("a0", 9);
        notes.put("a#0", 10);
        notes.put("b0", 11);
        notes.put("c1", 12);
        notes.put("c#1",13);
        notes.put("d1", 14);
        notes.put("d#1", 15);
        notes.put("e1", 16);
        notes.put("f1", 17);
        notes.put("f#1", 18);
        notes.put("g1", 19);
        notes.put("g#1",20);
        notes.put("a1", 21);
        notes.put("a#1", 22);
        notes.put("b1", 23);
        notes.put("c2", 24);
        notes.put("c#2", 25);
        notes.put("d2", 26);
        notes.put("d#2",27);
        notes.put("e2", 28);
        notes.put("f2", 29);
        notes.put("f#2", 30);
        notes.put("g2", 31);
        notes.put("g#2", 32);
        notes.put("a2", 33);
        notes.put("a#2",34);
        notes.put("b2", 35);
        notes.put("c3", 36);
        notes.put("c#3", 37);
        notes.put("d3", 38);
        notes.put("d#3", 39);
        notes.put("e3", 40);
        notes.put("f3",41);
        notes.put("f#3", 42);
        notes.put("g3", 43);
        notes.put("g#3", 44);
        notes.put("a3", 45);
        notes.put("a#3", 46);
        notes.put("b3", 47);
        notes.put("c4",48);
        notes.put("c#4", 49);
        notes.put("d4", 50);
        notes.put("d#4", 51);
        notes.put("e4", 52);
        notes.put("f4", 53);
        notes.put("f#4", 54);
        notes.put("g4",55);
        notes.put("g#4", 56);
        notes.put("a4", 57);
        notes.put("a#4", 58);
        notes.put("b4", 59);
        notes.put("c5", 60);
        notes.put("c#5", 61);
        notes.put("d5",62);
        notes.put("d#5", 63);
        notes.put("e5", 64);
        notes.put("f5", 65);
        notes.put("f#5", 66);
        notes.put("g5", 67);
        notes.put("g#5", 68);
        notes.put("a5",69);
        notes.put("a#5", 70);
        notes.put("b5", 71);
        notes.put("c6", 72);
        notes.put("c#6", 73);
        notes.put("d6", 74);
        notes.put("d#6", 75);
        notes.put("e6",76);
        notes.put("f6", 77);
        notes.put("f#6", 78);
        notes.put("g6", 79);
        notes.put("g#6", 80);
        notes.put("a6", 81);
        notes.put("a#6", 82);
        notes.put("b6",83);
        notes.put("c7", 84);
        notes.put("c#7", 85);
        notes.put("d7", 86);
        notes.put("d#7", 87);
        notes.put("e7", 88);
        notes.put("f7", 89);
        notes.put("f#7",90);
        notes.put("g7", 91);
        notes.put("g#7", 92);
        notes.put("a7", 93);
        notes.put("a#7", 94);
        notes.put("b7", 95);
        notes.put("c8", 96);
        notes.put("c#8",97);
        notes.put("d8", 98);
        notes.put("d#8", 99);
        notes.put("e8", 100);
        notes.put("f8", 101);
        notes.put("f#8", 102);
        notes.put("g8", 103);
        notes.put("g#8",104);
        notes.put("a8", 105);
        notes.put("a#8", 106);
        notes.put("b8", 107);
        notes.put("c9", 108);
        notes.put("c#9", 109);
        notes.put("d9", 110);
        notes.put("d#9",111);
        notes.put("e9", 112);
        notes.put("f9", 113);
        notes.put("f#9", 114);
        notes.put("g9", 115);
        notes.put("g#9", 116);
        notes.put("a9", 117);
        notes.put("a#9",118);
        notes.put("b9", 119);
        notes.put("c10", 120);
        notes.put("c#10", 121);
        notes.put("d10", 122);
        notes.put("d#10", 123);
        notes.put("e10", 124);
        notes.put("f10",125);
        notes.put("f#10", 126);
        notes.put("g10", 127);
        
    }
    
    /*
    * creates and returns a new short message for a note with the string note, status of the message, and the desired velocity.
    */
    public static ShortMessage createShortMessage(String note, int status, int vel ){
        ShortMessage mm = new ShortMessage();
        int pitch = MessageCreator.getNote(note);
        try{
            mm.setMessage(status,pitch,vel);
        }catch(Exception e){
            System.out.println(e.toString());
        }
        return mm;
    }
    /*
    * creates and returns a new short message for a note with the integer note value, status of the message, and the desired velocity.
    */
    public static ShortMessage createShortMessage(int note, int status, int vel ){
        ShortMessage mm = new ShortMessage();
        try{
            mm.setMessage(status,note,vel);
        }catch(Exception e){
            System.out.println(e.toString());
        }
        return mm;
    }
    
    /*
    *  returns the integer value of the input note using the hashmap notes.
    */
    public static int getNote(String note){
        return (int)notes.get(note);
    }
}
