package Server.Domain.UserManager;

import Server.Domain.ExternalComponents.PaymentSystem;

/**
 * This class in the only class who communicates with the external payment system,
 * and should get one as parameter when created
 */

public class PaymentSystemAdapter
{
    private PaymentSystem externalSystem;

    private PaymentSystemAdapter(PaymentSystem externalSystem)
    {
        this.externalSystem = externalSystem;
    }

    // Inner class to provide instance of class
    private static class CreateThreadSafeSingleton
    {
        private static final PaymentSystemAdapter INSTANCE = new PaymentSystemAdapter(new PaymentSystem());
    }

    public static PaymentSystemAdapter getInstance()
    {
        return CreateThreadSafeSingleton.INSTANCE;
    }

    /*
    pay --amount-- shekels taken from --bankAccount--
     */
    public void pay (double price, String bankAccount){
        externalSystem.pay(price, bankAccount);
    }

    public boolean canPay(double price, String bankAccount){
        if (price<0) return false;
        return externalSystem.canPay(price, bankAccount);
    }

}