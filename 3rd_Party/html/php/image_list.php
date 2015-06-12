<?php

class ImageListStatus {
    public $result_code = 0;
    public $message = "Image list retrival failed.";
}

class ImageListResults {
    public $name = "";
    public $img = "";
    public $id = "";
}

$dir = "uploads/";
$actual_link = "http://$_SERVER[HTTP_HOST]/api/uploads/";

$status              = new ImageListStatus();
$status->message     = "Image upload successful.";
$status->result_code = 1;


if (is_dir($dir)) {
    if ($dh = opendir($dir)) {
        
        $img_arr = array();
        
        $id_count = 0;
        while (($file = readdir($dh)) !== false) {
            
            if ($file != '.' && $file != '..') {
                
                $results = new ImageListResults();
                                
                $results->id = $id_count;
                $results->img = $actual_link . $file . "";
                $results->name = $file;
                                
                array_push($img_arr, $results);
                $id_count++;
            }
            
        }
        closedir($dh);
    }
    
}
$data['image_list'] = $img_arr;

$response = array(
    'status' => $status,
    'data' => $data
);

echo (json_encode($response));
?>
