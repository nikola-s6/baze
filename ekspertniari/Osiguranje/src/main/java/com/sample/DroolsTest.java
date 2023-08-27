package com.sample;

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
            Automobil a = new Automobil();
            a.setSnaga(50);
            a.setNov(false);
            a.setPremijskiStepen(4);
            a.setBrojNezgoda(3);
        	
            kSession.insert(a);
            kSession.fireAllRules();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
