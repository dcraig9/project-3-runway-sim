class Plane {

   static private int planeCount = 0;       
   // the plane number arrived to the queue 
   // should be in incrementing order
   
   private int time; 
   //the time the plane arrived in queue
   
   private char operation;  
   // the kind of operation the 					
   //plane is doing 'L" is  for landing
   // 'T' is for taking off 
   
   private int planeNo;   
   // plane number

   
   /**
   * Initialize a Plane with a specified initial time and status character.
   * @param aTime
   *   the integer value of time (ex, minute) that the plane arrived
   * @param landingOrTakeOff
   *   a character value representing the type of operation the plane is doing
   *   If landingOrTakeOff is 'L' it means the plane is landing 
   *   If landingOrTakeOff is 'T' it means Taking off. 
   * @postcondition
   *   arrival time is set to aTime
   *   operation is set to landingOrTakeOff  
   *   The planeCount increments by one for this new plane 
   *   and this value is assigned to the planeNo
   **/   
   public Plane( int aTime, char landingOrTakeOff)
   {
      time = aTime;
      operation =  landingOrTakeOff;
      planeNo = ++planeCount;
   }

   /**
   * Modification method to set the plane count to zero in this node
   * @param - none
   * @postcondition
   *   The total number of number of planes that have been recorded
   *   is set to zero.
   **/
   public static void resetCount() {
      planeCount=0;
   }

   /**
   * Accessor method to get the arrival time from this Plane.   
   * @param - none
   * @return
   *   the arrival time from this Plane
   **/
   public int getTime()  {
      return time;
   }
   
   /**
   * Accessor method to get the planeNo from this Plane.   
   * @param - none
   * @return
   *   the plane number from this Plane
   **/
   public int getPlaneNo () {
       return planeNo;
   }
   
   /**
   * Accessor method to get the operation status character from this Plane.   
   * @param - none
   * @return
   *   the operation status character from this Plane
   *     'L' is  for landing
   *     'T' is for taking off 
   *
   **/
   public char getOperation () {
       return operation;
   }
   
   /**
   * Accessor method to get the plane count from this Plane.   
   * @param - none
   * @return
   *   the plane count from from this Plane
   **/        
   private static int getPlaneCount()
   {
        return planeCount;
   }

}// end Plane
