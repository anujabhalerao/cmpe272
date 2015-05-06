package com.example.vinothbaalaji.pollencarefinal;

import com.ibm.mobile.services.data.IBMDataObject;
import com.ibm.mobile.services.data.IBMDataObjectSpecialization;

/**
 * Created by VinothBaalaji on 4/26/2015.
 */

@IBMDataObjectSpecialization("DeviceItem")
public class DeviceItem extends IBMDataObject {
    public static final String VALUE = "VALUE";

    public void setValue(String value){
        setObject(VALUE,value);
    }

    public String getValue(){
        return (String)getObject(VALUE);
    }
}
