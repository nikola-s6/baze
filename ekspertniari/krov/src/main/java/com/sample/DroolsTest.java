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
        	Krov k = new Krov();
        	k.setKvadratura(50.23);
        	k.dodajProkisnjavanje("sredina");
        	k.dodajProkisnjavanje("ivica");
        	k.setUlegao(true);
        	k.setBrojNedostajucihCrepova(40);
        	k.setOluciZardjali(true);
        	k.setDrvoSaKrosnjom(false);
        	
            kSession.insert(k);
            kSession.fireAllRules();
            k.toString();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
