import java.util.ArrayList;
import java.util.List;

public class Utils {
	/*
	 DESCRIPTION: util class
	 HAVE: Vt-set | Vn-set | opg-matrix
	 */
	ArrayList<Character> Vn;
	ArrayList<Character> Vt;
	ArrayList<Character> sym;
	// ArrayList<Pair<String, Character>> spec;
	int[][] opgMatrix;

	public Utils() {
		this.Vn = new ArrayList<Character>(List.of('E', 'T', 'F'));
		this.Vt = new ArrayList<Character>(List.of('+', '*', '(', ')', 'i', '#'));
		this.sym = new ArrayList<Character>(List.of('+', '*', 'i', '(', ')', '#'));

		/* fill the opg matrix */
		/**
		 * @tips
		 * > 	-- 1
		 * < 	-- 2
		 * = 	-- 3
		 * err 	-- 0
		 *
		 * + -- 0
		 * * -- 1
		 * i -- 2
		 * ( -- 3
		 * ) -- 4
		 * # -- 5
		 *
		 * 		+	*	i	(	)	#
		 * 	+	>	<	<	<	<	>
		 * 	*	>	>	<	<	>	>
		 * 	i	>	>			>	>
		 * 	(	<	<	<	<	=
		 * 	)	>	>			>	>
		 * 	#	<	<	<	<
		 *
		 *
		 *  E -> E + T
		 *  E -> T
		 *  T -> T * F
		 *  T -> F
		 *  F -> (E)
		 *  F -> i
		 */
		this.opgMatrix = new int[][]{
				{1, 2, 2, 2, 2, 1},
				{1, 1, 2, 2, 1, 1},
				{1, 1, 0, 0, 1, 1},
				{2, 2, 2, 2, 3, 0},
				{1, 1, 0, 0, 1, 1},
				{2, 2, 2, 2, 0, 0}
		};

//		this.spec = new ArrayList<Pair<String, Character>>(List.of(
//				new Pair<>("E+E", 'E'),
//				new Pair<>("E*E", 'E'),
//				new Pair<>("(E)", 'E'),
//				new Pair<>("i",	  'E')
//		));
	}

	public int symIndex(Character c) {
		return this.sym.indexOf(c);
	}

	public boolean isTerminal(Character c) {
		if (this.Vt.indexOf(c) >= 0) return true;
		return false;
	}

	public int result(Character c1, Character c2) {
		int x = this.symIndex(c1);
		int y = this.symIndex(c2);
		return this.opgMatrix[x][y];
	}
}
