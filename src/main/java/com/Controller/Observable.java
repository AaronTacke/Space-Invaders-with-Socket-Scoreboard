package com.Controller;



import com.Model.Observer;

import java.util.LinkedList;
import java.util.List;

public abstract class Observable {
    List<Observer> observers;
    public Observable(){
        observers = new LinkedList<>();
    }

    public void addObserver(Observer o){
        observers.add(o);
    }

    public void clearObservers(){
        observers.clear();
    }

    public int observerCount(){
        return observers.size();
    }

    public void notifyObservers(char information){
        for(Observer o: observers) o.update(information);
    }
}
