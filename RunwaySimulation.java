// File: RunwaySimulation.java
// 

/********************************************************************************************
* Test program for Airport Simulation.
* User enters starting values for basic simulation parameters.
* Files used by this program: Averager.java , BooleanSource.java, LinkedQueue.java, LinkedStack.java
*                             Node.java, Plane.java, Runway.java
*
* Authors: Donald Craig and Joe Eckstein
* Date: 04/04/2018
*
********************************************************************************************/

import java.util.*;


class RunwaySimulation 
{

   public static void main(String [] args)
   {
      //create items/variables needed for sim
      boolean done=false;
      boolean pCrashed=false;
      int simTime, takeoffTime,landTime, landArrival, takeoffArrival, maxWait,totalCrash;
      int takeoffPlaneTotal,landPlaneTotal;
      double landingProb,takeoffProb;
      Scanner scan = new Scanner(System.in);
      String event,crash;
      Plane arriving=null,leaving=null;
      
      while (!done)
      {
         // landingQ, takeoffQ, crash stack
      
         LinkedQueue<Plane> landingQ = new LinkedQueue<Plane>();
         LinkedQueue<Plane> takeoffQ = new LinkedQueue<Plane>();
         //LinkedStack<Plane> crashStack = new LinkedStack<Plane>();
         //crashStack may work better as stack of strings... could store plane # and time of crash easier
         LinkedStack<String> crashStack = new LinkedStack<String>(); //stores plane # and time of crash in a string
                                                                     //formatted for printing at end of sim
         LinkedStack<String> eventStack = new LinkedStack<String>(); //track events for activity report each minute
      
      
         //get user to input sim parameters
         System.out.println("Welcome to the Airport Sim Center!");
         System.out.println("We'll need to have you input the starting parameters for todays sim run before we start.");
         System.out.println("Please enter all values as positive integers that will be used to represent whole minutes.");
         System.out.print("Total simulation time : ");
         simTime=getInt();
         System.out.println();
         System.out.print("Time needed for a plane to take off : ");
         takeoffTime=getInt();
         System.out.println();
         System.out.print("Time needed for a plane to land : ");
         landTime=getInt();
         System.out.println();
         System.out.print("Average time between planes arriving for landing : ");
         landArrival=getInt();
         System.out.println();
         System.out.print("Average time between planes arriving for take off : ");
         takeoffArrival=getInt();
         System.out.println();
         
         System.out.print("Maximum time before landing plane runs out of fuel : ");
         maxWait=getInt();
         System.out.println();
         
         
         //set probabilities
         landingProb = (double)1/landArrival;
         takeoffProb = (double)1/takeoffArrival;
         //create Runway using parameters
         
         Runway runwayOne = new Runway(takeoffTime, landTime);
         //set boolean source probababilities
         BooleanSource land = new BooleanSource(landingProb);
         BooleanSource takeoff = new BooleanSource(takeoffProb);
         
         //set Averager objects to track times
         Averager landAvg = new Averager();
         Averager takeoffAvg = new Averager();
         
         takeoffPlaneTotal=0;
         landPlaneTotal=0;
         //character for status JE
         char status;
      
         //run sim
         for (int loop=1; loop<simTime; loop++)             // main sim loop
         {
            int timeNow=loop;
            // JE
            System.out.println("During minute " + loop + ":");
            
            
            //NOTE - doesn't need to be put on a stack, just output to screen.
            
            //check for planes waiting to take off, add to queue if there is
            if (takeoff.query())
            {
               leaving = new Plane(timeNow,'T');
               takeoffQ.add(leaving);
               takeoffPlaneTotal++;
               System.out.println("\t Arrived for Takeoff : Plane# " + Integer.toString(leaving.getPlaneNo()));
               //eventStack.push(event);
            }
            
            
            //check for planes waiting to land, add to queue if there is
            if (land.query())
            {
               arriving = new Plane(timeNow,'L');
               landingQ.add(arriving);
               landPlaneTotal++;
               System.out.println("\t Arrived for Landing : Plane# " + Integer.toString(arriving.getPlaneNo()));
               //eventStack.push(event);
            }
            
            
            
            //check runway status, if not busy then assign a plane to use it
            //revised this to get planes from Q's and send to runway - DC
            if (!runwayOne.isBusy())
            {
               do
               {
                  pCrashed=false;
                  if (!landingQ.isEmpty())
                  {
                     arriving=(Plane)landingQ.remove();
                     landAvg.addNumber( (double)(loop-arriving.getTime()) );
                     if ((arriving.getTime()-loop) > maxWait)
                     {
                        crash="Plane # " + Integer.toString(arriving.getPlaneNo())+ " crashed at time : " + Integer.toString(loop);
                        crashStack.push(crash);
                        pCrashed=true;
                     }
                     else
                     {
                     //didn't crash so send arriving to runway
                     runwayOne.startUsingRunway('L');
                     }
                  }
               } while (pCrashed);
               
               if ((landingQ.isEmpty()) && (!runwayOne.isBusy()))
               {
                  if (!takeoffQ.isEmpty())
                  {
                     leaving=(Plane)takeoffQ.remove();
                     takeoffAvg.addNumber( (double)(loop-leaving.getTime()) );
                     runwayOne.startUsingRunway('T');
                  }
                  //send leaving to runway
                  //runwayOne.startUsingRunway('T');
               }
            }
            else        //runway IS busy so do this block
            {
            
            // may not need this block DC
            
            
            }
            
            
               //need to report detailed status
               //get status JE
            status = runwayOne.kindOfOperation();
            // revised status output to use a switch statement   - DC
            // may need to look at math for the 'finishing' part
            // **** need to move planes off runway after they land - JE  they are staying past finishing.
            switch(status)
            {
               case 'T' :  System.out.print("\t Runway: Plane #" + leaving.getPlaneNo() + " is taking off.");
                           if( (loop - leaving.getTime()) == takeoffTime )
                              System.out.print("(finishing)");
                           System.out.println();
                           break;
               case 'L' :  System.out.print("\t Runway: Plane #" + arriving.getPlaneNo() + " is landing.");
                           if( (loop - arriving.getTime()) == landTime )
                              System.out.print("(finishing)");
                           System.out.println();
                           break;
               case 'I' :  System.out.println("\t Runway: Idle");
                           break;
            }               
         
            // print report of activities for current minute before starting next 
            //System.out.println("During minute " + Integer.toString(loop) + " :");
            //while (!eventStack.isEmpty())      // prints all events for current "minute", stack will be empty for next minute events
            //{
            //   System.out.println("\t" + eventStack.pop());
            //}
            
            //reduce remaining runway time by 1 JE
            runwayOne.reduceRemainingTime();
            
         } //end main sim loop
      
         totalCrash=crashStack.size();
         while (!crashStack.isEmpty())
         {
            System.out.println("\t" + crashStack.pop());
         }
         
         //generate report after sim ends JE
         
         System.out.println("Number of planes that came to runway for takeoff: " + takeoffPlaneTotal );
         System.out.println("Number of planes that came to runway for landing: " + landPlaneTotal );
         System.out.println("Number of planes that crashed: " + totalCrash );
         System.out.println("Average time waiting in takeoff queue: " + takeoffAvg.average());
         System.out.println("Average time waiting in landing queue:  " + landAvg.average());     
         

         
         
         
         done=runAgain();
   
      }// end while loop that repeats whole sim program
   
   } //end main
   
   public static int getInt( )           // method to get an integer as input
   {
      Scanner numScan = new Scanner(System.in);
      int input=0;
      boolean valid=false;
      while(!valid)
      {
         try
         {
         
            input=numScan.nextInt();
            if (input<=0) 
            {
               valid = false;
               throw new Exception("Value must be positive.");
            }
            else
            {
               valid = true;
            }  
         }
         catch (InputMismatchException e) 
         {
            System.out.println("Invalid entry, must be an integer.");
            numScan.next();
         }
         catch (Exception e) {
            System.out.println(e.getMessage() );
         }
      }
      return input; 
   }//end getInt
   
   public static boolean runAgain()
   {
      boolean done=false;
      String again;
      char answer;
      
      Scanner getAnswer = new Scanner(System.in);
      System.out.print("Would you like run another simulation?  (Y/N)");
         again = getAnswer.next();
         answer = again.charAt(0);
         if (answer == 'Y' || answer == 'y') 
         {
            done = false;
            getAnswer.nextLine();
         }
         else {
            done = true;
         }
      return done; 
   }//end runAgain
   
   
} //end code