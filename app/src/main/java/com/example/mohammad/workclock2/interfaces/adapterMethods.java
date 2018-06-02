package com.example.mohammad.workclock2.interfaces;

import java.util.ArrayList;

/**
 * Created by Mohammad on 3/23/2018.
 */

public interface adapterMethods {

    public <T>void Add(T item);

    public <T>void Updated(T item);

    public void removeWithPosition(int position);

    public void removeWithIdentifier(long Id);

    public <T>void OnDataUpDate(ArrayList<T> listProject);

}
