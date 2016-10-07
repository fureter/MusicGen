/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MusicStructure;

import java.util.HashMap;
import musicgen.MidiGen.MessageCreator;

/**
 *
 * @author FurEterinUmbrae
 */
public class Scale {
    private static HashMap scale = new HashMap();
    
    public static void init(){
        scale.put("Major", new int[]{0,2,4,5,7,9,11,12});
        scale.put("Minor", new int[]{0,2,3,5,7,8,10,12});
        scale.put("Iwato",new int[]{0,1,5,6,10,12});
        scale.put("HarmonicMinor",new int[]{0,2,3,5,7,8,11,12});
        scale.put("MelodicMinorAscend",new int[]{0,2,3,5,7,9,11,12});
    }
    
    public static int[] getScale(String a){
        return (int[])scale.get(a);
    }
    
    public static int[] getKeyScaleFromRoot(String scale, String key){
        int ke = MessageCreator.getNote(key);
        int[] sc = getScale(scale).clone();

        
        System.out.println("Root Key is: " + ke);
        
        for(int i = 0; i < sc.length;i++){
            System.out.println(sc[i]);
            sc[i]+=ke;
            System.out.println(sc[i]);
        }
        
        return sc;
    }
}
