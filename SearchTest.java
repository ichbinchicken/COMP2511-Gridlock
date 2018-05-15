import static org.junit.Assert.*;

import org.junit.Test;

@SuppressWarnings("unused")
public class SearchTest {

	@Test
	public void testSearchBoard() {
		BoardState state = new BoardState(6);
		Integer[] array1 = {21,21,21,10,15,18,
							 1,22,22,10,15,18,
							 1, 0,34,34,15,18,
							25,25, 7, 0, 0, 0,
							 0, 4, 7, 0,28,28,
							 0, 4,31,31,32,32};
		
		Integer[] array4 = {21,21,21, 1, 3, 3,
							01,22,22, 1, 3, 3,
							01,00,34,34, 3, 3,
							22,22, 1, 0, 0, 0,
							00,01, 1, 0,22,22,
							00,01,22,22,22,22};
		
		Integer[] array2 ={	19,19,0,0,0,18,
							3,0,0,12,0,18,
							3,34,34,12,0,18,
							3,0,0,12,0,0,
							1,0,0,0,28,28,
							1,0,33,33,33,0};
		
		Integer[] array3 ={	01,19,19,21,21,21,
							01,22,22,23,23,23,
							00,34,34,12,15,18,
							00,00,00,12,15,18,
							00,00,00,12,15,18,
							00,31,31,33,33,33 };
		
		Integer[] gen1 = { 	01,04,21,21,21,00,
							01,04,22,22,00,00,
							00,00,00,00,34,34,
							25,25,07,10,15,18,
							28,28,07,10,15,18,
							31,31,32,32,15,18};

		Search search = new Search();

		
		//state.GivenBoard(array4);
		///state.GivenBoard(array2);
		//state.GivenBoard(array3);
		//search.SearchBoard(state);

		
		state.GivenBoard(gen1);
		search.GenBoard(state, 50);
		
	}

}
