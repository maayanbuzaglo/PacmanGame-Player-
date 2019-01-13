package Main;
import java.util.Random;
/**
 * this class represents a naive symmetric encryption algorithm.
 * DO NOT use it for real encryption it is much too weak for educational use only!!
 * @author boaz
 */
public class Targil
{
	public static int KEY = 12345678;
	public static void main(String[] a) {
		simple_test0();
	}
	/* DO NOT change! use this code for all encryption / decryption tasks in Ex1. */
	public static String enc(String msg, int key)
	{
		String ans = "";
		Random rand = new Random(key);
		for(int i=0; i<msg.length(); i=i+1) {
			char c = msg.charAt(i);
			int s = c;
			int rd = rand.nextInt()%(256*256);
			int s2 = s^rd;
			char c2 = (char)(s2);
			ans +=c2;
		}
		return ans;
	}
	public static void simple_test0()
	{
		String msg = "1234567890 עברית abc";
		String msg_enc = enc(msg,KEY);
		String msg_dec = enc(msg_enc, KEY);
		System.out.println("Orig: "+msg);
		System.out.println("Enc: "+msg_enc);
		System.out.println("Dec: "+msg_dec);
	}
}
/** run example:
	Orig: 1234567890 עברית abc
	Enc: 
	Dec: 1234567890 עברית abc
 **/
