<html>
<head>
<title>Vear GAL Integration Run on ${vearStatsInitPull.runDate} </title>
</head>
<body style='font-family="sans-serif"'>
<div>
Summary of the Counts from VEAR GAL Integration Run on ${vearStatsInitPull.runDate}: <br>
<div>
<br>

New Additions: <br> <br>
Total New Records Read from VEAR: ${vearStatsInitPull.vearRecordsRead}
<br>
Total Records Populated with GAL Info: ${vearStatsInitPull.vearUpdatedRecords} <br>
	<#if (vearStatsInitPull.vearUpdatedRecords != 0)> 
	<br> List of Newly Populated VEAR Accounts: <br>
	 <table border="1">
		  <tr>
		    <th>VEAR Stakeholder Id</th>
		    <th>VA User Name</th>
		    <th>First Name</th>
		    <th>Last Name</th>
		    <th>Title</th>
		    <th>Email</th>
		    <th>Phone</th>
		  </tr>
		  <#list vearStatsInitPull.addedPersons as addedPerson>
		  <tr>
		    <td>${addedPerson.getStakeholderId()?c}</td>
		    <td>${addedPerson.getVaUserName()}</td>
		    <td>${addedPerson.getFirstName()}</td>
		    <td>${addedPerson.getLastName()}</td>
		    <#if (addedPerson.getTitle()??)>
		    	<td>${addedPerson.getTitle()}</td>
		    <#else>
		    	<td></td>
		    </#if>
		    <td>${addedPerson.getEmail()}</td>
		    <#if (addedPerson.getTelephoneNumber()??)>
		    	<td>${addedPerson.getTelephoneNumber()}</td>
		    <#else>
		    	<td></td>
		    </#if>
		    
		  </tr>
		</#list>
	</table>
	</#if>	

<#if (vearStatsInitPull.vearPeronsNotFound != 0)>	
	<br><br>
	Errors Found = ${vearStatsInitPull.vearPeronsNotFound}, Invalid VA User Name Records listed: <br>
	<table border="1">
	  <tr>
	    <th>VEAR Stakeholder Id</th>
	    <th>VA User Name</th>
	    <th>First Name</th>
	    <th>Last Name</th>
	    <th>Title</th>
	    <th>Email</th>
	    <th>Phone</th>
	  </tr>
		<#list vearStatsInitPull.notFoundPersons as notFoundPerson>
		<tr>
		    <td>${notFoundPerson.getStakeholderId()?c}</td>
		    <td>${notFoundPerson.getVaUserName()}</td>
		    <td>${notFoundPerson.getFirstName()}</td>
		    <td>${notFoundPerson.getLastName()}</td>
		    <#if (notFoundPerson.getTitle()??)>
		    	<td>${notFoundPerson.getTitle()}</td>
		    <#else>
		    	<td></td>
		    </#if>
		     <#if (notFoundPerson.getEmail()??)>
		    	<td>${notFoundPerson.getEmail()}</td>
		    <#else>
		    	<td></td>
		    </#if>
		      <#if (notFoundPerson.getTelephoneNumber()??)>
		    	<td>${notFoundPerson.getTelephoneNumber()}</td>
		    <#else>
		    	<td></td>
		    </#if>
		    
		</tr>
		</#list>
	</table>
</#if>	

<#if (vearStatsInitPull.vearPersonsNotMapped != 0)>	
	<br><br>
	Errors Found = ${vearStatsInitPull.vearPersonsNotMapped}, Missing VA User Name Records listed: <br>
	<table border="1">
	  <tr>
	    <th>VEAR Stakeholder Id</th>
	    <th>VA User Name</th>
	    <th>First Name</th>
	    <th>Last Name</th>
	    <th>Title</th>
	    <th>Email</th>
	    <th>Phone</th>
	  </tr>
		<#list vearStatsInitPull.notMappedPersons as notMappedPerson>
			<tr>
			    <td>${notMappedPerson.getStakeholderId()?c}</td>
			    <#if (notMappedPerson.getVaUserName()??)>
		    		<td>${notMappedPerson.getVaUserName()}</td>
		    	<#else>
		    		<td></td>
		    	</#if>
			    
			    <td>${notMappedPerson.getFirstName()}</td>
			    <td>${notMappedPerson.getLastName()}</td>
			    <#if (notMappedPerson.getTitle()??)>
		    		<td>${notMappedPerson.getTitle()}</td>
		    	<#else>
		    		<td></td>
		    	</#if>
			    <#if (notMappedPerson.getEmail()??)>
		    		<td>${notMappedPerson.getEmail()}</td>
		    	<#else>
		    		<td></td>
		    	</#if>
			    <#if (notMappedPerson.getTelephoneNumber()??)>
		    		<td>${notMappedPerson.getTelephoneNumber()}</td>
		    	<#else>
		    		<td></td>
		    	</#if>
			    
			</tr>
		</#list>
	</table>
</#if>	

</div>
<br><br>
<div>
Updates: <br><br>
Total Records Read from VEAR: ${vearStatsCompare.vearRecordsRead}<br>
Total Records Updated: ${vearStatsCompare.vearUpdatedRecords} <br>

<#if (vearStatsCompare.vearUpdatedRecords != 0)>
	 <br> List of Updated VEAR Accounts: <br>
	 <table border="1">
		  <tr>
		    <th>VEAR Stakeholder Id</th>
		    <th>VA User Name</th>
		    <th>First Name</th>
		    <th>Last Name</th>
		    <th>Title</th>
		    <th>Email</th>
		    <th>Phone</th>
		  </tr>
		  <#list vearStatsCompare.updatedPersons as updatedPerson>
		  <tr>
		    <td>${updatedPerson.getStakeholderId()?c}</td>
		    <td>${updatedPerson.getVaUserName()}</td>
		    <#if (updatedPerson.getFirstName()??)>
		    	<td>${updatedPerson.getFirstName()}</td>
		    <#else>
		    	<td></td>
		    </#if>
		    <#if (updatedPerson.getLastName()??)>
		    	<td>${updatedPerson.getLastName()}</td>
		    <#else>
		    	<td></td>
		    </#if>
			<#if (updatedPerson.getTitle()??)>
		    	<td>${updatedPerson.getTitle()}</td>
		    <#else>
		    	<td></td>
		    </#if>
		    
		    <td>${updatedPerson.getEmail()}</td>
		    <#if (updatedPerson.getTelephoneNumber()??)>
		    	<td>${updatedPerson.getTelephoneNumber()}</td>
		    <#else>
		    	<td></td>
		    </#if>
		  </tr>
		</#list>
	</table>
</#if>
</div>
<br><br>
<div>
<br> Disabled Records: <br><br>
Total Disabled Records Found = ${vearStatsCompare.vearDisableRecords} <br>
<#if (vearStatsCompare.vearDisableRecords != 0)>	
	 List of Disabled VEAR Accounts: <br>
	 <table border="1">
		  <tr>
		    <th>VEAR Stakeholder Id</th>
		    <th>VA User Name</th>
		    <th>First Name</th>
		    <th>Last Name</th>
		    <th>Title</th>
		    <th>Email</th>
		    <th>Phone</th>
		  </tr>
		  <#list vearStatsCompare.disabledPersons as disabledPerson>
		  <tr>
		    <td>${disabledPerson.getStakeholderId()?c}</td>
		    <td>${disabledPerson.getVaUserName()}</td>
		    <td>${disabledPerson.getFirstName()}</td>
		    <td>${disabledPerson.getLastName()}</td>
		    <#if (disabledPerson.getTitle()??)>
		    	<td>${disabledPerson.getTitle()}</td>
		    <#else>
		    	<td></td>
		    </#if>
		    <#if (disabledPerson.getEmail()??)>
		    	<td>${disabledPerson.getEmail()}</td>
		    <#else>
		    	<td></td>
		    </#if>
		    
		    <#if (disabledPerson.getTelephoneNumber()??)>
		    	<td>${disabledPerson.getTelephoneNumber()}</td>
		    <#else>
		    	<td></td>
		    </#if>
		    
		  </tr>
		</#list>
	</table>
</#if>

</div>

</body>

</html>