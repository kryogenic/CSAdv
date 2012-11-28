package org.kryogenic.csadv.java2.labs;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class IsoTriangle {
	public static void main(final String... args) {
		System.out.print("Enter the desired triangle height: ");
		int h = -1; // height
		try {
			// read the string from the input
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			String input = reader.readLine();
			// remove anything that isn't a number from the string
			String sanitizedInput = "";
			char[] numbers = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
			for(char c : input.toCharArray()) {
				boolean isNumber = false;
				for(char num : numbers) {
					if(c == num) {
						isNumber = true;
						break;
					}
				}
				if(isNumber)
					sanitizedInput += c;
			}
			// assign 'h'
			h = Integer.parseInt(sanitizedInput.length() == 0 ?  "0" : sanitizedInput);
		} catch (IOException e) {
			System.out.println("Cannot read from System.in");
			System.exit(1);
		}
		// create triangle
		for(int i = 1; i <= h; i++) {
			printSpaces(h - i);
			printStars(i * 2 - 1);
			System.out.println();
		}
	}
	
	public static void printSpaces(int num) {
		for(int i = 0; i < num; i++) {
			System.out.print(" ");
		}
	}
	
	public static void printStars(int num) {
		for(int i = 0; i < num; i++) {
			System.out.print("*");
		}
	}
}
