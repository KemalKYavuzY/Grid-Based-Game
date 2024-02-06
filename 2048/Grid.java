package GridBasedGame;

import api.Tile;

/**
 * Represents the game's grid.
 * 
 * @author Kemal Yavuz
 *
 */
public class Grid {
	
	private int width; 		//Instance variable for width of the board.
	
	private int height;		//Instance variable for height of the board.
	
	private Tile[][] grid;	//Creates the game board that will contain Tile objects
	
	/**
	 * Creates a new grid.
	 * 
	 * @param width  number of columns
	 * @param height number of rows
	 */
	public Grid(int width, int height) {
		this.width = width;						  //Sets the number of the columns in the gird.
		this.height = height;					 //Sets the number of the rows in the gird.
		this.grid = new Tile[height][width];	//Sets the size of the game board.
		
	}

	/**
	 * Get the grid's width.
	 * 
	 * @return width
	 */
	public int getWidth() {
		
		return width;
	}

	/**
	 * Get the grid's height.
	 * 
	 * @return height
	 */
	public int getHeight() {
		
		return height;
	}

	/**
	 * Gets the tile for the given column and row.
	 * 
	 * @param x the column
	 * @param y the row
	 * @return the tile object
	 */
	public Tile getTile(int x, int y) {
		
		return grid[y][x];		//Returns the Tile object at the parameter cordinates.
	}

	/**
	 * Sets the tile for the given column and row. Calls tile.setLocation().
	 * 
	 * @param tile the tile to set
	 * @param x    the column
	 * @param y    the row
	 */
	public void setTile(Tile tile, int x, int y) {
		tile.setLocation(x, y);		 //Calls the setLocation method from the Tile Class and sets the object's location to the parameter cordinates.
		grid[y][x] = tile;			//Puts the created Tile object to the parameter cordinates in the grid.
	}
	
	@Override
	public String toString() {
		String str = "";
		for (int y=0; y<getHeight(); y++) {
			if (y > 0) {
				str += "\n";
			}
			str += "[";
			for (int x=0; x<getWidth(); x++) {
				if (x > 0) {
					str += ",";
				}
				str += getTile(x, y);
			}
			str += "]";
		}
		return str;
	}
}
