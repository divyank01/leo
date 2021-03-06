/**
 *  Leo is an open source framework for REST APIs.
 *
 *  Copyright (C) 2016  @author Divyank Sharma
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *  
 *  For details please read LICENSE file.
 *  
 *  In Addition to it if you find any bugs or encounter any issue you need to notify me.
 *  I appreciate any suggestions to improve it.
 *  @mailto: divyank01@gmail.com
 */
package org.leo.rest.auth;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class Crypto {

	private Cipher ecipher;
	private Cipher dcipher;
	private SecretKey key;
	private DESKeySpec spec;
	private SecretKeyFactory factory;
	private Base64.Encoder encoder=Base64.getMimeEncoder();
	private Base64.Decoder decoder=Base64.getMimeDecoder();
	
	private static Crypto crypto;
	private Crypto(){}
	protected static Crypto getCrypto(){
		if(crypto==null)
			crypto=new Crypto();
		return crypto;
	}
	
	
	protected void init(String fileAdr) throws Exception {
		FileReader fr=null;
		BufferedReader br=null;
		try{
			File file=new File(Thread.currentThread().
					getContextClassLoader()
					.getResource(fileAdr).toURI());
			fr=new FileReader(file);
			br=new BufferedReader(fr);
			factory= SecretKeyFactory.getInstance("DES");
			String val=br.readLine();
			spec= new DESKeySpec(val.getBytes());
			key=factory.generateSecret(spec);
			ecipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
			dcipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
			ecipher.init(Cipher.ENCRYPT_MODE, key);
			dcipher.init(Cipher.DECRYPT_MODE, key);
		}finally{
			fr.close();
			br.close();
		}
	}
	
	protected String encrypt(String str) throws Exception {
		byte[] utf8 = str.getBytes("UTF8");
		byte[] enc = ecipher.doFinal(utf8);
		return encoder.encodeToString(enc);
	}

	protected String decrypt(String str) throws Exception {
		byte[] dec = decoder.decode(str);
		byte[] utf8 = dcipher.doFinal(dec);
		return new String(utf8, "UTF8");
	}
	/**
	 * Testing function
	 * @param args
	 */
	public static void main(String... args){
		Crypto c=new Crypto();
		try {
			c.init("keyFile");
			System.out.println(c.encrypt("sjrhgwuivbsuegfivsdbfg"));
			System.out.println(c.decrypt(c.encrypt("Is this the new shit..")));
			System.out.println(c.decrypt("6xAuysEj7vWV+oy2qZqP6wXKTzCKKompmXfpbwWtk9sx2K6wyUWvtA=="));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
