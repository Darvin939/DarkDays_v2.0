package darvin939.DarkDays.Utils;

import java.util.Random;

public class Randomizer {

	private final static Integer a = 2;
	private final static Integer b = a * 3;

	public static boolean isPercent(Integer chance) {
		int type = getType(chance);
		Random r = new Random();
		int summ = 0;
		for (int i = 0; i < type; i++) {
			int s = r.nextInt(b);
			if (s < a)
				summ += 1;
		}
		if (summ == type) {
			return true;
		}
		return false;
	}

	public static Integer getType(Integer chance) {
		int type;
		if (chance > 84)
			type = 1;
		else if (chance <= 84 && chance > 68)
			type = 2;
		else if (chance <= 68 && chance > 52)
			type = 3;
		else if (chance <= 52 && chance > 36)
			type = 4;
		else if (chance <= 36 && chance > 20)
			type = 5;
		else if (chance <= 20 && chance > 4)
			type = 6;
		else
			type = 7;
		return type;
	}

	public static Integer[] getPeriod(Integer chance) {
		int type = getType(chance);
		Integer[] out = new Integer[] { 0, 100 };
		if (type == 1)
			out = new Integer[] { 84, 16 };
		if (type == 2)
			out = new Integer[] { 68, 16 };
		if (type == 3)
			out = new Integer[] { 52, 16 };
		if (type == 4)
			out = new Integer[] { 36, 16 };
		if (type == 5)
			out = new Integer[] { 20, 16 };
		if (type == 6)
			out = new Integer[] { 4, 16 };
		if (type == 7)
			out = new Integer[] { 0, 4 };
		return out;
	}
}
