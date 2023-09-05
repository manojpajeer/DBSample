<?php

    require_once '../config/connection.php';

    $name = $_POST['uname'];

    if(mysqli_query($conn, "INSERT INTO register (name, status, date) VALUES ('$name', 'Active', NOW())")){

        $response['success'] = true;
        $response['message'] = "You have registered successfully!";

    } else{

        $response['success'] = false;
        $response['message'] = "Unable to register your data!";
    }
    

    echo json_encode($response);

    ?>
