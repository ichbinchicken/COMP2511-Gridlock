import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.*;

import org.junit.Test;
@Deprecated
@SuppressWarnings("unused")
public class SearchTest {

		@Test

		public void SizeTest() {
			Puzzle p=null;
			int moves=0;
			int i=0;
			while(i<100) {
				while(moves<20) {
					p = new Puzzle(7,10, true);
					moves = p.getInitMoves();
					//System.out.println(moves);
				}
				System.out.println(p.getInitMoves());
				System.out.println(p.GetBoard());
				p.printBoard();
				i++;
				moves = 0;
			}
		}

}
