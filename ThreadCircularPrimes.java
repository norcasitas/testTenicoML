/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nico orcasitas
 */
public class ThreadCircularPrimes extends Thread {

    private Integer from, to;
    private LinkedList<Integer> circularPrimes; //numbers
    public final int maxThreads = 10; //estimated maximum number of threads in the system

    /**
     * constructor
     *
     * @param from number from
     * @param to number to
     */
    public ThreadCircularPrimes(Integer from, Integer to) {
        this.from = from;
        this.to = to;
    }

    /**
     * Returns true if a number es prime
     *
     * @param number
     * @return
     */
    public boolean isPrime(Integer number) {
        if (number == 2 || number == 3 || number == 5 || number == 7) {
            return true;
        } else if (0 == number % 2)// if the number is a even then isn't a prime
        {
            return false;
        }
        Double sqrt = Math.sqrt(number);
        Double nxtInt = Math.ceil(sqrt); //next int of the sqrt number
        if (sqrt == nxtInt) //if sqrt is an integer then isn't a prime
        {
            return false;
        } else {
            int auxCount = 3;
            boolean result = true;
            while ((result) && (auxCount <= nxtInt)) {
                if (number % auxCount == 0) {
                    result = false;
                }
                auxCount += 2;
            }
            return result;
        }
    }

    /**
     * returns true if the circular numbers are primes
     *
     * @param number
     * @return
     */
    public boolean nmbCircularPrime(Integer number) {
        boolean arePrimes = isPrime(number);
        String strNumber = number.toString();
        for (int i = 1; i < strNumber.length() && arePrimes; i++) {
            char firstDigit = strNumber.charAt(0); //obtain the first digit
            strNumber = strNumber.substring(1) + firstDigit; // enqueue the first digit on the last
            arePrimes = arePrimes && isPrime(Integer.valueOf(strNumber));
        }
        return arePrimes;
    }

    private LinkedList<Integer> calcCircularPrimes(Integer from, Integer to) {
        if (from.equals(to) || Thread.activeCount() >= this.maxThreads) { //case basis, calcuate if the number is a circular prime
            LinkedList<Integer> ret = new LinkedList<>();
            for (int i = from; i <= to; i++) {
                if (nmbCircularPrime(i)) //if the numLinkedListber is a circular prime 
                {
                    ret.add(i);// return the number
                }
            }
            return ret;
        } else {
            Integer half = from + ((to - from) / 2);
            ThreadCircularPrimes firstHalf = new ThreadCircularPrimes(from, half);
            ThreadCircularPrimes secondHalf = new ThreadCircularPrimes(half + 1, to);
            firstHalf.start();
            secondHalf.start();
            try {
                firstHalf.join();
                secondHalf.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(ThreadCircularPrimes.class.getName()).log(Level.SEVERE, null, ex);
            }
            for (int i = 0; i < secondHalf.circularPrimes.size(); i++) {
                firstHalf.circularPrimes.add(secondHalf.circularPrimes.get(i));
            }
            return firstHalf.circularPrimes;
        }
    }

    public void run() {
        this.circularPrimes = calcCircularPrimes(from, to);
    }

    public LinkedList<Integer> getCircularPrimes() {
        return circularPrimes;
    }

    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ThreadCircularPrimes t = null;
        switch (args.length) {
            case 0:
                t = new ThreadCircularPrimes(2, 1000000); //calculate circular primes from two to one millon        }
                t.start();
                break;
            case 1:
                t = new ThreadCircularPrimes(2, Integer.valueOf(args[0])); //calculate circular primes from two to one millon        }
                t.start();
                break;
            case 2:
                t = new ThreadCircularPrimes(Integer.valueOf(args[0]), Integer.valueOf(args[1])); //calculate circular primes from two to one millon        }
                t.start();
                break;
            default: 
                    System.out.println("param error! \n, possible uses: ");
                    System.out.println("java ThreadCircularPrimes");
                    System.out.println("java ThreadCircularPrimes 'MAX_NUMBER'");
                    System.out.println("java ThreadCircularPrimes 'MIN_NUMBER' 'MAX_NUMBER'");
                    System.exit(1);
        }
        try {
            t.join(); //wait for complete
        } catch (InterruptedException ex) {
            Logger.getLogger(ThreadCircularPrimes.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("the circular primes are : \n" + t.getCircularPrimes());
        System.out.println("in total are : " + t.getCircularPrimes().size());
    }
}
