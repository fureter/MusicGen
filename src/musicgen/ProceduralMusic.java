/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package musicgen;

import MusicStructure.Measure;
import MusicStructure.Note;
import MusicStructure.Scale;
import MusicStructure.Score;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import javax.sound.midi.Track;
import musicgen.MidiGen.EventCreator;
import musicgen.MidiGen.MessageCreator;
import musicgen.PatternGeneration.Markov;

/**
 *
 * @author FurEterinUmbrae
 */
public class ProceduralMusic {
    
    public static void createSimpleMusic(Track t,String key){
        for(long i = 0; i < MusicGen.length; i+=MusicGen.dt*4){
            for(long j = i; j < i+(MusicGen.dt*4); j+=MusicGen.dt){
               double note = Math.abs(Math.sin(j)+Math.sin(6*(j))+Math.sin(12*j)+Math.sin(3*j));
               double ran = Math.random();
               ran-=.5;
               ran*=2;
               
               double rndLen = Math.random();
               rndLen*=2;
               int ranLen = (int)rndLen;
               int rand = (int)ran;
               
               int not = clampValue(69,86,0,4,note) + rand;
               if(ranLen>.99){
                    t.add(EventCreator.createMidiEvent(MessageCreator.createShortMessage(not, MusicGen.NOTE_ON, 0x40), j));
                    t.add(EventCreator.createMidiEvent(MessageCreator.createShortMessage(not, MusicGen.NOTE_OFF, 0x60), (long)(j+(MusicGen.dt)*ranLen)));
               }
            }
            for(long j = i; j < i+(MusicGen.dt*4); j+=MusicGen.dt){
               double note = Math.abs(Math.cos(j)+Math.cos((j))+Math.sin(3*j)+Math.cos(9*(j)));
               double ran = Math.random();
               ran-=.5;
               ran*=2;
               int rand = (int)ran;
               
               double rndLen = Math.random();
               rndLen*=2;
               int ranLen = (int)rndLen;
               
               int not = clampValue(54,62,0,4,note) + rand;
               if(ranLen>.99){
                    t.add(EventCreator.createMidiEvent(MessageCreator.createShortMessage(not, MusicGen.NOTE_ON, 0x40), j));
                    t.add(EventCreator.createMidiEvent(MessageCreator.createShortMessage(not, MusicGen.NOTE_OFF, 0x60), (long)(j+(MusicGen.dt)*ranLen)));
               }
            }
        }
    }
    
    public static int clampValue(int min, int max, double range1, double range2, double value){
        double per = value/(range2-range1);
        int iter = (int)((max-min)*per);
        return min+iter;
    }
    
    public static void markovSectionated(Track t,Track t2, String key, String key2, String scale,int numPatterns,int patternLenMax,int timeSig,int sections, Random ran){
        Markov m = new Markov(Scale.getKeyScaleFromRoot(scale,key));
        MusicGen.seed = System.currentTimeMillis();
        Markov n = new Markov(Scale.getKeyScaleFromRoot(scale,key2));
        
        Score s = new Score();
        Score c = new Score();
        
        int sec = ran.nextInt(numPatterns)+2;
        
        System.out.println("Number of Sections: " + sec);
        
        for(int i = 0; i < sec; i++){
            int lenPattern = ran.nextInt(patternLenMax)+1;
            //int lenPattern = 4;
            System.out.println("Number of Segments: " + lenPattern);
            ArrayList measures = new ArrayList<>();
            ArrayList measures2 = new ArrayList<>();
            for(int j = 0; j < lenPattern;j++){
                System.out.println("Section: " + j +" out of: " + lenPattern);
                measures.add(new Measure(j*MusicGen.dt*timeSig,m.makeMusic(timeSig)));
                measures2.add(new Measure(j*MusicGen.dt*timeSig,n.makeMusic(timeSig)));
                System.out.println("Measure start: "+j*MusicGen.dt);
            }
            s.addSection(measures);
            c.addSection(measures2);
        }
        
        System.out.println("Finished Creating Sections");
        int[] numSec = new int[sec];
        for(int i = 0; i < sec;i++){
            System.out.println(i);
            numSec[i] = i;
        }
        Markov se = new Markov(numSec);
        
        int[] secOrder = checkRepeat(se,sections,0,10);
        
        removeZeroNotes(s);
        removeZeroNotes(c);
        
        removeDissonence(s,c,scale);
        removeDissonence(s,c,scale);
        
        addChord(c,scale,key,ran);
        
        System.out.println("Beggining Track Writing");
        
        long sectionTime = 0;
        for(int i = 0; i < secOrder.length;i++){
            ArrayList mes = s.getSection(secOrder[i]);
            int num = 0;
            for (Object me : mes) {
                Measure current = (Measure) me;
                num++;
                for (Note note : current.notes) {
                    t.add(EventCreator.createMidiEvent(MessageCreator.createShortMessage(note.note, MusicGen.NOTE_ON, 35), sectionTime+current.timeStart+note.start));
                    t.add(EventCreator.createMidiEvent(MessageCreator.createShortMessage(note.note, MusicGen.NOTE_OFF, 35), sectionTime+current.timeStart+note.start+note.duration));
                }
            }
            sectionTime+=(timeSig*MusicGen.dt)*num;
            num = 0;
        }
        
        sectionTime = 0;
        for(int i = 0; i < secOrder.length;i++){
            ArrayList mes = c.getSection(secOrder[i]);
            int num = 0;
            for (Object me : mes) {
                Measure current = (Measure) me;
                num++;
                //System.out.println("  Measure start: " + current.timeStart);
                for (Note note : current.notes) {
                    //System.out.println("    Note StartTime: " + note.start);
                    //System.out.println("    Note duration: " + note.duration);
                    t2.add(EventCreator.createMidiEvent(MessageCreator.createShortMessage(note.note, MusicGen.NOTE_ON+MusicGen.track2channel, 25), sectionTime+current.timeStart+note.start));
                    t2.add(EventCreator.createMidiEvent(MessageCreator.createShortMessage(note.note, MusicGen.NOTE_OFF+MusicGen.track2channel, 25), sectionTime+current.timeStart+note.start+note.duration));
                }
            }
            sectionTime+=(timeSig*MusicGen.dt)*num;
            //System.out.println("Section time: " +sectionTime);
            num = 0;
        }
        
    }
    
    public static void markovSectionatedWithVel(Track t, String key, String key2, String scale,int numPatterns,int patternLenMax,int timeSig,int sections, Random ran){
        Markov m = new Markov(Scale.getKeyScaleFromRoot(scale,key));
        MusicGen.seed = System.currentTimeMillis();
        Markov n = new Markov(Scale.getKeyScaleFromRoot(scale,key2));
        MusicGen.seed = System.currentTimeMillis();
        //Markov vel = generateVelocityGraph(MusicGen.velLow,MusicGen.velHigh,MusicGen.velStep);
        //Markov velL = generateVelocityGraph(MusicGen.velLow,MusicGen.velHigh,MusicGen.velStep);
        
        Score s = new Score();
        Score c = new Score();
        
        //Score velS = new Score();
        //Score velSL = new Score();
        
        int sec = ran.nextInt(numPatterns)+2;
        
        System.out.println("Number of Sections: " + sec);
        
        for(int i = 0; i < sec; i++){
            int lenPattern = ran.nextInt(patternLenMax)+1;
            System.out.println("Number of Segments: " + lenPattern);
            ArrayList measures = new ArrayList<>();
            ArrayList measures2 = new ArrayList<>();
            //ArrayList velList = new ArrayList<>();
            //ArrayList velListL = new ArrayList<>();
            for(int j = 0; j < lenPattern;j++){
                System.out.println("Section: " + j +" out of: " + lenPattern);
                measures.add(new Measure(0,m.makeMusic(timeSig)));
                measures2.add(new Measure(0,n.makeMusic(timeSig)));
                //velList.add(new Measure(0,vel.makeMusic(timeSig)));
                //velListL.add(new Measure(0,velL.makeMusic(timeSig)));
            }
            s.addSection(measures);
            c.addSection(measures2);
            //velS.addSection(velList);
            //velSL.addSection(velListL);
        }
        
        System.out.println("Finished Creating Sections");
        int[] numSec = new int[sec];
        for(int i = 0; i < sec;i++){
            System.out.println(i);
            numSec[i] = i;
        }
        Markov se = new Markov(numSec);
        
        int[] secOrder = checkRepeat(se,sections,0,10);
        
        removeDissonence(s,c,scale);
        
        System.out.println("Beggining Track Writing");
        
        long time = 0;
        for(int i = 0; i < secOrder.length;i++){
            ArrayList mes = s.getSection(secOrder[i]);
            //ArrayList velA = velS.getSection(secOrder[i]);
            for (int j = 0; j < mes.size();j++) {
                Measure current = (Measure) mes.get(j);
                //Measure currentVel = (Measure) velA.get(j);
                for (int k = 0; k < current.notes.size();k++) {
                    t.add(EventCreator.createMidiEvent(MessageCreator.createShortMessage(current.notes.get(k).note, MusicGen.NOTE_ON, 30), time));
                    t.add(EventCreator.createMidiEvent(MessageCreator.createShortMessage(current.notes.get(k).note, MusicGen.NOTE_OFF, 30), time+current.notes.get(k).duration-1));
                    time+=current.notes.get(k).duration;
                }
            }
        }
        
        time = 0;
        for(int i = 0; i < secOrder.length;i++){
            ArrayList mes = c.getSection(secOrder[i]);
            //ArrayList velA = velSL.getSection(secOrder[i]);
            for (int j = 0; j < mes.size();j++) {
                Measure current = (Measure) mes.get(j);
                //Measure currentVel = (Measure) velA.get(j);
                for (int k = 0; k < current.notes.size();k++) {
                    t.add(EventCreator.createMidiEvent(MessageCreator.createShortMessage(current.notes.get(k).note, MusicGen.NOTE_ON, 30), time));
                    t.add(EventCreator.createMidiEvent(MessageCreator.createShortMessage(current.notes.get(k).note, MusicGen.NOTE_OFF, 30), time+current.notes.get(k).duration-1));
                    time+=current.notes.get(k).duration;
                }
            }
        }
        
    }
    
    public static int[] checkRepeat(Markov m, int sections, int currentTry, int maxTrys){
        int[] secOrder = m.makeSectionOrder(sections);
        
        if(currentTry >=maxTrys){
            return secOrder;
        }
        
        int before = secOrder[0];
        int count = 0;
        for(int i = 1; i < secOrder.length;i++){
            if(secOrder[i] == before){
                count++;
                if(count >= MusicGen.MaxRepeat){
                    return checkRepeat(m,sections,currentTry+1,maxTrys);
                }
            }else{
                before = secOrder[i];
                count=0;
            }
        }
        return secOrder;
    }
    
    public static Markov generateVelocityGraph(int lower, int upper, int step){
        
        int[] input = new int[((upper-lower)/step)];
        
        for(int i = 0; i < (upper-lower)/step; i++){
            input[i] = (i*step)+lower;
        }
        
        Markov vel = new Markov(input);
        return vel;
    }
    
    public static void removeDissonence(Score a, Score b,String scale){
        
        System.out.println("Removing Dissonence");
        
        for(int i = 0; i < a.counter;i++){
            ArrayList mA = a.getSection(i);
            ArrayList mB = b.getSection(i);
            
            int[] scaleList = Scale.getScale(scale);
            
            for(int j = 0; j < mA.size();j++){
                Measure sA = (Measure)mA.get(j);
                Measure sB = (Measure)mB.get(j);
                
                for(int k = 0; k < sA.notes.size();k++){
                    if(sB.notes.size() >= sA.notes.size()){
                        
                        Note nA = sA.notes.get(k);
                        Note nB = sB.notes.get(k);
                        
                        int dist = (nA.note%12)-(nB.note%12);
                        
                        //System.out.println("Distance: " + dist);
                        
                        if(Math.abs(dist) == 1){
                            if(!(nA.note == 0 || nB.note==0)){
                                //System.out.println("Dist: 1");
                                //System.out.println("A:"+nA.note + " " + "B:"+nB.note);
                                nA.note = (nA.note+(scaleList[findNoteInScale(nA.note,scaleList)+1] - scaleList[findNoteInScale(nA.note,scaleList)]));
                            }
                        }
                        else if(Math.abs(dist) == 2){
                            if(!(nA.note == 0 || nB.note==0)){
                                //System.out.println("Dist: 2");
                                //System.out.println("A:"+nA.note + " " + "B:"+nB.note);
                                nA.note = (nA.note+(scaleList[findNoteInScale(nA.note,scaleList)+1] - scaleList[findNoteInScale(nA.note,scaleList)]));
                            }
                        }
                        else if(Math.abs(dist) == 7){
                            if(!(nA.note == 0 || nB.note==0)){
                                //System.out.println("Dist: 7");
                                //System.out.println("A:"+nA.note + " " + "B:"+nB.note);
                                nA.note = (nA.note+(scaleList[findNoteInScale(nA.note,scaleList)+1] - scaleList[findNoteInScale(nA.note,scaleList)]));
                            }
                        }
                        else if(Math.abs(dist) == 11){
                            if(!(nA.note == 0 || nB.note==0)){
                                //System.out.println("Dist: 11");
                                //System.out.println("A:"+nA.note + " " + "B:"+nB.note);
                                nA.note = (nA.note+(scaleList[findNoteInScale(nA.note,scaleList)+1] - scaleList[findNoteInScale(nA.note,scaleList)]));
                            }
                        }
                        
                    }
                }
            }
        }
        
        System.out.println("Done Dissonence" + "\n");
    }
    
    public static int findNoteInScale(int note, int[] scale){
        int mod = note%12;
        for(int i = 0; i < scale.length;i++){
            if(scale[i] == mod){
                return i;
            }
        }
        return 0;
    }
    
    public static void checkIfInKeyScale(Score a, Score b, String scale, String key){
        
    }
    
    public static void addChord(Score a, String scale, String key, Random r){
        int[] scaleList = Scale.getScale(scale);
        for(int i = 0; i < a.counter;i++){
            ArrayList mA = a.getSection(i);
            
            
            
            for (int j = 0; j < mA.size();j++) {             
                Measure sA = (Measure) mA.get(j);
                for (int k = sA.notes.size()-1; k >= 0;k--) {
                    double chance = 0;
                    if(sA.notes.get(k).duration > 1){
                        chance+=.0;
                    }
                    if(r.nextDouble() < .0+chance){
                        double style = r.nextDouble();
                        if(style <= .33){
                            Note not = sA.notes.get(k);
                            int pos = findNoteInScale(not.note,scaleList);
                            if(pos <=2 ){
                                int dist1 = 12 - scaleList[(scaleList.length-2)-(pos)];
                                int dist2 = 12 - scaleList[(scaleList.length-4)-(pos)];
                                System.out.println(dist1 + " " + dist2);
                                System.out.println("Chord: " + not.note + " " + (not.note-dist1) + " " + (not.note-dist2));
                                ((Measure)(a.getSection(i).get(j))).addNote(not.note-dist1,not.duration,not.start);
                                ((Measure)(a.getSection(i).get(j))).addNote(not.note-dist2,not.duration,not.start);
                            }else if( pos < 4){
                                int dist1 = scaleList[pos] - scaleList[pos-2];
                                int dist2 = 12 - scaleList[scaleList.length-2-(pos-2)];
                                System.out.println(dist1 + " " + dist2);
                                System.out.println("Chord: " + not.note + " " + (not.note-dist1) + " " + (not.note-dist2));
                                ((Measure)(a.getSection(i).get(j))).addNote(not.note-dist1,not.duration,not.start);
                                ((Measure)(a.getSection(i).get(j))).addNote(not.note-dist2,not.duration,not.start);
                            }else{
                                int dist1 = scaleList[pos] - scaleList[pos-2];
                                int dist2 = scaleList[pos] - scaleList[pos-4];
                                System.out.println(dist1 + " " + dist2);
                                System.out.println("Chord: " + not.note + " " + (not.note-dist1) + " " + (not.note-dist2));
                                ((Measure)(a.getSection(i).get(j))).addNote(not.note-dist1,not.duration,not.start);
                                ((Measure)(a.getSection(i).get(j))).addNote(not.note-dist2,not.duration,not.start);
                                        }
                        }else if( style <=.66){
                            
                        }else{
                            
                        }
                    }
                }
            }
        }
    }
    
    public static void removeZeroNotes(Score a){
        for(int i = 0; i < a.counter; i++){
            ArrayList mea = a.getSection(i);
            for(int j = 0; j < mea.size();j++){
                Measure me = (Measure)mea.get(j);
                for(int k = me.notes.size()-1; k >= 0;k--){
                    if(me.notes.get(k).duration == 0){
                        me.notes.remove(k);
                    }
                }
            }
        }
    }
}
