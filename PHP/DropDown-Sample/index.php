<?php


$conn = mysqli_connect('localhost', 'root', '', 'solar');


if(isset($_POST['submit']))
{
    $cat_id = $_POST['category'];

    $path_banner = time() . "." . pathinfo($_FILES['image']['name'], PATHINFO_EXTENSION);

    if (move_uploaded_file($_FILES['image']['tmp_name'], "images/Product/" . $path_banner)) {


        if(mysqli_query($conn, "INSERT INTO product (category_id, name, price, descrption, image, warranty, date, status) 
            VALUES ('$cat_id', 'test name', '10', 'desc', '$path_banner', '5', NOW(), 1)")){

            echo  "Record inserted successfully";
        } else{

            echo "Unable to insert";
        }
    } else{

        echo "Unable to upload image on server";
    }
}
?>


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>
<body>
    <form enctype="multipart/form-data" method="POST">
    <label>Choose Category</label>
    <select name="category" required>
        <option value="">Choose</option>
        <?php

            $res = mysqli_query($conn, "SELECT id, name FROM category WHERE status = true");

            if(mysqli_num_rows($res)>0){

                while($row = mysqli_fetch_assoc($res)){
                ?>
                    <option value="<?php echo $row['id'];?>"><?php echo $row['name'];?></option>
                <?php
                }
            }
        ?>
        
    </select>

    <input type="file" name="image" accept="image/*"/>
    <button type="submit" name="submit">Submit</button>
    </form>

    <?php

            $resImage = mysqli_query($conn, "SELECT image FROM product WHERE id = 7");

            if(mysqli_num_rows($resImage)>0){

                $rowImage = mysqli_fetch_assoc($resImage);
                ?>
                    <img src = "images/Product/<?php echo $rowImage['image'];?>"/>
                <?php
            }
    ?>
</body>
</html>