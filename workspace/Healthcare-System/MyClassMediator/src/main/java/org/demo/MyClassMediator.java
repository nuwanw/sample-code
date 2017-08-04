package org.demo;

import org.apache.synapse.MessageContext; 
import org.apache.synapse.mediators.AbstractMediator;

public class MyClassMediator extends AbstractMediator { 

	public boolean mediate(MessageContext context) { 
		// TODO Implement your mediation logic here 
		return true;
	}
}
