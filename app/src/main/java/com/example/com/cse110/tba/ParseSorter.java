package com.example.com.cse110.tba;

import android.util.Log;

import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Joshua Lynch on 4/27/2015.
 */
public class ParseSorter
{

    public ParseSorter () {
        Log.d("ParseSorter", "NEW PARSESORTER");
    }

    public List<ParseObject> sortListings(List<ParseObject> unsorted, String sortField, String type,
                                          int direction)
    {
        List<ParseObject> finalList = new ArrayList<ParseObject>();

        if(sortField.equals("Date"))
        {


            while(!unsorted.isEmpty()) //perform operation until all elements are moved to new List
            {
                ParseObject rank = new ParseObject(type);
                Date dummyDate = new Date();
                dummyDate.setTime(0);
                rank.put("createdAt", dummyDate);
                for(ParseObject d: unsorted)
                {
                    long date = d.getCreatedAt().getTime();
                    long otherDate = dummyDate.getTime();
                    //long otherDate = 0;

                    if(date >= otherDate)
                    {
                        rank = d;
                        dummyDate.setTime(d.getCreatedAt().getTime());
                    }

                }
                finalList.add(rank);

                unsorted.remove(unsorted.indexOf(rank));

            }
        }
        while(!unsorted.isEmpty()) //perform operation until all elements are moved to new List
        {
            ParseObject rank = new ParseObject(type);
            rank.put(sortField, 0);
            for(ParseObject d: unsorted)
            {
                if(d.getDouble(sortField) <= rank.getDouble(sortField))
                {
                    rank.put(sortField, d);
                }

            }
            finalList.add(rank);

            unsorted.remove(unsorted.indexOf(rank));

            //finalList =  mergeSort(unsorted, sortField);

        }
        if(direction == 1)
        {
            List<ParseObject> moreFinalList = new ArrayList<ParseObject>();
            for(int i = finalList.size() - 1; i >= 0; i--)
            {
                moreFinalList.add(finalList.get(i));
            }
            return moreFinalList;
        }
        return finalList;
    }

    public List<ParseObject> mergeSort(List<ParseObject> whole, String sortField)
    {
        List<ParseObject> left = new ArrayList<ParseObject>();
        List<ParseObject> right = new ArrayList<ParseObject>();
        int center;

        if(whole.size() == 1)
            return whole;
        else
        {
            center = whole.size() / 2;
            // copy the left half of whole into the left.
            for(int i=0; i<center; i++)
            {
                left.add(whole.get(i));
            }

            //copy the right half of whole into the new arraylist.
            for(int i=center; i<whole.size(); i++)
            {
                right.add(whole.get(i));
            }

            // Sort the left and right halves of the arraylist.
            left  = mergeSort(left, sortField);
            right = mergeSort(right, sortField);


            // Merge the results back together.
            merge(left, right, whole, sortField);

        }
        return whole;
    }

    private void merge(List<ParseObject> left, List<ParseObject> right, List<ParseObject> whole,
                       String sortField)
    {
        int leftIndex = 0;
        int rightIndex = 0;
        int wholeIndex = 0;

        while (leftIndex < left.size() && rightIndex < right.size())
        {
            if (left.get(leftIndex).getDouble(sortField) < right.get(rightIndex).getDouble(sortField))
            {
                whole.set(wholeIndex,left.get(leftIndex));
                leftIndex++;
            }
            else
            {
                whole.set(wholeIndex, right.get(rightIndex));
                rightIndex++;
            }
            wholeIndex++;
        }

        List<ParseObject> rest = new ArrayList<ParseObject>();
        int restIndex;
        if (leftIndex >= left.size())
        {
            rest = right;
            restIndex = rightIndex;
        }
        else
        {
            rest = left;
            restIndex = leftIndex;
        }

        for (int i = restIndex; i < rest.size(); i++)
        {
            whole.set(wholeIndex, rest.get(i));
            wholeIndex++;
        }
    }
}
