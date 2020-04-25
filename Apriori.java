package Apriori;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

public class Apriori {

	public static void main(String[] args) {

		String[] transactions = { "ABE", "BD", "BC", "ABD", "AC", "BC", "AC", "ABCE", "ABC" };
		int minSup = 2;

		HashMap<String, Integer> c = new HashMap<>();
		for (int i = 0; i < transactions.length; i++) {
			String tid = transactions[i];
			for (int j = 0; j < tid.length(); j++) {
				char ch = tid.charAt(j);
				if (c.containsKey(ch + "")) {
					int val = c.get(ch + "");
					c.put(ch + "", ++val);
				} else {
					c.put(ch + "", 1);
				}
			}
		}

		System.out.println("C1 => " + c);

		HashMap<String, Integer> l = generate_frequent_set(transactions, minSup, c);
		System.out.println("L1 => " + l);

		int counter = 2;
		while (c.size() > 0) {
			c = join(l);
			System.out.println("C" + counter + " (by joining) L" + (counter - 1) + " => " + c);

			prune(c, l);
			System.out.println("C" + counter + " (by pruning) => " + c);

			generate_Candidate(c, transactions);
			System.out.println("C" + counter + " (with support count) => " + c);

			l = generate_frequent_set(transactions, minSup, c);
			System.out.println("L" + counter + " => " + l);
			counter++;
		}

	}

	public static void generate_Candidate(HashMap<String, Integer> c, String[] transactions) {
		ArrayList<String> itemsInC = new ArrayList<>(c.keySet());
		for (int i = 0; i < transactions.length; i++) {
			String tid = transactions[i];
			for (int j = 0; j < itemsInC.size(); j++) {
				String item = itemsInC.get(j);
				int count = 0;
				for (int k = 0; k < item.length(); k++) {
					String atkthpos = item.charAt(k) + "";
					if (tid.contains(atkthpos)) {
						count++;
					}
				}

				if (count == item.length()) {
					int val = c.get(item);
					c.put(item, ++val);
				}
			}

		}
	}

	public static HashMap<String, Integer> join(HashMap<String, Integer> l) {
		HashMap<String, Integer> c = new HashMap<>();
		ArrayList<String> itemInL = new ArrayList<>(l.keySet());

		for (int i = 0; i < itemInL.size(); i++) {
			for (int j = i + 1; j < itemInL.size(); j++) {
				String forchecki = itemInL.get(i).substring(0, itemInL.get(i).length() - 1);
				String forcheckj = itemInL.get(j).substring(0, itemInL.get(j).length() - 1);
				if (forchecki.equals(forcheckj)) {
					String newPotentialCandidate = itemInL.get(i) + itemInL.get(j).charAt(itemInL.get(j).length() - 1);
					c.put(newPotentialCandidate, 0);
				}
			}
		}

		return c;
	}

	public static HashMap<String, Integer> generate_frequent_set(String[] transactions, int minSup,
			HashMap<String, Integer> c) {
		HashMap<String, Integer> l = new HashMap<>();
		ArrayList<String> list = new ArrayList<>(c.keySet());
		for (int i = 0; i < list.size(); i++) {
			int support = c.get(list.get(i));
			if (support >= minSup) {
				l.put(list.get(i), support);
			}
		}

		return l;
	}

	public static HashMap<String, Integer> prune(HashMap<String, Integer> c, HashMap<String, Integer> l) {
		ArrayList<String> itemsInC = new ArrayList<>(c.keySet());
		ArrayList<String> llist = new ArrayList<>(l.keySet());

		for (int i = 0; i < itemsInC.size(); i++) {
			String itemC = itemsInC.get(i);
			String itemCstr1 = itemC.substring(0, itemC.length() - 2);
			String itemCstr2 = itemC.substring(itemC.length() - 2);

			String bin1s = "";

			for (int j = 0; j < itemCstr1.length(); j++) {
				bin1s += "1";
			}

			String bitmask = "1";

			ArrayList<String> list = new ArrayList<>();
			for (int j = 0; j < itemCstr1.length(); j++) {
				int val = Integer.parseInt(bin1s) - Integer.parseInt(bitmask);
				String sval = Integer.toString(val);
				if (sval.length() < bin1s.length()) {
					sval = "0" + sval;
				}
				String potentialCandidate = "";
				for (int k = 0; k < sval.length(); k++) {
					char atkthpos = sval.charAt(k);
					if (atkthpos == '1') {
						potentialCandidate = potentialCandidate + "" + atkthpos + "";
					}
				}

				list.add(potentialCandidate + itemCstr2);
				int forbitmask = Integer.parseInt(bitmask);
				forbitmask <<= 1;
				bitmask = Integer.toBinaryString(forbitmask);
			}

			for (int h = 0; h < list.size(); h++) {
				String tobeChecked = list.get(h);
				if (!l.containsKey(tobeChecked)) {
					c.remove(itemC);
				}

			}

		}

		return c;
	}

	// C1 => {A=6, B=7, C=6, D=2, E=2}
	// L1 => {A=6, B=7, C=6, D=2, E=2}
	// C2(w.o. count)=> {AB=0, BC=0, CD=0, DE=0, AC=0, BD=0, CE=0, AD=0, BE=0,
	// AE=0}
	// C2 => {AB=4, BC=4, CD=0, DE=0, AC=4, BD=2, CE=1, AD=1, BE=2, AE=2}
	// L2 => {AB=4, BC=4, AC=4, BD=2, BE=2, AE=2}
	// C3(w.o. count)=> {ABC=0, BCD=0, BDE=0, ACE=0, ABE=0, BCE=0}
	// C3 => {ABC=2, ABE=2}
	// L3 => {ABC=2, ABE=2}
	// C4(w.o. count)=> {ABCE=0}
	// C4 => {}
	// L4 => {}

}
