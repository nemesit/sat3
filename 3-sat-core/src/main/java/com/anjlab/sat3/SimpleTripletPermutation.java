/*
 * Copyright (c) 2010 AnjLab
 * 
 * This file is part of 
 * Reference Implementation of Romanov's Polynomial Algorithm for 3-SAT Problem.
 * 
 * Reference Implementation of Romanov's Polynomial Algorithm for 3-SAT Problem 
 * is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Reference Implementation of Romanov's Polynomial Algorithm for 3-SAT Problem
 * is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with
 * Reference Implementation of Romanov's Polynomial Algorithm for 3-SAT Problem.
 * If not, see <http://www.gnu.org/licenses/>.
 */
package com.anjlab.sat3;

public class SimpleTripletPermutation implements ITripletPermutation
{
    private final static int _2_power_21 = 2097152;
    
    private int[] abc;

    private int[] canonicalName;
    private long canonicalHashCode;
    
    SimpleTripletPermutation(int[] abc, int[] canonicalName, long canonicalHashCode)
    {
        this.abc = abc;
        this.canonicalName = canonicalName;
        this.canonicalHashCode = canonicalHashCode;
    }
    
    public SimpleTripletPermutation(int a, int b, int c)
    {
        abc = new int[] {a, b, c};
        
        if (a <= 0 || b <= 0 || c <= 0)
            throw new IllegalArgumentException("a <= 0 || b <= 0 || c <= 0 (" + this + ")");
        
        if (a == b || b == c || a == c)
            throw new IllegalArgumentException("a == b || b == c || a == c (" + this + ")");

        setCanonicalAttributes(a, b, c);
    }

    final void setCanonicalAttributes(int a, int b, int c)
    {
        if (a < b)
        {
            if (b < c) canonicalName = new int[] {a, b, c}; else
            if (c < a) canonicalName = new int[] {c, a, b}; else
                       canonicalName = new int[] {a, c, b};
        }
        else
        {
            if (c < b) canonicalName = new int[] {c, b, a}; else
            if (a < c) canonicalName = new int[] {b, a, c}; else
                       canonicalName = new int[] {b, c, a};
        }
        
        //    Fitting three integer to 64-bit long
        //    requires each integer be <= 2^21

        if (a > _2_power_21 || b > _2_power_21 || c > _2_power_21)
            throw new IndexOutOfBoundsException("a > _2_power_21 || b > _2_power_21 || c > _2_power_21 (" + this + ")");

        canonicalHashCode = (long)((long) canonicalName[2] << (21 * 2))
                          | (long)((long) canonicalName[1] << 21)
                          | (canonicalName[0]);
    }

    public final int getAName()
    {
        return abc[0];
    }

    public final int getBName()
    {
        return abc[1];
    }

    public final int getCName()
    {
        return abc[2];
    }

    public final int[] getABC()
    {
        return abc;
    }

    public final int[] getCanonicalName()
    {
        return canonicalName;
    }

    public final long canonicalHashCode()
    {
        return canonicalHashCode;
    }

    public final boolean hasSameVariablesAs(ITripletPermutation permutation)
    {
        return canonicalHashCode == permutation.canonicalHashCode();
    }

    public final boolean hasVariable(int varName)
    {
        return abc[0] == varName || abc[1] == varName || abc[2] == varName;
    }

    public final void transposeTo(ITripletPermutation targetPermutation)
    {
        if (Helper.EnableAssertions)
        {
            if (!hasSameVariablesAs(targetPermutation))
            {
                throw new IllegalArgumentException(targetPermutation 
                                                   + " should have same variables as " 
                                                   + abc[0] + "," + abc[1] + "," + abc[2]);
            }
        }
        
        transpose(targetPermutation.getAName(), targetPermutation.getBName());
    }

    public final void transposeTo(int targetA, int targetB, int targetC)
    {
        if (Helper.EnableAssertions)
        {
            if (!(hasVariable(targetA) && hasVariable(targetB) && hasVariable(targetC)))
            {
                throw new IllegalArgumentException(targetA + "," + targetB + "," + targetC 
                                                   + " should have same variables as " 
                                                   + abc[0] + "," + abc[1] + "," + abc[2]);
            }
        }
        
        transpose(targetA, targetB);
    }

    public final void transposeTo(int[] targetABC)
    {
        if (Helper.EnableAssertions)
        {
            if (targetABC.length != 3)
            {
                throw new IllegalArgumentException("abc.length != 3");
            }
            if (!(hasVariable(targetABC[0]) && hasVariable(targetABC[1]) && hasVariable(targetABC[2])))
            {
                throw new IllegalArgumentException(targetABC[0] + "," + targetABC[1] + "," + targetABC[2] 
                                                   + " should have same variables as " 
                                                   + abc[0] + "," + abc[1] + "," + abc[2]);
            }
        }
        transpose(targetABC[0], targetABC[1]);
    }

    private void transpose(int targetA, int targetB)
    {
        if (targetA != abc[0])
        {
            if (targetA == abc[1]) swapAB(); else swapAC();
        }
        if (targetB != abc[1])
        {
            if (targetB == abc[0]) swapAB(); else swapBC();
        }
        if (targetA != abc[0])
        {
            if (targetA == abc[1]) swapAB(); else swapAC();
        }
    }
    
    public void swapAB()
    {
        int t = abc[0];
        abc[0] = abc[1];
        abc[1] = t;
    }

    public void swapAC()
    {
        int t = abc[0];
        abc[0] = abc[2];
        abc[2] = t;
    }

    public void swapBC()
    {
        int t = abc[1];
        abc[1] = abc[2];
        abc[2] = t;
    }

    public String toString()
    {
        return abc[0] + "," + abc[1] + "," + abc[2];
    }
}
