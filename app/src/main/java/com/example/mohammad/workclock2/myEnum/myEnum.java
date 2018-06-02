package com.example.mohammad.workclock2.myEnum;

/**
 * Created by Mohammad on 3/22/2018.
 */

public class myEnum {

    public enum TypeInsert{
        Automatic(0),
        Handy(1),
        Timer(2);
        private final int Id;
        TypeInsert(int myId) {
            this.Id = myId;
        }

        public int getValue(){return this.Id;}
    }

    public enum TypeInOut{
        fadeIn,
        fadeOut
    }
}
