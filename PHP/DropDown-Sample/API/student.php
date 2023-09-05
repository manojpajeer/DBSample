<?php

    require_once '../config/connection.php';

    $response=array();

    $qry = "SELECT * FROM register";

    $res = mysqli_query($conn, $qry);
    if(mysqli_num_rows($res)>0)
    {
        while($rows = mysqli_fetch_assoc($res))
        {
            $send["name"] = $rows['name'];
            $send["status"] = $rows['status'];
            $send["date"] = $rows['date'];

            array_push($response,$send);
        }
    }
    else
    {
        $response=null;
    }  
    echo (json_encode($response));

    ?>
