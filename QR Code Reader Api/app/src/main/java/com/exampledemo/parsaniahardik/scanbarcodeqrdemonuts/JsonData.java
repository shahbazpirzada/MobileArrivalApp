package com.exampledemo.parsaniahardik.scanbarcodeqrdemonuts;

/**
 * Created by Stech on 4/5/2018.
 */

public class JsonData {

    private  static JsonData  jData;

    String CustomerName;
    String BranchName;
    String TicketID;
    String ServiceName;

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getBranchName() {
        return BranchName;
    }

    public void setBranchName(String branchName) {
        BranchName = branchName;
    }

    public String getTicketID() {
        return TicketID;
    }

    public void setTicketID(String ticketID) {
        TicketID = ticketID;
    }

    public String getServiceName() {
        return ServiceName;
    }

    public void setServiceName(String serviceName) {
        ServiceName = serviceName;
    }

  public static synchronized JsonData getInstance(){
        if (jData == null){
            jData = new JsonData();
        }

        return jData;

  }





}
