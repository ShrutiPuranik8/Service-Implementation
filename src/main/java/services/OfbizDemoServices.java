package com.Apache.ofbizdemo.services;
import java.util.Map;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.ServiceUtil;

//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;

//import org.apache.ofbiz.base.util.Debug;
//import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilValidate;
//import org.apache.ofbiz.entity.Delegator;
//import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
//import org.apache.ofbiz.service.ServiceUtil;
//import org.apache.ofbiz.service.DispatchContext;
//import java.util.Map;
import java.util.HashMap;


public class OfbizDemoServices {

    public static final String module = OfbizDemoServices.class.getName();

    public static Map<String, Object> createOfbizDemo(DispatchContext dctx, Map<String, ? extends Object> context) {
        Map<String, Object> result = ServiceUtil.returnSuccess();
        Delegator delegator = dctx.getDelegator();
        try {
            GenericValue ofbizDemo = delegator.makeValue("OfbizDemo");
            // Auto generating next sequence of ofbizDemoId primary key
            ofbizDemo.setNextSeqId();
            // Setting up all non primary key field values from context map
            ofbizDemo.setNonPKFields(context);
            // Creating record in database for OfbizDemo entity for prepared value
            ofbizDemo = delegator.create(ofbizDemo);
            result.put("ofbizDemoId", ofbizDemo.getString("ofbizDemoId"));
            Debug.log("==========This is my first Java Service implementation in Apache OFBiz. OfbizDemo record created successfully with ofbizDemoId: "+ofbizDemo.getString("ofbizDemoId"));
        } catch (GenericEntityException e) {
            Debug.logError(e, module);
            return ServiceUtil.returnError("Error in creating record in OfbizDemo entity ........" +module);
        }
        return result;
    }
    public static Map<String, Object> createEmployee(DispatchContext dctx, Map<String, ? extends Object> context) {
        Map<String, Object> result = ServiceUtil.returnSuccess();
        Delegator delegator = dctx.getDelegator();
        String employeeId = (String) context.get("employeeId");
        try {
            GenericValue Employee = delegator.makeValue("Employee");
            if (UtilValidate.isNotEmpty(employeeId)) {
                //for updating
                Employee.set("employeeId", employeeId);
            } else {
                //for creating new data
                // Auto generating next sequence of ofbizDemoId primary key
                Employee.setNextSeqId();
            }
            // Setting up all non primary key field values from context map
            Employee.setNonPKFields(context);
            // Creating record in database for OfbizDemo entity for prepared value
            Employee = delegator.createOrStore(Employee);
            result.put("employeeId", Employee.getString("employeeId"));
        } catch (GenericEntityException e) {
            Debug.logError(e, module);
            return ServiceUtil.returnError("Error in creating record in OfbizDemo entity ........" +module);
        }
        return result;
    }

    public static Map<String, Object> CustomerEvent1(DispatchContext dctx, Map<String, ? extends Object> context) {

        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        GenericValue userLogin = (GenericValue) context.get("userLogin");


        String lastName = (String) context.get("lastName");
        String firstName = (String) context.get("firstName");
        String email = (String) context.get("email");
        Map<String, Object> map = new HashMap<>();
        map.put("firstName", firstName);
        map.put("lastName", lastName);

        Map<String, Object> result1= new HashMap<>();
        try
        {
            map.put("userLogin",userLogin );
             result1 = dispatcher.runSync("createPerson", map);
        }
        catch(GenericServiceException e)
        {
            Debug.logError(e, "exception");
        }

        String partyId = (String) result1.get("partyId");
          map.put("partyId", partyId);

        try
        {
            Map<String, Object> result12 = dispatcher.runSync("getPartyPostalAddress", map);
            String contactMechId=(String) result12.get("contactMechId");
            Map<String, Object> map1 = new HashMap<>();
            map1.put("emailAddress",email);
            map1.put("contactMechId", contactMechId);
            map1.put("userLogin",userLogin );
            Map<String, Object> result121 = dispatcher.runSync("createEmailAddress", map1);
            Debug.log("==============="+email);
            Map<String, Object> contact = new HashMap<>();
            contact.put("areaCode",context.get("area"));
            contact.put("countryCode",context.get("country"));
            contact.put("contactNumber",context.get("contact"));
            contact.put("userLogin",userLogin );

            Map<String, Object> conResult = dispatcher.runSync("createTelecomNumber",contact);

//                Map<String, Object> result3 = dispatcher.runSync("createEmailAddress", map1);
//            Map<String, Object> post = new HashMap<>();
//            post.put("address1",context.get("address"));
//            post.put("city",context.get("city"));
//            post.put("postalCode",context.get("zip"));
//            post.put("countryGeoId",context.get("country"));
//            post.put("state",context.get("state"));
//            post.put("userLogin",userLogin );


        }
        catch(GenericServiceException e)
        {
            Debug.logError(e, "exception");
        }
        return ServiceUtil.returnSuccess();
    }
}