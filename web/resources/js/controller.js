$(function () {
    $.get("upload?json", updateFeatureLayers);
});

$("#sampleFile").change(function(){
    readURL(this);
});

$("#featureLayers").change(function () {
    updateFeatureItems(this.selectedIndex);
});

$("#featureItems").change(function () {
   updatePerson($("#featureLayers").prop('selectedIndex'), this.selectedIndex);
});

$("#uploadBtn").click(function () {
    performUpload(this);
});

$("#submitBtn").click(function () {
    performSubmit(this);
});