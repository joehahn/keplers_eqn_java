//test_keplers_eqn.jave
//by Joe Hahn, jhahn@spacescience.org, 3 May 2013 and updated 4 June 2013.
//
//This demonstrates how to use the Orbit.java class, and it also tests keplers_eqn()
//that solve Kepler's equation numerically. The output is used to check the numerical
//accuracy and speed of keplers_eqn().

import java.io.*;
import orbit.*;

class test_keplers_eqn {

  public static void main(String args[]) {

    //Solve Kepler's equation N times.
    int N = 10000000;

    //Save a subset of the N solutions, for plotting and testing purposes.
    int Nsave = 100000;

    //This is the maximum error allowed when keplers_eqn() is used to solve for the
    //orbit's eccentric anomaly E. Shrink this number to increase numerical accuracy.
    double max_error = 1E-15;

    //The remaining definitions.
    double e[] = new double[N];
    double M[] = new double[N];
    double E[] = new double[N];
    int iterations[] = new int[N];
    Orbit orb = new Orbit(1.0, 0.0, 0.0, 0.0, 0.0, 0.0);
    long t0_ns, t1_ns;
    double dt_sec, x;

    //Current time in nanoseconds
    t0_ns = System.nanoTime();

    //Solve kepler's equation N times.
    for (int i=0; i<N; i++){
      x = Math.pow(10.0, -6.0*Math.random());
      e[i] = orb.e = 1.0 - x;
      M[i] = orb.M = Math.PI*(2.0*Math.random() - 1);
      E[i] = orb.keplers_eqn(max_error);
      iterations[i] = orb.i_ke;
    }

    //Execution time in seconds
    t1_ns = System.nanoTime();
    dt_sec = (t1_ns - t0_ns)*(1.0e-9);
    System.out.println("execution time (sec) = " + dt_sec);

    //Write results from first Nsave orbits to file so that output can be inspected and tested.
    try{
      FileOutputStream fos = new FileOutputStream("test_keplers_eqn.dat");
      DataOutputStream dos = new DataOutputStream(fos);
      dos.writeDouble(dt_sec);
      dos.writeInt(Nsave);
      for (int i=0; i<Nsave; i++) dos.writeDouble(e[i]);
      for (int i=0; i<Nsave; i++) dos.writeDouble(M[i]);
      for (int i=0; i<Nsave; i++) dos.writeDouble(E[i]);
      for (int i=0; i<Nsave; i++) dos.writeInt(iterations[i]);
      dos.close();
    }catch(IOException exc){
      System.out.println("IOException: " + exc);
    }
  }
}

