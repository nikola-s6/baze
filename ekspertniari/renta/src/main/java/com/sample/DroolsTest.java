package com.sample;

import java.util.ArrayList;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

/**
 * This is a sample class to launch a rule.
 */
public class DroolsTest {

    public static final void main(String[] args) {
        try {
            // load up the knowledge base
	        KieServices ks = KieServices.Factory.get();
    	    KieContainer kContainer = ks.getKieClasspathContainer();
        	KieSession kSession = kContainer.newKieSession("ksession-rules");

            // go !
        	
        	Klijent k = new Klijent();
        	k.setGodine(25);
        	k.setPosedujeVazecuVozackuB(true);
        	k.setGodinePosedovanjaVozacke(2.5);
        	
            kSession.insert(k);
            kSession.fireAllRules();
            System.out.println(k);
            
            
            
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

}