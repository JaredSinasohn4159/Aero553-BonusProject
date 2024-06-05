package src;

import java.util.Scanner;  //this is only used to get user input, not used for algorithm

public class Exponentiator {
    public static final double TOL = 1e-10;
    public static final double INIT_PREV_IT = 1000;
    public static final double PI = 3.141592653589;


    public static void main(String[] args){
        //run();
        test();
    }

    /**
     * power() performs the operation x^y on any real x and y values without the use of the math library.  Can return
     * imaginary powers.
     * @param x The base of the x^y operation.  Can be any real number.
     * @param y The exponent of the x^y operation.  Can be any real number.
     * @return Double array of length two of the real and imaginary parts of x^y
     */
    public static double[] power(double x, double y){
        // special cases
        // if the exponent is zero, return 1
        if(y==0){
            return new double[]{1,0};
        }
        // if the base is zero and the exponent is not one and the exponent is greater than zero, return zero
        if(x==0 && y > 0){
            return new double[]{0,0};
        }
        if(x==0 && y < 0){
            return new double[]{Double.POSITIVE_INFINITY, 0};
        }
        // if the base is one, return 1
        if(x==1){
            return new double[]{1,0};
        }


        // Now performing computations
        // If the exponent is an integer value, use the simple looping power algorithm and return that
        if(isInt(y)){
            // send in the absolute value of the power to make it work
            double int_result = int_power(x,(int)abs(y));
            // if the power was negative, invert the result
            if(y<0){
                int_result = 1/int_result;
            }
            // return the result
            return new double[]{int_result,0};
        }
        //
        int int_y = (int)y;

        /*
         * Doing the calculation of x^y for non integer y values in two steps
         * First, perform x^y on the integer part of the exponent using the looping algorithm
         * Second, perform e^y(lnx) for the decimal part of y.
         * Doing this in two steps allows for easier convergence and less error of the taylor series expansions
         * since the numbers we are using are small, as well as allowing for faster computation.
         */
        // Get decimal part of the exponent
        double decimal = y-int_y;
        // Get result by doing (x^integer)*(e^(decimal*ln(x))
        double result = int_power(x,(int)abs(int_y))*ln_exp_power(abs(x),abs(decimal));
        // If the exponent was less than zero, invert the result
        if(y<0){
            result = 1/result;
        }
        // Now dealing with imaginary results using Euler's identity and De Moivre's theorem
        // where x^y = ||x^y||*cos(y*pi)+i*sin(y*pi)
        // With some smart mathematical manipulation, you can just enter the decimal part of y
        double re = result;
        double im = 0;
        if(x<0){
            // calculate real part
            re = result*cos(decimal*PI,0,INIT_PREV_IT,TOL);
            // calculate imaginary part using same cosine function shifted by pi/2 to turn it into sine
            im = result*cos(decimal*PI-PI/2,0,INIT_PREV_IT,TOL);
        }
        // if either of the parts are much much smaller than the other (TOL orders of magnitude, which right now is 10
        // orders of magnitude smaller), set it to zero
        if(im != 0 && abs(re/im) < TOL){
            re = 0;
        }
        if(re != 0 && abs(im/re) < TOL){
            im = 0;
        }
        // return the real and imaginary parts
        return new double[]{re,im};
    }

    /**
     * Recursive function to calculate the Taylor Series expansion of the natural logarithm.
     * @param x The value to perform the natural logarithm on
     * @param n The nth part of the Taylor Series sum.  n SHOULD ALWAYS BE SET TO ZERO IF USED BY THE USER.
     * @param prev_it The value of the previous iteration
     * @param tol The tolerance to which a converged solution has been met
     * @return Either zero on the final iteration when a good enough solution has been found or
     */
    public static double ln(double x, int n, double prev_it, double tol){
        if(x <= 0){
            throw new ArithmeticException("Logarithm cannot take values less than zero.");
        }
        double curr_it = 1/((double)2*n+1)*int_power((x-1)/(x+1),2*n+1);
        try {
            if((prev_it - curr_it < tol && prev_it - curr_it > -1*tol) || n > 20000){
                return 0;
            }else{
                return 2*curr_it + ln(x,n+1,curr_it, tol);
            }
        } catch (StackOverflowError e) {
            return 0;
        }
    }

    /**
     * Recursive function to calculate the Taylor Series expansion of e^x.
     * @param x The value to perform the natural logarithm on
     * @param n The nth part of the Taylor Series sum.  n SHOULD ALWAYS BE SET TO ZERO IF USED BY THE USER.
     * @param prev_it The value of the previous iteration
     * @param tol The tolerance to which a converged solution has been met
     * @return Either zero on the final iteration when a good enough solution has been found or
     */
    public static double exp(double x, int n, double prev_it, double tol){
        double curr_it = 1/factorial(n)*int_power(x,n);
        try {
            if((prev_it - curr_it < tol && prev_it - curr_it > -1*tol) || n > 20000){
                return 0;
            }else{
                return curr_it + exp(x,n+1,curr_it, tol);
            }
        } catch (StackOverflowError e) {
            return 0;
        }
    }

    /**
     * Recursive function to calculate the Taylor Series expansion of cosine(x).
     * @param x The value to perform the natural logarithm on
     * @param n The nth part of the Taylor Series sum.  n SHOULD ALWAYS BE SET TO ZERO IF USED BY THE USER.
     * @param prev_it The value of the previous iteration
     * @param tol The tolerance to which a converged solution has been met
     * @return Either zero on the final iteration when a good enough solution has been found or
     */
    public static double cos(double x, int n, double prev_it, double tol){
        int negative_1_n = -2*(n%2)+1;
        double curr_it = negative_1_n/factorial(2*n)*int_power(x,2*n);
        try {
            if((prev_it - curr_it < tol && prev_it - curr_it > -1*tol) || n > 20000){
                return 0;
            }else{
                return curr_it + cos(x,n+1,curr_it, tol);
            }
        } catch (StackOverflowError e) {
            return 0;
        }
    }

    /**
     * Looping function to calculate a simple algorithm for x^y where y is an integer.
     * @param x The base of the operation
     * @param y The exponent of the operation
     * @return x^y
     */
    public static double int_power(double x, int y){
        if(y == 0){
            return 1;
        }
        double result = 1;
        for(int i = 0; i < y; i++){
            result *= x;
        }
        return result;
    }

    /**
     * Abstracted algorithm to calculate x^y via the algorithm e^(y*ln(x)).
     * @param x The base of the operation
     * @param y The exponent of the operation
     * @return e^(y*log(x))
     */
    public static double ln_exp_power(double x, double y){
        double logx = ln(x,0,INIT_PREV_IT,TOL);
        double ylogx = y*logx;
        double eylogx = exp(ylogx,0,INIT_PREV_IT,TOL);
        return eylogx;
    }

    /**
     * Algorithm to calculate the factorial of a number.
     * @param x The number to perform a factorial on
     * @return x!
     */
    public static double factorial(double x){
        if(x == 0){
            return 1;
        }
        double result = 1;
        for(int i = 1; i <= x; i++){
            result *= i;
        }
        return result;
    }

    /**
     * Algorithm to calculate the absolute value of a number.
     * @param x The number to perform a factorial on
     * @return |x|
     */
    public static double abs(double x){
        if(x<0){
            x = -1*x;
        }
        return x;
    }

    /**
     * Boolean check to determine whether a number is an integer or not.
     * @param x The number to perform a factorial on
     * @return whether the number is an integer or not
     */
    public static boolean isInt(double x){
        return (int)x == x;
    }

    /**
     * The function that runs the calculator
     */
    public static void run(){
        while(true){
            System.out.println("Welcome to the exponent calculator");
            Scanner reader = new Scanner(System.in);
            System.out.println("Enter the base: ");
            double x = reader.nextDouble();
            System.out.println("Enter the exponent: ");
            double y = reader.nextDouble();
            double[] calced = power(x,y);
            if(calced[1] == 0){
                System.out.println(x + " raised to the power of " + y + " equals " + calced[0]);
            } else if(calced[1] < 0) {
                System.out.println(x + " raised to the power of " + y + " equals " + calced[0] + " - " + (-1*calced[1]) + " i");
            }else{
                System.out.println(x + " raised to the power of " + y + " equals " + calced[0] + " + " + calced[1] + " i");
            }
            System.out.println("Again? (y/n): ");
            String inputString = reader.next();
            if(!inputString.equals("y")){
                System.out.println("Thank you for choosing my calculator.");
                reader.close();
                System.exit(0);
            }
        }
    }

    public static void test(){
        System.out.println("Testing small values");
        double[] small_test_cases = {3,-7,0.25,-0.75,16.375,-4.3125};
        double[] calced;
        for(int i = 0; i < small_test_cases.length; i++){
            for(int j = 0; j < small_test_cases.length; j++){
                double x = small_test_cases[i];
                double y = small_test_cases[j];
                calced = power(x,y);
                if(calced[1] == 0){
                    System.out.println(x + " raised to the power of " + y + " equals " + calced[0]);
                } else if(calced[1] < 0) {
                    System.out.println(x + " raised to the power of " + y + " equals " + calced[0] + " - " + (-1*calced[1]) + " i");
                }else{
                    System.out.println(x + " raised to the power of " + y + " equals " + calced[0] + " + " + calced[1] + " i");
                }
            }
        }
        calced = power(-2147483647, 0.5);
        System.out.println(-2147483647 + " raised to the power of " + 0.5 + " equals " + calced[0] + " + " + calced[1] + " i");
    }
}

