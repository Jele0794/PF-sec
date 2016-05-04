package com.edmundo.pfseguridad.AES;

/**
 * Created by Edmundo on 26/04/16.
 */
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AESEncriptacion {
    private final String ALGORITMO = "AES";//algoritmo (si cambia la imprementacion tambien)
    private final int LONGITUD = 128;//longitud de la llave ()
    private final String CODIFICACION = "ASCII";//como se convertira a byte, esto sera mas adelante
    private AESKey aesKey;

    public AESEncriptacion(AESKey aesKey) {
        this.aesKey = aesKey;
    }

    public AESKey generaKey() {
        KeyGenerator kgen = null;
        try {
            kgen = KeyGenerator.getInstance(ALGORITMO);
        } catch (NoSuchAlgorithmException ex) {
            error(ex);
        }
        kgen.init(LONGITUD);
        SecretKey skey = kgen.generateKey();
        aesKey = new AESKey();
        System.out.println(skey.getEncoded());
        aesKey.setEncoded(HexToString(skey.getEncoded()));
        System.out.println(HexToString(skey.getEncoded()));
        System.out.println(aesKey.getEncoded());
        return aesKey;
    }

    public String encripta(String cadena) {
        String encriptado = null;
        try {
            byte[] raw = StringToHex(aesKey.getEncoded());
            SecretKeySpec skeySpec = new SecretKeySpec(raw, ALGORITMO);
            Cipher cipher = Cipher.getInstance(ALGORITMO);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(cadena.getBytes(CODIFICACION));
            encriptado = HexToString(encrypted);

        } catch (IllegalBlockSizeException ex) {
            error(ex);
        } catch (BadPaddingException ex) {
            error(ex);
        } catch (UnsupportedEncodingException ex) {
            error(ex);
        } catch (InvalidKeyException ex) {
            error(ex);
        } catch (NoSuchAlgorithmException ex) {
            error(ex);
        } catch (NoSuchPaddingException ex) {
            error(ex);
        }
        return encriptado;
    }

    private String HexToString(byte[] arregloEncriptado) {
        String textoEncriptado = "";
        for (int i = 0; i < arregloEncriptado.length; i++) {
            int aux = arregloEncriptado[i] & 0xff;
            if (aux < 16) {
                textoEncriptado = textoEncriptado.concat("0");
            }
            textoEncriptado = textoEncriptado.concat(Integer.toHexString(aux));
        }
        return textoEncriptado;
    }

    private byte[] StringToHex(String encriptado) {
        byte[] enBytes = new byte[encriptado.length() / 2];
        for (int i = 0; i < enBytes.length; i++) {
            int index = i * 2;
            String aux = encriptado.substring(index, index + 2);
            int v = Integer.parseInt(aux, 16);
            enBytes[i] = (byte) v;
        }
        return enBytes;
    }

    public String desencriptar(String encriptado) {
        String originalString = null;
        try {
            byte[] raw = StringToHex(aesKey.getEncoded());
            SecretKeySpec skeySpec = new SecretKeySpec(raw, ALGORITMO);
            Cipher cipher = Cipher.getInstance(ALGORITMO);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] original = cipher.doFinal(StringToHex(encriptado));
            originalString = new String(original);

        } catch (IllegalBlockSizeException ex) {
            error(ex);
        } catch (BadPaddingException ex) {
            error(ex);
        } catch (InvalidKeyException ex) {
            error(ex);
        } catch (NoSuchAlgorithmException ex) {
            error(ex);
        } catch (NoSuchPaddingException ex) {
            error(ex);
        }
        return originalString;
    }
    private void error(Exception ex) {
        System.err.print(ex.getMessage());
    }

}
