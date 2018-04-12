/********************************************************************************************
* File: Runway.java
*
* Authors: Donald Craig and Joe Eckstein
* Date: 04/11/2018
*
********************************************************************************************/

public class Runway
{
   private  int timeForLanding;
   private  int timeForTakeoff;
   private int runwayTimeLeft; 
   private char operation;             
      // operation can be: I - Idle, L-Landing, T-takeoff

   /**
   * Initialize a Runway with a specified amount of time for takeoff
   *  and amount of time for landing.
   * @param time_takeoff
   *   the integer value of time (ex, minute) that the runway needs to use
   *  for each takeoff
   * @param time_landing
   *   the integer value of time (ex, minute) that the runway needs to use
   *  for each landing
   * @postcondition
   *   operation is set to I for Idle.
   *   runwayTimeLeft counter set to zero.
   **/   
   public Runway ( int time_takeoff, int time_landing)
   {
      timeForTakeoff=time_takeoff;
      timeForLanding=time_landing;
      runwayTimeLeft=0;
      
      operation='I';
   
   }

   /**
   * Get the runway in use status from isBusy()
   * @param - none
   * @return
   *   The return value is either true or false,
   *   If the runway is in use by a plane - true.
   *   If runway is idle - false.
   **/   
   public boolean isBusy() 
   {
      if (runwayTimeLeft == 0)
         return false;
      else
         return true;
   }

   /**
   *  Reduce the remaining time of runwayTimeLeft
   *  using reduceRemainingTime()
   *  @param - none
   *  @postcondition - runwayTimeLeft is reduced by one
   **/
   public void reduceRemainingTime()
   {
      if(runwayTimeLeft > 0)
      {
         runwayTimeLeft--;
      }//end if statement
   }//end reduceRemainingTime method

   /**
   * Set the use condition for the runway and begin corresponding timer.
   * @param - typeOfUse character is 'I' for idle
   *           'T' for takeoff
   *           'L' for landing
   * @postcondition - 'I' sets runwayTimeLeft to zero
   *                  'T' sets runwayTimeLeft to timeForTakeoff
   *                  'L' sets runwayTimeLeft to timeForLanding
   **/
   public void startUsingRunway(char typeOfUse)
   { 
      operation=typeOfUse;
      
      switch(typeOfUse)
      {
         case  'I':  runwayTimeLeft=0;
                     break;
         case  'T':  runwayTimeLeft=timeForTakeoff;
                     break;
         case  'L':  runwayTimeLeft=timeForLanding;
                     break;
      } 
   }

   /**
   * Accessor method to Get the character representing the operational use of the Runway 
   * @precondition -  if isBusy() returns false then no one is currently using the runway
   *        therefore must be in the Idle state. If isBusy returns true, then
   *        the runway is either being used for Landing or Takeoff by a plane.
   * @param - none
   * @return - returns the type of operation the runway is used for. 
   *     returns  'L' if the runway is used for is landing. 
   *     returns  'T' if  the runway is used for taking off. 
   *     returns  'I' if the runway is idle 
   **/
   public char kindOfOperation()
   {
      
      char status = ' ';

      if(!isBusy())
      {
         status = 'I';
      }//end if statement
      else
      {
         status = operation;
      }//end else statement
      return status; 
   }  

}//end Runway