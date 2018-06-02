package com.example.mohammad.workclock2.interfaces;

import java.io.Serializable;

/**
 * Created by Mohammad on 3/22/2018.
 */

public interface tableDataBaseListener extends Serializable {
    public <T>void OnItemAdded(T item);
    public <T>void OnItemDeleted(T item);
    public <T>void OnItemUpdated(T item);
}
