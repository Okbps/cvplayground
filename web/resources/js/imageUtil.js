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
            // $('#modImage').attr('src', '/upload?img='+arr[0]+"&timestamp=" + new Date().getTime());
            $('#avImage').attr('src', '/upload?img='+arr[1]+"&timestamp=" + new Date().getTime());
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
    }
}

$("#sampleFile").change(function(){
    readURL(this);
});
