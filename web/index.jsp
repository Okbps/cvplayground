<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<jsp:include page="headTag.jsp"/>
<body>

<form id="fileForm" runat="server">
    <label for="sampleFile">Select image</label>
    <input id="sampleFile" name="sampleFile" type="file" />
    <input id="uploadBtn" type="button" value="Submit" onClick="performSubmit();"></input>
    <br>

    <div class="divTable">
        <div class="divTableBody">
            <div class="divTableRow">
                <div class="divTableCell"><img id="srcImage" alt="source image" height="300" width="300"/></div>
                <div class="divTableCell"><img id="modImage" alt="modified image" height="300" width="300" /></div>
            </div>
        </div>
    </div>
</form>

</body>
</html>