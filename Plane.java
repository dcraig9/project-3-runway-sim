class Plane {
   static private int planeCount = 0;       
               // the plane number arrived to the queue 
               // should be in incrementing order
   private int time; //the time the plane arrived in queue
   private char operation;  // the kind of operation the 					//plane is doing 'L" is  for landing
                    // 'T' is for taking off 
   private int planeNo;   // plane number

   public Plane( int aTime, char landingOrTakeOff)
// operation  is the type of operation the plane is doing. // If landingOrTakeOff is 'L' it means the plane is landing // If landingOrTakeOff is 'T' it means Taking off. 
   {
      time = aTime;
      operation =  landingOrTakeOff;
      planeNo = ++planeCount;
   }

   public int getTime()  {
      return time;
   }
   public int getPlaneNo () {
       return planeNo;
   }
   public char getOperation () {
       return operation;
   }
           
   private static int getPlaneCount()
   {
        return planeCount;
   }

}// end Plane
