package hw3;

import java.util.ArrayList;
import java.util.Random;

import api.ScoreUpdateListener;
import api.ShowDialogListener;
import api.Tile;

/**
 * Class that models a game.
 * 
 * @author Kemal Yavuz
 *
 */
public class ConnectGame {
	private ShowDialogListener dialogListener;
	
	private ScoreUpdateListener scoreListener;
	
	private int width; 		//Instance variable for width of the board.
	
	private int height; 	//Instance variable for height of the board.
	
	private int min; 		//Instance variable for the minimum tile level.
	
	private int max; 		//Instance variable for the maximum tile level.
	
	private int randNum;	//Instance variable for a new random number.
	
	private long Score;		//Instance variable to keep track of the score.
	
	private Grid Grid; 		//Declares the Grid object to create it in the constructor.
	
	private ArrayList<Tile> SelectedTiles = new ArrayList<Tile>();		//Created an ArrayList to store all the Tiles.
	
	private Tile tile;		//Created a Tile object to keep track of the current selection.
	
	private boolean isStarted = false;		//Created a boolean variable to check if the first selection is made or not.
	
	private boolean emptyArray = false;		//A boolean variable that becomes true when we should empty the array.
	
	/**
	 * Constructs a new ConnectGame object with given grid dimensions and minimum
	 * and maximum tile levels.
	 * 
	 * @param width  grid width
	 * @param height grid height
	 * @param min    minimum tile level
	 * @param max    maximum tile level
	 * @param rand   random number generator
	 */
	public ConnectGame(int width, int height, int min, int max, Random rand) {
		this.width = width;									//Sets the grid width.
		this.height = height; 							   //Sets the grid height.
		this.min = min; 							      //Sets the minimum tile level.
		this.max = max; 							     //Sets the maximum tile level.
		Grid = new Grid(width, height); 			    //Creates the Grid object using the width and height parameters.
	}

	/**
	 * Gets a random tile with level between minimum tile level inclusive and
	 * maximum tile level exclusive. For example, if minimum is 1 and maximum is 4,
	 * the random tile can be either 1, 2, or 3.
	 * <p>
	 * DO NOT RETURN TILES WITH MAXIMUM LEVEL
	 * 
	 * @return a tile with random level between minimum inclusive and maximum
	 *         exclusive
	 */
	public Tile getRandomTile() {
		Random rand = new Random();
		randNum = rand.nextInt(max - min) + min;	//Random number between the minimum and the maximum.
		Tile randTile = new Tile(randNum); 		   //Creates a new Tile object using a random level value.
	
		return randTile;				  
	}

	/**
	 * Regenerates the grid with all random tiles produced by getRandomTile().
	 */
	public void radomizeTiles() {
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {			 //This nested for loop passes every location in the grid.
				Grid.setTile(getRandomTile(), j, i);	//Puts a random tile at that x and y location.
			}
		}
		
	}

	/**
	 * Determines if two tiles are adjacent to each other. The may be next to each
	 * other horizontally, vertically, or diagonally.
	 * 
	 * @param t1 one of the two tiles
	 * @param t2 one of the two tiles
	 * @return true if they are next to each other horizontally, vertically, or
	 *         diagonally on the grid, false otherwise
	 */
	public boolean isAdjacent(Tile t1, Tile t2) {		
		if (Math.abs(t1.getX() - t2.getX()) == 1 && Math.abs(t1.getY()- t2.getY()) == 1) {				
			return true; 	//Returns true if Tile 1 is next to Tile 2 diagonally.
		} else if (Math.abs(t1.getX() - t2.getX()) == 1 && Math.abs(t1.getY()- t2.getY()) == 0) {		
			return true; 	//Returns true if Tile 1 is next to Tile 2 horizontally.
		} else if (Math.abs(t1.getX() - t2.getX()) == 0 && Math.abs(t1.getY()- t2.getY()) == 1) {		
			return true; 	//Returns true if Tile 1 is next to Tile 2 vertically.
		} else {
			return false;	//Returns false otherwise.
		}
	}

	/**
	 * Indicates the user is trying to select (clicked on) a tile to start a new
	 * selection of tiles.
	 * <p>
	 * If a selection of tiles is already in progress, the method should do nothing
	 * and return false.
	 * <p>
	 * If a selection is not already in progress (this is the first tile selected),
	 * then start a new selection of tiles and return true.
	 * 
	 * @param x the column of the tile selected
	 * @param y the row of the tile selected
	 * @return true if this is the first tile selected, otherwise false
	 */
	public boolean tryFirstSelect(int x, int y) {
		Tile nextTile = Grid.getTile(x, y);		 	 	//Creates a tile at that given parameter cordinates.
		if (isStarted == false) {					   //Checks if the selection is started or not.
			if (nextTile.isSelected() == true) {	  
				return false;	//Returns false if the selection has been already made.
			} else  if (nextTile.isSelected() != true) {
				nextTile.setSelect(true);				   //Sets the selection to true.
				SelectedTiles.add(nextTile);			  //Adds the tile to the selected tiles list.
				tile = nextTile;						 //Updates the current tile.
				isStarted = true;						//Selection is started.
				return true;	//Returns true if it's the first selection.
			}
		}
		
		return false;
	}

	/**
	 * Indicates the user is trying to select (mouse over) a tile to add to the
	 * selected sequence of tiles. The rules of a sequence of tiles are:
	 * 
	 * <pre>
	 * 1. The first two tiles must have the same level.
	 * 2. After the first two, each tile must have the same level or one greater than the level of the previous tile.
	 * </pre>
	 * 
	 * For example, given the sequence: 1, 1, 2, 2, 2, 3. The next selected tile
	 * could be a 3 or a 4. If the use tries to select an invalid tile, the method
	 * should do nothing. If the user selects a valid tile, the tile should be added
	 * to the list of selected tiles.
	 * 
	 * @param x the column of the tile selected
	 * @param y the row of the tile selected
	 */
	public void tryContinueSelect(int x, int y) {
		Tile nextTile = Grid.getTile(x, y);		//Creates a template tile at that given parameter cordinates which will present the next tile.
		
		if (tile != null && isAdjacent(tile, nextTile) && !(nextTile.isSelected())) {		//If current selection exists and adjacent to the created tile at the parameter cordinates,
			if (SelectedTiles.size() == 1) {			       							   //And size of the selection tiles list is one, so the first two lines will have to same level,
				if (tile.getLevel() == nextTile.getLevel()) { 							  //Checks if the next tile and the current tile has the same level.
					SelectedTiles.add(nextTile);			                             //If true, adds that tile into the selected tiles list.
					nextTile.setSelect(true);											//Selects it.
					tile = nextTile;					  							   //And the next tile becomes the current tile.
				}
			} else if (SelectedTiles.size() > 1) {									//Or if the size of the selection tiles list is  larger than one, so the lines after the first two lines will have to same level or one level bigger,								
				if ((tile.getLevel() == nextTile.getLevel() || tile.getLevel() + 1 == nextTile.getLevel())) {
	                		  				     							      //Checks if the next tile and the current tile has the same level.
					SelectedTiles.add(nextTile);			                     //If true, adds that tile into the selected tiles list.
					nextTile.setSelect(true);				         			//Selects it.
					tile = nextTile;					     				   //And the next tile becomes the current tile.
				}
			}
		} else if (tile != null && isAdjacent(tile, nextTile) && nextTile.isSelected()) {		        //If the next tile location we are going is already selected,
			if (tile.getLevel() == nextTile.getLevel() || tile.getLevel() - 1 == nextTile.getLevel()){ //And the level difference is less than one.
				tile.setSelect(false);													   		      //It Unselects the current file and go backs to the older location.
				SelectedTiles.remove(tile);
				tile = nextTile;
			}
			
		}
	}

	/**
	 * Indicates the user is trying to finish selecting (click on) a sequence of
	 * tiles. If the method is not called for the last selected tile, it should do
	 * nothing and return false. Otherwise it should do the following:
	 * 
	 * <pre>
	 * 1. When the selection contains only 1 tile reset the selection and make sure all tiles selected is set to false.
	 * 2. When the selection contains more than one block:
	 *     a. Upgrade the last selected tiles with upgradeLastSelectedTile().
	 *     b. Drop all other selected tiles with dropSelected().
	 *     c. Reset the selection and make sure all tiles selected is set to false.
	 * </pre>
	 * 
	 * @param x the column of the tile selected
	 * @param y the row of the tile selected
	 * @return return false if the tile was not selected, otherwise return true
	 */
	public boolean tryFinishSelection(int x, int y) {
		Tile tempTile = Grid.getTile(x, y);		//Creates a template tile at that given parameter cordinates
		
		if (tempTile.isSelected()) {		   //If the tile is already selected, sets the boolean emptyArray variable to true,
			Score = 0;						  //So it can empty the array later because it is a wrong selection.
			emptyArray = true;				 
		}
		if (SelectedTiles.size() > 1) {												//If the selected tiles are more than one,
			if (tempTile == SelectedTiles.get(SelectedTiles.size() - 1)) {		   //And the tile that we created with the parameter cordinates and the last selected tile equals,
				upgradeLastSelectedTile();										  //Upgrades the last selected tiles.
				dropSelected();													 //Drops all other selected tiles
				for (int i = 0; i < SelectedTiles.size(); i++) {			    //Resets the selection and start calculating the score.
					Score += SelectedTiles.get(i).getValue();				   //Adds the tiles values together.
					SelectedTiles.get(i).setSelect(false);
					isStarted = false;
				}
				
				if (tempTile.getX() == SelectedTiles.get(SelectedTiles.size() - 1).getX() && tempTile.getY() == SelectedTiles.get(SelectedTiles.size() - 1).getY()) {
					Score = 0;
					emptyArray = true; //If the last selected tile and the tile that we created with the parameter cordinates have the same x and y, this is a wrong selection so we need to empty the array.
				}
				
				Score *= 2;						//Multiplied the score by 2 because the grid contains all twos and it's powers.
				if (scoreListener != null) {   //Updates the score.
					scoreListener.updateScore(getScore());
				}
				return true;
			}
		} else if (SelectedTiles.size() == 1) {										//If there is only one selected file,
			for (int i = 0; i < SelectedTiles.size(); i++) {			           //Resets the selection and start calculating the score.
				Score += SelectedTiles.get(i).getValue();				          //Adds the tiles values together.
				SelectedTiles.get(i).setSelect(false);
				isStarted = false;
			}
			
			if (tempTile.getX() == SelectedTiles.get(SelectedTiles.size() - 1).getX() && tempTile.getY() == SelectedTiles.get(SelectedTiles.size() - 1).getY()) {
				Score = 0;
				emptyArray = true; //If the last selected tile and the tile that we created with the parameter cordinates have the same x and y, this is a wrong selection so we need to empty the array.
			}
			
			Score *= 2;						//Multiplied the score by 2 because the grid contains all twos and it's powers.
			if (scoreListener != null) {   //Updates the score.
				scoreListener.updateScore(getScore());
			}
			return true;
		}

		return false;
	}

	/**
	 * Increases the level of the last selected tile by 1 and removes that tile from
	 * the list of selected tiles. The tile itself should be set to unselected.
	 * <p>
	 * If the upgrade results in a tile that is greater than the current maximum
	 * tile level, both the minimum and maximum tile level are increased by 1. A
	 * message dialog should also be displayed with the message "New block 32,
	 * removing blocks 2". Not that the message shows tile values and not levels.
	 * Display a message is performed with dialogListener.showDialog("Hello,
	 * World!");
	 */
	public void upgradeLastSelectedTile() {
		tile.setLevel(tile.getLevel() + 1);		//Increases the level of the last selected tile by 1.
		
		
		if (tile.getLevel() > max) {		//If the level of the current tile is larger than the maximum,
			max++;						   //Increases maximum and minimum by 1.
			min++;
			dialogListener.showDialog("New block " + (int)Math.pow(2, max) + ", " + "removing blocks " + (int)Math.pow(2, min));	//Displayes the intended message.
			
		}
		
		SelectedTiles.remove(tile);			   //Removes that tile from the list of selected tiles.
		tile.setSelect(false);				  //Unselects the current tile.
	}

	/**
	 * Gets the selected tiles in the form of an array. This does not mean selected
	 * tiles must be stored in this class as a array.
	 * 
	 * @return the selected tiles in the form of an array
	 */
	public Tile[] getSelectedAsArray() {
		Tile[] tiles;
		
		if (emptyArray) {											//Whenever the array is supposed to be emptied following happens,
			for (int i = SelectedTiles.size() - 1; i >= 0; i--) {  
					SelectedTiles.get(i).setSelect(false);		  //Unselectes all the tiles in our selected tiles array list.
					SelectedTiles.remove(i);					 //And removes those tiles from the list.
			}
			emptyArray = false;		                           //Sets the boolean variable back to false so the next round will function correctly.
		}
		
			tiles = new Tile[SelectedTiles.size()];				//Creates a new Tile object using the size of the array list we have.
			for (int i = 0; i < SelectedTiles.size(); i++) {   //Fills the array with the array list elements.
				tiles[i] = SelectedTiles.get(i);
			}
		
		return tiles;
	}

	/**
	 * Removes all tiles of a particular level from the grid. When a tile is
	 * removed, the tiles above it drop down one spot and a new random tile is
	 * placed at the top of the grid.
	 * 
	 * @param level the level of tile to remove
	 */
	public void dropLevel(int level) {
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {			//This nested for loop aims to check every location in the grid.
				
				Tile tempTile = Grid.getTile(j, i);		       //Creates a template tile at that given parameter cordinates.
				if (tempTile.getLevel() == level) {			  //Checks if the level of that tile at that location is equal to the parameter or not.
					for (int k = i; k > 0; k--) {			 //From that y cordinate, it drceases back to 0 to check all the tiles above the selected one.
						tempTile = Grid.getTile(j, k - 1);  //Gets the tile above the selected tile.
						Grid.setTile(tempTile, j, k);	   //Sets it to one below.
					}  

					Grid.setTile(getRandomTile(), j, 0);	//Creates a random tile at the top of that column.
				}
			}
		}
	}

	/**
	 * Removes all selected tiles from the grid. When a tile is removed, the tiles
	 * above it drop down one spot and a new random tile is placed at the top of the
	 * grid.
	 */
	public void dropSelected() {
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {			//This nested for loop aims to check every location in the grid.
				
				Tile tempTile = Grid.getTile(j, i);		       //Creates a template tile at that given parameter cordinates.
				if (tempTile.isSelected() == true) {	      //Checks if the tile at that location is selected or not.
					for (int k = i; k > 0; k--) {		     //From that y cordinate, it drceases back to 0 to check all the tiles above the selected one.
						tempTile = Grid.getTile(j, k - 1);  //Gets the tile above the selected tile.
						Grid.setTile(tempTile, j, k);	   //Sets it to one below.
					}  

					Grid.setTile(getRandomTile(), j, 0);	//Creates a random tile at the top of that column.
				}
			}
		}
	}

	/**
	 * Remove the tile from the selected tiles.
	 * 
	 * @param x column of the tile
	 * @param y row of the tile
	 */
	public void unselect(int x, int y) {
		Tile tempTile = Grid.getTile(x, y);		//Creates a template tile at that given parameter cordinates.
		
		for (int i = 0; i < SelectedTiles.size() - 1; i++) {		//Checks every element of the selected tiles array list.
			if (SelectedTiles.get(i) == tempTile) {				   //If the element at that location equals to the parameter,
				SelectedTiles.get(i).setSelect(false);			  //Unselects that tile.
				SelectedTiles.remove(i);					     //Removes it from the array list.
			}
		}
	}

	/**
	 * Gets the player's score.
	 * 
	 * @return the score
	 */
	public long getScore() {
		
		return Score;
	}

	/**
	 * Gets the game grid.
	 * 
	 * @return the grid
	 */
	public Grid getGrid() {
		
		return Grid;
	}

	/**
	 * Gets the minimum tile level.
	 * 
	 * @return the minimum tile level
	 */
	public int getMinTileLevel() {
		
		return min;
	}

	/**
	 * Gets the maximum tile level.
	 * 
	 * @return the maximum tile level
	 */
	public int getMaxTileLevel() {
		
		return max;
	}

	/**
	 * Sets the player's score.
	 * 
	 * @param score number of points
	 */
	public void setScore(long score) {
		
		Score = score;
	}

	/**
	 * Sets the game's grid.
	 * 
	 * @param grid game's grid
	 */
	public void setGrid(Grid grid) {
		
		Grid = grid;
	}

	/**
	 * Sets the minimum tile level.
	 * 
	 * @param minTileLevel the lowest level tile
	 */
	public void setMinTileLevel(int minTileLevel) {

		min = minTileLevel;
	}

	/**
	 * Sets the maximum tile level.
	 * 
	 * @param maxTileLevel the highest level tile
	 */
	public void setMaxTileLevel(int maxTileLevel) {

		max = maxTileLevel;
	}

	/**
	 * Sets callback listeners for game events.
	 * 
	 * @param dialogListener listener for creating a user dialog
	 * @param scoreListener  listener for updating the player's score
	 */
	public void setListeners(ShowDialogListener dialogListener, ScoreUpdateListener scoreListener) {
		this.dialogListener = dialogListener;
		this.scoreListener = scoreListener;
	}

	/**
	 * Save the game to the given file path.
	 * 
	 * @param filePath location of file to save
	 */
	public void save(String filePath) {
		GameFileUtil.save(filePath, this);
	}

	/**
	 * Load the game from the given file path
	 * 
	 * @param filePath location of file to load
	 */
	public void load(String filePath) {
		GameFileUtil.load(filePath, this);
	}
}
