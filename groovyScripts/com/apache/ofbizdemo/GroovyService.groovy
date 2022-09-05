import org.apache.ofbiz.entity.GenericEntityException;

def createEmployee()
{
    result = [:];
    String employeeId = (String) context.get("employeeId");

    try {
        Employee = delegator.makeValue("Employee");
        employeeId = from("employeeId").where("employeeId",employeeId).queryOne();

        if(employeeId == null)
        {

            Employee.setNextSeqId();
            Employee.setPKFields(context);

        }
        else
        {
            Employee.put("employeeId",employeeId);
        }

        Employee.setNonPKFields(context);
        Employee = delegator.createOrStore(Employee);

        result.employeeId = Employee.employeeId;

    } catch (GenericEntityException e) {
        logError(e.getMessage());
        return error("Record not created");
    }

    return result;
}