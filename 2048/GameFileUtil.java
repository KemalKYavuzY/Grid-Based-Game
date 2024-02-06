package hw3;

import java.io.BufferedReader;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import api.Tile;

/**
 * Utility class with static methods for saving and loading game files.
 * 
 * @author Kemal Yavuz
 *
 */
public class GameFileUtil {
	/**
	 * Saves the current game state to a file at the given file path.
	 * <p>
	 * The format of the file is one line of game data followed by multiple lines of
	 * game grid. The first line contains the: width, height, minimum tile level,
	 * maximum tile level, and score. The grid is represented by tile levels. The
	 * conversion to tile values is 2^level, for example, 1 is 2, 2 is 4, 3 is 8, 4
	 * is 16, etc. The following is an example:
	 * 
	 * <pre>
	 * 5 8 1 4 100
	 * 1 1 2 3 1
	 * 2 3 3 1 3
	 * 3 3 1 2 2
	 * 3 1 1 3 1
	 * 2 1 3 1 2
	 * 2 1 1 3 1
	 * 4 1 3 1 1
	 * 1 3 3 3 3
	 * </pre>
	 * 
	 * @param filePath the path of the file to save
	 * @param game     the game to save
	 */
	public static void save(String filePath, ConnectGame game) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
			
			//Writes the current game state in the intended format.
	        writer.write(game.getGrid().getWidth() + " " + game.getGrid().getHeight() + " " +
	                     game.getMinTileLevel() + " " + game.getMaxTileLevel() + " " +
	                     game.getScore());
	        writer.write('\n');
	        
	        for (int i = 0; i < game.getGrid().getHeight(); i++) {
	        	for (int j = 0; j < game.getGrid().getWidth(); j++) {
	        		Tile tile = game.getGrid().getTile(j, i);
	        		
	        			if (j == game.getGrid().getWidth() - 1) {				  //If it's at the last element in that array it doesn't write an empty space.
	        				writer.write(tile.getLevel() + "");		   			 //It's just creates a empty string.
	        			} else if (j != game.getGrid().getWidth() - 1) {		//If it's not at the last element in that array it writes empty spaces.
	        				writer.write(tile.getLevel() + " ");
	        			}
	        		
	        	}
	        	
	        	if (i != game.getGrid().getHeight() - 1) {		//It goes into new line only if it's not in the last line.
	        		writer.write('\n');
        		} 
	        	
	        }

			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Loads the file at the given file path into the given game object. When the
	 * method returns the game object has been modified to represent the loaded
	 * game.
	 * <p>
	 * See the save() method for the specification of the file format.
	 * 
	 * @param filePath the path of the file to load
	 * @param game     the game to modify
	 */
	public static void load(String filePath, ConnectGame game) {
		try {
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		String[] read = reader.readLine().split(" ");		  		 //Reads the first line from the filePath and splits the line into array elements at the empty space points.
		int width = Integer.parseInt(read[0]);			         	//Sets the first element in that line to width.
		int height = Integer.parseInt(read[1]);					   //Sets the second element in that line to height.
		Grid grid = new Grid(width, height);			   		  //Creates the grid using values of the height and width.
		game.setMinTileLevel(Integer.parseInt(read[2]));  		 //Sets the third element in that line to minimum tile level.
		game.setMaxTileLevel(Integer.parseInt(read[3])); 		//Sets the fourth element in that line to maximum tile level.
		game.setScore(Long.parseLong(read[4]));			   	   //Sets the fifth element in that line to score.
		
			for (int i = 0; i < height; i++) {
				String[] nextRead = reader.readLine().split(" ");					   //Reads the next line and splits the line into array elements.
				for (int j =0; j < width; j++) {								      //For loops goes into every location and updates it.
					Tile tempTile = new Tile(Integer.parseInt(nextRead[j]));		 //Creates a new Tile object with level value of the next element in the array.
					grid.setTile(tempTile, j, i);									//Puts it into the grid at that location.
				}
			}

			game.setGrid(grid);		//Updates the gird.
	        reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
