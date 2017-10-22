<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<jsp:include page="headTag.jsp"/>
<body>

<form id="fileForm" runat="server">
    <label for="sampleFile">Select image</label>
    <input id="sampleFile" name="sampleFile" type="file" />

    <%--<label for="hatGrowthFactor">Hat factor</label>--%>
    <%--<input id="hatGrowthFactor" name="hatGrowthFactor" type="number" step="0.01" min="0" value="3.0">--%>

    <%--<label for="hatOffsetY">Hat Y-offset</label>--%>
    <%--<input id="hatOffsetY" name="hatOffsetY" type="number" step="0.01" value="0.8">--%>

    <input id="uploadBtn" type="button" value="Upload" onClick="performSubmit();">
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
                    <img id="avImage" alt="avatar image"/>
                </div>

                <div class="divTableCell" style="vertical-align: top">
                    <select id="featureLayers">
                        <option>body</option>
                        <option>head</option>
                    </select>
                    <br>

                    <button type="button"><</button>

                    <label id="featureItem">
                        <select id="featureItems">
                            <option>no items</option>
                        </select>
                    </label>

                    <button type="button">></button>
                    <br>
                    <br>
                    <input id="submitBtn" type="button" value="Submit" onClick="">
                </div>
            </div>
        </div>
    </div>

</form>

</body>
</html>