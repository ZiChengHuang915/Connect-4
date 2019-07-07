import javax.swing.*;
import java.awt.event.*;

public class ConnectFourListener implements MouseListener
{
   private ConnectFourGUI gui;
   private ConnectFour game;
   private boolean over = true; // initialize to true for first time running, stores if the previous thread has finished running
    
   public ConnectFourListener (ConnectFour game, ConnectFourGUI gui) {
      this.game = game;
      this.gui = gui;
      gui.addListener (this);
   }


   public void mouseClicked (MouseEvent event) {
      JLabel label = (JLabel) event.getComponent();
      final int column = gui.getColumn (label);                // stores the column of the mouse click
      // Checks if the previous thread(action) is finished running
      if(over)                                                 // if the previous thread has finished running
      {
         over = false;                                         // resets to false because the thread is running 
         Thread drop = new Thread(
            new Runnable()                                     // thread accessing EDT 
            {
               
               // Animation that shows the disc dropping down the rows
               public void run() 
               {
                  // Loops through the rows starting from the top
                  for(int r = 0; r <= game.getBottomRow(column); r++)  
                  {
                     gui.setPiece(r, column, game.curPlayer);  // display the disc at the current row
                     
                     // Catches any exceptions while sleeping 
                     try
                     {                                         // try 
                        Thread.sleep(10);                      // sleeps for 10 milliseconds
                     }
                     catch(Exception e)                        // if there is an exception
                     {
                        e.printStackTrace();                   // prints error message
                     }
                     gui.clearPiece(r, column);                // clear the screen of the disc at the current row
                  }
                  game.play(column);                           // when loop is over, goes into play method 
                  over = true;                                 // thread is finished running, change variable to true
               }
            });
         drop.start();                                         // sets thread status to activate
      }
   }

   public void mousePressed (MouseEvent event) {
   }

   public void mouseReleased (MouseEvent event) {
   }

   public void mouseEntered (MouseEvent event) {
      JLabel label = (JLabel) event.getComponent();
      int column = gui.getColumn (label);         // stores the current column that the player is hovering over
      // Checks if the topmost row is empty and the drop animation thread is not running
      if(game.getBottomRow(column) != -1 && over) // if no threads are running and the column is not full
      {
         gui.setPiece(0, column, game.curPlayer); // displays where the disc is going to be while mouse is hovering over
      }
   }

   public void mouseExited (MouseEvent event) {
      JLabel label = (JLabel) event.getComponent();
      int column = gui.getColumn (label);         // stores the current column that the player is hovering over
      // Checks if the topmost row is empty and the drop animation thread is not running
      if(game.getBottomRow(column) != -1 && over) // if no threads are running and the column is not full
      {
         gui.clearPiece(0, column);               // clears the disc if the mouse exits the column
      }
   }
}
