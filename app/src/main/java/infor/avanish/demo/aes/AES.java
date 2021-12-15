package infor.avanish.demo.aes;// Java program to demonstrate the creation
// of Encryption and Decryption with Java AES
import android.os.Build;

import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class AES {
	// Class private variables
	private static final String SECRET_KEY = "avanishhhhhhhhhhhhhhhhh2021";
	
	private static final String SALT = "ssshhhhhhhhhhh!!!!";

	// This method use to encrypt to string
	public static byte[] encrypt(byte[] byteToEncrypt)
	{
		try {

			// Create default byte array
			byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0,
						0, 0, 0, 0, 0, 0, 0, 0 };
			IvParameterSpec ivspec
				= new IvParameterSpec(iv);

			// Create SecretKeyFactory object
			SecretKeyFactory factory
				= SecretKeyFactory.getInstance(
					"PBKDF2WithHmacSHA256");
			
			// Create KeySpec object and assign with
			// constructor
			KeySpec spec = new PBEKeySpec(
				SECRET_KEY.toCharArray(), SALT.getBytes(),
				65536, 256);
			SecretKey tmp = factory.generateSecret(spec);
			SecretKeySpec secretKey = new SecretKeySpec(
				tmp.getEncoded(), "AES");

			Cipher cipher = Cipher.getInstance(
				"AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey,
						ivspec);
			// Return encrypted string
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
				return Base64.getEncoder().encode(
					cipher.doFinal(byteToEncrypt));
			}
		}
		catch (Exception e) {
			System.out.println("Error while encrypting: "
							+ e.toString());
		}
		return null;
	}

	// This method use to decrypt to string
	public static byte[] decrypt(byte[] byteToDecrypt)
	{
		try {

			// Default byte array
			byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0,
						0, 0, 0, 0, 0, 0, 0, 0 };
			// Create IvParameterSpec object and assign with
			// constructor
			IvParameterSpec ivspec
				= new IvParameterSpec(iv);

			// Create SecretKeyFactory Object
			SecretKeyFactory factory
				= SecretKeyFactory.getInstance(
					"PBKDF2WithHmacSHA256");

			// Create KeySpec object and assign with
			// constructor
			KeySpec spec = new PBEKeySpec(
				SECRET_KEY.toCharArray(), SALT.getBytes(),
				65536, 256);
			SecretKey tmp = factory.generateSecret(spec);
			SecretKeySpec secretKey = new SecretKeySpec(
				tmp.getEncoded(), "AES");

			Cipher cipher = Cipher.getInstance(
				"AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, secretKey,
						ivspec);
			// Return decrypted string
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
				return cipher.doFinal(
					Base64.getDecoder().decode(byteToDecrypt));
			}
		}
		catch (Exception e) {
			System.out.println("Error while decrypting: "
							+ e.toString());
		}
		return null;
	}
}

