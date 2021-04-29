package Server.Domain.UserManager;

import Server.Domain.CommonClasses.Response;

import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Appointment {

    private Map<Integer, List<String>> storeAppointments;

    private ReadWriteLock lock;
    private Lock writeLock;
    private Lock readLock;


    public Appointment() {
        this.storeAppointments = new ConcurrentHashMap<>();

        lock = new ReentrantReadWriteLock();
        writeLock = lock.writeLock();
        readLock = lock.readLock();
    }

    public void addAppointment(int storeId, String name){
        writeLock.lock();
        if(this.storeAppointments.containsKey(storeId))
            this.storeAppointments.get(storeId).add(name);
        else{
            List<String> appsList = new Vector<>();
            appsList.add(name);
            this.storeAppointments.put(storeId, appsList);
        }
        writeLock.unlock();
    }

    public Response<String> removeAppointment(int storeId, String name){
        Response<String> response;
        writeLock.lock();
        if(this.storeAppointments.containsKey(storeId) && this.storeAppointments.get(storeId).contains(name)) {
            response = new Response<>(name, false, "");
            this.storeAppointments.get(storeId).remove(name);
            Publisher.getInstance().notify(name, "Your ownership canceled at store "+ storeId);
        }
        else{
            response = new Response<>(null, true, "Tried removing appointment for nonexistent user");
        }
        writeLock.unlock();
        return response;
    }

    public Response<List<String>> getAppointees(int storeId){
        Response<List<String>> response;
        readLock.lock();
        if(this.storeAppointments.containsKey(storeId)){
            response = new Response<>(this.storeAppointments.get(storeId), false, "");
        }
        else response = new Response<>(null, true, "No appointments for given store");
        readLock.unlock();
        return response;
    }

    public boolean contains(int storeId, String appointee){
        readLock.lock();
        if(this.storeAppointments.containsKey(storeId)){
            boolean result = this.storeAppointments.get(storeId).contains(appointee);
            readLock.unlock();
            return result;
        }
        readLock.unlock();
        return false;
    }
}