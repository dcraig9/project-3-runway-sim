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
      int simTime, takeoffTime,landTime, landArrival, takeoffArrival, maxWait;
      int takeoffPlaneTotal,landPlaneTotal;
      double landingProb,takeoffProb;
      Scanner scan = new Scanner(System.in);
      String event;
      
      while (!done)
      {
         // landingQ, takeoffQ, crash stack
      
         LinkedQueue<Plane> landingQ = new LinkedQueue<Plane>();
         LinkedQueue<Plane> takeoffQ = new LinkedQueue<Plane>();
         LinkedStack<Plane> crashStack = new LinkedStack<Plane>();
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
         landingProb = (double)(1/landArrival);
         takeoffProb = (double)(1/takeoffArrival);
         
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
         for (int loop=1; loop>simTime; loop++)             // main sim loop
         {
            // NOTE - rename loop to timeNow?
            
            int timeNow=loop;
            
            // JE
            System.out.println("During minute " + loop + ":");
            
            
            //NOTE - doesn't need to be put on a stack, just output to screen.
            
            //check for planes waiting to take off, add to queue if there is
            if (takeoff.query())
            {
               Plane leaving = new Plane(timeNow,'T');
               takeoffQ.add(leaving);
               takeoffPlaneTotal++;
               event="Arrived for Takeoff : Plane# " + Integer.toString(leaving.getPlaneNo());
               eventStack.push(event);
            }
            
            
            //check for planes waiting to land, add to queue if there is
            if (land.query())
            {
               Plane arriving = new Plane(timeNow,'L');
               landingQ.add(arriving);
               landPlaneTotal++;
               event="Arrived for Landing : Plane# " + Integer.toString(arriving.getPlaneNo());
               eventStack.push(event);
            }
            
            
            
            //check runway status, if not busy then assign a plane to use it
            if (runwayOne.isBusy())
            {
               //need to report detailed status
               //get status JE
               status = runwayOne.kindOfOperation()
               
               if ( status == 'T')
               {
                  System.out.println("Runway: Plane #" + leaving.getPlaneNo() + " is taking off.");
                  if( (loop - leaving.getTime()) == takeoffTime )
                     System.out.print("(finishing)");
                                    
               }   
               else if ( status == 'L')
               {
                  System.out.println("Runway: Plane #" + arriving.getPlaneNo() + " is landing.");
                  if( (loop - arriving.getTime()) == landTime )
                     System.out.print("(finishing)");
               }   
           
            }
            else
            {
               status = runwayOne.kindOfOperation()
               if ( status == 'I')
               {
                  System.out.println("Runway: Idle");
               }
            
               // check landing queue, if not empty then get plane from it
               if (!landingQ.isEmpty())
               {
                    //get one plane from Queue, add it to runway status = L
                       //**** JE
               // HOW TO - remove plane from landing queue - and place on runway queue?
               // What are planes called in the queue?  what is their variable?
               //*****
                    //do we just create a new plane variable to hold removed plane?
                    Plane landing = landingQ.remove();
                    runwayOne.startUsingRunway('L');     
              
              
               // need to check if plane has been waiting too long and crashed. If crashed, send to 
               // crash stack and process next plane in landing queue. Send plane to runway for landing.
               
               //to calculate whether plane has crashed JE
                  if ( (loop - arriving.getTime()) > landTime)
                  // remove one from arriving and add to 
                  Plane crashing = landingQ.remove();
                  crashStack.push(crashing);
               
               
               
               // if landingQ is empty, then check takeoff queue and
               // it is not empty as well, send a plane to runway for takeoff
            }
            
                     
            // print report of activities for current minute before starting next
            
            
         
            //reduce remaining runway time by 1 JE
            runwayOne.reduceRemainingTime();

         
         } //end main sim loop
      
      
      
         //generate report after sim ends JE
         
         System.out.println("Number of planes that came to runway for takeoff: " + leaving.getPlaneCount() );
         System.out.println("Number of planes that came to runway for landing: " + arriving.getPlaneCount() );
         System.out.println("Number of planes that crashed: " + );
         System.out.println("Average time waiting in takeoff queue: "+ );
         System.out.println("Average time waiting in landing queue:  "+ );     
         
         
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