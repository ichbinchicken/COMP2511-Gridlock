import java.util.ArrayList;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
	private static final int  EMPTY=0;
	private static final int  GOALCAR=5;
	private static final int  HORCAR=4;
	private static final int  HORTRUCK=3;
	private static final int  VERCAR=1;
	private static final int  VERTRUCK=2;

	
	
    public Main() {

		

    	
    	
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Screen screen = new BoardScreen(primaryStage);
        screen.show();
    }

    public static void main(String[] args) {
        launch(args);

    }
    

}
