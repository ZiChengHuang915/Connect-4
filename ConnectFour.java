/* 
 *	File Name:     ConnectFour.java
 *	Name:          Bobby Ma, ZiCheng Huang
 * Class:         ICS3U1-01 (B)
 *	Date:          May 28, 2018
 * Description:   This is the main class of the Connect 4 game. It is a two-player board game in which the 
 *                players take turns in dropping alternating coloured discs into a seven-column, six-row 
 *                vertically-suspended grid. The object of the game is to connect four singly-coloured discs 
 *                in a row - vertically, horizontally, or diagonally - before the opponent can do likewise.               
 */
public class ConnectFour 
{
   final int EMPTY = -1;   // represents an empty slot on the game board
   final int NUMPLAYER;    // number of players in the game
   final int NUMROW;       // number of rows on the game board
   final int NUMCOL;       // number of columns on the game board
   final int MAXGAME;      // number of wins needed to win the overall match
   final int CHAINNUM = 4; // number of discs in a row to count as a win
   
   ConnectFourGUI gui;     // the game board object
   int numMove;            // number of occupied slots on the game board
   int curPlayer = (int) (Math.random() * 2);      // 0 = black; 1 = red; the starting player is randomized
   int grid[][];           // the game board
   int score[];            // the score for each player
   
      
   /*     ConnectFour constuctor. Initializes all variables, constants, and arrays upon program execution.
    *     Parameters: gui - responsible for calling the methods in the ConnectFourGUI class
    *     Returns:    N/A
    */
   public ConnectFour(ConnectFourGUI gui) 
   {
      this.gui = gui;                   // sets the gui instance to the value of the parameter.
      NUMPLAYER = gui.NUMPLAYER;        // sets the number of players
      NUMROW = gui.NUMROW;              // sets the number of rows
      NUMCOL = gui.NUMCOL;              // sets the number of columns
      MAXGAME = gui.MAXGAME;            // sets the number of games needed to win 
      grid = new int[NUMROW][NUMCOL];   // instantiates the grid array to the number of rows times the number of columns
      resetGrid();                      // initialize grid to -1(EMPTY)
      score = new int[NUMPLAYER];       // instantiates the score array; automatically initialized to 0
      gui.setNextPlayer(curPlayer);     // displays the starting player
   } // ConnectFour(ConnectFourGUI gui) method
      
   /*    The resetGrid method clears the grid of any current piece values and assigns them to the empty value.
    *    Parameters: N/A
    *    Returns:    N/A
    */
   private void resetGrid()
   {
      // Goes through the rows
      for(int r = 0; r < NUMROW; r++)
      {
         // Goes through the columns
         for(int c = 0; c < NUMCOL; c++)
         {
            grid[r][c] = EMPTY;     // assigns the current grid index to the preassigned empty value
         }
      }
   } // resetGrid() method
   
   /*     The play method is called when a mouse click is registered. Contains the main logic of the game.
    *     Parameters: column - the index of the column that the player clicked on the game board.
    *     Returns:    N/A
    */ 
   public void play(int column) 
   {
      // Checks if the selected column is not full
      if(drop(column) != -1)                                // if the selected column is not full
      {
         int row = drop(column);                            // finds lowest empty row of the selected column
         gui.setPiece(row, column, curPlayer);              // displays the dropped disc
         grid[row][column] = curPlayer;                     // assigns the piece to the grid array
         numMove++;                                         // increments the number of moves
         
         // Checks if the move results in a win
         if(makeWinner(row, column))                        // if the move results in a win
         {
            score[curPlayer]++;                             // increments the score of the player
            gui.setPlayerScore(curPlayer, score[curPlayer]);// displays the score
            gui.showWinnerMessage(curPlayer);               // displays winner message (of current player)
            
            // Checks if the updated score results in a match win
            if(score[curPlayer] == MAXGAME)                 // if the score reaches the max score to win a match  
            {
               gui.showFinalWinnerMessage(curPlayer);       // Displays the final winner message
               gui.anotherGame(); 						         // Asks if the user want to play another game
               resetScore();                                // Resets the score of all players
               gui.setPlayerScore(0, 0); 					      // Displays the score of player 0 (black)
               gui.setPlayerScore(1, 0);                    // Displays the score of player 1 (white)
            }
            
            gui.resetGameBoard();                           // resets the game board graphically
            resetGrid();                                    // resets the grid array; sets all values to -1(EMPTY)
            curPlayer = nextPlayer(curPlayer);              // switches player number
            gui.setNextPlayer(curPlayer);                   // displays next player
            numMove = 0;                                    // resets number of moves 
         }
         // Checks if the move results in a tie by checking if the number of moves exceeds NUMROW * NUMCOL,
         // which is the maximum amount of moves on the grid
         else if(numMove == NUMROW * NUMCOL)                // if the number moves exceeds the max number of moves                                                           
         {
            gui.showTieGameMessage();                       // displays tie game message
            gui.resetGameBoard();                           // resets the game board graphically
            resetGrid();                                    // resists the grid array; sets all values to -1(EMPTY)
            curPlayer = nextPlayer(curPlayer);              // switches player number
            gui.setNextPlayer(curPlayer);                   // displays next player
            numMove = 0;                                    // resets number of moves
         }
         // Since it is neither a win nor a tie, continue playing by switching players and waiting for input
         else                                               // if the move is neither a win nor a tie
         {
            curPlayer = nextPlayer(curPlayer);              // switches player number
            gui.setNextPlayer(curPlayer);                   // displays next player
         }
      }
   } // play(int column) method
   
   /*     The drop method determines whether the current column is full or not, and returns the lowest empty row index. 
    *     Parameters:       column - the index of the column that the player has clicked.
    *     Returns(integer): -1 if the entire column is full, 
    *                       otherwise the index of the lowest empty row number. 
    */
   private int drop(int column)
   {
      // Checks if the top row of the column is empty
      if(grid[0][column] != EMPTY)        // if the top row of the column is not empty
      {
         return -1;                       // since the top row in not empty, 
                                          // therefore the entire column is full; returns -1 
      }
      else                                // if the top row is empty
      {
         int row;                         // variable keeps track of the current row index 
         // Goes through the rows starting from the bottom going upwards excluding the topmost row          			         
         for (row = NUMROW - 1; row >= 1; row--)
         {
            // Checks if the current coordinate is empty
            if (grid[row][column] == -1)  // if the current coordinate is empty
            {
               return row;                // returns the row index
            }
         }                  
      }
      return 0;                           // Since the top row is empty and all the other rows have been checked  
                                          // (except row 0), return topmost row because it will definitely be empty
   } // drop(int column) method
   
    /*    The makeWinner method checks if the current move made by the player results in a chain and therefore a win.
    *    Parameters:       row - index of the row of the move made by the player.
    *                      column - index of the column of the move made by the player.
    *    Returns(boolean): true if the move results in a 4 in a row in any direction,
    *                      otherwise returns false. 
    */
   private boolean makeWinner(int row, int column)
   {
      // Variables to keep track of the chain count in all 4 directions
      // The 1 represents the current row and column that the player just played
      // Calls on the recursive methods to determine the chain count
      int horizontalCount = 1 + leftCount(row, column) + rightCount(row, column);
      int verticalCount = 1 + upCount(row, column) + downCount(row, column);
      int diagonalSlashCount = 1 + leftDownCount(row, column) + rightUpCount(row, column);
      int diagonalBackSlashCount = 1 + leftUpCount(row, column) + rightDownCount(row, column);
      
      // Checks if any of the 4 directions results in a chain that is equal or greater than the required amount
      if (horizontalCount >= CHAINNUM || verticalCount >= CHAINNUM 
      || diagonalSlashCount >= CHAINNUM || diagonalBackSlashCount >= CHAINNUM)   // if any of the directions 
                                                                                 // exceeds the required 4 in a row
      {
         return true;                                                            // returns true if there is a winner
      }
      return false;                                                              // otherwise returns false
   }
   
   /*    The next 8 methods are the same but with differnt directional checking.
    *    These methods checks if the disc adjacent to the current disc is the same, and 
    *    if so checks further in that direction, returning with the number of continuous
    *    adjacent chains in the one direction.
    *    Parameters:        row - index of the row of the played disc.
    *                       column - index of the column of the played disc.
    *    Returns(integer) : 1 if the disc is the same as the current disc, and calls on the method again.
    *                       0 otherwise, and stops checking in that direction because the chain is broken.
    */  
   private int leftCount(int row, int column)
   {
      // Checks if the adjacent disc in specified direction is the same as the current disc
      if (column - 1 != -1 && grid[row][column - 1] == grid[row][column])  // if the disc in the selected direction 
                                                                           // is the same as the current disc
      {
         return 1 + leftCount(row, column - 1); // returns 1, and calls the method again
      }
      return 0;                                 // otherwise returns 0, does not call method again
   }
   
   private int rightCount(int row, int column)
   {
      // Checks if the adjacent disc in specified direction is the same as the current disc
      if (column + 1 != NUMCOL && grid[row][column + 1] == grid[row][column]) // if the disc in the selected direction 
                                                                              // is the same as the current disc
      {
         return 1 + rightCount(row, column + 1); // returns 1, and calls the method again
      }
      return 0;                                  // otherwise returns 0, does not call method again
   }
   
   private int upCount(int row, int column)
   {
      // Checks if the adjacent disc in specified direction is the same as the current disc
      if (row - 1 != -1 && grid[row - 1][column] == grid[row][column])  // if the disc in the selected direction 
                                                                        // is the same as the current disc
      {
         return 1 + upCount(row - 1, column);   // returns 1, and calls the method again
      }
      return 0;                                 // otherwise returns 0, does not call method again
   }
   
   private int downCount(int row, int column)
   {
      // Checks if the adjacent disc in specified direction is the same as the current disc
      if (row + 1 != NUMROW && grid[row + 1][column] == grid[row][column]) // if the disc in the selected direction 
                                                                           // is the same as the current disc
      {
         return 1 + downCount(row + 1, column); // returns 1, and calls the method again
      }
      return 0;                                 // otherwise returns 0, does not call method again
   }
   
   private int leftUpCount(int row, int column)
   {
      // Checks if the adjacent disc in specified direction is the same as the current disc
      if (column - 1 != -1 && row - 1 != -1                                // if the disc in the selected direction
      && grid[row - 1][column - 1] == grid[row][column])                   // is the same as the current disc
      {
         return 1 + leftUpCount(row - 1, column - 1); // returns 1, and calls the method again
      }
      return 0;                                       // otherwise returns 0, does not call method again
   }
   
   private int rightDownCount(int row, int column)
   {
      // Checks if the adjacent disc in specified direction is the same as the current disc
      if (column + 1 != NUMCOL && row + 1 != NUMROW                        // if the disc in the selected direction                                                                            
      && grid[row + 1][column + 1] == grid[row][column])                   // is the same as the current disc
      {
         return 1 + rightDownCount(row + 1, column + 1); // returns 1, and calls the method again
      }
      return 0;                                          // otherwise returns 0, does not call method again
   }
   
   private int leftDownCount(int row, int column)
   {
      // Checks if the adjacent disc in specified direction is the same as the current disc
      if (column - 1 != -1 && row + 1 != NUMROW                            // if the disc in the selected direction
      && grid[row + 1][column - 1] == grid[row][column])                   // is the same as the current disc
      {
         return 1 + leftDownCount(row + 1, column - 1);  // returns 1, and calls the method again
      }
      return 0;                                          // otherwise returns 0, does not call method again
   }
   
   private int rightUpCount(int row, int column)
   {
      // Checks if the adjacent disc in specified direction is the same as the current disc
      if (column + 1 != NUMCOL && row - 1 != -1                            // if the disc in the selected direction
      && grid[row - 1][column + 1] == grid[row][column])                   // is the same as the current disc
      {
         return 1 + rightUpCount(row - 1, column + 1);   // returns 1, and calls the method again
      }
      return 0;                                          // otherwise returns 0, does not call method again
   }
   
   /*    The resetScore method clears the scores of all players and resets them to 0.
    *    Parameters: N/A
    *    Returns:    N/A
    */
   private void resetScore()
   {
      // Goes through all the players
      for(int i = 0; i < NUMPLAYER; i++)
      {
         score[i] = 0;                    // assigns the score of the player to 0
      }
   } // resetScore() method
   
   /*    The nextPlayer class returns the next player and alternates the turns.
    *    Parameters:       curPlayer - the integer corresponding to the current player.
    *    Returns(integer): 0 if the current player is 1,
    *                      1 if the current player is 0.       
    */
   private int nextPlayer(int curPlayer)
   {    
      return ((curPlayer + 1) % 2);       // returns the remainder of the current player + 1 divided by 2
                                          // if current player is 0, returns (0 + 1) % 2 = 1
                                          // if current player is 1, reutnrs (1 + 1) % 2 = 0
   } // nextPlayer(int curPlayer) method
   
   /*    The getBottomRow method is accessed from the Listener class and calls on the drop method, ensuring that
    *    the drop method in this class remains private as per the intructions.
    *    Parameters: column - column that the player clicked on
    *    Returns(integer): same as the drop method (accessor)
    */
   public int getBottomRow(int column)
   {
      return drop(column);
   } // getBottomRow(int column) method
    
} // ConnectFour class   
