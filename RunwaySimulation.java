// File: RunwaySimulation.java
// 

/********************************************************************************************
* Test program for simulation of an Airport with a single runway.
* User enters starting values for basic simulation parameters. Landing queue has priority over takeoff.
* Files used by this program: Averager.java , BooleanSource.java, LinkedQueue.java, LinkedStack.java
*                             Node.java, Plane.java, Runway.java
*
* Authors: Donald Craig and Joe Eckstein
* Date: 04/04/2018
*
********************************************************************************************/

import java.util.*;
import java.text.DecimalFormat;

class RunwaySimulation 
{

   public static void main(String [] args)
   {
      //create items/variables needed for sim
      boolean done=false,event=false;
      boolean pCrashed=false;
      int simTime, takeoffTime,landTime, landArrival, takeoffArrival, maxWait,totalCrash;
      int takeoffPlaneTotal,landPlaneTotal,startL,startT;
      double landingProb,takeoffProb;
      Scanner scan = new Scanner(System.in);
      String crash;
      char status;
      Plane arriving=null,leaving=null;
      Plane takeoffTemp=null,landTemp=null; 
      DecimalFormat twoDigit = new DecimalFormat( "#.00" );
      
            
      while (!done)        //Repeat Sim program until user decides to stop
      {
         //initialize all counters, landingQ, takeoffQ, crash stack
         Plane.resetCount();
         takeoffPlaneTotal=0;
         landPlaneTotal=0;
         startL=0;
         startT=0;     
         LinkedQueue<Plane> landingQ = new LinkedQueue<Plane>();
         LinkedQueue<Plane> takeoffQ = new LinkedQueue<Plane>();
         LinkedStack<String> crashStack = new LinkedStack<String>();      
      
         //get user to input sim parameters
         System.out.println();
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
         
         //create Runway using parameters       
         Runway runwayOne = new Runway(takeoffTime, landTime);
         
         //set boolean source probababilities
         landingProb = (double)1/landArrival;
         takeoffProb = (double)1/takeoffArrival;
         BooleanSource land = new BooleanSource(landingProb);
         BooleanSource takeoff = new BooleanSource(takeoffProb);
         
         //set Averager objects to track wait times
         Averager landAvg = new Averager();
         Averager takeoffAvg = new Averager();
                  
         //Print summary of parameters to be used for sim 
         System.out.println("The time of simulation : " + simTime + " minutes");
         System.out.println("The time needed for a takeoff : " + takeoffTime + " minutes");
         System.out.println("The time needed for a landing : " + landTime + " minutes");
         System.out.println("The average time between arrivals for takeoffs : " + takeoffArrival + " minutes");
         System.out.println("The average time between arrivals for landings : " + landArrival + " minutes");
         System.out.println("The maximum time a plane can stay in air before crashing : " + maxWait + " minutes");
         System.out.println();
      
         for (int loop=1; loop<simTime+1; loop++)             // main sim for loop
         {
            System.out.println("During minute " + loop + ":");
            event=false;
            pCrashed=true;
            if (startL>0)
               startL--;
            else 
            {
               if (startT>0)
                  startT--;
            }
            int timeNow=loop;
                  
            //check for planes waiting to take off, add to queue if there is
            if (takeoff.query())
            {
               leaving = new Plane(timeNow,'T');
               takeoffQ.add(leaving);
               takeoffPlaneTotal++;
               System.out.println("\t Arrived for Takeoff : Plane# " + Integer.toString(leaving.getPlaneNo()));
            }
            
            //check for planes waiting to land, add to queue if there is
            if (land.query())
            {
               arriving = new Plane(timeNow,'L');
               landingQ.add(arriving);
               landPlaneTotal++;
               System.out.println("\t Arrived for Landing : Plane# " + Integer.toString(arriving.getPlaneNo()));
            }
            
            
            //check runway status, if not busy then assign a plane to use it
            if (runwayOne.isBusy())
            {
               status = runwayOne.kindOfOperation();
               
               switch(status)
               {
                  case 'T' :  System.out.print("\t Runway: Plane #" + takeoffTemp.getPlaneNo() + " is taking off.");
                              if (startT==1)
                              {
                                 System.out.print("(finishing)");
                                 runwayOne.startUsingRunway('I');
                                 
                              }
                              System.out.println();
                              event=true;
                              break;
                  case 'L' :  System.out.print("\t Runway: Plane #" + landTemp.getPlaneNo() + " is landing.");                             
                              if (startL==1) 
                              {
                                 System.out.print("(finishing)");
                                 runwayOne.startUsingRunway('I');
                                 
                              }
                              System.out.println();
                              event=true;
                              break;
               }             
               
            }
            else //runway not busy, so check Q's for planes to use
            {
               while (pCrashed)
               {
                  if (!landingQ.isEmpty() && (!runwayOne.isBusy()) )
                  {
                     landTemp=(Plane)landingQ.remove();
                     landAvg.addNumber( (double)(loop-landTemp.getTime()) );
                     if ( (loop-maxWait) >= landTemp.getTime() )
                     {
                        crash="Plane # " + Integer.toString(landTemp.getPlaneNo())+ " crashed at time : " + (landTemp.getTime() + maxWait);
                        crashStack.push(crash);
                        pCrashed=true;
                     }
                     else
                     {
                        pCrashed=false; //didn't crash so send arriving to runway
                        runwayOne.startUsingRunway('L');
                        event=true;
                        System.out.println("\t Runway: Plane #" + landTemp.getPlaneNo() + " is landing.");
                        startL=landTime;
                     }
                  }
                  else
                     pCrashed=false;
               }
               
               if ((landingQ.isEmpty()) && (!runwayOne.isBusy()))//landing queue empty and runway not busy so check takeoff queue
               {
                  if (!takeoffQ.isEmpty())
                  {
                     takeoffTemp=(Plane)takeoffQ.remove();
                     event=true;
                     takeoffAvg.addNumber( (double)(loop-takeoffTemp.getTime()) );
                     runwayOne.startUsingRunway('T');
                     System.out.println("\t Runway: Plane #" + takeoffTemp.getPlaneNo() + " is taking off.");
                     startT=takeoffTime;

                  }
               }                            
            }
                       

            if (!event)                               //idle if nothing happening on runway this turn 
               System.out.println("\t Runway: Idle");              
                     
         } //end main sim for loop
         
         //report section -  send summary of all sim results to screen
         totalCrash=crashStack.size();         
         if (crashStack.size() > 0)
         {
            System.out.println();
            System.out.println("Crashed Planes:");
            while (!crashStack.isEmpty())
            {
               System.out.println("\t" + crashStack.pop());
            }
         }
         else
         {
            System.out.println();
            System.out.println("No planes crashed.");
         }
              
         System.out.println();
         System.out.println("Number of planes that came to runway for takeoff: " + takeoffPlaneTotal );
         System.out.println("Number of planes that came to runway for landing: " + landPlaneTotal );
         System.out.println("Number of planes that crashed: " + totalCrash );
         System.out.println("Average time waiting in takeoff queue: " + twoDigit.format(takeoffAvg.average()) + " minutes");
         System.out.println("Average time waiting in landing queue:  " + twoDigit.format(landAvg.average()) + " minutes");     
         
     
         done=runAgain(); // ask user if they want to run another sim
   
      }// end while loop that repeats whole sim program
      System.out.println();
      System.out.println("Thank you for using our Airport Simulator. Have a great day!");
   } //end main
   
   public static int getInt( )           //dedicated method to get an integer as input
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
   
   public static boolean runAgain()   //ask user if they wish to run another sim
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