package com.example.com.cse110.tba;

import com.parse.ParseObject;

import java.util.List;

/**
 * Created by Joshua Lynch on 4/26/2015.
 */
public interface DBAsync
{
    abstract void onGetBooksLoad(List<ParseObject> result);
}
