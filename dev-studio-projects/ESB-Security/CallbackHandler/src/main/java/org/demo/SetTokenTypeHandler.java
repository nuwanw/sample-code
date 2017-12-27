package org.demo;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.xpath.AXIOMXPath;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.handlers.AbstractHandler;
import org.jaxen.JaxenException;

import java.util.List;

/**
 * To avoid.
 * org.apache.axis2.AxisFault: Error in adding token into store.
 * org.apache.ws.security.WSSecurityException: An invalid security token was provided (Bad TokenType "")
 * <p>
 * <wsse:SecurityTokenReference
 * xmlns:wsu="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd"
 * wsu:Id="STRId-5C264936DBA85C9F91146614733766082"
 * xmlns:wsse11='http://docs.oasis-open.org/wss/oasis-wss-wssecurity-secext-1.1.xsd'
 * wsse11:TokenType='http://docs.oasis-open.org/wss/oasis-wss-soap-message-security-1.1#EncryptedKey'>
 * <wsse:KeyIdentifier
 * EncodingType="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary"
 * ValueType="http://docs.oasis-open.org/wss/oasis-wss-soap-message-security-1.1#EncryptedKeySHA1">
 * ekLfmUNY3Rne2Q1YQLhLQJhu7KA=
 * </wsse:KeyIdentifier>
 * </wsse:SecurityTokenReference>
 * <p>
 * <phase name="PreSecurity">
 * <handler name="MyOutHandler" class="org.demo.SetTokenTypeHandler"/>
 * </phase>
 */
public class SetTokenTypeHandler extends AbstractHandler {

    public InvocationResponse invoke(MessageContext ctx) throws AxisFault {
        SOAPEnvelope msgEnvelope = ctx.getEnvelope();
        OMNamespace ns = msgEnvelope.getOMFactory()
                .createOMNamespace("http://docs.oasis-open.org/wss/oasis-wss-wssecurity-secext-1.1.xsd", "wsse11");
        try {
            AXIOMXPath xpath = new AXIOMXPath("//wsse:SecurityTokenReference");
            xpath.addNamespace("wsse",
                    "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd");
            List<OMElement> securityTokenReferenceList = xpath.selectNodes(msgEnvelope);
            for (OMElement securityTokenReference : securityTokenReferenceList) {
                if (securityTokenReference != null) {
                    securityTokenReference.addAttribute("TokenType",
                            "http://docs.oasis-open.org/wss/oasis-wss-soap-message-security-1.1#EncryptedKey", ns);
                }
            }
        } catch (JaxenException e) {
            //ignore
        }

        return InvocationResponse.CONTINUE;
    }
}