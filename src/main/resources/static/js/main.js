$(function() {
	var types = {
		"operations": ":tables",
		"tables": "operations:columns,conditions",
		"columns": "tables:",
		"conditions": "tables:",
	}
	
    // Remove button click
    $(document).on(
        'click',
        '[data-role="dynamic-fields"] > .form-wrapper [data-role="remove"]',
        function(e) {
            e.preventDefault();
            $(this).closest('.form-wrapper').remove();
        }
    );
    
    // Add button click
    $(document).on(
        'click',
        '[data-role="dynamic-fields"] > .form-wrapper [data-role="add"]',
        function(e) {
            e.preventDefault();
            var container = $(this).closest('[data-role="dynamic-fields"]');
            var new_field_group = container.children().filter('.form-wrapper:last-child').clone();
            var dataIndex = new_field_group.attr("data-index");
            var dataType = new_field_group.attr("data-type");
            var maxIndex = 1;
            $('[data-type=' + dataType + ']').each(function(index) {
				if ($(this).attr('data-index') > maxIndex) {
					maxIndex = parseInt($(this).attr('data-index'));
				}
			});
            dataIndex = parseInt(dataIndex);
            dataIndex = dataIndex + maxIndex;
            new_field_group.attr("data-index", dataIndex);
            new_field_group.find('input[type=text]').each(function(){
                $(this).val('');
            });
            container.append(new_field_group);
            new_field_group.find("input").each(function (indexitem, itemdata) {
	            var name = $(itemdata).attr("name");
	            if (name !== undefined && name !== null) {
					var tempName = name;
					tempName = tempName.replaceAll('[', ',');
					tempName = tempName.replaceAll(']', '');
					var nameSplit = tempName.split(',');
					var type = nameSplit[0];
					var index = parseInt(nameSplit[1]) + 1;
	                const regex = /\[[\d]+\]/gi;
	                var newname = name.replace(regex, '[' + index + ']');
	                $(itemdata).attr("name", newname);
	            }
	        });
	        
	        var index = {};
	        
	        new_field_group.find("[data-index]").each(function (indexitem, itemdata) {
	        	maxIndex = 1;
				var dataType = $(itemdata).attr("data-type");
				if (index[dataType] == null || index[dataType] == 0) {
					$('[data-type=' + dataType + ']').each(function(idx) {
						if ($(this).attr('data-index') > maxIndex) {
							console.log("masuk if 2 = " + dataType);
							maxIndex = parseInt($(this).attr('data-index'));
						}
						index[dataType] = maxIndex;
					});
				}
				index[dataType] = parseInt(index[dataType]) + 1;
				$(itemdata).attr("data-index", index[dataType]);
			});
			
        }
    );
    
    $(document).on(
        'click',
        '[data-role="reset"]',
        function(e) {
            e.preventDefault();
            $('form').find('input').each(function(){
                $(this).val('');
                
            });
            $("select[name='operation[]']").each(function(){
			    $(this).prop('selectedIndex', 0);
			});
        }
    );
    
    function rec(layer1, type, index) {
		var dataLayer2 = {};
		var dataLayer4 = {};
		var arrIndex = {};
		var arr = [];
		
		var typeSplit = types[type].split(':');
		var typeChild = '';
		var typeChildSplit = '';
		
		if (typeSplit[1] != '') {
			typeChild = typeSplit[1];
			typeChildSplit = typeChild.split(',');
		}
		
		dataLayer2 = layer1[type];
		if (index != 0) {
			dataLayer2 = dataLayer2[index];
			$.each(dataLayer2, function(key, value){
				arrIndex[key] = value;
			});
		} else {
			$.each(dataLayer2, function(key, value){			
				$.each(value, function(key1, value1){
					arrIndex[key] = value1;
				});
			});
		}
		
		if (typeChildSplit != '') {
			$.each(typeChildSplit, function(key, value){
				arr = [];
				$.each(arrIndex, function(key1, value1){
					dataLayer4 = arrIndex[key1];
					dataLayer4[value] = rec(layer1, value, key1);
					arr.push(dataLayer4);
				});
			});
		} else {
			$.each(arrIndex, function(key, value){
				dataLayer4 = value;
				arr.push(dataLayer4);
			});
		}
		
		return arr;
	}
    
    $(document).on(
        'click',
        '[data-role="submit"]',
        function(e) {
            e.preventDefault();
            
            $(this).prop("disabled", true);
            $('[data-role="reset"]').prop("disabled", true);
            $("#spinner-submit").show();
            
            var isComplete = true;
            var data = {};
            
			$("input").each(function(indexitem, itemdata) {
				var name = $(itemdata).attr("name");
			    var values = $('input[name="' + name + '"]').map(function () {
				    return this.value;
				}).get();
			    var id = $(itemdata).attr("id");
			    var val = $(itemdata).val();
			    
			    if ((name) && name !== "" && ((id) && id !== "") && ((val) && val !== "")) {

			    } else {
					isComplete = false;
				}
			});
			
			if (!isComplete) {
				$("#modal-message").text('Harap lengkapi seluruh data');
				$("#btn-modal").click();
				$(this).removeAttr('disabled');
        		$("#spinner-submit").hide();
        		$('[data-role="reset"]').removeAttr('disabled');
				return;
			} else {
				var layer1 = {};
				var layer2 = {};
				var layer3 = {};
				var layer4 = {};
				
				$("select").each(function(indexitem, itemdata) {
					layer2 = {};
					layer3 = {};
					layer4 = {};
					
					var name = $(itemdata).attr("name");
					var value = $(itemdata).val();
					var tempName = name;
					tempName = tempName.replaceAll('[', ',');
					tempName = tempName.replaceAll(']', '');
					var nameSplit = tempName.split(',');
					var type = nameSplit[0];
					var dataIndex = $(this).closest('[data-type="' + type + '"]').attr("data-index");
					
					var dataIndexParent = dataIndex;
					var typeParent = types[type];
					typeParent = typeParent.split(':');
					typeParent = typeParent[0];
					if (typeParent != "") {
						dataIndexParent = $(this).closest('[data-type="' + typeParent + '"]').attr("data-index");
					}
					
					layer2 = layer1[type];
					if (layer2 == null) {
						layer2 = {};
					}
					
					layer3 = layer2[dataIndexParent];
					if (layer3 == null) {
						layer3 = {};
					}
					
					layer4 = layer3[dataIndex];
					if (layer4 == null) {
						layer4 = {};
					}
					layer4[nameSplit[2]] = value;
					
					layer3[dataIndex] = layer4;
					layer2[dataIndexParent] = layer3;
					layer1[type] = layer2;
				});
				
				$("input").each(function(indexitem, itemdata) {
					layer2 = {};
					layer3 = {};
					layer4 = {};
					
					if ($(itemdata).attr("type") != "file") {
						var name = $(itemdata).attr("name");
						var value = $(itemdata).val();
						var tempName = name;
						tempName = tempName.replaceAll('[', ',');
						tempName = tempName.replaceAll(']', '');
						var nameSplit = tempName.split(',');
						var type = nameSplit[0];
						var dataIndex = $(this).closest('[data-type="' + type + '"]').attr("data-index");
						
						var dataIndexParent = dataIndex;
						
						var typeParent = types[type];
						typeParent = typeParent.split(':');
						typeParent = typeParent[0];
						if (typeParent != "") {
							dataIndexParent = $(this).closest('[data-type="' + typeParent + '"]').attr("data-index");
						}
						
						layer2 = layer1[type];
						if (layer2 == null) {
							layer2 = {};
						}
						
						layer3 = layer2[dataIndexParent];
						if (layer3 == null) {
							layer3 = {};
						}
						
						layer4 = layer3[dataIndex];
						if (layer4 == null) {
							layer4 = {};
						}
						layer4[nameSplit[2]] = value;
						
						layer3[dataIndex] = layer4;
						layer2[dataIndexParent] = layer3;
						layer1[type] = layer2;
					}
				});
				
				var isFinish = false;
				var obj = {};
				
				$.each(types, function(key, value){
					var tempDataLayer2 = {};
					
					if (!isFinish) {
						tempDataLayer2 = rec(layer1, key, 0);
						isFinish = true;
						obj[key] = tempDataLayer2;
					}
				});
			}
			
			data = obj;
            
            var file = $('#file').prop('files')[0];
            
            var baseUrl = $(location).attr("origin");
            
            const formData = new FormData();
		    formData.append('request', JSON.stringify(obj)); // Menambahkan data teks
		    formData.append('file', file); // Menambahkan file
		
		    try {
				var settings = {
				  	"url": baseUrl + "/api/upload",
				  	"method": "POST",
				  	"timeout": 0,
				  	"processData": false,
				  	"mimeType": "multipart/form-data",
				  	"contentType": false,
				  	"data": formData
				};
				
				setTimeout(function(){
					$.ajax(settings).done(function (response) {
					  	response = JSON.parse(response);
					  	var filename = response.fileDownloadUri;
					  	var a = document.createElement('a');
					  	var url = baseUrl + "/api/" + filename;
					  	filename = filename.split("/");
					  	filename = filename[1];
					  	a.href = url;
					  	a.download = filename;
					  	document.body.append(a);
					  	a.click();
					  	a.remove();
					  	window.URL.revokeObjectURL(url);
					  	$('[data-role="submit"]').prop("disabled", false);
					  	$("#spinner-submit").hide();
					  	$('[data-role="reset"]').removeAttr('disabled');
					}).error(function () {
					  	$("#modal-message").text("Terjadi kesalahan, silahkan ulangi dengan data yang benar");
					  	$("#btn-modal").click();
					  	$('[data-role="submit"]').removeAttr('disabled');
   						$("#spinner-submit").hide();
   						$('[data-role="reset"]').removeAttr('disabled');
					});
				},2000);
		    } catch (error) {
	      		console.error('Error:', error);
		  		$("#modal-message").text("Terjadi kesalahan, silahkan ulangi dengan data yang benar");
			  	$("#btn-modal").click();
			  	$('[data-role="submit"]').removeAttr('disabled');
   				$("#spinner-submit").hide();
   				$('[data-role="reset"]').removeAttr('disabled');
		    }
        }
    );
});


