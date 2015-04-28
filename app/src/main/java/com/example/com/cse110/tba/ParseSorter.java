package com.example.com.cse110.tba;

import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Joshua Lynch on 4/27/2015.
 */
public class ParseSorter
{
    public List<ParseObject> sortListings(List<ParseObject> unsorted, String sortField, String type)
    {
        List<ParseObject> finalList = new ArrayList<>();

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
                    long otherDate = rank.getCreatedAt().getTime();
                    if(date >= otherDate)
                    {
                        rank = d;
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
                if((float)(d.get(sortField)) >= (float)(rank.get(sortField)))
                {
                    rank.put(sortField, d);
                }

            }
            finalList.add(rank);

            unsorted.remove(unsorted.indexOf(rank));

        }
        return finalList;
    }
}
