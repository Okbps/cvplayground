<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<jsp:include page="headTag.jsp"/>
<body>

<form id="fileForm" runat="server">
    <label for="sampleFile">Select image</label>
    <input id="sampleFile" name="sampleFile" type="file" />

    <input id="uploadBtn" type="button" value="Upload">
    <br>

    <div class="divTable">
        <div class="divTableBody">
            <div class="divTableRow">
                <div class="divTableCell"><img id="srcImage" alt="source image" /></div>
            </div>
        </div>
    </div>

    <div class="divTable">
        <div class="divTableBody">
            <div class="divTableRow">
                <div class="divTableCell">
                    <img id="personImage" alt="person image"/>
                </div>

                <div class="divTableCell" style="vertical-align: top">
                    <select id="featureLayers">
                        <option>body</option>
                        <option>head</option>
                    </select>
                    <br>

                    <select id="featureItems">
                        <option>no items</option>
                    </select>
                    <br>
                    <br>

                    <input id="updatePersonBtn" type="button" value="Update person">
                    <br>
                    <br>
                    <input id="updateSetBtn" type="button" value="Update training set">
                </div>
            </div>
        </div>
    </div>

</form>

</body>
</html>