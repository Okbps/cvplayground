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
    uploadFileOnServer(this);
});

$("#updatePersonBtn").click(function () {
    updatePersonOnServer(this);
});

$("#updateSetBtn").click(function () {
    updateTrainingSet(this);
});