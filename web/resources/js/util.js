var featureLayers;
var selectedFeatures;

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

    updateFeatureItems(0);
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

    if(selectedFeatures!==undefined) {
        var selectedItem = selectedFeatures[featureLayers[layersInd].path];

        $('#featureItems').find('option[value="' + selectedItem + '"]')
            .prop('selected', true);
    };
}

function performUpload(input){
    form_data = new FormData(document.getElementById("fileForm"));

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

function performSubmit(input){
    $.ajax({
        url: "/upload",
        type: "POST",
        data: JSON.stringify(selectedFeatures),
        async:true,
        // contentType: 'application/json',
        success: function(data){
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
