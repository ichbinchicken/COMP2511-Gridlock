import static org.junit.Assert.*;

import org.junit.Test;

public class SearchTest {

	@Test
	public void testSearchBoard() {
		BoardState state = new BoardState(6);
		//Integer[] array = {21,21,21,10,15,18,1,22,22,10,15,18,1,0,34,34,15,18,25,25,7,0,0,0,0,4,7,0,28,28,0,4,31,31,32,32};
		
		Integer[] array = {	19,19,0,0,0,18,
							3,0,0,12,0,18,
							3,34,34,12,0,18,
							3,0,0,12,0,0,
							1,0,0,0,28,28,
							1,0,33,33,33,0};

		state.GivenBoard(array);
		Search search = new Search();
		search.SearchBoard(state);
		fail("Not yet implemented");
	}

}
