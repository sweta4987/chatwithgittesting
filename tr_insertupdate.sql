
--trigeer to insert and update the data in swetestdata table
CREATE TRIGGER dbo.swecreatetrigger
ON swetestdata
FOR [INSERT]
AS
    select * from swetestdata
    INSERT INTO dbo.swetestdata(EmpID, Operation, UpdatedDate)
    SELECT EmployeeID ,'INSERT',GETDATE() FROM INSERTED;

    INSERT INTO dbo.swetestdata(EmpID, Operation, UpdatedDate)
    SELECT EmployeeID,'UPDATE', GETDATE() FROM DELETED;