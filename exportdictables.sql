

--bcp sql dictionary tables to text file
declare @sql varchar(8000)
select @sql = 'bcp information_schema.syscolumns out
       c:\bcp\syscolumns.txt -c -t| -T -S' + '@@servernameexecmaster..xp_cmdshell@sql'

--create view to bcp the data from dictionary tables to text file
create view swe_vw_bcpMasterSysobjects
as
  select top 100 percent
      name ,
      crdate = convert(varchar(8), crdate, 112) ,
      crtime = convert(varchar(8), crdate, 108)
   from master..sysobjects
   order by crdate desc
go
declare @sql varchar(8000)
select @sql = 'bcp tempdb..vw_bcpMasterSysobjects out
                 c:\bcp\sysobjects.txt -c -t, -T -S' + @@servername
exec master..xp_cmdshell @sql