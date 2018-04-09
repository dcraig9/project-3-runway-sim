public class Runway
{
   private  int timeForLanding;
   private  int timeForTakeoff;
   private int runwayTimeLeft; 
   private char operation;             
// operation can be: I – Idle, L-Landing, T-takeoff

   public Runway ( int time_takeoff, int time_landing)
   {
      timeForTakeoff=time_takeoff;
      timeForLanding=time_landing;
      runwayTimeLeft=0;
      
      operation='I';
   
   }
//set the time for landing, time for takeoff, and the //operation to idle. 

   public boolean isBusy() 
   {
      if (runwayTimeLeft == 0)
         return false;
      else
         return true;
   }

   public void reduceRemainingTime()
   {
      if(runwayTimeLeft > 0)
      {
         runwayTimeLeft--;
      }//end if statement
   }//end reduceRemainingTime method

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
// if typeOfUse is 'T' - then the operation is take off  //and set the runway time left 
// to the time it takes for takeoff.
// if typeOfUse is 'L' - then the operation is landing and //set the runway time left 
// to the time it takes for landing
// if typrOfUse is ‘I’ – then the runway is idle, set the //runway time left to zero

   public char kindOfOperation()
   {
      
      char status = ' ';

      //if isBusy returns false then no one is currently using the runway

      //therefore must be in the Idle state. If isBusy returns true, then

      //the runway is either being used for Landing or Takeoff by a plane.

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
// returns the type of operation the runway is used for. 
// returns  'L' if the runway is used for is landing. 
// returns  'T' if  the runway is used for taking off. 
// returns ‘I’, if the runway is idle 

}//end Runway
