//Orbit.java
//by Joe Hahn, jhahn@spacescience.org, 3 May 2013 and tweaked slightly 4 June 2013.
//
//These are the methods that are used by the Orbits class. This class
//describes an orbit using six standard orbit elements: a=semimajor axis, e=eccentricity,
//I=inclination, O=longitude of ascending node, w=longitude of periapse, and
//M=mean anomaly, with the angles I,O,w,M in units of radians in the inverval
//-Pi <= I,O,w,M <= Pi. Currently this class is rather limited since the only
//useful method here is keplers_eqn() which solves Kepler's equation numerically
//for an elliptic orbit having 0<=e<1. Inputs are the orbit's eccentricity e and
//mean anomaly M, and the output is the eccentric anomaly E in radians plus
//optional output i_ke=number of iterations used by keplers_eqn(). See test_keplers_eqn.java
//for example code that shows how to use keplers_eqn(). I intend to come back to this
//class to make it more useful by adding methods that will advance a particle along
//its orbit by some timestep dt, and methods that will convert orbit elements into
//cartesian coordinates and velocities, and vise versa.

package orbit;

public class Orbit {

  //These are the usual orbit elements: semimajor axis a, eccentricity e, inclination I,
  //longitude of ascending node O, longitude of periapse w, and mean anomaly M.
  public double a, e, I, O, w, M;

  //The Orbit constructor sets the six orbit elements.
  public Orbit(double ac, double ec, double Ic, double Oc, double wc, double Mc){
    a = ac;
    e = ec;
    I = Ic;
    O = Oc;
    w = wc;
    M = Mc;
  }

  //These quanties are computed by keplers_eqn(), and are only used for testing.
  public int i_ke;             //The number of iterations used.
  public boolean quiet_ke;     //Don't warn about convergence failures when quiet_ke=true. 

  public double keplers_eqn(double max_error){
    //This uses Halley's method to solve kepler's equation iteratively for the
    //eccentric anomaly E for an orbit having eccentricity e and mean anomaly M.
    //The allowed error in E is < max_error. This algorithm is from Danby's
    //textbook, Fundamentals of Celestial Mechanics, 2nd edition.
    int max_iteration = 15;
    double pi, sgn, E, f, error, es, ec, df, ddf, dddf, d1, d2, d3;

    //Put M in the interval -Pi < M < Pi and construct the initial solultion.
    pi = Math.PI;
    if (M >  pi) M = M - 2.0*pi;
    if (M < -pi) M = M + 2.0*pi;
    sgn = 1.0;
    if (Math.sin(M) < 0.0) sgn = -1.0;
    E = M + sgn*(0.85)*e;
    f = 1.0;
    error = 1.0;

    //Solve kepler's equation iteratively to improve the solution E.
    for(i_ke=0; i_ke < max_iteration; i_ke++){
      es = e*Math.sin(E);
      ec = e*Math.cos(E);
      f = E - es - M;
      error = Math.abs(f);
      if (error < max_error) break;
      df = 1.0 - ec;
      ddf = es;
      dddf = ec;
      d1 = -f/df;
      d2 = -f/(df + d1*ddf/2.0);
      d3 = -f/(df + d2*ddf/2.0 + d2*d2*dddf/6.0);
      E = E + d3;
    }

     //Warn if solution did not converge.
     if ((error >= max_error) && (!quiet_ke))
       System.out.println("***Warning*** Orbit.update_ecc_anomaly failed to converge.");

    return E;
  }
}

