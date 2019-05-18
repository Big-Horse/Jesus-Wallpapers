const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();
var storage = require('@google-cloud/storage');
const sharp = require('sharp');
const _ = require('lodash');
const path = require('path');
const os = require('os');

var imagesRef = admin.database().ref('images' + '/');

exports.onImageUploaded = functions.storage.object().onFinalize((object) => {
    console.log("image added");
    const fileBucket = object.bucket; // The Storage bucket that contains the file.
    const filePath = object.name; // File path in the bucket.
    const fileNameNoExt = filePath.split('.').slice(0, -1).join('.')
    const contentType = object.contentType; // File content type.

    var urlDownload = "";


    if( !filePath.startsWith("thumbs/") && !filePath.startsWith("wallpapers/")) {

      console.log("fileBucket", fileBucket);
      console.log("filePath", filePath);
      console.log("contentType", contentType);
      
      imagesRef.child(fileNameNoExt).set({
          uri:filePath
      }, function(error) {
      });

      return generateVersions(fileBucket, filePath, fileNameNoExt).then(()=>{
          return 0;
      });

    }
    else{
      return 0;
    }

});


function generateVersions(fileBucket, filePath, fileNameNoExt){  
    const SIZES = [[150,250], [400,700]]; 

    var i = 0;
    
    const bucket = admin.storage().bucket(fileBucket);
    const tempFilePath = path.join(os.tmpdir(), filePath);
  
    return bucket.file(filePath).download({
      destination: tempFilePath
    }).then(() => {
  
      _.each(SIZES, (size) => {

        var folder = "";
        var refUri = "";
        
        if(i == 0){
          folder = "thumbs/";
          refUri = "uriThumb"
        }
        else if(i == 1){
          folder = "wallpapers/";
          refUri = "uriWallpaper"
        }
  
        let newFileName = `${fileNameNoExt}_${size[0]}_X_${size[1]}.jpg`
        let newFileTemp = path.join(os.tmpdir(), newFileName);
        let newFilePath = `${folder}${newFileName}`
  
        sharp(tempFilePath)
          .resize(size[0], size[1])
          .toFile(newFileTemp, (err, info) => {
  
            bucket.upload(newFileTemp, {
              destination: newFilePath
            });  
          });

        var updates = {};

        updates[fileNameNoExt + "/" + refUri] = newFileName;

        imagesRef.update(updates).then(()=>{
        });

        i++;
  
      }).then(() => {

      });

    });
}


/*
exports.onImageDeleted = functions.storage.object().onDelete((object) => {
  console.log("image deleted");
  const fileBucket = object.bucket; // The Storage bucket that contains the file.
  const filePath = object.name; // File path in the bucket.
  const fileName = filePath.split('/f').pop();
  const fileNameNoExt = fileName.split('.').slice(0, -1).join('.')
  const contentType = object.contentType; // File content type.
  const metageneration = object.metageneration; // Number of times metadata has been generated. New objects have a value of 1.

  console.log("fileBucket", fileBucket);
  console.log("filePath", filePath);
  console.log("fileName", fileName);
  console.log("contentType", contentType);
  console.log("metageneration", metageneration);

  return imagesRef.child(fileNameNoExt).remove();

});
*/