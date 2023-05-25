package com.example.ibookApp.functions;
import android.util.Base64;

import com.example.ibookApp.DTOs.UsuarioDTO;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Utils {
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+-=[]{}|;:,.<>/?";
    public static SecretKey generateKey()
            throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        byte[] keyBytes = { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F };
        SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");
        return secretKey;
    }

    public static byte[] encryptMsg(String message, SecretKey secret) throws NoSuchPaddingException, NoSuchAlgorithmException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidKeySpecException
    {
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        byte[] cipherText = cipher.doFinal(message.getBytes("UTF-8"));
        return cipherText;
    }

    public static String decryptMsg(byte[] cipherText, SecretKey secret)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException
    {
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secret);
        String decryptString = new String(cipher.doFinal(cipherText), "UTF-8");
        return decryptString;
    }

    public static String bytesToString(byte[] bytes) {
        String encodedString = Base64.encodeToString(bytes, Base64.DEFAULT);
        return encodedString;
    }

    public static void enviarEmail(String destinatario, String novaSenha) {
        /*try {
            Email email = new SimpleEmail();
            email.setHostName("smtp.gmail.com");
            email.setSmtpPort(587);
            email.setAuthenticator(new DefaultAuthenticator("suporteibookoficial@gmail.com", "pid4ibook"));
            email.setStartTLSEnabled(true);
            email.setFrom("suporteibookoficial@gmail.com");
            email.addTo(destinatario);
            email.setSubject("Recuperação de senha iBook");
            email.setMsg("Olá, <br> Sua nova senha de acesso ao App iBook é: " + novaSenha + ".");
            email.send();
        } catch (EmailException e) {
            throw new RuntimeException(e);
        }*/
    }

    public static String gerarSenha() {
        Random random = new Random();
        StringBuilder senha = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(CHARACTERS.length());
            senha.append(CHARACTERS.charAt(index));
        }

        return senha.toString();
    }
    public static void logout(){
        UsuarioDTO usuario = null;
        UserSingleton.getInstance().setUser(usuario);
    }
    public static UsuarioDTO getUser(){
        return UserSingleton.getInstance().getUser();
    }

}
