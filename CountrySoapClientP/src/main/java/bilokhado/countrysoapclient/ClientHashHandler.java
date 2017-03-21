package bilokhado.countrysoapclient;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Set;

/**
 * Class represents client-side handler which signs every SOAP request to the service with the key provided
 */
public class ClientHashHandler implements SOAPHandler<SOAPMessageContext> {

    private byte[] keyBytes;

    public ClientHashHandler(String key) {
        this.keyBytes = getBytes(key);
    }

    private byte[] getBytes(String data) {
        if (data == null)
            return new byte[0];
        try {
            return data.getBytes("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            throw new IllegalStateException("UTF-8 encoding is needed, but not supported");
        }
    }

    @Override
    public Set<QName> getHeaders() {
        return null;
    }

    @Override
    public boolean handleMessage(SOAPMessageContext context) {
        Boolean directionIsOut = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        if (directionIsOut) {
            signMessage(context.getMessage());
        }
        return true;
    }

    private void signMessage(SOAPMessage message) {
        try {
            SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();
            SOAPHeader header = envelope.getHeader();
            if (header == null)
                header = envelope.addHeader();
            QName signatureWrapper = new QName("http://localhost", "auth");
            header.addHeaderElement(signatureWrapper);
            String timestamp = Long.toString(System.currentTimeMillis() / 1000L);
            String signature = getSignature(timestamp, keyBytes);
            Node node = header.getFirstChild();
            append(node, "Timestamp", timestamp);
            append(node, "Signature", signature);
        } catch (SOAPException ex) {
            throw new IllegalStateException("Unable to add signature", ex);
        }
    }

    private String getSignature(String data, byte[] keyBytes) {
        try {
            byte[] bytesToSign = getBytes(data);
            Mac signer = Mac.getInstance("HmacSHA256");
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "HmacSHA256");
            signer.init(keySpec);
            signer.update(bytesToSign);
            byte[] signatureBytes = signer.doFinal();
            return Base64.getEncoder().encodeToString(signatureBytes);
        } catch (NoSuchAlgorithmException | InvalidKeyException ex) {
            throw new IllegalStateException("Unable to sign", ex);
        }
    }

    private void append(Node node, String elementName, String elementText) {
        Element element = node.getOwnerDocument().createElement(elementName);
        element.setTextContent(elementText);
        node.appendChild(element);
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {
        return true;
    }

    @Override
    public void close(MessageContext context) {
    }

}
