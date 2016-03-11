<?php

class ImageUploadStatus
{
    public $result_code = 0;
    public $message = "Image upload failed.";
}

class ImageResults
{
    public $name = "";
    public $url = "";
    public $size = "";
}

$status  = new ImageUploadStatus();
$results = new ImageResults();

if ($_FILES["file"]["error"] > 0) {
    header("HTTP/1.1 400 Bad Request");
    // echo "Error: " . $_FILES ["file"] ["error"] . "<br /> \n";
    $status->message = "Error: " . $_FILES["file"]["error"];
} else {

    $ext         = explode('.', $_FILES['file']['name']);
    $name        = isset($_REQUEST['first_name']) && isset($_REQUEST['last_name']) ? $_REQUEST['first_name'] . '_' . $_REQUEST['last_name'] . millitime() . '.' . $ext[count($ext) - 1] : millitime() . '_' . $_FILES['file']['name'];
    $target_path = "../api/uploads/";
    $target_path = $target_path . basename($name);
    
    //if (is_uploaded_file ( $_FILES ['uploadedfile'] ['tmp_name'] )) {
    // echo "There was an error uploading the file, please try again!";
    //$status->message = "There was an error uploading the file, please try again!";
    //}
    
    if (move_uploaded_file($_FILES['file']['tmp_name'], $target_path)) {
        // echo "The file " . basename ( $_FILES ['file'] ['name'] ) . " has been uploaded";
        
        $status->message     = "Image upload successful";
        $status->result_code = 1;
        
        $results->name = $name;
        $results->url  = $target_path . "";
        $results->size = ($_FILES["file"]["size"] / 1); // 1024 to convert in KB
    } else {
        // echo "There was an error Moving the file, please try again!";
        //$status->message = "There was an error Moving the file, please try again!";
        
        switch ($_FILES['file']['error']) {
            case 0: //no error; possible file attack!
                $status->message = "There was a problem with your upload.";
                break;
            case 1: //uploaded file exceeds the upload_max_filesize directive in php.ini
                $status->message = "The file you are trying to upload is too big.";
                break;
            case 2: //uploaded file exceeds the MAX_FILE_SIZE directive that was specified in the html form
                $status->message = "The file you are trying to upload is too big.";
                break;
            case 3: //uploaded file was only partially uploaded
                $status->message = "The file you are trying upload was only partially uploaded.";
                break;
            case 4: //no file was uploaded
                $status->message = "You must select an image for upload.";
                break;
            default: //a default error, just in case!  :)
                $status->message = "There was a problem with your upload.";
                break;
        }
        
    }
    
    $response = array(
        'status' => $status,
        'results' => $results
    );
    
    // echo (json_encode ( $status ));
    // echo (json_encode ( $results ));
    echo (json_encode($response));
}

function millitime()
{
    $microtime = microtime();
    $comps     = explode(' ', $microtime);
    
    // Note: Using a string here to prevent loss of precision
    // in case of "overflow" (PHP converts it to a double)
    return sprintf('%d%03d', $comps[1], $comps[0] * 1000);
}
?>
