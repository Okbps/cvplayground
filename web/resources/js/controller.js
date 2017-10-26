$(function () {
    $.get("upload?json", updateFeatureLayers);
});

$("#sampleFile").change(function(){
    readURL(this);
});

$("#featureLayers").change(function () {
    updateFeatureItems(this.selectedIndex);
});

$("#uploadBtn").click(function () {
    performUpload(this);
});

$("#submitBtn").click(function () {
    performSubmit(this);
});