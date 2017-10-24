var featureLayers;
var selectedFeatures;

$(function () {
    $.get("upload?json", updateFeatureLayers);
});

function updateFeatureLayers(data) {
    featureLayers = $.parseJSON(data);
    $('#featureLayers')
        .find('option')
        .remove()
        .end();

    $.each(featureLayers, function (i, item) {
        $('#featureLayers')
            .append($('<option>', {
                value: item.path,
                text: item.alias
            }))
    });
}

function updateFeatureItems(layersInd){
    $('#featureItems')
        .find('option')
        .remove()
        .end();

    featureLayers[layersInd].fileNames
        .forEach(function (t) {
            $('#featureItems')
                .append($('<option>', {
                    value: t,
                    text: t
                }))
        });

    var selectedItem = selectedFeatures[featureLayers[layersInd].path];

    $('#featureItems option[value="'+selectedItem+'"]')
        .prop('selected', true);
}

function  performSubmit(input){
    form_data = new FormData(document.getElementById("fileForm"));

    var x = new XMLHttpRequest();

    $.ajax({
        url: "/upload",
        type: "POST",
        data: form_data,
        enctype: 'multipart/form-data',
        processData: false,
        contentType: false,
        async:true,
        datatype: 'image/jpeg',
        success: function(data){
            var arr = $.parseJSON(data);
            $('#avImage').attr('src', '/upload?img='+arr['fileName']+"&timestamp=" + new Date().getTime());
            selectedFeatures = arr['selectedFeatures'];
        }
    });
}

function readURL(input) {

    if (input.files && input.files[0]) {
        var reader = new FileReader();

        reader.onload = function (e) {
            $('#srcImage').attr('src', e.target.result);
        };

        reader.readAsDataURL(input.files[0]);
    };
}

$("#sampleFile").change(function(){
    readURL(this);
});

$("#featureLayers").change(function () {
    updateFeatureItems(this.selectedIndex);
});