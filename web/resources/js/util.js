var person;
var featureLayers;

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

    if(person!==undefined) {
        var selectedItem = person['selectedFeatures'][featureLayers[layersInd].path];

        $('#featureItems').find('option[value="' + selectedItem + '"]')
            .prop('selected', true);
    };
}

function updatePerson(layersInd, itemsInd){
    person['selectedFeatures'][featureLayers[layersInd].path] = featureLayers[layersInd].fileNames[itemsInd];
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
            person = $.parseJSON(data);
            $('#avImage').attr('src', '/upload?img='+person['fileName']+"&timestamp=" + new Date().getTime());
        }
    });
}

function performSubmit(input){
    $.ajax({
        url: "/upload",
        type: "POST",
        data: JSON.stringify(person),
        async:true,
        contentType: 'application/json',
        success: function(data){
            $('#avImage').attr('src', '/upload?img='+person['fileName']+"&timestamp=" + new Date().getTime());
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
